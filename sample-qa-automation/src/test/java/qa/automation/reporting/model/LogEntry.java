package qa.automation.reporting.model;

import qa.automation.reporting.model.enums.LogType;
import java.time.Instant;

public class LogEntry {
    public LogType type;
    public String message;
    public String screenshotPath;
    public Instant timestamp;

    public LogEntry(LogType type, String message) {
        this.type = type;
        this.message = message;
    }

    public void attachScreenshot(String path) {
        this.screenshotPath = path;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }
}

