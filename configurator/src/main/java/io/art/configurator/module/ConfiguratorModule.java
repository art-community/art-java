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

package io.art.configurator.module;

import io.art.configurator.module.ConfiguratorModuleConfiguration.*;
import io.art.configurator.source.*;
import io.art.core.module.*;
import lombok.*;
import static io.art.configurator.constants.ConfiguratorConstants.ConfiguratorKeys.*;
import static io.art.core.context.Context.*;
import static java.nio.file.Paths.*;
import static java.util.Optional.*;

@Getter
public class ConfiguratorModule implements StatelessModule<ConfiguratorModuleConfiguration, Configurator> {
    private final String id = ConfiguratorModule.class.getSimpleName();
    private final ConfiguratorModuleConfiguration configuration = new ConfiguratorModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    @Getter(lazy = true)
    private static final StatelessModuleProvider<ConfiguratorModuleConfiguration> configuratorModule = context().getModule(ConfiguratorModule.class.getSimpleName());

    @Override
    public void onLoad() {
        configure(configurator -> configurator
                .from(new EnvironmentConfigurationSource())
                .from(new PropertiesConfigurationSource())
        );
        EnvironmentConfigurationSource environment = getConfiguration().getEnvironment();
        PropertiesConfigurationSource properties = getConfiguration().getProperties();
        ofNullable(environment.getString(MODULE_CONFIG_FILE_ENVIRONMENT))
                .filter(path -> get(path).toFile().exists())
                .ifPresent(path -> configure(configurator -> configurator.from(new FileConfigurationSource(path))));
        environment.getStringList(MODULE_CONFIG_FILES_ENVIRONMENT)
                .stream()
                .filter(path -> get(path).toFile().exists())
                .forEach(path -> configure(configurator -> configurator.from(new FileConfigurationSource(path))));
        ofNullable(properties.getString(MODULE_CONFIG_FILE_PROPERTY))
                .filter(path -> get(path).toFile().exists())
                .ifPresent(path -> configure(configurator -> configurator.from(new FileConfigurationSource(path))));
        properties.getStringList(MODULE_CONFIG_FILES_PROPERTY)
                .stream()
                .filter(path -> get(path).toFile().exists())
                .forEach(path -> configure(configurator -> configurator.from(new FileConfigurationSource(path))));
    }

    public static StatelessModuleProvider<ConfiguratorModuleConfiguration> configuratorModule() {
        return getConfiguratorModule();
    }

    public static void main(String[] args) {
        context().loadModule(new ConfiguratorModule());
        System.out.println(configuratorModule().getConfiguration());
    }
}
