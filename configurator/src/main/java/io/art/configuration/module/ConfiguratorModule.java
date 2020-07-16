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
    private static final StatelessModuleProvider<ConfiguratorModuleConfiguration> configuratorModule = context().getModule(ConfiguratorModule.class.getSimpleName());

    @Override
    public void onLoad() {
        configure(configurator -> configurator
                .from(new EnvironmentConfigurationSource())
                .from(new PropertiesConfigurationSource())
        );
        FILE_CONFIGURATION_EXTENSIONS.stream()
                .map(extension -> ConfiguratorModule.class.getClassLoader().getResource(DEFAULT_MODULE_CONFIGURATION_FILE + DOT + extension))
                .filter(Objects::nonNull)
                .map(resource -> new FileConfigurationSource(new File(ExceptionHandler.<URI>wrapException(InternalRuntimeException::new).call(resource::toURI))))
                .forEach(source -> configure(configurator -> configurator.from(source)));
        EnvironmentConfigurationSource environment = getConfiguration().getEnvironment();
        PropertiesConfigurationSource properties = getConfiguration().getProperties();
        ofNullable(environment.getString(MODULE_CONFIG_FILE_ENVIRONMENT))
                .map(path -> get(path).toFile())
                .filter(File::exists)
                .ifPresent(file -> configure(configurator -> configurator.from(new FileConfigurationSource(file))));
        environment.getStringList(MODULE_CONFIG_FILES_ENVIRONMENT)
                .stream()
                .map(path -> get(path).toFile())
                .filter(File::exists)
                .forEach(file -> configure(configurator -> configurator.from(new FileConfigurationSource(file))));
        ofNullable(properties.getString(MODULE_CONFIG_FILE_PROPERTY))
                .map(path -> get(path).toFile())
                .filter(File::exists)
                .ifPresent(file -> configure(configurator -> configurator.from(new FileConfigurationSource(file))));
        properties.getStringList(MODULE_CONFIG_FILES_PROPERTY)
                .stream()
                .map(path -> get(path).toFile())
                .filter(File::exists)
                .forEach(file -> configure(configurator -> configurator.from(new FileConfigurationSource(file))));
    }

    public static StatelessModuleProvider<ConfiguratorModuleConfiguration> configuratorModule() {
        return getConfiguratorModule();
    }

    public static void main(String[] args) {
        context().loadModule(new ConfiguratorModule());
        System.out.println(configuratorModule().getConfiguration());
    }
}
