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

package io.art.model.module;

import io.art.model.configurator.*;
import io.art.model.customizer.*;
import io.art.model.server.*;
import lombok.*;
import static io.art.model.constants.ModelConstants.*;
import java.util.function.*;

@Getter
@Builder
public class ModuleModel {
    private final String mainModuleId;
    private final ServerModel serverModel;
    @Builder.Default
    private ConfiguratorModel configuratorModel = ConfiguratorModel.builder().build();

    public ModuleModel configure(UnaryOperator<ConfiguratorCustomizer> configurator) {
        configuratorModel = configurator.apply(new ConfiguratorCustomizer()).apply();
        return this;
    }

    public static ModuleCustomizer module() {
        return new ModuleCustomizer(DEFAULT_MODULE_ID);
    }

    public static ModuleCustomizer module(String id) {
        return new ModuleCustomizer(id);
    }
}
