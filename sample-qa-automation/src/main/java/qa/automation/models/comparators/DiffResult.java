package qa.automation.models.comparators;

import java.util.LinkedHashMap;
import java.util.Map;

public class DiffResult {
  public boolean equal;
  public final Map<String, String> diffs = new LinkedHashMap<>();

  public DiffResult(boolean equal) { this.equal = equal; }
}