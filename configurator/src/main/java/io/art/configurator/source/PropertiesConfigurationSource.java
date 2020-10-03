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

import io.art.core.module.*;
import lombok.*;
import static com.typesafe.config.ConfigFactory.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import java.time.*;
import java.util.*;

@Getter
public class PropertiesConfigurationSource implements ModuleConfigurationSource {
    private final ConfigurationSourceType type = PROPERTIES;
    private final TypesafeConfigurationSource typesafeConfigurationSource = new TypesafeConfigurationSource(PROPERTIES, systemProperties());

    @Override
    public Integer getInt(String path) {
        return typesafeConfigurationSource.getInt(path);
    }

    @Override
    public Long getLong(String path) {
        return typesafeConfigurationSource.getLong(path);
    }

    @Override
    public Boolean getBool(String path) {
        return typesafeConfigurationSource.getBool(path);
    }

    @Override
    public Double getDouble(String path) {
        return typesafeConfigurationSource.getDouble(path);
    }

    @Override
    public Float getFloat(String path) {
        return typesafeConfigurationSource.getFloat(path);
    }

    @Override
    public String getString(String path) {
        return typesafeConfigurationSource.getString(path);
    }

    @Override
    public Duration getDuration(String path) {
        return typesafeConfigurationSource.getDuration(path);
    }

    @Override
    public ModuleConfigurationSource getNested(String path) {
        return typesafeConfigurationSource.getNested(path);
    }

    @Override
    public List<Integer> getIntList(String path) {
        return typesafeConfigurationSource.getIntList(path);
    }

    @Override
    public List<Long> getLongList(String path) {
        return typesafeConfigurationSource.getLongList(path);
    }

    @Override
    public List<Boolean> getBoolList(String path) {
        return typesafeConfigurationSource.getBoolList(path);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return typesafeConfigurationSource.getDoubleList(path);
    }

    @Override
    public List<String> getStringList(String path) {
        return typesafeConfigurationSource.getStringList(path);
    }

    @Override
    public List<Duration> getDurationList(String path) {
        return typesafeConfigurationSource.getDurationList(path);
    }

    @Override
    public List<ModuleConfigurationSource> getNestedList(String path) {
        return typesafeConfigurationSource.getNestedList(path);
    }

    @Override
    public Set<String> getKeys() {
        return typesafeConfigurationSource.getKeys();
    }

    @Override
    public boolean has(String path) {
        return typesafeConfigurationSource.has(path);
    }
}
