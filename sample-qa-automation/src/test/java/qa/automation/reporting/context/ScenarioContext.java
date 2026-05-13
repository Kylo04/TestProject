package qa.automation.reporting.context;

import qa.automation.reporting.model.ScenarioNode;
import qa.automation.reporting.model.StepNode;
import qa.automation.reporting.model.TestReport;
import qa.automation.reporting.model.enums.Status;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ScenarioContext {

    private static TestReport report;

    private static final Map<String, ScenarioNode> BY_TESTCASE_ID =
            new ConcurrentHashMap<>();

    private static final ThreadLocal<ScenarioNode> CURRENT =
            new ThreadLocal<>();

    private ScenarioContext() {
    }

    public static void init(TestReport testReport) {
        report = testReport;
        BY_TESTCASE_ID.clear();
        CURRENT.remove();
    }

    public static void start(String testCaseId, ScenarioNode node) {
        BY_TESTCASE_ID.put(testCaseId, node);
        CURRENT.set(node);
    }

    public static ScenarioNode currentNullable() {
        return CURRENT.get();
    }

    public static void end(String testCaseId) {
        BY_TESTCASE_ID.remove(testCaseId);
        CURRENT.remove();
    }

    public static ScenarioNode fromTestCaseId(String testCaseId) {
        return BY_TESTCASE_ID.get(testCaseId);
    }

    public static Status deriveScenarioStatus(List<StepNode> steps) {

        boolean hasWarn = false;
        boolean hasPartial = false;
        boolean hasSkipped = false;

        for (StepNode step : steps) {
            if (step.status == Status.FAILED) {
                return Status.FAILED;
            }
            if (step.status == Status.PARTIAL) {
                hasPartial = true;
            }
            if (step.status == Status.PASSED_WITH_WARN) {
                hasWarn = true;
            }
            if (step.status == Status.SKIPPED) {
                hasSkipped = true;
            }
        }

        if (hasPartial) return Status.PARTIAL;
        if (hasWarn) return Status.PASSED_WITH_WARN;
        if (hasSkipped) return Status.SKIPPED;

        return Status.PASSED;
    }
}