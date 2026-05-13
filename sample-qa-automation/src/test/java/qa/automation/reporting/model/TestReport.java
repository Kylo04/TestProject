package qa.automation.reporting.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TestReport {
    public Instant executionStart;
    public Instant executionEnd;
    public List<FeatureNode> features = new ArrayList<>();

    public double totalDurationMinutes() {
        if (executionStart == null || executionEnd == null) return 0.0;
        return (Duration.between(executionStart, executionEnd).toMillis()) / 60000.0;
    }

    public double parallelExecutionMinutes() {
        if (executionStart == null || executionEnd == null) return 0.0;
        return Duration.between(executionStart, executionEnd).toMillis() / 60000.0;
    }

    public double totalAggregatedExecutionMinutes() {
        return features.stream()
                .mapToDouble(f -> f.durationSeconds() / 60.0)
                .sum();
    }

    public double parallelEfficiencyRatio() {
        double parallel = parallelExecutionMinutes();
        if (parallel == 0.0) return 0.0;
        return totalAggregatedExecutionMinutes() / parallel;
    }

}

