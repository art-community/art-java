/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.model.customizer;

import io.art.configurator.configuration.*;
import io.art.configurator.custom.*;
import lombok.*;

public class ConfiguratorCustomizer {
    @Getter
    private final Custom configuration = new Custom();

    public ConfiguratorCustomizer registry(CustomConfigurationRegistry registry) {
        configuration.customConfigurations = registry;
        return this;
    }

    private static class Custom extends ConfiguratorModuleConfiguration {
        private CustomConfigurationRegistry customConfigurations;
    }
}
