package qa.automation.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
  public static void write(String path, String content) {
    try {
      File f = new File(path);
      if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
      try (FileOutputStream out = new FileOutputStream(f)) {
        out.write(content.getBytes(StandardCharsets.UTF_8));
      }
    } catch (Exception e) {
      throw new RuntimeException("Write failed: " + path, e);
    }
  }

    public static String read(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (Exception e) {
            throw new RuntimeException("Read failed: " + path, e);
        }
    }
}