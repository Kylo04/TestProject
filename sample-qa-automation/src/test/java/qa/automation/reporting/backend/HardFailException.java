package qa.automation.reporting.backend;

public class HardFailException extends RuntimeException {

    public HardFailException(String message) {
        super(message);
    }

    public HardFailException(String message, Throwable cause) {
        super(message, cause);
    }
}

