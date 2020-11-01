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

import io.art.value.mapper.*;
import lombok.*;
import static io.art.core.factory.CollectionsFactory.*;
import java.util.*;

@ToString
public class MappersRegistry {
    private final Map<Class<?>, ValueToModelMapper<?, ?>> toModel = mapOf();
    private final Map<Class<?>, ValueFromModelMapper<?, ?>> fromModel = mapOf();

    public MappersRegistry putToModel(Class<?> model, ValueToModelMapper<?, ?> mapper) {
        toModel.put(model, mapper);
        return this;
    }

    public MappersRegistry putFromModel(Class<?> model, ValueFromModelMapper<?, ?> mapper) {
        fromModel.put(model, mapper);
        return this;
    }

    public ValueToModelMapper<?, ?> getToModel(Class<?> model) {
        return toModel.get(model);
    }

    public ValueFromModelMapper<?, ?> getFromModel(Class<?> model) {
        return fromModel.get(model);
    }
}
