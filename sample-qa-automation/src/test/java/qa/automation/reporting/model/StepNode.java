package qa.automation.reporting.model;

import qa.automation.reporting.model.enums.Status;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class StepNode {
    public String title;
    public Status status;
    public Instant startTime;
    public Instant endTime;
    public List<LogEntry> logs = new ArrayList<>();

    public long durationMillis() {
        if (startTime == null || endTime == null) return 0;
        return Duration.between(startTime, endTime).toMillis();
    }

}

