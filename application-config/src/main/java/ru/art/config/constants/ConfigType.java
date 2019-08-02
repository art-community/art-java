/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.config.constants;

public enum ConfigType {
    PROPERTIES,
    JSON,
    HOCON,
    GROOVY,
    REMOTE_ENTITY_CONFIG,
    YAML;

    public boolean isTypesafeConfig() {
        return this == PROPERTIES || this == JSON || this == HOCON;
    }

    public boolean isGroovyConfig() {
        return this == GROOVY;
    }

    public boolean isYamlConfig() {
        return this == YAML;
    }

    public boolean isRemoteEntityConfig() {
        return this == REMOTE_ENTITY_CONFIG;
    }
}


