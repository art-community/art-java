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

import io.art.configurator.constants.ConfiguratorModuleConstants.*;
import io.art.configurator.exception.*;
import io.art.configuration.yaml.source.*;
import io.art.core.source.*;
import lombok.*;
import static com.typesafe.config.ConfigFactory.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.FileConfigurationExtensions.*;
import static io.art.core.extensions.FileExtensions.*;
import java.io.*;
import java.time.*;
import java.util.*;

@Getter
public class FileConfigurationSource implements ConfigurationSource {
    private final ConfigurationSourceType type;
    private final ConfigurationSource source;

    public FileConfigurationSource(ConfigurationSourceType type, File file) {
        this.type = type;
        source = selectSource(type, file);
    }

    @Override
    public Integer getInt(String path) {
        return source.getInt(path);
    }

    @Override
    public Long getLong(String path) {
        return source.getLong(path);
    }

    @Override
    public Boolean getBool(String path) {
        return source.getBool(path);
    }

    @Override
    public Double getDouble(String path) {
        return source.getDouble(path);
    }

    @Override
    public Float getFloat(String path) {
        return source.getFloat(path);
    }

    @Override
    public String getString(String path) {
        return source.getString(path);
    }

    @Override
    public Duration getDuration(String path) {
        return source.getDuration(path);
    }

    @Override
    public ConfigurationSource getNested(String path) {
        return source.getNested(path);
    }

    @Override
    public List<Integer> getIntList(String path) {
        return source.getIntList(path);
    }

    @Override
    public List<Long> getLongList(String path) {
        return source.getLongList(path);
    }

    @Override
    public List<Boolean> getBoolList(String path) {
        return source.getBoolList(path);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return source.getDoubleList(path);
    }

    @Override
    public List<String> getStringList(String path) {
        return source.getStringList(path);
    }

    @Override
    public List<Duration> getDurationList(String path) {
        return source.getDurationList(path);
    }

    @Override
    public List<ConfigurationSource> getNestedList(String path) {
        return source.getNestedList(path);
    }

    @Override
    public Set<String> getKeys() {
        return source.getKeys();
    }

    @Override
    public boolean has(String path) {
        return source.has(path);
    }

    private static ConfigurationSource selectSource(ConfigurationSourceType type, File file) {
        String extension = parseExtension(file.getAbsolutePath());
        switch (extension) {
            case HOCON_EXTENSION:
            case JSON_EXTENSION:
            case CONF_EXTENSION:
            case PROPERTIES_EXTENSION:
                return new TypesafeConfigurationSource(type, parseFile(file));
            case YAML_EXTENSION:
            case YML_EXTENSION:
                return new YamlConfigurationSource(type, file);
        }
        throw new UnknownConfigurationFileExtensionException(extension);
    }
}
