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
import io.art.core.collection.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapper.ValueFromModelMapper.*;
import io.art.value.mapper.ValueToModelMapper.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.value.factory.EntityFactory.*;
import java.util.*;
import java.util.function.*;

@ForGenerator
@RequiredArgsConstructor
public class EntityMapping {
    public static <K, V> EntityToModelMapper<Map<K, V>> toMap(PrimitiveToModelMapper<K> toKey, PrimitiveFromModelMapper<K> fromKey, ValueToModelMapper<V, ? extends Value> value) {
        return entity -> let(entity, notNull -> notNull.asMap(toKey, fromKey, value));
    }

    public static <K, V> EntityFromModelMapper<Map<K, V>> fromMap(PrimitiveToModelMapper<K> toKey, PrimitiveFromModelMapper<K> fromKey, ValueFromModelMapper<V, ? extends Value> value) {
        Function<Map<K, V>, Entity> mapper = notNull -> entity(notNull.keySet()
                .stream()
                .map(fromKey::map)
                .collect(setCollector()), key -> value.map(notNull.get(toKey.map(key))));
        return entity -> let(entity, mapper);
    }


    public static <K, V> EntityToModelMapper<ImmutableMap<K, V>> toImmutableMap(PrimitiveToModelMapper<K> toKey, PrimitiveFromModelMapper<K> fromKey, ValueToModelMapper<V, ? extends Value> value) {
        return entity -> let(entity, notNull -> notNull.asImmutableMap(toKey, fromKey, value));
    }

    public static <K, V> EntityFromModelMapper<ImmutableMap<K, V>> fromImmutableMap(PrimitiveToModelMapper<K> toKey, PrimitiveFromModelMapper<K> fromKey, ValueFromModelMapper<V, ? extends Value> value) {
        Function<ImmutableMap<K, V>, Entity> mapper = notNull -> entity(notNull.keySet()
                .stream()
                .map(fromKey::map)
                .collect(setCollector()), key -> value.map(notNull.get(toKey.map(key))));
        return entity -> let(entity, mapper);
    }


    public static <K, V> EntityToModelMapper<Map<K, V>> toMutableMap(PrimitiveToModelMapper<K> toKey, ValueToModelMapper<V, ? extends Value> value) {
        return entity -> let(entity, notNull -> notNull.toMap(toKey, value));
    }
}
