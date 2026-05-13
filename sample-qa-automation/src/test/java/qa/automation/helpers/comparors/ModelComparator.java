package qa.automation.helpers.comparors;

import qa.automation.models.ModelClass;
import qa.automation.models.comparators.DiffResult;
import qa.automation.reporting.Reporter;
import qa.automation.utils.CompareUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

public class ModelComparator {

  public static DiffResult compare(ModelClass expected, ModelClass actual) {
    DiffResult r = new DiffResult(true);
    if (!CompareUtils.eq(expected.id, actual.id)) { r.equal = false; r.diffs.put("id", diff(expected.id, actual.id)); }
    if (!CompareUtils.eqIgnoreCase(expected.name, actual.name)) { r.equal = false; r.diffs.put("name", diff(expected.name, actual.name)); }
    if (!CompareUtils.eqIgnoreCase(expected.status, actual.status)) { r.equal = false; r.diffs.put("status", diff(expected.status, actual.status)); }
    return r;
  }


    public static <T> void compare(T expected, T actual) {
        DiffResult r = new DiffResult(true);

        if (expected == actual) {
            return;
        }

        if (expected == null || actual == null) {
            r.equal = false;
            r.diffs.put("Object", "Expected: " + expected + ", Actual: " + actual);
            Reporter.fail("One of the objects is null. Differences: " + r.diffs);
        }

        Class<?> clazz = expected.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                continue;
            }

            try {
                field.setAccessible(true);
                Object valExp = field.get(expected);
                Object valAct = field.get(actual);

                // Compare simple values (Strings, Integers, etc.)
                if (!Objects.equals(valExp, valAct)) {
                    r.equal = false;
                    r.diffs.put(field.getName(), "Expected: '" + valExp + "', Actual: '" + valAct + "'");
                }
            } catch (IllegalAccessException e) {
                Reporter.fail("Test framework could not access field: " + field.getName());

            }
        }

        if (!r.equal) {
            Reporter.fail("Objects did not match! Differences: \n" + r.diffs);
        }

    }

  private static String diff(Object e, Object a) {
    return "expected=" + e + " | actual=" + a;
  }
}