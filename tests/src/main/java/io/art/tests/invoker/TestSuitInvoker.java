package io.art.tests.invoker;

import io.art.logging.*;
import io.art.meta.exception.*;
import io.art.meta.invoker.*;
import io.art.tests.configuration.*;
import lombok.experimental.*;
import static com.google.common.base.Throwables.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.tests.constants.TestsModuleConstants.Messages.*;
import static io.art.tests.constants.TestsModuleConstants.Methods.*;
import static java.text.MessageFormat.*;

@UtilityClass
public class TestSuitInvoker {
    public static void invokeTestSuit(TestSuitConfiguration suit) {
        apply(suit.getSetupInvoker(), MetaMethodInvoker::invoke);
        for (TestSuitConfiguration.TestConfiguration test : suit.getTests().values()) {
            apply(suit.getBeforeTestInvoker(), MetaMethodInvoker::invoke);
            String name = test.getTestInvoker().getDelegate().name();
            name = name.substring(name.indexOf(TEST_METHOD_PREFIX) + 1);
            try {
                logStart(suit, name);
                test.getTestInvoker().invoke();
                logComplete(suit, name);
            } catch (MetaException metaException) {
                logError(suit, metaException.getCause(), name);
            }
            apply(suit.getAfterTestInvoker(), MetaMethodInvoker::invoke);
        }
        apply(suit.getCleanupInvoker(), MetaMethodInvoker::invoke);
    }

    private static void logStart(TestSuitConfiguration suit, String test) {
        String message = format(TEST_INVOCATION_MESSAGE, test);
        if (withLogging()) {
            Logging.logger(suit.getDefinition().definition().type()).info(message);
            return;
        }
        System.out.println(message);
    }

    private static void logError(TestSuitConfiguration suit, Throwable cause, String test) {
        String message = format(TEST_COMPLETED_MESSAGE, test, getStackTraceAsString(cause));
        if (withLogging()) {
            Logging.logger(suit.getDefinition().definition().type()).error(message);
            return;
        }
        System.err.println(message);
    }

    private static void logComplete(TestSuitConfiguration suit, String test) {
        String message = format(TEST_COMPLETED_MESSAGE, test);
        if (withLogging()) {
            Logging.logger(suit.getDefinition().definition().type()).info(message);
            return;
        }
        System.out.println(message);
    }
}
