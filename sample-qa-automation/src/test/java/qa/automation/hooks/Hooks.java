package qa.automation.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import qa.automation.reporting.context.TestContext;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        TestContext ctx = new TestContext();
        ctx.setCucumberScenario(scenario);
        TestContext.set(ctx);
    }

    @After
    public void afterScenario(Scenario scenario) {
        TestContext.clear();
    }
}