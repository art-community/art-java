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

package io.art.value.mapping;

import io.art.core.annotation.*;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapper.ValueFromModelMapper.*;
import io.art.value.mapper.ValueToModelMapper.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.cast;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.OptionalExtensions.*;
import static java.util.Optional.*;
import java.util.*;

@UtilityClass
@ForGenerator
public class OptionalMapping {
    public static <T> ValueFromModelMapper<Optional<T>, Value> fromOptional(ValueFromModelMapper<T, ? extends Value> valueMapper) {
        return value -> let(unwrap(value), valueMapper::map);
    }

    public static <T> PrimitiveFromModelMapper<Optional<T>> fromOptional(PrimitiveFromModelMapper<T> valueMapper) {
        return value -> let(unwrap(value), valueMapper::map);
    }

    public static <T> ArrayFromModelMapper<Optional<T>> fromOptional(ArrayFromModelMapper<T> valueMapper) {
        return value -> let(unwrap(value), valueMapper::map);
    }

    public static <T> EntityFromModelMapper<Optional<T>> fromOptional(EntityFromModelMapper<T> valueMapper) {
        return value -> let(unwrap(value), valueMapper::map);
    }

    public static <T> BinaryFromModelMapper<Optional<T>> fromOptional(BinaryFromModelMapper<T> valueMapper) {
        return value -> let(unwrap(value), valueMapper::map);
    }


    public static <T> ValueToModelMapper<Optional<T>, Value> toOptional(ValueToModelMapper<T, ? extends Value> valueMapper) {
        return value -> ofNullable(value).map(optional -> valueMapper.map(cast(optional)));
    }

    public static <T> EntityToModelMapper<Optional<T>> toOptional(EntityToModelMapper<T> valueMapper) {
        return value -> ofNullable(value).map(valueMapper::map);
    }

    public static <T> BinaryToModelMapper<Optional<T>> toOptional(BinaryToModelMapper<T> valueMapper) {
        return value -> ofNullable(value).map(valueMapper::map);
    }

    public static <T> ArrayToModelMapper<Optional<T>> toOptional(ArrayToModelMapper<T> valueMapper) {
        return value -> ofNullable(value).map(valueMapper::map);
    }

    public static <T> PrimitiveToModelMapper<Optional<T>> toOptional(PrimitiveToModelMapper<T> valueMapper) {
        return value -> ofNullable(value).map(valueMapper::map);
    }
}
