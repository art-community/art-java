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
import io.art.configurator.exception.*;
import io.art.configurator.model.*;
import io.art.configurator.source.*;
import io.art.core.checker.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorKeys.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.FileConfigurationExtensions.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static java.nio.file.Paths.*;
import static lombok.AccessLevel.*;
import java.io.*;
import java.util.*;

@Getter
public class ConfiguratorModule implements StatelessModule<ConfiguratorModuleConfiguration, Configurator> {
    private final String id = ConfiguratorModule.class.getSimpleName();
    private final ConfiguratorModuleConfiguration configuration = new ConfiguratorModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<ConfiguratorModuleConfiguration> configuratorModule = context().getStatelessModule(ConfiguratorModule.class.getSimpleName());

    public ImmutableArray<ConfigurationSource> orderedSources() {
        return configuration.orderedSources();
    }

    public ConfiguratorModule configure() {
        configure(configurator -> configurator
                .from(new EnvironmentConfigurationSource())
                .from(new PropertiesConfigurationSource())
        );
        ClassLoader loader = ConfiguratorModule.class.getClassLoader();
        FILE_CONFIGURATION_EXTENSIONS.stream()
                .map(extension -> loader.getResource(DEFAULT_MODULE_CONFIGURATION_FILE + DOT + extension))
                .filter(Objects::nonNull)
                .findFirst()
                .map(resource -> new FileConfigurationSource(EMPTY_STRING, RESOURCES_FILE, fileOf(resource)))
                .ifPresent(source -> configure(configurator -> configurator.from(source)));
        EnvironmentConfigurationSource environment = getConfiguration().getEnvironment();
        PropertiesConfigurationSource properties = getConfiguration().getProperties();
        List<String> configFilePaths = environment.getStringArray(MODULE_CONFIG_FILES_ENVIRONMENT).toMutable();
        configFilePaths.addAll(properties.getStringArray(MODULE_CONFIG_FILES_PROPERTY).toMutable());
        configureByFile(configFilePaths);
        return this;
    }

    @Override
    public void beforeReload() {
        orderedSources().forEach(ConfigurationSource::refresh);
    }

    private void configureByFile(List<String> paths) {
        paths
                .stream()
                .filter(EmptinessChecker::isNotEmpty)
                .map(path -> get(path).toFile())
                .filter(File::exists)
                .forEach(file -> configure(configurator -> configurator.from(new FileConfigurationSource(EMPTY_STRING, CUSTOM_FILE, file))));
    }

    public static ConfigurationSource configuration() {
        return configuratorModule().configuration().getConfiguration();
    }

    public static ConfigurationSource configuration(String section) {
        return orThrow(configuration().getNested(section), () -> new ConfigurationNotFoundException(section));
    }

    public static <T> T configuration(Class<T> type) {
        return configuration(EMPTY_STRING, type);
    }

    public static <T> T configuration(String section, Class<T> type) {
        return orThrow(configuratorModule().configuration().getCustomConfiguration(new CustomConfigurationModel(section, type)), () -> new ConfigurationNotFoundException(section));
    }

    public static StatelessModuleProxy<ConfiguratorModuleConfiguration> configuratorModule() {
        return getConfiguratorModule();
    }
}
