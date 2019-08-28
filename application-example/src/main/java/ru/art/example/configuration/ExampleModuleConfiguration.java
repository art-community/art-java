/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.example.configuration;

import lombok.*;
import ru.art.core.module.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.example.constants.ExampleAppModuleConstants.ConfigKeys.*;
import static ru.art.example.constants.ExampleAppModuleConstants.DefaultConfigValues.*;

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
