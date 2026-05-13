package qa.automation.utils;

public class FrontUtils {
  public static String normalizeText(String s) {
    if (s == null) return null;
    return s.replace("\u00A0"," ").trim().replaceAll("\\s+", " ");
  }
}