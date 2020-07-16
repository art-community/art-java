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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import io.art.entity.immutable.Entity;
import io.art.entity.immutable.Value;
import io.art.entity.constants.ValueType;
import io.art.entity.exception.ValueMappingException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static io.art.core.caster.Caster.cast;
import static io.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static io.art.entity.immutable.Value.isPrimitive;
import static io.art.entity.immutable.Value.isPrimitiveType;
import static io.art.entity.constants.ExceptionMessages.VALUE_TYPE_IS_NULL;
import static io.art.entity.constants.ValueType.ENTITY;

@Getter
public class EntitySchema extends ValueSchema {
    private final List<EntityFieldSchema> fieldsSchema = dynamicArrayOf();

    EntitySchema(Entity entity) {
        super(ENTITY);
        Set<? extends Map.Entry<? extends Value, ? extends Value>> fields = entity.getFields().entrySet();
        for (Map.Entry<? extends Value, ? extends Value> entry : fields) {
            Value key = entry.getKey();
            if (!isPrimitive(key)) continue;;
            Value value = entry.getValue();
            if (isNull(value)) continue;
            fieldsSchema.add(new EntityFieldSchema(value.getType(), key.toString(), fromValue(value)));
        }
    }

    private EntitySchema() {
        super(ENTITY);
    }

    @Override
    public List<?> toTuple() {
        List<?> tuple = dynamicArrayOf(getType().ordinal());
        fieldsSchema.stream().map(EntityFieldSchema::toTuple).forEach(value -> tuple.add(cast(value)));
        return tuple;
    }

    public static EntitySchema fromTuple(List<?> tuple) {
        EntitySchema schema = new EntitySchema();
        tuple.stream()
                .skip(1)
                .map(element -> (List<?>) element)
                .map(EntityFieldSchema::fromTuple)
                .forEach(schema.fieldsSchema::add);
        return schema;
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
                case ARRAY:
                    return new EntityFieldSchema(type, name, CollectionValueSchema.fromTuple((List<?>) tuple.get(2)));
            }
            throw new ValueMappingException(VALUE_TYPE_IS_NULL);
        }
    }
}
