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
import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static java.util.stream.Collectors.*;
import java.time.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class TypesafeConfigurationSource implements ConfigurationSource {
    private final ModuleConfigurationSourceType type;
    private final Config typesafeConfiguration;

    @Override
    public Integer getInt(String path) {
        return orNull(path, typesafeConfiguration::hasPath, typesafeConfiguration::getInt);
    }

    @Override
    public Long getLong(String path) {
        return orNull(path, typesafeConfiguration::hasPath, typesafeConfiguration::getLong);
    }

    @Override
    public Boolean getBool(String path) {
        return orNull(path, typesafeConfiguration::hasPath, typesafeConfiguration::getBoolean);
    }

    @Override
    public Double getDouble(String path) {
        return orNull(path, typesafeConfiguration::hasPath, typesafeConfiguration::getDouble);
    }

    @Override
    public Float getFloat(String path) {
        return let(getDouble(path), Number::floatValue);
    }

    @Override
    public String getString(String path) {
        return orNull(path, typesafeConfiguration::hasPath, typesafeConfiguration::getString);
    }

    @Override
    public Duration getDuration(String path) {
        return orNull(path, typesafeConfiguration::hasPath, typesafeConfiguration::getDuration);
    }

    @Override
    public ConfigurationSource getNested(String path) {
        return new TypesafeConfigurationSource(type, typesafeConfiguration.atPath(path));
    }

    @Override
    public List<Integer> getIntList(String path) {
        return orEmptyList(path, typesafeConfiguration::hasPath, typesafeConfiguration::getIntList);
    }

    @Override
    public List<Long> getLongList(String path) {
        return orEmptyList(path, typesafeConfiguration::hasPath, typesafeConfiguration::getLongList);
    }

    @Override
    public List<Boolean> getBoolList(String path) {
        return orEmptyList(path, typesafeConfiguration::hasPath, typesafeConfiguration::getBooleanList);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return orEmptyList(path, typesafeConfiguration::hasPath, typesafeConfiguration::getDoubleList);
    }

    @Override
    public List<String> getStringList(String path) {
        return orEmptyList(path, typesafeConfiguration::hasPath, typesafeConfiguration::getStringList);
    }

    @Override
    public List<Duration> getDurationList(String path) {
        return orEmptyList(path, typesafeConfiguration::hasPath, typesafeConfiguration::getDurationList);
    }

    @Override
    public List<ConfigurationSource> getNestedList(String path) {
        return orEmptyList(path, typesafeConfiguration::hasPath, typesafeConfiguration::getConfigList)
                .stream()
                .map(config -> new TypesafeConfigurationSource(type, config))
                .collect(toList());
    }

    @Override
    public Set<String> getKeys() {
        return typesafeConfiguration.root().keySet();
    }

    @Override
    public boolean has(String path) {
        return typesafeConfiguration.hasPath(path);
    }
}
