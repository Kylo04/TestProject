package qa.automation.hooks;

import io.cucumber.java.*;

import qa.automation.reporting.Reporter;
import qa.automation.reporting.backend.HtmlReportingBackend;
import qa.automation.reporting.context.FeatureContext;
import qa.automation.reporting.context.ScenarioContext;
import qa.automation.reporting.context.ReportContext;
import qa.automation.reporting.backend.html.HtmlReportGenerator;
import qa.automation.reporting.model.FeatureNode;
import qa.automation.reporting.model.ScenarioNode;
import qa.automation.reporting.model.TestReport;
import qa.automation.reporting.model.enums.Status;

import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ReportHooks {

    @BeforeAll(order = 0)
    public static void initReporting() {
        Reporter.setBackend(new HtmlReportingBackend());

        TestReport report = ReportContext.get();
        FeatureContext.init(report);
        ScenarioContext.init(report);

        report.executionStart = Instant.now();
    }

    @Before(order = 0)
    public void beforeScenario(io.cucumber.java.Scenario scenario) {

        FeatureContext.start(extractFeatureName(scenario));

        ScenarioNode node = new ScenarioNode();
        node.name = scenario.getName();
        node.startTime = Instant.now();
        node.status = Status.PASSED;
        node.steps = new ArrayList<>();

        FeatureNode feature = FeatureContext.current();
        feature.scenarios.add(node);

        String key = scenario.getId();
        ScenarioContext.start(key, node);
    }

    @After(order = 10)
    public void afterScenario(io.cucumber.java.Scenario cucumberScenario) {

        ScenarioNode execution = ScenarioContext.currentNullable();
        if (execution == null) {
            ScenarioContext.end(cucumberScenario.getId());
            return;
        }

        execution.endTime = Instant.now();
        execution.status =
                ScenarioContext.deriveScenarioStatus(execution.steps);

        FeatureNode feature = FeatureContext.current();
        feature.scenarios.stream()
                .filter(s -> s.name.equals(execution.name))
                .findFirst()
                .ifPresent(outline -> {
                    outline.status = updateScenState(outline.status, execution.status);
                });

        ScenarioContext.end(cucumberScenario.getId());
    }

    @AfterAll(order = 0)
    public static void generateReport() throws Exception {

        TestReport report = ReportContext.get();
        report.executionEnd = Instant.now();

        String timestamp =
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));

        HtmlReportGenerator.generate(
                report,
                Path.of("reports/automation-report" + timestamp + ".html")
        );
    }

    private String extractFeatureName(io.cucumber.java.Scenario scenario) {
        if (scenario.getUri() == null) {
            return "UnknownFeature";
        }

        String uri = scenario.getUri().toString();

        if (uri.startsWith("classpath:")) {
            uri = uri.substring("classpath:".length());
        }

        uri = uri.replace("\\", "/");

        int lastSlash = uri.lastIndexOf('/');
        int lastDot = uri.lastIndexOf('.');

        if (lastDot > lastSlash) {
            uri = uri.substring(0, lastDot);
        }

        if (lastSlash >= 0) {
            uri = uri.substring(lastSlash + 1);
        }

        return uri.isBlank() ? "UnknownFeature" : uri;
    }

    private Status updateScenState(Status a, Status b) {
        if (a == Status.FAILED || b == Status.FAILED) return Status.FAILED;
        if (a == Status.PARTIAL || b == Status.PARTIAL) return Status.PARTIAL;
        if (a == Status.PASSED_WITH_WARN || b == Status.PASSED_WITH_WARN)
            return Status.PASSED_WITH_WARN;
        if (a == Status.SKIPPED || b == Status.SKIPPED)
            return Status.SKIPPED;
        return Status.PASSED;
    }
}