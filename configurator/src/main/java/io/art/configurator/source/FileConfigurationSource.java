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

import io.art.configurator.exception.*;
import io.art.core.module.*;
import lombok.*;
import static com.typesafe.config.ConfigFactory.*;
import static io.art.configurator.constants.ConfiguratorConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.FileExtensions.*;
import java.io.*;
import java.time.*;
import java.util.*;

@Getter
public class FileConfigurationSource implements ModuleConfigurationSource {
    private final String path;
    private final String type;
    private final ModuleConfigurationSource source;

    public FileConfigurationSource(String path) {
        this.path = path;
        type = FileConfigurationSource.class.getSimpleName() + COLON + path;
        source = selectSource(path);
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
    public String getString(String path) {
        return source.getString(path);
    }

    @Override
    public Duration getDuration(String path) {
        return source.getDuration(path);
    }

    @Override
    public ModuleConfigurationSource getInner(String path) {
        return source.getInner(path);
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
    public List<ModuleConfigurationSource> getInnerList(String path) {
        return source.getInnerList(path);
    }

    @Override
    public Map<String, Integer> getIntMap(String path) {
        return source.getIntMap(path);
    }

    @Override
    public Map<String, Long> getLongMap(String path) {
        return source.getLongMap(path);
    }

    @Override
    public Map<String, Boolean> getBoolMap(String path) {
        return source.getBoolMap(path);
    }

    @Override
    public Map<String, Double> getDoubleMap(String path) {
        return source.getDoubleMap(path);
    }

    @Override
    public Map<String, String> getStringMap(String path) {
        return source.getStringMap(path);
    }

    @Override
    public Map<String, Duration> getDurationMap(String path) {
        return source.getDurationMap(path);
    }

    @Override
    public Map<String, ModuleConfigurationSource> getInnerMap(String path) {
        return source.getInnerMap(path);
    }

    @Override
    public Set<String> getKeys() {
        return source.getKeys();
    }

    @Override
    public boolean has(String path) {
        return source.has(path);
    }

    private static ModuleConfigurationSource selectSource(String path) {
        String extension = parseExtension(path);
        switch (extension) {
            case HOCON_EXTENSION:
            case JSON_EXTENSION:
            case CONF_EXTENSION:
            case PROPERTIES_EXTENSION:
                return new TypesafeConfigurationSource(parseFile(new File(path)));
        }
        throw new UnknownConfigurationFileExtensionException(extension);
    }
}
