package qa.automation.reporting.context;

import io.cucumber.java.Scenario;

import java.util.ArrayList;
import java.util.List;

public class TestContext {

    private static final ThreadLocal<TestContext> CURRENT =
            new ThreadLocal<>();

    public static void set(TestContext ctx) {
        CURRENT.set(ctx);
    }

    public static TestContext get() {
        TestContext ctx = CURRENT.get();
        if (ctx == null) {
            throw new IllegalStateException(
                    "TestContext not initialised. " +
                            "Ensure Cucumber @Before hook calls TestContext.set()."
            );
        }
        return ctx;
    }

    public static void clear() {
        CURRENT.remove();
    }

    private final List<Throwable> softErrors = new ArrayList<>();


    private Scenario cucumberScenario;
    private boolean failScenarioOnAnySoft = false;

    public void resetDataObjects() {
    }

    public void addSoftError(Throwable t) {
        softErrors.add(t);
    }

    public List<Throwable> drainSoftErrors() {
        List<Throwable> list = new ArrayList<>(softErrors);
        softErrors.clear();
        return list;
    }

    public boolean hasSoftErrors() {
        return !softErrors.isEmpty();
    }

    public Scenario getCucumberScenario() {
        return cucumberScenario;
    }

    public void setCucumberScenario(Scenario scenario) {
        this.cucumberScenario = scenario;
    }

    public boolean isFailScenarioOnAnySoft() {
        return failScenarioOnAnySoft;
    }

    public void setFailScenarioOnAnySoft(boolean failOnSoft) {
        this.failScenarioOnAnySoft = failOnSoft;
    }

}