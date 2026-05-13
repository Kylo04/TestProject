package qa.automation.reporting.model;

import qa.automation.reporting.model.enums.Status;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ScenarioNode {
    public String id;
    public String name;
    public boolean outline;
    public String exampleLabel;
    public Status status;
    public Instant startTime;
    public Instant endTime;
    public List<StepNode> steps = new ArrayList<>();

    public Double durationSeconds() {
        if (startTime == null || endTime == null) return 0.0;
        return (double) (Duration.between(startTime, endTime).toMillis() / 1000.0);
    }
}

