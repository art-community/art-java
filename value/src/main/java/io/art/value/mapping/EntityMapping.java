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

import io.art.core.factory.*;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapper.ValueFromModelMapper.*;
import io.art.value.mapper.ValueToModelMapper.*;
import lombok.experimental.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.value.factory.EntityFactory.*;
import static java.util.stream.Collectors.*;
import java.util.*;

@UtilityClass
public class EntityMapping {
    public static <K, V> EntityToModelMapper<Map<K, V>> toMap(
            PrimitiveToModelMapper<K> toKeyMapper,
            PrimitiveFromModelMapper<K> fromKeyMapper,
            ValueToModelMapper<V, ? extends Value> valueMapper) {
        return entity -> let(entity, notNull -> notNull.asMap(toKeyMapper, fromKeyMapper, valueMapper));
    }

    public static <K, V> EntityToModelMapper<Map<K, V>> toMutableMap(PrimitiveToModelMapper<K> keyMapper, ValueToModelMapper<V, ? extends Value> valueMapper) {
        return entity -> let(entity, notNull -> notNull.mapToMap(keyMapper, valueMapper));
    }

    public static <K, V> EntityFromModelMapper<Map<K, V>> fromMap(
            PrimitiveToModelMapper<K> toKeyMapper,
            PrimitiveFromModelMapper<K> fromKeyMapper,
            ValueFromModelMapper<V, ? extends Value> valueMapper) {
        return entity -> let(
                entity,
                notNull -> entity(entity.keySet()
                                .stream()
                                .map(fromKeyMapper::map)
                                .collect(toCollection(SetFactory::set)),
                        key -> valueMapper.map(entity.get(toKeyMapper.map(key)))
                )
        );
    }
}
