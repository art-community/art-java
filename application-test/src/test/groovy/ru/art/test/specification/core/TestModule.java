package ru.art.test.specification.core;

import lombok.*;
import ru.art.core.module.Module;
import ru.art.core.module.*;
import ru.art.logging.*;
import static lombok.AccessLevel.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.test.specification.core.TestModule.TestModuleConfiguration.*;
import java.util.*;

@Getter
public class TestModule implements Module<TestModule.TestModuleConfiguration, TestModule.TestModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static TestModuleConfiguration testModule = context().getModule(TestModule.class.getName(), TestModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private final static TestModuleState testModuleState = context().getModuleState(TestModule.class.getName(), TestModule::new);
    private final String id = TestModule.class.getName();
    private final TestModuleConfiguration defaultConfiguration = new TestModuleDefaultConfiguration();
    private final TestModuleState state = new TestModuleState();

    public static TestModuleConfiguration testModule() {
        if (contextIsNotReady()) {
            loggingModule().getLogger(TestModule.class).info("Context is not ready. Using default configuration");
            return new TestModuleDefaultConfiguration();
        }
        return getTestModule();
    }

    public static TestModuleState testModuleState() {
        return getTestModuleState();
    }


    interface TestModuleConfiguration extends ModuleConfiguration {
        String FROM_FILE = "from file";
        String DEFAULT = "default";
        String OVERRIDE = "override";

        String getValue();

        @Getter
        class TestModuleFileConfiguration implements TestModuleConfiguration {
            private String value = configString("test", "value");
        }

        @Getter
        class TestModuleDefaultConfiguration implements TestModuleConfiguration {
            private String value = DEFAULT;
        }
    }

    @Getter
    public static class TestModuleState implements ModuleState {
        public List<String> collection = dynamicArrayOf();

        public void add(String element) {
            collection.add(element);
        }
    }
}