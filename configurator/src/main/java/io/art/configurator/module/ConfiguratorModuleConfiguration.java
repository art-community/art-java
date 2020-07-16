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

import com.google.common.collect.*;
import io.art.configurator.source.*;
import io.art.core.module.*;
import lombok.*;
import static com.google.common.collect.ImmutableMap.*;
import static io.art.core.caster.Caster.*;

@Getter
public class ConfiguratorModuleConfiguration implements ModuleConfiguration {
    private ImmutableMap<String, ModuleConfigurationSource> sources = of();

    public PropertiesConfigurationSource getProperties() {
        return cast(getSources().get(PropertiesConfigurationSource.class.getSimpleName()));
    }

    public EnvironmentConfigurationSource getEnvironment() {
        return cast(getSources().get(EnvironmentConfigurationSource.class.getSimpleName()));
    }

    public ImmutableMap<String, FileConfigurationSource> getFiles() {
        return getSources()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(FileConfigurationSource.class.getSimpleName()))
                .collect(toImmutableMap(
                        entry -> entry.getKey().substring(entry.getKey().indexOf(FileConfigurationSource.class.getSimpleName() + 1)),
                        entry -> cast(entry.getValue()))
                );
    }

    public FileConfigurationSource getFiles(String path) {
        return getFiles().get(path);
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<Configurator> {
        private final ConfiguratorModuleConfiguration configuration;

        @Override
        public Configurator from(ModuleConfigurationSource source) {
            configuration.sources = ImmutableMap.<String, ModuleConfigurationSource>builder()
                    .putAll(configuration.sources)
                    .put(source.getType(), source)
                    .build();
            return this;
        }
    }
}
