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

package io.art.value.module;

import io.art.core.module.*;
import io.art.value.configuration.*;
import io.art.value.configuration.ValueModuleConfiguration.*;
import io.art.value.registry.*;
import lombok.*;
import static io.art.core.context.Context.*;
import static lombok.AccessLevel.*;

@Getter
public class ValueModule implements StatelessModule<ValueModuleConfiguration, Configurator> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<ValueModuleConfiguration> valueModule = context().getStatelessModule(ValueModule.class.getSimpleName());
    private final String id = ValueModule.class.getSimpleName();
    private final ValueModuleConfiguration configuration = new ValueModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);

    public static StatelessModuleProxy<ValueModuleConfiguration> valueModule() {
        return getValueModule();
    }

    public static MappersRegistry mappers() {
        return valueModule().configuration().getRegistry();
    }
}
