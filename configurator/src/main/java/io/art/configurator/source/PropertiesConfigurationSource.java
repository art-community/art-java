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
import lombok.*;
import static com.typesafe.config.ConfigFactory.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.core.constants.StringConstants.*;
import java.util.*;

@Getter
public class PropertiesConfigurationSource implements ConfigurationSource {
    private final ConfigurationSourceType type = PROPERTIES;
    private final TypesafeConfigurationSource typesafeConfigurationSource = new TypesafeConfigurationSource(EMPTY_STRING, PROPERTIES, systemProperties());

    @Override
    public String getSection() {
        return typesafeConfigurationSource.getSection();
    }


    @Override
    public Boolean getBool(String path) {
        return typesafeConfigurationSource.getBool(path);
    }

    @Override
    public String getString(String path) {
        return typesafeConfigurationSource.getString(path);
    }

    @Override
    public ConfigurationSource getNested(String path) {
        return typesafeConfigurationSource.getNested(path);
    }


    @Override
    public ImmutableArray<Boolean> getBoolList(String path) {
        return typesafeConfigurationSource.getBoolList(path);
    }

    @Override
    public ImmutableArray<String> getStringList(String path) {
        return typesafeConfigurationSource.getStringList(path);
    }

    @Override
    public ImmutableArray<ConfigurationSource> getNestedList(String path) {
        return typesafeConfigurationSource.getNestedList(path);
    }


    @Override
    public ImmutableSet<String> getKeys() {
        return typesafeConfigurationSource.getKeys();
    }

    @Override
    public boolean has(String path) {
        return typesafeConfigurationSource.has(path);
    }
}
