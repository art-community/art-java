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

package io.art.configuration.module;

import io.art.configuration.module.ConfiguratorModuleConfiguration.*;
import io.art.configuration.source.*;
import io.art.core.exception.*;
import io.art.core.handler.*;
import io.art.core.module.*;
import lombok.*;
import static io.art.configuration.constants.ConfiguratorConstants.ConfigurationSourceType.CUSTOM_FILE;
import static io.art.configuration.constants.ConfiguratorConstants.ConfigurationSourceType.RESOURCES_FILE;
import static io.art.configuration.constants.ConfiguratorConstants.ConfiguratorKeys.*;
import static io.art.configuration.constants.ConfiguratorConstants.*;
import static io.art.configuration.constants.ConfiguratorConstants.FileConfigurationExtensions.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static java.nio.file.Paths.*;
import static java.util.Optional.*;
import java.io.*;
import java.net.*;
import java.util.*;

@Getter
public class ConfiguratorModule implements StatelessModule<ConfiguratorModuleConfiguration, Configurator> {
    private final String id = ConfiguratorModule.class.getSimpleName();
    private final ConfiguratorModuleConfiguration configuration = new ConfiguratorModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    @Getter(lazy = true)
    private static final StatelessModuleProxy<ConfiguratorModuleConfiguration> configuratorModule = context().getStatelessModule(ConfiguratorModule.class.getSimpleName());

    @Override
    public void onLoad() {
        configure(configurator -> configurator
                .from(new EnvironmentConfigurationSource())
                .from(new PropertiesConfigurationSource())
        );
        ClassLoader loader = ConfiguratorModule.class.getClassLoader();
        FILE_CONFIGURATION_EXTENSIONS.stream()
                .map(extension -> loader.getResource(DEFAULT_MODULE_CONFIGURATION_FILE + DOT + extension))
                .filter(Objects::nonNull)
                .findFirst()
                .map(resource -> new FileConfigurationSource(RESOURCES_FILE, new File(ExceptionHandler.<URI>wrapException(InternalRuntimeException::new).call(resource::toURI))))
                .ifPresent(source -> configure(configurator -> configurator.from(source)));
        EnvironmentConfigurationSource environment = getConfiguration().getEnvironment();
        PropertiesConfigurationSource properties = getConfiguration().getProperties();
        configureByFile(environment.getString(MODULE_CONFIG_FILE_ENVIRONMENT), environment.getStringList(MODULE_CONFIG_FILES_ENVIRONMENT));
        configureByFile(properties.getString(MODULE_CONFIG_FILE_PROPERTY), properties.getStringList(MODULE_CONFIG_FILES_PROPERTY));
    }

    private void configureByFile(String singlePath, List<String> multiplePaths) {
        ofNullable(singlePath)
                .map(path -> get(path).toFile())
                .filter(File::exists)
                .ifPresent(file -> configure(configurator -> configurator.from(new FileConfigurationSource(CUSTOM_FILE, file))));
        multiplePaths
                .stream()
                .map(path -> get(path).toFile())
                .filter(File::exists)
                .forEach(file -> configure(configurator -> configurator.from(new FileConfigurationSource(CUSTOM_FILE, file))));
    }

    public static StatelessModuleProxy<ConfiguratorModuleConfiguration> configuratorModule() {
        return getConfiguratorModule();
    }
}
