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

import io.art.value.immutable.*;
import io.art.value.mapper.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.CollectionsFactory.*;
import java.util.*;

public class MappersRegistry {
    private final Map<Class<?>, ValueToModelMapper<Object, Value>> toModel = mapOf();
    private final Map<Class<?>, ValueFromModelMapper<Object, Value>> fromModel = mapOf();

    public MappersRegistry register(Class<?> model, ValueToModelMapper<?, ?> mapper) {
        toModel.put(model, cast(mapper));
        return this;
    }

    public MappersRegistry register(Class<?> model, ValueFromModelMapper<?, ?> mapper) {
        fromModel.put(model, cast(mapper));
        return this;
    }

    public ValueToModelMapper<Object, Value> getToModel(Class<?> model) {
        return toModel.get(model);
    }

    public ValueFromModelMapper<Object, Value> getFromModel(Class<?> model) {
        return fromModel.get(model);
    }
}
