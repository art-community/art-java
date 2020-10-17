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

package io.art.core.source;

import static io.art.core.checker.NullityChecker.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import java.time.*;
import java.util.*;

public interface ConfigurationSource {
    Integer getInt(String path);

    Long getLong(String path);

    Boolean getBool(String path);

    Double getDouble(String path);

    Float getFloat(String path);

    String getString(String path);

    Duration getDuration(String path);

    ConfigurationSource getNested(String path);

    List<Integer> getIntList(String path);

    List<Long> getLongList(String path);

    List<Boolean> getBoolList(String path);

    List<Double> getDoubleList(String path);

    List<String> getStringList(String path);

    List<Duration> getDurationList(String path);

    List<ConfigurationSource> getNestedList(String path);

    default Map<String, Integer> getIntMap(String path) {
        ConfigurationSource nested = getNested(path);
        return nested.getKeys().stream().collect(toMap(identity(), nested::getInt));
    }

    default Map<String, Long> getLongMap(String path) {
        ConfigurationSource nested = getNested(path);
        return nested.getKeys().stream().collect(toMap(identity(), nested::getLong));
    }

    default Map<String, Boolean> getBoolMap(String path) {
        ConfigurationSource nested = getNested(path);
        return nested.getKeys().stream().collect(toMap(identity(), nested::getBool));
    }

    default Map<String, Double> getDoubleMap(String path) {
        ConfigurationSource nested = getNested(path);
        return nested.getKeys().stream().collect(toMap(identity(), nested::getDouble));
    }

    default Map<String, String> getStringMap(String path) {
        ConfigurationSource nested = getNested(path);
        return nested.getKeys().stream().collect(toMap(identity(), nested::getString));
    }

    default Map<String, Duration> getDurationMap(String path) {
        ConfigurationSource nested = getNested(path);
        return nested.getKeys().stream().collect(toMap(identity(), nested::getDuration));
    }

    default Map<String, ConfigurationSource> getNestedMap(String path) {
        return let(getNested(path), nested -> nested.getKeys().stream().collect(toMap(identity(), nested::getNested)));
    }

    ModuleConfigurationSourceType getType();

    Set<String> getKeys();

    boolean has(String path);

    interface ModuleConfigurationSourceType {
        int getOrder();
    }
}
