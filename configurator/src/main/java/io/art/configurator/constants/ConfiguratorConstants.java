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

package io.art.configurator.constants;

public interface ConfiguratorConstants {
    interface ConfiguratorKeys {
        String MODULE_CONFIG_FILE_ENVIRONMENT = "MODULE_CONFIG_FILE";
        String MODULE_CONFIG_FILES_ENVIRONMENT = "MODULE_CONFIG_FILES";
        String MODULE_CONFIG_FILE_PROPERTY = "module.config.file";
        String MODULE_CONFIG_FILES_PROPERTY = "module.config.files";
    }

    String HOCON_EXTENSION = "hocon";
    String CONF_EXTENSION = "conf";
    String PROPERTIES_EXTENSION = "properties";
    String JSON_EXTENSION = "json";
    String YAML_EXTENSION = "yaml";
    String YML_EXTENSION = "yml";

    interface ExceptionMessages {
        String UNKNOWN_CONFIGURATION_SOURCE_FILE_EXTENSION = "Unknown configuration source file extension: {0}";
    }
}
