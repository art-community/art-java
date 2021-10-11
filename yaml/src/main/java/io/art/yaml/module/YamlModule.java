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

package io.art.yaml.module;

import io.art.core.module.*;
import io.art.core.property.*;
import io.art.yaml.configuration.*;
import lombok.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;
import static io.art.core.property.LazyProperty.*;

@Getter
public class YamlModule implements StatelessModule<YamlModuleConfiguration, YamlModuleConfiguration.Configurator> {
    private static final LazyProperty<StatelessModuleProxy<YamlModuleConfiguration>> yamlModule = lazy(() -> context().getStatelessModule(YAML_MODULE_ID));
    private final String id = YAML_MODULE_ID;
    private final YamlModuleConfiguration configuration = new YamlModuleConfiguration();
    private final YamlModuleConfiguration.Configurator configurator = new YamlModuleConfiguration.Configurator(configuration);

    public static StatelessModuleProxy<YamlModuleConfiguration> yamlModule() {
        return yamlModule.get();
    }
}
