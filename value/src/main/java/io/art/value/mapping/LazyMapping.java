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

package io.art.value.mapping;

import io.art.core.annotation.*;
import io.art.core.property.*;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapper.ValueFromModelMapper.*;
import io.art.value.mapper.ValueToModelMapper.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.property.LazyProperty.*;

@UtilityClass
@UsedByGenerator
public class LazyMapping {
    public static <T> ValueFromModelMapper<LazyProperty<T>, Value> fromLazy(ValueFromModelMapper<T, ? extends Value> valueMapper) {
        return lazy -> let(lazy, value -> let(value.get(), valueMapper::map));
    }

    public static <T> ValueToModelMapper<LazyProperty<T>, Value> toLazy(ValueToModelMapper<T, ? extends Value> valueMapper) {
        return value -> let(value, lazy -> lazy(() -> valueMapper.map(cast(lazy))));
    }

    public static <T> PrimitiveFromModelMapper<LazyProperty<T>> fromLazy(PrimitiveFromModelMapper<T> valueMapper) {
        return lazy -> let(lazy, value -> let(value.get(), valueMapper::map));
    }

    public static <T> PrimitiveToModelMapper<LazyProperty<T>> toLazy(PrimitiveToModelMapper<T> valueMapper) {
        return value -> let(value, lazy -> lazy(() -> valueMapper.map(lazy)));
    }

    public static <T> ArrayFromModelMapper<LazyProperty<T>> fromLazy(ArrayFromModelMapper<T> valueMapper) {
        return lazy -> let(lazy, value -> let(value.get(), valueMapper::map));
    }

    public static <T> ArrayToModelMapper<LazyProperty<T>> toLazy(ArrayToModelMapper<T> valueMapper) {
        return value -> let(value, lazy -> lazy(() -> valueMapper.map(lazy)));
    }

    public static <T> EntityFromModelMapper<LazyProperty<T>> fromLazy(EntityFromModelMapper<T> valueMapper) {
        return lazy -> let(lazy, value -> let(value.get(), valueMapper::map));
    }

    public static <T> EntityToModelMapper<LazyProperty<T>> toLazy(EntityToModelMapper<T> valueMapper) {
        return value -> let(value, lazy -> lazy(() -> valueMapper.map(lazy)));
    }

    public static <T> BinaryFromModelMapper<LazyProperty<T>> fromLazy(BinaryFromModelMapper<T> valueMapper) {
        return lazy -> let(lazy, value -> let(value.get(), valueMapper::map));
    }

    public static <T> BinaryToModelMapper<LazyProperty<T>> toLazy(BinaryToModelMapper<T> valueMapper) {
        return value -> let(value, lazy -> lazy(() -> valueMapper.map(lazy)));
    }
}
