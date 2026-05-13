package qa.automation.reporting.backend;

import qa.automation.playwright.PlaywrightSession;
import qa.automation.reporting.Reporter;
import qa.automation.reporting.context.ScenarioContext;
import qa.automation.reporting.model.LogEntry;
import qa.automation.reporting.model.ScenarioNode;
import qa.automation.reporting.model.StepNode;
import qa.automation.reporting.model.enums.LogType;
import qa.automation.reporting.model.enums.Status;
import qa.automation.reporting.util.ScreenshotUtil;

public class HtmlReportingBackend implements ReportingBackend {

    @Override
    public void log(LogType type, String message, boolean screenshot) {

        ScenarioNode scenario = ScenarioContext.currentNullable();
        if (scenario == null || scenario.steps.isEmpty()) {
            return;
        }
        StepNode step = scenario.steps.getLast();
        LogEntry entry = new LogEntry(type, message);

        if (screenshot) {
            try {
                entry.screenshotPath = ScreenshotUtil.capture(PlaywrightSession.page());
            } catch (Exception e) {
                Reporter.warn("Screenshot capture failed");
                entry.screenshotPath = null;
            }
        }
        step.logs.add(entry);
        switch (type) {
            case FAIL -> {
                step.status = Status.FAILED;
            }
            case SOFT_FAIL -> {
                if (step.status != Status.FAILED) {
                    step.status = Status.PARTIAL;
                }
            }
            case WARN -> {
                if (step.status == Status.PASSED) {
                    step.status = Status.PASSED_WITH_WARN;
                }
            }
            case SKIP -> {
                if (step.status == Status.PASSED) {
                    step.status = Status.SKIPPED;
                }
            }
            default -> {
            }
        }
    }
}