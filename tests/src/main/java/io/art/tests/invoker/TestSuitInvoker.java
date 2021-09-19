package io.art.tests.invoker;

import io.art.meta.invoker.*;
import io.art.tests.configuration.*;
import lombok.experimental.*;
import static io.art.core.checker.NullityChecker.*;

@UtilityClass
public class TestSuitInvoker {
    public static void invokeTestSuit(TestSuitConfiguration suit) {
        apply(suit.getSetupInvoker(), MetaMethodInvoker::invoke);
        for (TestSuitConfiguration.TestConfiguration test : suit.getTests().values()) {
            apply(suit.getBeforeTestInvoker(), MetaMethodInvoker::invoke);
            test.getTestInvoker().invoke();
            apply(suit.getAfterTestInvoker(), MetaMethodInvoker::invoke);
        }
        apply(suit.getCleanupInvoker(), MetaMethodInvoker::invoke);
    }
}
