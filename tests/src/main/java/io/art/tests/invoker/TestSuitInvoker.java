package io.art.tests.invoker;

import io.art.tests.configuration.*;
import lombok.experimental.*;

@UtilityClass
public class TestSuitInvoker {
    public static void invokeTestSuit(TestSuitConfiguration suit) {
        suit.getSetupInvoker().invoke();
        for (TestSuitConfiguration.TestConfiguration test : suit.getTests().values()) {
            suit.getBeforeTestInvoker().invoke();
            test.getTestInvoker().invoke();
            suit.getAfterTestInvoker().invoke();
        }
        suit.getCleanupInvoker().invoke();
    }
}
