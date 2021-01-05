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

package io.art.configurator.source;

import com.typesafe.config.*;
import io.art.core.collection.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.combiner.SectionCombiner.combine;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static java.util.Objects.*;

@Getter
@RequiredArgsConstructor
public class TypesafeConfigurationSource implements ConfigurationSource {
    private final String section;
    private final ModuleConfigurationSourceType type;
    private final Config typesafeConfiguration;

    @Override
    public Boolean getBool(String path) {
        return orNull(path, typesafeConfiguration::hasPath, typesafeConfiguration::getBoolean);
    }

    @Override
    public String getString(String path) {
        return orNull(path, typesafeConfiguration::hasPath, typesafeConfiguration::getString);
    }

    @Override
    public ConfigurationSource getNested(String path) {
        Config configuration = this.typesafeConfiguration.atPath(path);
        if (isNull(configuration) || !this.typesafeConfiguration.hasPath(path) || configuration.isEmpty()) {
            return null;
        }
        return new TypesafeConfigurationSource(combine(section, path), type, configuration);
    }


    @Override
    public ImmutableArray<Boolean> getBoolList(String path) {
        return orEmptyImmutableArray(path, typesafeConfiguration::hasPath, typesafeConfiguration::getBooleanList);
    }

    @Override
    public ImmutableArray<String> getStringList(String path) {
        return orEmptyImmutableArray(path, typesafeConfiguration::hasPath, typesafeConfiguration::getStringList);
    }

    @Override
    public ImmutableArray<ConfigurationSource> getNestedList(String path) {
        return orEmptyImmutableArray(path, typesafeConfiguration::hasPath, typesafeConfiguration::getConfigList)
                .stream()
                .map(config -> new TypesafeConfigurationSource(combine(section, path), type, config))
                .collect(immutableArrayCollector());
    }


    @Override
    public ImmutableSet<String> getKeys() {
        return immutableSetOf(typesafeConfiguration.root().keySet());
    }

    @Override
    public boolean has(String path) {
        return typesafeConfiguration.hasPath(path);
    }
}
