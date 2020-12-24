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
import io.art.core.factory.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapper.ValueFromModelMapper.*;
import io.art.value.mapper.ValueToModelMapper.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.value.constants.ValueConstants.ValueType.*;
import static io.art.value.factory.EntityFactory.*;
import static io.art.value.factory.PrimitivesFactory.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;

@UsedByGenerator
@RequiredArgsConstructor
public class EntityMapping {
    private final Entity entity;

    public <T, V extends Value> T map(String key, ValueToModelMapper<T, V> mapper) {
        return map(stringPrimitive(key), mapper);
    }

    public <T, V extends Value> T map(Long key, ValueToModelMapper<T, V> mapper) {
        return map(longPrimitive(key), mapper);
    }

    public <T, V extends Value> T map(Integer key, ValueToModelMapper<T, V> mapper) {
        return map(intPrimitive(key), mapper);
    }

    public <T, V extends Value> T map(Double key, ValueToModelMapper<T, V> mapper) {
        return map(doublePrimitive(key), mapper);
    }

    public <T, V extends Value> T map(Boolean key, ValueToModelMapper<T, V> mapper) {
        return map(boolPrimitive(key), mapper);
    }

    public <T, V extends Value> T map(Byte key, ValueToModelMapper<T, V> mapper) {
        return map(bytePrimitive(key), mapper);
    }

    public <T, V extends Value> T map(Float key, ValueToModelMapper<T, V> mapper) {
        return map(floatPrimitive(key), mapper);
    }

    public <T, V extends Value> T map(Character key, ValueToModelMapper<T, V> mapper) {
        return map(charPrimitive(key), mapper);
    }

    public <T, V extends Value> T map(Short key, ValueToModelMapper<T, V> mapper) {
        return map(shortPrimitive(key), mapper);
    }

    public <T, V extends Value> T map(Primitive primitive, ValueToModelMapper<T, V> mapper) {
        return entity.map(primitive, mapper);
    }

    public <T, V extends Value> T mapNested(String key, ValueToModelMapper<T, V> mapper) {
        return let(entity.find(key), value -> mapper.map(cast(value)));
    }

    public <T, V extends Value> T mapOrDefault(String key, PrimitiveType type, ValueToModelMapper<T, V> mapper) {
        return entity.mapOrDefault(key, type, mapper);
    }

    public static <K, V> EntityToModelMapper<Map<K, V>> toMap(PrimitiveToModelMapper<K> toKey, PrimitiveFromModelMapper<K> fromKey, ValueToModelMapper<V, ? extends Value> value) {
        return entity -> let(entity, notNull -> notNull.asMap(toKey, fromKey, value));
    }

    public static <K, V> EntityToModelMapper<Map<K, V>> toMutableMap(PrimitiveToModelMapper<K> keyMapper, ValueToModelMapper<V, ? extends Value> valueMapper) {
        return entity -> let(entity, notNull -> notNull.toMap(keyMapper, valueMapper));
    }

    public static <K, V> EntityFromModelMapper<Map<K, V>> fromMap(PrimitiveToModelMapper<K> toKey, PrimitiveFromModelMapper<K> fromKey, ValueFromModelMapper<V, ? extends Value> value) {
        Function<Map<K, V>, Entity> mapper = notNull -> entity(notNull.keySet()
                .stream()
                .map(fromKey::map)
                .collect(toCollection(SetFactory::set)), key -> value.map(notNull.get(toKey.map(key))));
        return entity -> let(entity, mapper);
    }
}
