package qa.automation.reporting.cucumber;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import qa.automation.reporting.context.ScenarioContext;
import qa.automation.reporting.context.ReportContext;
import qa.automation.reporting.model.TestReport;
import qa.automation.reporting.model.StepNode;
import qa.automation.reporting.model.ScenarioNode;
import qa.automation.reporting.model.enums.Status;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestRunFinished;

public class CucumberEventListener implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {

        publisher.registerHandlerFor(TestRunStarted.class, event -> {
            TestReport report = ReportContext.get();
            report.executionStart = event.getInstant();
        });

        publisher.registerHandlerFor(TestRunFinished.class, event -> {
            TestReport report = ReportContext.get();
            report.executionEnd = event.getInstant();
        });

        publisher.registerHandlerFor(TestCaseStarted.class, event -> {
            ScenarioNode scenario = ScenarioContext.fromTestCaseId(event.getTestCase().getId().toString());
            if (scenario != null) {
                scenario.startTime = event.getInstant();
            }
        });

        publisher.registerHandlerFor(TestCaseFinished.class, event -> {
            ScenarioNode scenario = ScenarioContext.fromTestCaseId(event.getTestCase().getId().toString());
            if (scenario != null) {
                scenario.endTime = event.getInstant();
            }
        });

        publisher.registerHandlerFor(TestStepStarted.class, event -> {

            if (!(event.getTestStep() instanceof PickleStepTestStep pst)) {
                return;
            }

            ScenarioNode scenario = ScenarioContext.fromTestCaseId(event.getTestCase().getId().toString());
            if (scenario == null) return;

            StepNode step = new StepNode();
            step.title = pst.getStep().getKeyword() + pst.getStep().getText();
            step.startTime = event.getInstant();
            step.status = Status.PASSED;

            scenario.steps.add(step);
        });

        publisher.registerHandlerFor(TestStepFinished.class, event -> {

            if (!(event.getTestStep() instanceof PickleStepTestStep)) {
                return;
            }

            ScenarioNode scenario = ScenarioContext.fromTestCaseId(event.getTestCase().getId().toString());
            if (scenario == null || scenario.steps.isEmpty()) return;

            StepNode step = scenario.steps.get(scenario.steps.size() - 1);

            step.endTime = event.getInstant();

            if (event.getResult().getStatus() == io.cucumber.plugin.event.Status.FAILED) {
                step.status = Status.FAILED;
            } else if (event.getResult().getStatus() == io.cucumber.plugin.event.Status.SKIPPED) {
                step.status = Status.SKIPPED;
            }
        });
    }
}