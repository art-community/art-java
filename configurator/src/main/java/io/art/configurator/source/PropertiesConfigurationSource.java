/*
 * ART
 *
 * Copyright 2019-2021 ART
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

import io.art.core.source.*;
import lombok.*;
import lombok.experimental.Delegate;
import static com.typesafe.config.ConfigFactory.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.*;
import static io.art.configurator.constants.ConfiguratorModuleConstants.ConfigurationSourceType.*;
import static io.art.core.constants.StringConstants.*;

@Getter
public class PropertiesConfigurationSource implements NestedConfiguration {
    private final ConfigurationSourceType type = PROPERTIES;
    @Delegate
    private final TypesafeConfigurationSource typesafeConfigurationSource = new TypesafeConfigurationSource(EMPTY_STRING, PROPERTIES, systemProperties());
}
