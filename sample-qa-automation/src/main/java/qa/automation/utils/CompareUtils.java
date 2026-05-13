package qa.automation.utils;

import java.util.Objects;

public class CompareUtils {
    public static boolean eq(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    public static boolean eqIgnoreCase(String str1, String str2) {
        return str1.equalsIgnoreCase(str2);
    }
}
