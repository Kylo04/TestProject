package qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import qa.automation.reporting.Reporter;
import qa.automation.utils.FrontUtils;
import qa.automation.utils.RetryUtils;
import qa.automation.utils.TimeUtils;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class BasePage {
    protected final Page page;
    protected final int SLOW_TIMEOUT = 1000;
    protected final int MIN_TIMEOUT = 2000;
    protected final int MAX_TIMEOUT = 5000;

    protected BasePage(Page page) {
        this.page = Objects.requireNonNull(page);
    }

    public abstract String resolve(String key);

    protected Locator $(String selector) {
        return page.locator(selector);
    }

    private void exec(String action, Runnable r, boolean shot) {
        long startTime = System.currentTimeMillis();
        try {
            RetryUtils.retry(r);
            long duration = System.currentTimeMillis() - startTime;
            Reporter.pass(action + " ----->(" + duration + " ms)", shot);
        } catch (RuntimeException e) {
            long duration = System.currentTimeMillis() - startTime;
            Reporter.fail(action + " ----->(" + duration + " ms)" + e, true);
        }
    }

    private <T> T execGet(String action, Supplier<T> s, boolean shot) {
        long startTime = System.currentTimeMillis();
        try {
            T val = RetryUtils.retry(s);
            long duration = System.currentTimeMillis() - startTime;
            Reporter.info(action + " ----->(" + duration + " ms)", shot);
            return val;
        } catch (RuntimeException e) {
            long duration = System.currentTimeMillis() - startTime;
            Reporter.fail(action + " ----->(" + duration + " ms)" + e, true);
            return null;
        }
    }

    public void click(Locator locator, boolean shot) {
        String action = "Click: " + locator;
        long globalStart = System.currentTimeMillis();

        try {
            RetryUtils.retry(() -> {
                try {
                    if (!locator.isVisible()) {
                        Reporter.info("Element not visible yet → Playwright will auto-wait");
                    }
                    if (!locator.isEnabled()) {
                        Reporter.info("Element not enabled yet → waiting...");
                    }
                    locator.waitFor(new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(MAX_TIMEOUT));
                    locator.scrollIntoViewIfNeeded();
                    locator.click(new Locator.ClickOptions().setTrial(true));
                    locator.click();

                } catch (Exception primaryEx) {
                    Reporter.info("Normal click failed → trying fallback 1 (scroll + retry)");
                    try {
                        locator.scrollIntoViewIfNeeded();
                        locator.click();
                    } catch (Exception retryEx) {
                        Reporter.warn("Fallback 1 failed → trying fallback 2 (force click)");
                        locator.click(new Locator.ClickOptions().setForce(true));
                    }
                }
            });

            long totalDuration = System.currentTimeMillis() - globalStart;
            if (totalDuration > MIN_TIMEOUT) {
                Reporter.warn("Very slow click: " + totalDuration + " ms", shot);
            } else if (totalDuration > SLOW_TIMEOUT) {
                Reporter.info("Slow click: " + totalDuration + " ms", shot);
            } else {
                Reporter.pass(action + " (" + totalDuration + " ms)", shot);
            }
        } catch (Exception e) {
            long totalDuration = System.currentTimeMillis() - globalStart;
            Reporter.fail(action + " FAILED (" + totalDuration + " ms): " + e.getMessage());
        }
    }

    public void click(String selector) {
        click($(selector), false);
    }

    public void click(String selector, boolean shot) {
        click($(selector), shot);
    }

    public void click(Locator locator) {
        click(locator, false);
    }

    public void clickIfVisible(Locator locator) {
        if (locator.isVisible()) click(locator);
    }

    public void fill(String selector, String text) {
        exec("Fill: " + selector + " | '" + text + "'", () -> $(selector).fill(text), false);
    }

    public void fill(String selector, String text, boolean shot) {
        exec("Fill: " + selector + " | '" + text + "'", () -> $(selector).fill(text), shot);
    }

    public void fill(Locator locator, String text) {
        exec("Fill: " + locator + " | '" + text + "'", () -> locator.fill(text), false);
    }

    public void fill(Locator locator, String text, boolean shot) {
        exec("Fill: " + locator + " | '" + text + "'", () -> locator.fill(text), shot);
    }

    public void type(String selector, String text) {
        exec("Type: " + selector + " | '" + text + "'", () -> {
            Locator l = $(selector);
            l.fill("");
            l.fill(text);
        }, false);
    }

    public void type(String selector, String text, boolean shot) {
        exec("Type: " + selector + " | '" + text + "'", () -> {
            Locator l = $(selector);
            l.fill("");
            l.fill(text);
        }, shot);
    }

    public void type(Locator locator, String text) {
        exec("Type: " + locator + " | '" + text + "'", () -> {
            locator.fill("");
            locator.fill(text);
        }, false);
    }

    public void type(Locator locator, String text, boolean shot) {
        exec("Type: " + locator + " | '" + text + "'", () -> {
            locator.fill("");
            locator.fill(text);
        }, shot);
    }

    public String getText(String selector) {
        return norm(execGet("Get textContent: " + selector, () -> $(selector).textContent(), false));
    }

    public String getText(String selector, boolean shot) {
        return norm(execGet("Get textContent: " + selector, () -> $(selector).textContent(), shot));
    }

    public String getText(Locator locator) {
        return norm(execGet("Get textContent: " + locator, locator::textContent, false));
    }

    public String getText(Locator locator, boolean shot) {
        return norm(execGet("Get textContent: " + locator, locator::textContent, shot));
    }

    public void waitVisible(String selector, double timeoutMs) {
        exec("Wait visible: " + selector, () ->
                $(selector).waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(timeoutMs)), false);
    }

    public void waitVisible(String selector, double timeoutMs, boolean shot) {
        exec("Wait visible: " + selector, () ->
                $(selector).waitFor(new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(timeoutMs)), shot);
    }

    public void wait(Locator loc) {
        exec("Wait for locator", () -> TimeUtils.waitForLocator(loc), false);
    }

    public void hardwait(Integer time) {
        exec("Wait for locator", () -> TimeUtils.hardWait(time), false);
    }

    public void wait(Page page) {
        exec("Wait for page ", () -> TimeUtils.waitForPageLoad(page), false);
    }

    private String norm(String s) {
        if (s == null) return "";
        return FrontUtils.normalizeText(s.trim());
    }

    public void launch(String url) {
        exec("Launch: " + url, () -> {
            page.setDefaultNavigationTimeout(60_000);
            page.navigate(url);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            safeDisableAnimations();
            page.waitForLoadState(LoadState.NETWORKIDLE);
        }, false);
    }

    private void safeDisableAnimations() {
        try {
            page.addStyleTag(new Page.AddStyleTagOptions()
                    .setContent("*,*::before,*::after{transition:none!important;animation:none!important}"));
        } catch (RuntimeException ignored) {
        }
    }

}