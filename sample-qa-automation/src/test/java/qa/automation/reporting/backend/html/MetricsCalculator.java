package qa.automation.reporting.backend.html;

import qa.automation.reporting.model.*;
import qa.automation.reporting.model.enums.Status;

import java.util.HashSet;
import java.util.Set;

import static qa.automation.reporting.context.FeatureContext.deriveFeatureStatus;

public class MetricsCalculator {
    public static ReportMetrics calculate(TestReport report) {
        ReportMetrics m = new ReportMetrics();
        Set<String> countedScenarios = new HashSet<>();

        for (FeatureNode f : report.features) {
            Status featureStatus = deriveFeatureStatus(f.scenarios);
            switch (featureStatus) {
                case FAILED -> m.failedFeatures++;
                case PARTIAL -> m.partialFeatures++;
                case SKIPPED -> m.skippedFeatures++;
                default -> m.passedFeatures++;
            }

            for (ScenarioNode s : f.scenarios) {
                String scenarioKey = f.name + "::" + s.name;
                if (countedScenarios.add(scenarioKey)) {
                    switch (s.status) {
                        case FAILED -> m.failedScenarios++;
                        case PARTIAL, PASSED_WITH_WARN -> m.partialScenarios++;
                        case SKIPPED -> m.skippedScenarios++;
                        default -> m.passedScenarios++;
                    }
                }

                for (StepNode step : s.steps) {
                    boolean hasDefectLog = false;

                    for (LogEntry log : step.logs) {
                        switch (log.type) {
                            case FAIL -> {
                                m.failedSteps++;
                                hasDefectLog = true;
                            }
                            case SOFT_FAIL -> {
                                m.softFailSteps++;
                                hasDefectLog = true;
                            }
                            case WARN -> {
                                m.warnSteps++;
                                hasDefectLog = true;
                            }
                            case SKIP -> {
                                m.skippedSteps++;
                                hasDefectLog = true;
                            }
                            default -> {
                            }
                        }
                    }
                    if (!hasDefectLog) {
                        m.passedSteps++;
                    }
                }
            }
        }
        return m;
    }
}