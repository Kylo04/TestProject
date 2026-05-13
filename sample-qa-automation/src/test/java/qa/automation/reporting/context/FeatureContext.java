package qa.automation.reporting.context;

import qa.automation.reporting.model.FeatureNode;
import qa.automation.reporting.model.ScenarioNode;
import qa.automation.reporting.model.TestReport;
import qa.automation.reporting.model.enums.Status;

import java.util.ArrayList;
import java.util.List;

public final class FeatureContext {

    private static TestReport report;

    private static final Object FEATURES_LOCK = new Object();
    private static final ThreadLocal<FeatureNode> CURRENT = new ThreadLocal<>();

    private FeatureContext() {
    }

    public static void init(TestReport testReport) {
        report = testReport;
    }

    public static void start(String featureName) {
        FeatureNode feature;
        synchronized (FEATURES_LOCK) {
            feature = report.features.stream()
                    .filter(f -> f.name.equals(featureName))
                    .findFirst()
                    .orElseGet(() -> {
                        FeatureNode f = new FeatureNode();
                        f.name = featureName;
                        f.scenarios = new ArrayList<>();
                        report.features.add(f);
                        return f;
                    });
        }
        CURRENT.set(feature);
    }

    public static FeatureNode current() {
        return CURRENT.get();
    }

    public static Status deriveFeatureStatus(List<ScenarioNode> scenarios) {

        boolean hasPartial = false;
        boolean hasSkipped = false;

        for (ScenarioNode s : scenarios) {
            switch (s.status) {
                case FAILED:
                    return Status.FAILED;
                case PARTIAL, PASSED_WITH_WARN:
                    hasPartial = true;
                    break;
                case SKIPPED:
                    hasSkipped = true;
                    break;
                default:
                    break;
            }
        }

        if (hasPartial) return Status.PARTIAL;
        if (hasSkipped) return Status.SKIPPED;
        return Status.PASSED;
    }

    public static void end() {
        CURRENT.remove();
    }
}