package qa.automation.reporting.util;

import com.microsoft.playwright.Page;
import qa.automation.reporting.Reporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public final class ScreenshotUtil {

    private ScreenshotUtil() { }

    public static String capture(Page page) {
        try {
            Files.createDirectories(Paths.get("reports/screenshots"));

            String fileName = "screenshot_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")) + ".png";
            Path path = Paths.get("reports/screenshots", fileName);

            page.screenshot(new Page.ScreenshotOptions().setPath(path).setFullPage(true));

            return path.toString();

        } catch (IOException e) {
            throw new RuntimeException("Unable to capture screenshot", e);
        }
    }

    public static String getBase64Image(String path) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(path));
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            Reporter.softFail("Could not find background image at: " + path);
            return "";
        }
    }
}
