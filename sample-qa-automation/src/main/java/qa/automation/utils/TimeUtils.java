package qa.automation.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public final class TimeUtils {
    private TimeUtils() {}

    public static void waitMs(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); throw new RuntimeException(e); }
    }

    public static void hardWait(int seconds) {
        waitMs(seconds * 1000L);
    }

    public static void waitForLocator(Locator locator, int pollMs, int maxMs) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));
        RetryUtils.until(locator::isVisible, maxMs / pollMs, pollMs);
    }

    public static void waitForLocator(Locator locator) {
        waitForLocator(locator, 200, 20000);
    }

    public static void waitForPageLoad(Page page) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForLoadState(LoadState.LOAD);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }
}