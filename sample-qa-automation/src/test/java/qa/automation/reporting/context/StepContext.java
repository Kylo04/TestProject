package qa.automation.reporting.context;

import qa.automation.reporting.model.StepNode;
import qa.automation.reporting.model.enums.Status;

public final class StepContext {

    private static final InheritableThreadLocal<StepNode> CURRENT =
            new InheritableThreadLocal<>();

    private StepContext() {
    }

    public static void set(StepNode step) {
        CURRENT.set(step);
    }

    public static StepNode current() {
        StepNode step = CURRENT.get();
        if (step == null) {
            throw new IllegalStateException("No active step in StepContext");
        }
        return step;
    }

    public static StepNode currentNullable() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }

    public static void upgradeStatus(Status newStatus) {
        StepNode step = current();

        if (newStatus == Status.FAILED) {
            step.status = Status.FAILED;
            return;
        }

        if (newStatus == Status.PARTIAL &&
                (step.status == Status.PASSED || step.status == Status.PASSED_WITH_WARN)) {
            step.status = Status.PARTIAL;
            return;
        }

        if (newStatus == Status.PASSED_WITH_WARN &&
                step.status == Status.PASSED) {
            step.status = Status.PASSED_WITH_WARN;
        }
    }
}