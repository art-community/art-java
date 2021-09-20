package io.art.tests.invoker;

import io.art.logging.*;
import io.art.meta.exception.*;
import io.art.tests.*;
import io.art.tests.configuration.*;
import lombok.experimental.*;
import static com.google.common.base.Throwables.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.handler.CauseHandler.*;

@UtilityClass
public class TestSuitInvoker {
    public static void invokeTestSuit(TestSuitConfiguration suit) {
        TestSuit instance = cast(suit.getDefinition().creator().instantiate().create());
        instance.setup();
        for (TestSuitConfiguration.TestConfiguration test : suit.getTests().values()) {
            instance.beforeTest();
            try {
                test.getTestInvoker().invoke();
            } catch (MetaException metaException) {
                handleCause(metaException).consume(AssertionError.class, error -> logError(suit, error));
            }
            instance.afterTest();
        }
        instance.cleanup();
    }

    private static void logError(TestSuitConfiguration suit, AssertionError error) {
        if (withLogging()) {
            Logging.logger(suit.getDefinition().definition().type()).error(error.getMessage(), error);
            return;
        }
        System.err.println(getStackTraceAsString(error));
    }
}
