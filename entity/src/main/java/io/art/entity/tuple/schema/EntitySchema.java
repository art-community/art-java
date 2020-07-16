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

package io.art.entity.tuple.schema;

import com.google.common.collect.*;
import io.art.entity.constants.*;
import io.art.entity.exception.*;
import io.art.entity.immutable.Value;
import io.art.entity.immutable.*;
import lombok.*;
import static com.google.common.collect.ImmutableList.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.constants.ExceptionMessages.*;
import static io.art.entity.constants.ValueType.*;
import static io.art.entity.immutable.Value.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.Map.*;

@Getter
public class EntitySchema extends ValueSchema {
    private final ImmutableList<EntityFieldSchema> fieldsSchema;

    EntitySchema(Entity entity) {
        super(ENTITY);
        Set<? extends Entry<Primitive, ? extends Value>> fields = entity.asMap().entrySet();
        ImmutableList.Builder<EntityFieldSchema> schemaBuilder = ImmutableList.builder();
        for (Entry<Primitive, ? extends Value> entry : fields) {
            Primitive key = entry.getKey();
            Value value = entry.getValue();
            if (isEmpty(key) || isNull(value)) {
                continue;
            }
            let(fromValue(value), schema -> schemaBuilder.add(new EntityFieldSchema(value.getType(), key.getString(), schema)));
        }
        fieldsSchema = schemaBuilder.build();
    }

    private EntitySchema(ImmutableList<EntityFieldSchema> fieldsSchema) {
        super(ENTITY);
        this.fieldsSchema = fieldsSchema;
    }

    @Override
    public List<?> toTuple() {
        List<?> tuple = dynamicArrayOf(getType().ordinal());
        fieldsSchema.stream().map(EntityFieldSchema::toTuple).forEach(value -> tuple.add(cast(value)));
        return tuple;
    }

    public static EntitySchema fromTuple(List<?> tuple) {
        return new EntitySchema(tuple.stream()
                .skip(1)
                .map(element -> (List<?>) element)
                .map(EntityFieldSchema::fromTuple)
                .collect(toImmutableList()));
    }

    @Getter
    @EqualsAndHashCode
    public static class EntityFieldSchema {
        private final String name;
        private final ValueType type;
        private final ValueSchema schema;

        EntityFieldSchema(ValueType type, String name, ValueSchema schema) {
            this.type = type;
            this.name = name;
            this.schema = schema;
        }

        public List<?> toTuple() {
            if (isNull(schema)) {
                return dynamicArrayOf(type.ordinal(), name, emptyList());
            }
            return dynamicArrayOf(type.ordinal(), name, schema.toTuple());
        }

        public static EntityFieldSchema fromTuple(List<?> tuple) {
            ValueType type = ValueType.values()[((Integer) tuple.get(0))];
            String name = (String) tuple.get(1);
            if (isPrimitiveType(type)) {
                return new EntityFieldSchema(type, name, new ValueSchema(type));
            }

            switch (type) {
                case ENTITY:
                    return new EntityFieldSchema(type, name, EntitySchema.fromTuple((List<?>) tuple.get(2)));
                case BINARY:
                    return new EntityFieldSchema(type, name, new ValueSchema(type));
                case ARRAY:
                    return new EntityFieldSchema(type, name, ArraySchema.fromTuple((List<?>) tuple.get(2)));
            }

            throw new ValueMappingException(UNKNOWN_VALUE_TYPE);
        }
    }
}
