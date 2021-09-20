package io.art.tests.invoker;

import io.art.logging.*;
import io.art.meta.exception.*;
import io.art.meta.invoker.*;
import io.art.tests.configuration.*;
import lombok.experimental.*;
import static com.google.common.base.Throwables.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.handler.CauseHandler.*;

@UtilityClass
public class TestSuitInvoker {
    public static void invokeTestSuit(TestSuitConfiguration suit) {
        apply(suit.getSetupInvoker(), MetaMethodInvoker::invoke);
        for (TestSuitConfiguration.TestConfiguration test : suit.getTests().values()) {
            apply(suit.getBeforeTestInvoker(), MetaMethodInvoker::invoke);
            try {
                test.getTestInvoker().invoke();
            } catch (MetaException metaException) {
                handleCause(metaException)
                        .consume(AssertionError.class, error -> logError(suit, error))
                        .get();
            }
            apply(suit.getAfterTestInvoker(), MetaMethodInvoker::invoke);
        }
        apply(suit.getCleanupInvoker(), MetaMethodInvoker::invoke);
    }

    private static void logError(TestSuitConfiguration suit, AssertionError error) {
        if (withLogging()) {
            Logging.logger(suit.getDefinition()).error(error.getMessage(), error);
            return;
        }
        System.err.println(getStackTraceAsString(error));
    }
}
