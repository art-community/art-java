/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.configurator.module;

import io.art.configurator.configuration.*;
import io.art.configurator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.property.*;
import lombok.*;
import static io.art.core.constants.StringConstants.*;

@Public
public class ConfiguratorInitializer implements ModuleInitializer<ConfiguratorModuleConfiguration, ConfiguratorModuleConfiguration.Configurator, ConfiguratorModule> {
    private final CustomConfigurationsConfigurator registry = new CustomConfigurationsConfigurator();

    public ConfiguratorInitializer configuration(Class<?> type) {
        return configuration(EMPTY_STRING, type);
    }

    public ConfiguratorInitializer configuration(String section, Class<?> type) {
        registry.register(new CustomConfiguration(section, type));
        return this;
    }

    public ConfiguratorModuleConfiguration initialize(ConfiguratorModule module) {
        return new Initial(registry.configure(module.orderedSources()));
    }

    @Getter
    @RequiredArgsConstructor
    private static class Initial extends ConfiguratorModuleConfiguration {
        private final ImmutableMap<CustomConfiguration, Property<?>> customConfigurations;
    }
}
