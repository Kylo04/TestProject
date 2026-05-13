package qa.automation.reporting.backend.html;

import qa.automation.reporting.model.TestReport;

public final class DashboardHtml {

    public static String render(ReportMetrics m, TestReport report) {
        int totalFeatures = m.passedFeatures + m.failedFeatures;
        int totalScenarios = m.passedScenarios + m.failedScenarios + m.partialScenarios;

        double featurePassPct = pct(m.passedFeatures, totalFeatures);
        double scenarioPassPct = pct(m.passedScenarios, totalScenarios);

        double defectPct =
                (m.defectSteps() * 100.0) / Math.max(1, m.totalSteps());

        return String.format("""
            <div class="dashboard">
               <h2>Sample Test Execution Dashboard</h2>
                <div class="metric">
                  <div class="metric-row">
                    Parallel Execution Time: <strong>%.2f minutes</strong>
                  </div>
                  <div class="metric-row">
                    Total Test Execution Time: <strong>%.2f minutes</strong>
                  </div>
                  <div class="metric-row">
                    Parallel Efficiency Ratio: <strong>%.2f times</strong>
                  </div>
                </div>
            
              <div class="dashboard-grid">
                <div class="card">
                  <h3>Features</h3>
                  <div class="passed">Passed: %d (%.1f%%)</div>
                  <div class="failed">Failed: %d (%.1f%%)</div>
                  <div class="partial">Partial: %d</div>
                  <div class="skipped">Skipped: %d</div>
                  <div class="chart-wrap">
                    <canvas id="featureChart"></canvas>
                  </div>
                </div>
            
                <div class="card">
                  <h3>Scenarios</h3>
                  <div class="passed">Passed: %d</div>
                  <div class="failed">Failed: %d</div>
                  <div class="partial">Partial: %d</div>
                  <div class="skipped">Skipped: %d</div>
                  <div class="chart-wrap">
                    <canvas id="scenarioChart"></canvas>
                  </div>
                </div>

                <div class="card">
                  <h3>Step Logs</h3>
                  <div class="passed">Passed: %d</div>
                  <div class="failed">Failed: %d</div>
                  <div class="warning">Soft Fail: %d</div>
                  <div class="partial">Skipped: %d</div>
                  <div class="chart-wrap">
                    <canvas id="stepChart"></canvas>
                  </div>
                </div>

                <div class="card">
                  <h3>Defect Density</h3>
                  <div class="failed">Total Defects: %d</div>
                  <div class="warning">Fail: %d | Soft Fail: %d | Warning: %d</div>
                  <div class="chart-wrap">
                    <canvas id="defectChart"></canvas>
                  </div>
                </div>

              </div>
            </div>
            """,
                report.parallelExecutionMinutes(),
                report.totalAggregatedExecutionMinutes(),
                report.parallelEfficiencyRatio(),
                m.passedFeatures, featurePassPct,
                m.failedFeatures, 100 - featurePassPct,
                m.partialFeatures,
                m.skippedFeatures,
                m.passedScenarios,
                m.failedScenarios,
                m.partialScenarios,
                m.skippedScenarios,
                m.passedSteps,
                m.failedSteps,
                m.softFailSteps,
                m.skippedSteps,
                m.defectSteps(),
                m.failedSteps,
                m.softFailSteps,
                m.warnSteps
        );
    }

    private static double pct(int part, int total) {
        return total == 0 ? 0 : (part * 100.0) / total;
    }
}
