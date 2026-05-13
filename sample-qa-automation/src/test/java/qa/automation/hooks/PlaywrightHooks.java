package qa.automation.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import qa.automation.playwright.PlaywrightSession;

public class PlaywrightHooks {

    private final PlaywrightSession session;

    public PlaywrightHooks(PlaywrightSession session) {
        this.session = session;
    }

    @Before(order = 0)
    public void beforeScenario() {
        session.start();
    }

    @After(order = 20)
    public void afterScenario() {
        session.stop();
    }
}