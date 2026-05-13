package qa.automation.helpers.comparors;

import qa.automation.enums.MatchMode;
import qa.automation.reporting.Reporter;
import qa.automation.utils.RetryUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class Comparor {

    public static <T> void match(T actual, T expected, String msg) {
        compare(actual, expected, msg, false);
    }

    public static <T> void matchWithRetry(T actual, T expected, String msg) {
        compare(actual, expected, msg, true);
    }

    private static <T> void compare(T actual, T expected, String msg, boolean retry) {
        Runnable logic = () -> {
            String formatted = String.format("%s: ACTUAL=%s, EXPECTED=%s", msg, actual, expected);
            if (Objects.equals(actual, expected))
                Reporter.pass("Matched successfully: " + formatted);
            else
                Reporter.softFail("Match failed: " + formatted);
        };

        if (retry) RetryUtils.retry(logic);
        else logic.run();
    }

    public static void containsStrings(String main, Set<String> subs, boolean useRegex,
                                          boolean ignoreCase, MatchMode mode) {
        String msg = String.format("Expected %s to contain %s", main, subs);
        if( (mode == MatchMode.ALL)
                ? subs.stream().allMatch(s -> matchSub(main, s, useRegex, ignoreCase))
                : subs.stream().anyMatch(s -> matchSub(main, s, useRegex, ignoreCase))){
            Reporter.pass("Matched successfully: " + msg);
        }else{
            Reporter.softFail("Match failed: " + msg);
        }
    }

    public static void containsString(String mainString, String substring) {
        containsStrings(mainString, Collections.singleton(substring), false, true, MatchMode.ANY);
    }

    private static boolean matchSub(String main, String sub, boolean regex, boolean ignoreCase) {
        if (ignoreCase) {
            main = main.toLowerCase();
            sub = sub.toLowerCase();
        }
        return regex
                ? Pattern.compile(sub, ignoreCase ? Pattern.CASE_INSENSITIVE : 0).matcher(main).find()
                : main.contains(sub);
    }

    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            Reporter.fail(msg + " is null");
        } else {
            Reporter.info(msg + " is not null");
        }
    }

    public static void isTrue(Boolean bool, String msg) {
        if (!bool) {
            Reporter.fail(msg + " is not true");
        } else {
            Reporter.info(msg + " is true");
        }
    }
}