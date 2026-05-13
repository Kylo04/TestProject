package qa.automation.playwright;

import com.microsoft.playwright.*;

public class PlaywrightSession {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;

    private static final ThreadLocal<Page> PAGE = new ThreadLocal<>();

    public void start() {
        playwright = Playwright.create();
        browser = playwright.chromium()
                .launch(new BrowserType.LaunchOptions().setHeadless(false));

        context = browser.newContext();
        PAGE.set(context.newPage());
    }

    public static Page page() {
        return PAGE.get();
    }

    public void stop() {
        try {
            if (PAGE.get() != null) PAGE.get().close();
        } finally {
            try {
                if (context != null) context.close();
            } finally {
                try {
                    if (browser != null) browser.close();
                } finally {
                    if (playwright != null) playwright.close();
                }
            }
        }
        PAGE.remove();
    }
}