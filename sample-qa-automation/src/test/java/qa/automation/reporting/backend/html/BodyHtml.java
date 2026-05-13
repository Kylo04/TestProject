package qa.automation.reporting.backend.html;

import qa.automation.reporting.model.*;
import qa.automation.reporting.model.enums.LogType;
import qa.automation.reporting.model.enums.Status;
import qa.automation.reporting.util.ScreenshotUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static qa.automation.reporting.context.ScenarioContext.deriveScenarioStatus;

public final class BodyHtml {

    public static String render(TestReport report) {

        StringBuilder sb = new StringBuilder();
        sb.append("<div class='container'>");
        sb.append("<div class='left'>");

        String projectRoot = System.getProperty("user.dir");
        String path = projectRoot + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testfiles" + File.separator + "inputFiles" + File.separator + "reportingFiles" + File.separator + "leftPaneBg.png";
        String base64Image = ScreenshotUtil.getBase64Image(path);
        sb.append("<style>");
        sb.append(".left::before { background-image: url('data:image/jpeg;base64,").append(base64Image).append("'); }");
        sb.append("</style>");

        for (FeatureNode f : report.features) {

            Status featureStatus = aggregateStatus(f.scenarios);

            sb.append("<div class='dropdown'>");
            sb.append("<div class='dropdown-header ")
                    .append(css(featureStatus))
                    .append("'>")
                    .append("<span class='arrow'>▶</span> Feature: ")
                    .append(f.name)
                    .append(" [")
                    .append(formatMinutes(f.durationSeconds()))
                    .append("]")
                    .append("</div>");
            sb.append("<div class='dropdown-content'>");

            Map<String, List<ScenarioNode>> grouped =
                    f.scenarios.stream()
                            .collect(Collectors.groupingBy(s -> s.name));

            for (Map.Entry<String, List<ScenarioNode>> entry : grouped.entrySet()) {

                String scenarioName = entry.getKey();
                List<ScenarioNode> runs = entry.getValue();
                Status scenarioStatus = aggregateStatus(runs);

                String id = scenarioName.replaceAll("\\s+", "_");

                sb.append("<label class='")
                        .append(css(scenarioStatus))
                        .append("'>")
                        .append("<input type='checkbox' checked ")
                        .append("onclick=\"toggleGroup('")
                        .append(id)
                        .append("')\"> ")
                        .append(scenarioName);
                if (runs.size() > 1) {
                    sb.append(" (Outline)");
                }
                sb.append("</label>");
            }
            sb.append("</div></div>");
        }
        sb.append("</div>");
        sb.append("<div class='right'>");

        for (FeatureNode f : report.features) {
            for (ScenarioNode s : f.scenarios) {

                String groupId = s.name.replaceAll("\\s+", "_");
                sb.append("<div class='scenario' data-group='")
                        .append(groupId)
                        .append("'>");
                sb.append("<h3 class='")
                        .append(css(deriveScenarioStatus(s.steps)))
                        .append("'>")
                        .append(s.name)
                        .append(" [")
                        .append(formatSeconds(s.durationSeconds()))
                        .append(" sec(s)]</h3>");

                for (StepNode step : s.steps) {
                    sb.append("<div class='dropdown'>");
                    sb.append("<div class='dropdown-header ")
                            .append(css(step.status))
                            .append("'>")
                            .append("<span class='arrow'>▶</span> ")
                            .append(step.title)
                            .append(" (")
                            .append(step.durationMillis())
                            .append(" ms)")
                            .append("</div>");
                    sb.append("<div class='dropdown-content'>");

                    for (LogEntry log : step.logs) {
                        sb.append("<div class='log ")
                                .append(css(log.type))
                                .append("'>")
                                .append(log.type)
                                .append(": ")
                                .append(log.message)
                                .append("</div>");

                        if (log.screenshotPath != null) {
                            String imgPath = log.screenshotPath.replace("\\", "/");
                            if (imgPath.contains("screenshots/")) {
                                imgPath = imgPath.substring(imgPath.indexOf("screenshots/"));
                            }
                            sb.append("<div class='log-screenshot'>")
                                    .append("<img class='screenshot-thumb' ")
                                    .append("src='").append(imgPath).append("' ")
                                    .append("onclick=\"openLightbox('").append(imgPath).append("')\" />")
                                    .append("</div>");
                        }
                    }
                    sb.append("</div></div>");
                }
                sb.append("</div>");
            }
        }
        sb.append("</div></div>");
        return sb.toString();
    }

    private static Status aggregateStatus(List<ScenarioNode> nodes) {
        boolean warn = false, partial = false, skipped = false;

        for (ScenarioNode s : nodes) {
            if (s.status == Status.FAILED) return Status.FAILED;
            if (s.status == Status.PARTIAL) partial = true;
            if (s.status == Status.PASSED_WITH_WARN) warn = true;
            if (s.status == Status.SKIPPED) skipped = true;
        }

        if (partial) return Status.PARTIAL;
        if (warn) return Status.PASSED_WITH_WARN;
        if (skipped) return Status.SKIPPED;
        return Status.PASSED;
    }

    private static String css(Status status) {
        return switch (status) {
            case FAILED -> "failed";
            case PARTIAL -> "partial";
            case PASSED_WITH_WARN -> "warning";
            case SKIPPED -> "skipped";
            default -> "passed";
        };
    }

    private static String css(LogType type) {
        return switch (type) {
            case INFO -> "info";
            case WARN -> "warning";
            case SOFT_FAIL -> "partial";
            case FAIL -> "failed";
            case SKIP -> "skipped";
            case PASS -> "passed";
        };
    }

    private static String formatMinutes(double seconds) {
        double minutes = seconds / 60.0;
        return String.format("%.2f min(s)", minutes);
    }

    private static String formatSeconds(double secs) {
        return String.format("%.2f", secs);
    }
}