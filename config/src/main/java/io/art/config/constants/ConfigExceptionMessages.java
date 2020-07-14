/*
 * ART Java
 *
 * Copyright 2019 ART
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

package io.art.config.constants;

public interface ConfigExceptionMessages {
    String SECTION_ID_IS_NULL = "SectionId is null";
    String CONFIG_ID_IS_NULL = "ConfigId is null";
    String SECTION_ID_IS_EMPTY = "SectionId is empty";
    String PATH_IS_EMPTY = "Path is empty";
    String CONFIG_TYPE_IS_NULL = "ConfigType is null";
    String UNKNOWN_CONFIG_TYPE = "Unknown config type: ''{0}'' ";
    String CONFIG_TYPE_IS_NOT_TYPESAFE = "Config type is not 'typesafe'";
    String CONFIG_TYPE_IS_NOT_YAML = "Config type is not 'yaml'";
    String CONFIG_TYPE_IS_NOT_ENTITY = "Config type is not 'entity'";
}
