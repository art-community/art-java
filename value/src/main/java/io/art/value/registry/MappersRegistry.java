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

package io.art.value.registry;

import io.art.core.caster.*;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapping.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.CollectionsFactory.*;
import java.util.*;

public class MappersRegistry {
    private final Map<Class<?>, ValueToModelMapper<Object, Value>> toModel = Caster.cast(mapOf(
            String.class, PrimitiveMapping.toString
    ));
    private final Map<Class<?>, ValueFromModelMapper<Object, Value>> fromModel = Caster.cast(mapOf(
            String.class, PrimitiveMapping.fromString
    ));

    public MappersRegistry register(Class<?> model, ValueToModelMapper<?, ?> mapper) {
        toModel.put(model, cast(mapper));
        return this;
    }

    public MappersRegistry register(Class<?> model, ValueFromModelMapper<?, ?> mapper) {
        fromModel.put(model, cast(mapper));
        return this;
    }

    public <T> ValueToModelMapper<T, Value> getToModel(Class<T> model) {
        return cast(toModel.get(model));
    }

    public <T> ValueFromModelMapper<T, Value> getFromModel(Class<T> model) {
        return cast(fromModel.get(model));
    }
}
