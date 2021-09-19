package io.art.tests.configuration;

import io.art.core.collection.*;
import io.art.meta.invoker.*;
import lombok.*;

@Getter
@Builder
public class TestSuitConfiguration {
    private final MetaMethodInvoker setupInvoker;
    private final MetaMethodInvoker beforeTestInvoker;
    private final MetaMethodInvoker cleanupInvoker;
    private final MetaMethodInvoker afterTestInvoker;
    private final ImmutableMap<String, TestConfiguration> tests;

    @Getter
    @Builder
    public static class TestConfiguration {
        private final MetaMethodInvoker testInvoker;
    }
}
