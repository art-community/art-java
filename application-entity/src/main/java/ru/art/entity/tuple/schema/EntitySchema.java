/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.entity.tuple.schema;

import lombok.*;
import ru.art.entity.Value;
import ru.art.entity.*;
import ru.art.entity.constants.*;
import ru.art.entity.exception.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.Value.*;
import static ru.art.entity.constants.ValueMappingExceptionMessages.*;
import static ru.art.entity.constants.ValueType.*;
import java.util.*;

@Getter
public class EntitySchema extends ValueSchema {
    private final List<EntityFieldSchema> fieldsSchema = dynamicArrayOf();

    EntitySchema(Entity entity) {
        super(ENTITY);
        Set<? extends Map.Entry<String, ? extends Value>> fields = entity.getFields().entrySet();
        for (Map.Entry<String, ? extends Value> entry : fields) {
            fieldsSchema.add(new EntityFieldSchema(entry.getValue().getType(), entry.getKey(), fromValue(entry.getValue())));
        }
    }

    private EntitySchema() {
        super(ENTITY);
    }

    @Override
    public List<?> toTuple() {
        List<?> tuple = dynamicArrayOf(getType().name());
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
                return dynamicArrayOf(type.name(), name, emptyList());
            }
            return dynamicArrayOf(type.name(), name, schema.toTuple());
        }

        public static EntityFieldSchema fromTuple(List<?> tuple) {
            ValueType type = ValueType.valueOf((String) tuple.get(0));
            String name = (String) tuple.get(1);
            if (isPrimitiveType(type)) {
                return new EntityFieldSchema(type, name, new ValueSchema(type));
            }

            switch (type) {
                case ENTITY:
                    return new EntityFieldSchema(type, name, EntitySchema.fromTuple((List<?>) tuple.get(2)));
                case COLLECTION:
                    return new EntityFieldSchema(type, name, CollectionValueSchema.fromTuple((List<?>) tuple.get(2)));
                case MAP:
                    return new EntityFieldSchema(type, name, MapValueSchema.fromTuple((List<?>) tuple.get(2)));
                case STRING_PARAMETERS_MAP:
                    return new EntityFieldSchema(type, name, StringParametersSchema.fromTuple((List<?>) tuple.get(2)));
            }
            throw new ValueMappingException(VALUE_TYPE_IS_NULL);
        }
    }
}
