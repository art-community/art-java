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
import io.art.core.lazy.*;
import io.art.entity.builder.*;
import io.art.entity.constants.*;
import io.art.entity.exception.*;
import io.art.entity.mapper.*;
import lombok.*;
import static com.google.common.collect.ImmutableMap.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.entity.constants.ExceptionMessages.*;
import static io.art.entity.constants.ValueType.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.Value.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public class Entity implements Value {
    @Getter
    private final ValueType type = ENTITY;
    private final Set<Primitive> fields;
    private final Function<Primitive, ? extends Value> valueProvider;

    public static EntityBuilder entityBuilder() {
        return new EntityBuilder();
    }

    public <T> ImmutableMap<Primitive, T> copyToMap(ValueToModelMapper<T, ? extends Value> mapper) {
        return fields.stream().collect(toImmutableMap(identity(), key -> mapper.map(cast(valueProvider.apply(key)))));
    }

    public <T> ImmutableMap<String, T> copyToStringMap(ValueToModelMapper<T, ? extends Value> mapper) {
        return fields.stream().collect(toImmutableMap(Primitive::getString, key -> mapper.map(cast(valueProvider.apply(key)))));
    }

    public <T> Map<String, T> asStringMap(ValueToModelMapper<T, ? extends Value> mapper) {
        return new ProxyStringMap<>(mapper);
    }

    public Value get(String name) {
        return get(stringPrimitive(name));
    }

    public Value get(Primitive primitive) {
        return valueProvider.apply(primitive);
    }

    public <T, V extends Value> T map(String name, ValueToModelMapper<T, V> mapper) {
        return map(stringPrimitive(name), mapper);
    }

    public <T, V extends Value> T map(Primitive primitive, ValueToModelMapper<T, V> mapper) {
        if (isNull(mapper)) throw new ValueMappingException(MAPPER_IS_NULL);
        return mapper.map(cast(get(primitive)));
    }

    public Value find(String key) {
        if (EmptinessChecker.isEmpty(key)) {
            return null;
        }
        Queue<String> sections = queueOf(key.split(ESCAPED_DOT));
        Entity entity = this;
        Value value = null;
        String section;
        while ((section = sections.poll()) != null) {
            value = entity.get(section);
            if (Value.isEmpty(value)) return null;
            if (!isEntity(value)) {
                if (sections.size() > 1) return null;
                return value;
            }
            entity = asEntity(value);
        }
        return value;
    }

    public <T, V extends Value> T mapNested(String name, ValueToModelMapper<T, V> mapper) {
        if (isNull(mapper)) throw new ValueMappingException(MAPPER_IS_NULL);
        return mapper.map(cast(find(name)));
    }

    @Override
    public boolean isEmpty() {
        return EmptinessChecker.isEmpty(fields);
    }

    @RequiredArgsConstructor
    public class ProxyStringMap<T> implements Map<String, T> {
        private final ValueToModelMapper<T, ? extends Value> mapper;
        private final LazyValue<ImmutableMap<Primitive, T>> evaluated = lazy(() -> Entity.this.copyToMap(mapper));

        @Override
        public int size() {
            return fields.size();
        }

        @Override
        public boolean isEmpty() {
            return fields.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return fields.contains(stringPrimitive(cast(key)));
        }

        @Override
        public boolean containsValue(Object value) {
            return evaluated.get().containsValue(value);
        }

        @Override
        public T get(Object key) {
            return mapper.map(cast(valueProvider.apply(stringPrimitive(cast(key)))));
        }

        @Override
        public T put(String key, T value) {
            throw new ValueMethodNotImplementedException("put");
        }

        @Override
        public T remove(Object key) {
            throw new ValueMethodNotImplementedException("remove");
        }

        @Override
        public void putAll(Map<? extends String, ? extends T> map) {
            throw new ValueMethodNotImplementedException("putAll");
        }

        @Override
        public void clear() {
            throw new ValueMethodNotImplementedException("clear");
        }

        @Override
        public Set<String> keySet() {
            return fields.stream().map(Primitive::getString).collect(toSet());
        }

        @Override
        public Collection<T> values() {
            return copyToMap(mapper).values();
        }

        @Override
        public Set<Entry<String, T>> entrySet() {
            return copyToStringMap(mapper).entrySet();
        }
    }

}
