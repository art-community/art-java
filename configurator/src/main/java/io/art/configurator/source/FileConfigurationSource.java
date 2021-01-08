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

import io.art.configuration.yaml.source.*;
import io.art.configurator.constants.ConfiguratorModuleConstants.*;
import io.art.configurator.exception.*;
import io.art.core.source.*;
import lombok.*;
import lombok.experimental.Delegate;
import static com.typesafe.config.ConfigFactory.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.FileConfigurationExtensions.*;
import static io.art.core.extensions.FileExtensions.*;
import java.io.*;

@Getter
public class FileConfigurationSource implements NestedConfiguration {
    private final String section;
    private final ConfigurationSourceType type;
    @Delegate
    private final NestedConfiguration source;

    public FileConfigurationSource(String section, ConfigurationSourceType type, File file) {
        this.section = section;
        this.type = type;
        source = selectSource(section, type, file);
    }

    private static NestedConfiguration selectSource(String section, ConfigurationSourceType type, File file) {
        String extension = parseExtension(file.getAbsolutePath());
        switch (extension) {
            case HOCON_EXTENSION:
            case JSON_EXTENSION:
            case CONF_EXTENSION:
            case PROPERTIES_EXTENSION:
                return new TypesafeConfigurationSource(section, type, file);
            case YAML_EXTENSION:
            case YML_EXTENSION:
                return new YamlConfigurationSource(section, type, file);
        }
        throw new UnknownConfigurationFileExtensionException(extension);
    }
}
