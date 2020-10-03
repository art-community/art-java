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

import io.art.configurator.configuration.*;
import io.art.configurator.configuration.ConfiguratorModuleConfiguration.*;
import io.art.configurator.source.*;
import io.art.core.checker.*;
import io.art.core.module.*;
import lombok.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorKeys.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.FileConfigurationExtensions.*;
import static io.art.core.caster.Caster.cast;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.extensions.FileExtensions.*;
import static java.nio.file.Paths.*;
import java.io.*;
import java.util.*;

@Getter
public class ConfiguratorModule implements StatelessModule<ConfiguratorModuleConfiguration, Configurator> {
    private final String id = ConfiguratorModule.class.getSimpleName();
    private final ConfiguratorModuleConfiguration configuration = new ConfiguratorModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    @Getter(lazy = true)
    private static final StatelessModuleProxy<ConfiguratorModuleConfiguration> configuratorModule = context().getStatelessModule(ConfiguratorModule.class.getSimpleName());

    public StatelessModuleProxy<ConfiguratorModuleConfiguration> loadConfigurations() {
        configure(configurator -> configurator
                .from(new EnvironmentConfigurationSource())
                .from(new PropertiesConfigurationSource())
        );
        ClassLoader loader = ConfiguratorModule.class.getClassLoader();
        FILE_CONFIGURATION_EXTENSIONS.stream()
                .map(extension -> loader.getResource(DEFAULT_MODULE_CONFIGURATION_FILE + DOT + extension))
                .filter(Objects::nonNull)
                .findFirst()
                .map(resource -> new FileConfigurationSource(RESOURCES_FILE, fileOf(resource)))
                .ifPresent(source -> configure(configurator -> configurator.from(source)));
        EnvironmentConfigurationSource environment = getConfiguration().getEnvironment();
        PropertiesConfigurationSource properties = getConfiguration().getProperties();
        configureByFile(addFirstToList(environment.getString(MODULE_CONFIG_FILE_ENVIRONMENT), environment.getStringList(MODULE_CONFIG_FILES_ENVIRONMENT)));
        configureByFile(addFirstToList(properties.getString(MODULE_CONFIG_FILE_PROPERTY), properties.getStringList(MODULE_CONFIG_FILES_PROPERTY)));
        return new StatelessModuleProxy<>(this);
    }

    private void configureByFile(List<String> paths) {
        paths
                .stream()
                .filter(EmptinessChecker::isNotEmpty)
                .map(path -> get(path).toFile())
                .filter(File::exists)
                .forEach(file -> configure(configurator -> configurator.from(new FileConfigurationSource(CUSTOM_FILE, file))));
    }

    public static StatelessModuleProxy<ConfiguratorModuleConfiguration> configuratorModule() {
        return getConfiguratorModule();
    }
}
