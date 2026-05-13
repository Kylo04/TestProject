package qa.automation.reporting.context;

import qa.automation.reporting.model.TestReport;

public final class ReportContext {

    private static final TestReport REPORT = new TestReport();

    private ReportContext() {
    }

    public static TestReport get() {
        return REPORT;
    }
}