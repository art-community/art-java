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
import io.art.configurator.configuration.ConfiguratorModuleConfiguration.*;
import io.art.configurator.source.*;
import io.art.core.checker.*;
import io.art.core.collection.*;
import io.art.core.context.*;
import io.art.core.file.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorKeys.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.FileConfigurationExtensions.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.property.LazyProperty.*;
import static java.nio.file.Paths.*;
import static java.text.MessageFormat.*;
import java.io.*;
import java.util.*;

@Getter
public class ConfiguratorModule implements StatelessModule<ConfiguratorModuleConfiguration, Configurator> {
    private final String id = CONFIGURATOR_MODULE_ID;
    private final ConfiguratorModuleConfiguration configuration = new ConfiguratorModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    private static final LazyProperty<StatelessModuleProxy<ConfiguratorModuleConfiguration>> configuratorModule = lazy(() -> context().getStatelessModule(CONFIGURATOR_MODULE_ID));

    @Override
    public void beforeReload(ContextService contextService) {
        orderedSources().forEach(ConfigurationSource::refresh);
    }

    @Override
    public void launch(ContextService contextService) {
        contextService.print(format(CONFIGURED_BY_MESSAGE, configuration.getConfiguration().getPath()));
    }

    public ImmutableArray<ConfigurationSource> orderedSources() {
        return configuration.orderedSources();
    }

    public ConfiguratorModule loadSources(ImmutableSet<String> modules) {
        configure(configurator -> configurator
                .from(new EnvironmentConfigurationSource(EMPTY_STRING))
                .from(new PropertiesConfigurationSource(EMPTY_STRING, context().configuration().getProperties()))
        );
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String resourceName = modules.contains(TESTS_MODULE_ID) ? TEST_CONFIGURATION_FILE : DEFAULT_MODULE_CONFIGURATION_FILE;
        FILE_CONFIGURATION_EXTENSIONS.stream()
                .map(extension -> loadResource(resourceName + DOT + extension, loader))
                .filter(Objects::nonNull)
                .findFirst()
                .map(resource -> new FileConfigurationSource(EMPTY_STRING, RESOURCES_FILE, resource))
                .ifPresent(source -> configure(configurator -> configurator.from(source)));
        EnvironmentConfigurationSource environment = getConfiguration().getEnvironment();
        PropertiesConfigurationSource properties = getConfiguration().getProperties();
        configureByFile(linkedListOf(environment.getString(CONFIGURATION_ENVIRONMENT), properties.getString(CONFIGURATION_PROPERTY)));
        return this;
    }

    private void configureByFile(List<String> paths) {
        paths
                .stream()
                .filter(EmptinessChecker::isNotEmpty)
                .map(path -> get(path).toFile())
                .filter(File::exists)
                .forEach(file -> configure(configurator -> configurator.from(new FileConfigurationSource(EMPTY_STRING, CUSTOM_FILE, new FileProxy(file)))));
    }

    private FileProxy loadResource(String path, ClassLoader loader) {
        return let(loader.getResourceAsStream(path), stream -> new FileProxy(path, () -> loader.getResourceAsStream(path)));
    }

    public static StatelessModuleProxy<ConfiguratorModuleConfiguration> configuratorModule() {
        return configuratorModule.get();
    }
}
