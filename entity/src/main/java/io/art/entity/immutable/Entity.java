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

package io.art.entity.immutable;

import com.google.common.collect.*;
import io.art.core.checker.*;
import io.art.core.exception.*;
import io.art.core.lazy.*;
import io.art.entity.builder.*;
import io.art.entity.constants.*;
import io.art.entity.mapper.ValueFromModelMapper.*;
import io.art.entity.mapper.*;
import lombok.*;
import static com.google.common.collect.ImmutableSet.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.entity.constants.ValueType.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.Value.*;
import static io.art.entity.mapper.ValueToModelMapper.*;
import static io.art.entity.mapping.PrimitiveMapping.toString;
import static io.art.entity.mapping.PrimitiveMapping.*;
import static java.util.Objects.*;
import javax.annotation.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public class Entity implements Value {
    @Getter
    private final ValueType type = ENTITY;
    private final Map<Primitive, LazyValue<?>> mappedValueCache = concurrentHashMap();
    private final ImmutableSet<Primitive> keys;
    private final Function<Primitive, ? extends Value> valueProvider;

    public static EntityBuilder entityBuilder() {
        return new EntityBuilder();
    }

    public int size() {
        return keys.size();
    }

    public Map<Primitive, ? extends Value> toMap() {
        return mapToMap(key -> key, value -> value);
    }

    public <K, V> Map<K, V> mapToMap(PrimitiveToModelMapper<K> keyMapper, ValueToModelMapper<V, ? extends Value> valueMapper) {
        Map<K, V> map = mapOf();
        for (Primitive key : keys) {
            let(map(key, valueMapper), value -> map.put(keyMapper.map(key), value));
        }
        return map;
    }

    public <T> Map<String, T> mapToStringMap(ValueToModelMapper<T, ? extends Value> mapper) {
        return mapToMap(toString, mapper);
    }

    public <T> Map<Integer, T> mapToIntMap(ValueToModelMapper<T, ? extends Value> mapper) {
        return mapToMap(toInt, mapper);
    }

    public <T> Map<Long, T> mapToLongMap(ValueToModelMapper<T, ? extends Value> mapper) {
        return mapToMap(toLong, mapper);
    }

    public <T> Map<Double, T> mapToDoubleMap(ValueToModelMapper<T, ? extends Value> mapper) {
        return mapToMap(toDouble, mapper);
    }

    public <T> Map<Float, T> mapToFloatMap(ValueToModelMapper<T, ? extends Value> mapper) {
        return mapToMap(toFloat, mapper);
    }

    public <T> Map<Boolean, T> mapToBoolMap(ValueToModelMapper<T, ? extends Value> mapper) {
        return mapToMap(toBool, mapper);
    }


    public Map<Primitive, ? extends Value> asMap() {
        return asMap(key -> key, key -> key, value -> value);
    }

    public <K, V> Map<K, V> asMap(PrimitiveToModelMapper<K> toKeyMapper, PrimitiveFromModelMapper<K> fromKeyMapper, ValueToModelMapper<V, ? extends Value> valueMapper) {
        return new ProxyMap<>(toKeyMapper, fromKeyMapper, valueMapper);
    }


    public Value get(String key) {
        return get(stringPrimitive(key));
    }

    public Value get(Long key) {
        return get(longPrimitive(key));
    }

    public Value get(Integer key) {
        return get(intPrimitive(key));
    }

    public Value get(Boolean key) {
        return get(boolPrimitive(key));
    }

    public Value get(Double key) {
        return get(doublePrimitive(key));
    }

    public Value get(Float key) {
        return get(floatPrimitive(key));
    }

    public Value get(Byte key) {
        return get(bytePrimitive(key));
    }

    public Value get(Primitive primitive) {
        if (valueIsNull(primitive)) {
            return null;
        }
        return valueProvider.apply(primitive);
    }


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

    public <T, V extends Value> T map(Primitive primitive, ValueToModelMapper<T, V> mapper) {
        return cast(let(mappedValueCache.getOrDefault(primitive, lazy(() -> let(cast(valueProvider.apply(primitive)), mapper::map))), LazyValue::get));
    }


    public Value find(String key) {
        if (EmptinessChecker.isEmpty(key)) {
            return null;
        }
        Value value;
        if (nonNull(value = get(key))) {
            return value;
        }
        Queue<String> sections = queueOf(key.split(ESCAPED_DOT));
        Entity entity = this;
        String section;
        while ((section = sections.poll()) != null) {
            value = entity.get(section);
            if (valueIsNull(value)) return null;
            if (!isEntity(value)) {
                if (sections.size() > 1) return null;
                return value;
            }
            entity = asEntity(value);
        }
        return value;
    }


    public class ProxyMap<K, V> implements Map<K, V> {
        private final ValueToModelMapper<V, ? extends Value> valueMapper;
        private final PrimitiveFromModelMapper<K> fromKeyMapper;
        private final LazyValue<Map<K, V>> evaluated;
        private final LazyValue<Set<K>> evaluatedFields;

        public ProxyMap(PrimitiveToModelMapper<K> toKeyMapper, PrimitiveFromModelMapper<K> fromKeyMapper, ValueToModelMapper<V, ? extends Value> valueMapper) {
            this.valueMapper = valueMapper;
            this.fromKeyMapper = fromKeyMapper;
            this.evaluated = lazy(() -> Entity.this.mapToMap(toKeyMapper, valueMapper));
            this.evaluatedFields = lazy(() -> keys.stream().map(toKeyMapper::map).collect(toImmutableSet()));
        }

        @Override
        public int size() {
            return keys.size();
        }

        @Override
        public boolean isEmpty() {
            return keys.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            if (isNull(key)) {
                return false;
            }
            return keys.contains(fromKeyMapper.map(cast(key)));
        }

        @Override
        public boolean containsValue(Object value) {
            if (isNull(value)) {
                return false;
            }
            return evaluated.get().containsValue(value);
        }

        @Override
        public V get(Object key) {
            if (isNull(key)) {
                return null;
            }
            return Entity.this.map(fromKeyMapper.map(cast(key)), valueMapper);
        }

        @Override
        public V put(K key, V value) {
            throw new NotImplementedException("put");
        }

        @Override
        public V remove(Object key) {
            throw new NotImplementedException("remove");
        }

        @Override
        public void putAll(@Nonnull Map<? extends K, ? extends V> map) {
            throw new NotImplementedException("putAll");
        }

        @Override
        public void clear() {
            throw new NotImplementedException("clear");
        }

        @Override
        @Nonnull
        public Set<K> keySet() {
            return evaluatedFields.get();
        }

        @Override
        @Nonnull
        public Collection<V> values() {
            return evaluated.get().values();
        }

        @Override
        @Nonnull
        public Set<Entry<K, V>> entrySet() {
            return evaluated.get().entrySet();
        }
    }

    public static Entity EMPTY = entityBuilder().build();
}
