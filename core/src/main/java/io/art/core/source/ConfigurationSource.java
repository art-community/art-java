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

import io.art.core.collection.*;
import io.art.core.extensions.*;
import io.art.core.parser.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collection.ImmutableMap.*;
import static java.util.function.Function.*;
import java.time.*;
import java.util.*;
import java.util.function.*;

public interface ConfigurationSource {
    String getSection();


    Boolean getBool(String path);

    String getString(String path);

    default Integer getInt(String path) {
        return letIfNotEmpty(getString(path), Integer::parseInt);
    }

    default Long getLong(String path) {
        return letIfNotEmpty(getString(path), Long::parseLong);
    }

    default Double getDouble(String path) {
        return letIfNotEmpty(getString(path), Double::parseDouble);
    }

    default Float getFloat(String path) {
        return letIfNotEmpty(getString(path), Float::parseFloat);
    }

    default Short getShort(String path) {
        return letIfNotEmpty(getString(path), Short::parseShort);
    }

    default Character getChar(String path) {
        String string = getString(path);
        return letIfNotEmpty(string, notEmpty -> notEmpty.charAt(0));
    }

    default Byte getByte(String path) {
        return letIfNotEmpty(getString(path), Byte::parseByte);
    }

    default Duration getDuration(String path) {
        return letIfNotEmpty(getString(path), DurationParser::parseDuration);
    }

    default UUID getUuid(String path) {
        return letIfNotEmpty(getString(path), UUID::fromString);
    }

    default LocalDateTime getLocalDateTime(String path) {
        return letIfNotEmpty(getString(path), LocalDateTime::parse);
    }

    default ZonedDateTime getZonedDateTime(String path) {
        return letIfNotEmpty(getString(path), ZonedDateTime::parse);
    }

    default Date getDate(String path) {
        return letIfNotEmpty(getZonedDateTime(path), DateTimeExtensions::toSimpleDate);
    }

    ConfigurationSource getNested(String path);

    default <T> T getNested(String path, Function<ConfigurationSource, T> mapper) {
        return let(getNested(path), mapper);
    }


    ImmutableArray<Boolean> getBoolList(String path);

    ImmutableArray<String> getStringList(String path);

    default ImmutableArray<Integer> getIntList(String path) {
        return getStringList(path)
                .stream()
                .map(Integer::parseInt)
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<Long> getLongList(String path) {
        return getStringList(path)
                .stream()
                .map(Long::parseLong)
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<Double> getDoubleList(String path) {
        return getStringList(path)
                .stream()
                .map(Double::parseDouble)
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<Short> getShortList(String path) {
        return getStringList(path)
                .stream()
                .map(Short::parseShort)
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<Character> getCharList(String path) {
        return getStringList(path)
                .stream()
                .map(string -> letIfNotEmpty(string, notEmpty -> notEmpty.charAt(0)))
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<Byte> getByteList(String path) {
        return getStringList(path)
                .stream()
                .map(Byte::parseByte)
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<Duration> getDurationList(String path) {
        return getStringList(path)
                .stream()
                .map(DurationParser::parseDuration)
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<UUID> getUuidList(String path) {
        return getStringList(path)
                .stream()
                .map(UUID::fromString)
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<LocalDateTime> getLocalDateTimeList(String path) {
        return getStringList(path)
                .stream()
                .map(LocalDateTime::parse)
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<ZonedDateTime> getZonedDateTimeList(String path) {
        return getStringList(path)
                .stream()
                .map(ZonedDateTime::parse)
                .collect(immutableArrayCollector());
    }

    default ImmutableArray<Date> getDateList(String path) {
        return getZonedDateTimeList(path)
                .stream()
                .map(DateTimeExtensions::toSimpleDate)
                .collect(immutableArrayCollector());
    }

    ImmutableArray<ConfigurationSource> getNestedList(String path);

    default <T> ImmutableArray<T> getNestedList(String path, Function<ConfigurationSource, T> mapper) {
        return getNestedList(path).stream().map(mapper).collect(immutableArrayCollector());
    }


    default <T> ImmutableMap<String, T> getNestedMap(String path, Function<ConfigurationSource, T> mapper) {
        ConfigurationSource nested = getNested(path);
        if (!has(path)) {
            return emptyImmutableMap();
        }
        return nested.getKeys().stream().collect(immutableMapCollector(identity(), key -> let(nested.getNested(key), mapper)));
    }

    default ImmutableMap<String, ConfigurationSource> getNestedMap(String path) {
        ConfigurationSource nested = getNested(path);
        if (!has(path)) {
            return emptyImmutableMap();
        }
        return nested.getKeys().stream().collect(immutableMapCollector(identity(), nested::getNested));
    }


    ModuleConfigurationSourceType getType();

    ImmutableSet<String> getKeys();

    boolean has(String path);

    interface ModuleConfigurationSourceType {
        int getOrder();
    }
}
