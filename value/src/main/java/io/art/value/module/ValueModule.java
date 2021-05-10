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

package io.art.value.module;

import io.art.core.module.*;
import io.art.value.configuration.*;
import io.art.value.configuration.ValueModuleConfiguration.*;
import io.art.value.immutable.Value;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.context.Context.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.lang.reflect.*;

@Getter
public class ValueModule implements StatelessModule<ValueModuleConfiguration, Configurator> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<ValueModuleConfiguration> valueModule = context().getStatelessModule(ValueModule.class.getSimpleName());
    private final String id = ValueModule.class.getSimpleName();
    private final ValueModuleConfiguration configuration = new ValueModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);

    static {
        registerDefault(ValueModule.class.getSimpleName(), ValueModule::new);
    }

    public static StatelessModuleProxy<ValueModuleConfiguration> valueModule() {
        return getValueModule();
    }

    public static <T> Value value(T model) {
        if (isNull(model)) return null;
        return valueModule().configuration().getMapper(model.getClass()).fromModel(model);
    }

    public static <T> Value value(T model, Type type) {
        if (isNull(model)) return null;
        return valueModule().configuration().getMapper(type).fromModel(model);
    }

    public static <T> T model(Class<T> type, Value value) {
        if (isNull(value)) return null;
        return cast(valueModule().configuration().getMapper(type).toModel(cast(value)));
    }

    public static <T> T model(Type type, Value value) {
        if (isNull(value)) return null;
        return cast(valueModule().configuration().getMapper(type).toModel(cast(value)));
    }
}
