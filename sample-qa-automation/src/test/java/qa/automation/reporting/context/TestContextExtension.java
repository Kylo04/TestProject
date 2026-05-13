package qa.automation.reporting.context;

import org.junit.jupiter.api.extension.*;

public class TestContextExtension implements BeforeEachCallback, AfterEachCallback {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(TestContextExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        context.getStore(NAMESPACE).put(TestContext.class, new TestContext());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        context.getStore(NAMESPACE).remove(TestContext.class);
    }

    public static TestContext get(ExtensionContext context) {
        return context.getStore(NAMESPACE).get(TestContext.class, TestContext.class);
    }
}
