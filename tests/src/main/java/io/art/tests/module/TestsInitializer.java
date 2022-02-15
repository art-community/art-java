package io.art.tests.module;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.tests.*;
import io.art.tests.configuration.*;
import io.art.tests.configuration.TestSuitConfiguration.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.Meta.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static io.art.tests.constants.TestsModuleConstants.Methods.*;
import java.util.*;

@Public
public class TestsInitializer implements ModuleInitializer<TestsModuleConfiguration, TestsModuleConfiguration.Configurator, TestsModule> {
    private final Set<Class<? extends Tests>> suitDefinitions = set();

    public TestsInitializer suit(Class<? extends Tests> suitClass) {
        suitDefinitions.add(suitClass);
        return this;
    }

    @Override
    public TestsModuleConfiguration initialize(TestsModule module) {
        return new Initial(lazy(this::createSuits));
    }

    private ImmutableMap<MetaClass<? extends Tests>, TestSuitConfiguration> createSuits() {
        Map<MetaClass<? extends Tests>, TestSuitConfiguration> suits = map();
        for (Class<? extends Tests> definition : suitDefinitions) {
            MetaClass<? extends Tests> suitMeta = declaration(definition);
            if (!suitMeta.definition().modifiers().contains(TESTS)) continue;
            TestSuitConfigurationBuilder suitBuilder = TestSuitConfiguration.builder().definition(cast(suitMeta));
            Map<String, TestConfiguration> tests = map();
            for (MetaMethod<MetaClass<?>, ?> method : suitMeta.methods()) {
                if (!method.parameters().isEmpty()) continue;
                switch (method.name()) {
                    case SETUP_METHOD_NAME:
                        suitBuilder.setupInvoker(new MetaMethodInvoker(suitMeta, method));
                        break;
                    case CLEANUP_METHOD_NAME:
                        suitBuilder.cleanupInvoker(new MetaMethodInvoker(suitMeta, method));
                        break;
                    case BEFORE_TEST_METHOD_NAME:
                        suitBuilder.beforeTestInvoker(new MetaMethodInvoker(suitMeta, method));
                        break;
                    case AFTER_TEST_METHOD_NAME:
                        suitBuilder.afterTestInvoker(new MetaMethodInvoker(suitMeta, method));
                        break;
                    default:
                        if (method.name().startsWith(TEST_METHOD_PREFIX)) {
                            tests.put(method.name(), TestConfiguration.builder().testInvoker(new MetaMethodInvoker(suitMeta, method)).build());
                        }
                        break;
                }
            }
            suits.put(suitMeta, suitBuilder.tests(immutableMapOf(tests)).build());
        }
        return immutableMapOf(suits);
    }

    @Getter
    @RequiredArgsConstructor
    private static class Initial extends TestsModuleConfiguration {
        private final LazyProperty<ImmutableMap<MetaClass<? extends Tests>, TestSuitConfiguration>> suits;
    }
}
