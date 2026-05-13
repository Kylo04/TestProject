package qa.automation.pages;

import com.microsoft.playwright.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PageManager {

  private final Page page;

  private final Map<Class<?>, Object> cache = new HashMap<>();

  public PageManager(Page page) {
    this.page = page;
  }

  private <T> T get(Class<T> cls, Supplier<T> creator) {
    return cls.cast(cache.computeIfAbsent(cls, c -> creator.get()));
  }

  public CommonPage common() {
    return get(CommonPage.class, () -> new CommonPage(page));
  }

  public String resolve(String dotted) {
    String[] parts = dotted.split("\\.");
    if (parts.length != 2)
      throw new IllegalArgumentException("Use Page.key naming format");

    String p = parts[0].toLowerCase();
    String key = parts[1];

    return switch (p) {
      case "common"        -> common().resolve(key);

      default -> throw new IllegalArgumentException("Unknown page: " + p);
    };
  }

  public String selectRaceCategory(String raceCategory) {
      if (raceCategory.equalsIgnoreCase("Horses"))
          return "common.category-hr";
      else if (raceCategory.equalsIgnoreCase("Dogs"))
          return "common.category-dg";
      else
          throw new IllegalArgumentException("Unsupported race category: " + raceCategory);
  }
}