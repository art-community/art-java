package io.art.tests.configuration;

import io.art.core.collection.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.tests.*;
import lombok.*;

@Getter
@Builder
public class TestSuitConfiguration {
    private final MetaClass<? extends TestSuit> definition;
    private final ImmutableMap<String, TestConfiguration> tests;

    @Getter
    @Builder
    public static class TestConfiguration {
        private final MetaMethodInvoker testInvoker;
    }
}
