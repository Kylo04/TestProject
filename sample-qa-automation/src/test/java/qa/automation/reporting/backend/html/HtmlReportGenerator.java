package qa.automation.reporting.backend.html;

import qa.automation.reporting.model.TestReport;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class HtmlReportGenerator {

    public static void generate(TestReport report, Path output) throws IOException {
        Path parentDir = output.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
        ReportMetrics m = MetricsCalculator.calculate(report);
        String html = buildHtml(report, m);

        Files.writeString(output, html, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static String buildHtml(TestReport report, ReportMetrics m) {

        StringBuilder sb = new StringBuilder();
        sb.append("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                <meta charset="UTF-8">
                <title>Sample Automation Test Report</title>
                
                <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0"></script>
                
                <style>
                """);

        sb.append(Css.BLOCK);
        sb.append("</style></head><body>");

        sb.append(DashboardHtml.render(m, report));
        sb.append(BodyHtml.render(report));
        sb.append(Js.render(m.passedFeatures, m.failedFeatures, m.partialFeatures, m.skippedFeatures, m.passedScenarios, m.failedScenarios, m.partialScenarios, m.skippedScenarios, m.passedSteps, m.failedSteps, m.softFailSteps, m.skippedSteps, m.failedSteps(), m.softFailSteps(), m.warnSteps()));
        sb.append("""
                <script>
                function toggle(el){
                  el.closest(".dropdown").classList.toggle("open");
                }
                
                function selectScenario(id){
                  document.querySelectorAll(".scenario-view").forEach(d=>{
                    d.style.display =
                      (document.getElementById(id).checked && d.dataset.id === id)
                        ? "block" : "none";
                  });
                }
                
                function pie(id, labels, data, colors){
                  const canvas = document.getElementById(id);
                  if (!canvas) return;
                
                  new Chart(canvas, {
                    type: 'pie',
                    data: {
                      labels: labels,
                      datasets: [{
                        data: data,
                        backgroundColor: colors,
                        hoverOffset: 14
                      }]
                    },
                    options: {
                      plugins: {
                        legend: {
                          position: 'bottom',
                          labels: { color: 'white' }
                        },
                        datalabels: {
                          color: '#fff',
                          font: { weight: 'bold', size: 12 },
                          formatter: (val, ctx) => {
                            const total = ctx.chart.data.datasets[0].data
                              .reduce((a,b)=>a+b,0);
                            return ((val/total)*100).toFixed(1) + '%';
                          }
                        }
                      }
                    },
                    plugins: [ChartDataLabels]
                  });
                }
                
                pie('featureChart', ['Passed','Failed'], [1,0],
                    ['#2ecc71','#e74c3c']);
                
                pie('scenarioChart', ['Passed','Failed','Partial'], [3,0,0],
                    ['#2ecc71','#e74c3c','#9b59b6']);
                
                pie('stepChart',
                    ['Passed','Failed','Soft Fail','Skipped'],
                    [11,0,1,0],
                    ['#2ecc71','#e74c3c','#f39c12','#9b59b6']);
                
                pie('defectChart', ['Defective','Clean'], [1,11],
                    ['#e74c3c','#2ecc71']);
                </script>
                
                <div id="lightbox-overlay" onclick="closeLightbox()">
                    <img id="lightbox-image" />
                </div>
                
                """);
        sb.append("</body></html>");

        return sb.toString();
    }
}