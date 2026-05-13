package qa.automation.reporting;

import io.cucumber.java.PendingException;
import lombok.Setter;
import qa.automation.reporting.backend.ReportingBackend;
import qa.automation.reporting.backend.HardFailException;
import qa.automation.reporting.model.enums.LogType;

public final class Reporter {

    @Setter
    private static ReportingBackend backend;

    private Reporter() {
    }

    private static void ensureBackend() {
        if (backend == null) {
            throw new IllegalStateException("ReportingBackend is not initialized. " + "Call Reporter.setBackend(...) in @BeforeAll.");
        }
    }

    public static void info(String msg) {
        ensureBackend();
        backend.log(LogType.INFO, msg, false);
    }

    public static void info(String msg, boolean screenshot) {
        ensureBackend();
        backend.log(LogType.INFO, msg, screenshot);
    }

    public static void pass(String msg) {
        ensureBackend();
        backend.log(LogType.PASS, msg, false);
    }

    public static void pass(String msg, boolean screenshot) {
        ensureBackend();
        backend.log(LogType.PASS, msg, screenshot);
    }

    public static void warn(String msg) {
        ensureBackend();
        backend.log(LogType.WARN, msg, false);
    }

    public static void warn(String msg, boolean screenshot) {
        ensureBackend();
        backend.log(LogType.WARN, msg, screenshot);
    }

    public static void softFail(String msg) {
        ensureBackend();
        backend.log(LogType.SOFT_FAIL, msg, false);
    }

    public static void softFail(String msg, boolean screenshot) {
        ensureBackend();
        backend.log(LogType.SOFT_FAIL, msg, screenshot);
    }

    public static void fail(String msg) {
        ensureBackend();
        backend.log(LogType.FAIL, msg, true);
        throw new HardFailException(msg);
    }

    public static void fail(String msg, boolean screenshot) {
        ensureBackend();
        backend.log(LogType.FAIL, msg, screenshot);
        throw new HardFailException(msg);
    }

    public static void skip(String msg) {
        ensureBackend();
        backend.log(LogType.SKIP, msg, false);
        throw new PendingException(msg);
    }

    public static void skip(String msg, boolean screenshot) {
        ensureBackend();
        backend.log(LogType.SKIP, msg, screenshot);
        throw new PendingException(msg);
    }
}