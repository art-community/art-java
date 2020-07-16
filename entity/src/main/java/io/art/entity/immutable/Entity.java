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

import io.art.core.checker.*;
import io.art.entity.builder.*;
import io.art.entity.constants.*;
import io.art.entity.exception.*;
import io.art.entity.mapper.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.primitive.PrimitivesFactory.*;
import static io.art.entity.immutable.Value.*;
import static io.art.entity.constants.ExceptionMessages.*;
import static io.art.entity.constants.ValueType.*;
import static java.util.Objects.*;
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

    public Value get(String name) {
        return valueProvider.apply(stringPrimitive(name));
    }

    public <T, V extends Value> T map(String name, ValueToModelMapper<T, V> mapper) {
        if (isNull(mapper)) throw new ValueMappingException(MAPPER_IS_NULL);
        return mapper.map(cast(get(name)));
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
}
