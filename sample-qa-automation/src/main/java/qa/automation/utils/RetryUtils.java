package qa.automation.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public final class RetryUtils {
    private RetryUtils() {
    }

    private static final int DEFAULT_TRIES = 3;
    private static final long BASE_WAIT_MS = 200;
    private static final long MAX_WAIT_MS = 2000;

    private static final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public static void retry(Runnable action) {
        retry(action, DEFAULT_TRIES, BASE_WAIT_MS, t -> true);
    }

    public static void retry(Runnable action, int times, long baseWaitMs) {
        retry(action, times, baseWaitMs, t -> true);
    }
    public static List<Map<String, String>> fetchSyncDataOnVirtualThread(String dbQuery, Object... params) {
        try {
            var futureReceipt = executor.submit(() -> waitForData(
                    () -> DbUtils.getGrid(dbQuery,params),
                    grid -> !grid.isEmpty(),
                    10
            ));


            return futureReceipt.get();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch data", e);
        }
    }

    public static <T> T waitForData(Supplier<T> action, Predicate<T> successCondition, int maxSeconds) {
        long end = System.currentTimeMillis() + (maxSeconds * 1000);

        while (System.currentTimeMillis() < end) {
            T result = action.get();
            if (successCondition.test(result)) {
                return result;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        throw new RuntimeException("Data did not appear in DB after " + maxSeconds + " seconds.");
    }
    public static void retry(Runnable action, int times, long baseWaitMs, Predicate<Throwable> shouldRetry) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(shouldRetry);
        RuntimeException last = null;
        for (int attempt = 1; attempt <= times; attempt++) {
            try {
                action.run();
                return;
            } catch (RuntimeException e) {
                last = e;
                if (!shouldRetry.test(e) || attempt == times) break;
                sleep(backoff(baseWaitMs, attempt));
            }
        }
        throw Objects.requireNonNull(last);
    }

    public static <T> T retry(Supplier<T> supplier) {
        return retry(supplier, DEFAULT_TRIES, BASE_WAIT_MS, t -> true);
    }

    public static <T> T retry(Supplier<T> supplier, int times, long baseWaitMs) {
        return retry(supplier, times, baseWaitMs, t -> true);
    }

    public static <T> T retry(Supplier<T> supplier, int times, long baseWaitMs, Predicate<Throwable> shouldRetry) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(shouldRetry);
        RuntimeException last = null;
        for (int attempt = 1; attempt <= times; attempt++) {
            try {
                return supplier.get();
            } catch (RuntimeException e) {
                last = e;
                if (!shouldRetry.test(e) || attempt == times) break;
                sleep(backoff(baseWaitMs, attempt));
            }
        }
        throw Objects.requireNonNull(last);
    }

    public static void until(Supplier<Boolean> condition, int times, long baseWaitMs) {
        Objects.requireNonNull(condition);
        for (int attempt = 1; attempt <= times; attempt++) {
            if (condition.get()) return;
            if (attempt < times) sleep(backoff(baseWaitMs, attempt));
        }
    }

    @Deprecated
    public static <T> T retryGet(Supplier<T> s, int times, long waitMs) {
        return retry(s, times, waitMs);
    }

    private static long backoff(long base, int attempt) {
        long exp = Math.min(MAX_WAIT_MS, (long) (base * Math.pow(2, attempt - 1)));
        double j = 0.9 + ThreadLocalRandom.current().nextDouble() * 0.2;
        return (long) (exp * j);
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted", ie);
        }
    }
}