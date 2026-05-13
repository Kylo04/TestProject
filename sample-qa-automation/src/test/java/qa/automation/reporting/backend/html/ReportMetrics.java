package qa.automation.reporting.backend.html;

public class ReportMetrics {

    public int passedFeatures, failedFeatures, partialFeatures, skippedFeatures;
    public int passedScenarios, failedScenarios, partialScenarios, skippedScenarios;
    public int passedSteps, failedSteps, softFailSteps, skippedSteps, warnSteps;

    public int defectSteps() {
        return failedSteps + softFailSteps + warnSteps;
    }

    public int totalSteps() {
        return passedSteps + failedSteps + softFailSteps + skippedSteps;
    }

    public int failedSteps() { return failedSteps; }

    public int softFailSteps() { return softFailSteps; }

    public int warnSteps() { return warnSteps; }
}
