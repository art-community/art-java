package ru.art.example.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import static ru.art.config.extensions.ConfigExtensions.configInt;
import static ru.art.config.extensions.ConfigExtensions.configString;
import static ru.art.example.constants.ExampleAppModuleConstants.ConfigKeys.*;
import static ru.art.example.constants.ExampleAppModuleConstants.DefaultConfigValues.DEFAULT_INT_CONFIG;
import static ru.art.example.constants.ExampleAppModuleConstants.DefaultConfigValues.DEFAULT_STRING_CONFIG;

/**
 * Module configuration
 * Made for ability of changing parameters in runtime
 */
public interface ExampleModuleConfiguration extends ModuleConfiguration {

    String getConfigExampleStringValue();

    int getConfigExampleIntValue();

    @Getter
    class ExampleModuleDefaultConfiguration implements ExampleModuleConfiguration {
        private final String configExampleStringValue = DEFAULT_STRING_CONFIG;
        private final int configExampleIntValue = DEFAULT_INT_CONFIG;
    }

    @Getter
    class ExampleModuleAgileConfiguration implements ExampleModuleConfiguration {
        private String configExampleStringValue;
        private int configExampleIntValue;

        public ExampleModuleAgileConfiguration() {
            refresh();
        }

        @Override
        public void refresh() {
            configExampleStringValue = configString(EXAMPLE_MODULE_CONFIG_SECTION_ID, STRING_CONFIG_FIELD, DEFAULT_STRING_CONFIG);
            configExampleIntValue = configInt(EXAMPLE_MODULE_CONFIG_SECTION_ID, INT_CONFIG_FIELD, DEFAULT_INT_CONFIG);
        }
    }
}
