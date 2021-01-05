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

import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import lombok.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.combiner.SectionCombiner.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.value.immutable.Value.*;
import static io.art.value.mapping.ArrayMapping.*;
import static io.art.value.mapping.PrimitiveMapping.toString;
import static io.art.value.mapping.PrimitiveMapping.*;
import static java.util.Objects.*;

@Getter
@RequiredArgsConstructor
public class EntityConfigurationSource implements ConfigurationSource {
    @Getter
    private final String section;
    private final ModuleConfigurationSourceType type = ENTITY;
    private final Entity entity;

    @Override
    public Boolean getBool(String path) {
        return entity.mapping().mapNested(path, toBool);
    }

    @Override
    public String getString(String path) {
        return entity.mapping().mapNested(path, toString);
    }

    @Override
    public ConfigurationSource getNested(String path) {
        Value nested = entity.find(path);
        if (isNull(nested)) {
            return null;
        }
        return new EntityConfigurationSource(combine(section, path), asEntity(nested));
    }

    @Override
    public ImmutableArray<Boolean> getBoolList(String path) {
        return entity.mapping().mapNested(path, array -> isArray(array) ? immutableArrayOf(toImmutableArray(toBool).map(asArray(array))) : emptyImmutableArray());
    }

    @Override
    public ImmutableArray<String> getStringList(String path) {
        return entity.mapping().mapNested(path, array -> isArray(array) ? immutableArrayOf(toImmutableArray(toString).map(asArray(array))) : emptyImmutableArray());
    }

    @Override
    public ImmutableArray<ConfigurationSource> getNestedList(String path) {
        Value nested = entity.get(path);
        if (isNull(nested)) {
            return emptyImmutableArray();
        }
        return asArray(nested).asStream().map(value -> new EntityConfigurationSource(combine(section, path), asEntity(value))).collect(immutableArrayCollector());
    }

    @Override
    public ImmutableSet<String> getKeys() {
        return entity.asMap().keySet().stream().map(toString::map).collect(immutableSetCollector());
    }

    @Override
    public boolean has(String path) {
        return nonNull(entity.find(path));
    }
}
