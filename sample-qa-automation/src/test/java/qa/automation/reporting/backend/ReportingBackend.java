package qa.automation.reporting.backend;

import qa.automation.reporting.model.enums.LogType;

public interface ReportingBackend {
    void log(LogType type, String message, boolean takeScreenshot);
}
