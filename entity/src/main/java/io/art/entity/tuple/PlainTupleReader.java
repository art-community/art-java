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

package io.art.entity.tuple;

import io.art.entity.builder.*;
import io.art.entity.constants.*;
import io.art.entity.immutable.*;
import io.art.entity.tuple.schema.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.entity.factory.ArrayFactory.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.BinaryValue.*;
import static io.art.entity.immutable.Entity.*;
import static io.art.entity.immutable.Value.*;
import static java.util.Objects.*;
import java.util.*;

@UtilityClass
public class PlainTupleReader {
    public static Value readTuple(List<?> tuple, ValueSchema schema) {
        if (isEmpty(tuple) || isNull(schema) || isEmpty(schema)) return null;
        if (isPrimitiveType(schema.getType())) {
            return readPrimitive(schema.getType(), tuple.get(0));
        }
        switch (schema.getType()) {
            case ENTITY:
                return readEntity(tuple, (EntitySchema) schema);
            case BINARY:
                return binary((byte[]) tuple.get(0));
            case ARRAY:
                return readArray(tuple, (ArraySchema) schema);
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    private static Primitive readPrimitive(ValueType type, Object value) {
        if (isNull(value)) {
            return null;
        }
        switch (type) {
            case STRING:
                return stringPrimitive((String) value);
            case LONG:
                return longPrimitive(((Number) value).longValue());
            case DOUBLE:
                return doublePrimitive((Double) value);
            case FLOAT:
                return floatPrimitive((Float) value);
            case INT:
                return intPrimitive((Integer) value);
            case BOOL:
                return boolPrimitive((Boolean) value);
            case BYTE:
                return bytePrimitive((Byte) value);
        }
        return null;
    }

    private static Entity readEntity(List<?> entity, EntitySchema schema) {
        if (isNull(schema)) return null;
        if (isEmpty(entity)) return null;
        EntityBuilder entityBuilder = entityBuilder();
        List<EntitySchema.EntityFieldSchema> fieldsSchema = schema.getFieldsSchema();
        for (int index = 0, fieldsSchemaSize = fieldsSchema.size(); index < fieldsSchemaSize; index++) {
            EntitySchema.EntityFieldSchema fieldSchema = fieldsSchema.get(index);
            Object value = entity.get(index);
            if (isNull(value) || isNull(fieldSchema)) continue;
            switch (fieldSchema.getType()) {
                case STRING:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> stringPrimitive((String) value));
                    break;
                case LONG:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> longPrimitive(((Number) value).longValue()));
                    break;
                case DOUBLE:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> doublePrimitive((Double) value));
                    break;
                case FLOAT:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> floatPrimitive((Float) value));
                    break;
                case INT:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> intPrimitive((Integer) value));
                    break;
                case BOOL:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> boolPrimitive((Boolean) value));
                    break;
                case BYTE:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> bytePrimitive((Byte) value));
                    break;
                case ENTITY:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> readEntity((List<?>) value, (EntitySchema) fieldSchema.getSchema()));
                    break;
                case BINARY:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> binary((byte[]) value));
                    break;
                case ARRAY:
                    entityBuilder.lazyPut(fieldSchema.getName(), () -> readArray((List<?>) value, (ArraySchema) fieldSchema.getSchema()));
                    break;
            }
        }
        return entityBuilder.build();
    }

    private static ArrayValue readArray(List<?> array, ArraySchema schema) {
        if (isNull(schema)) return null;
        if (isEmpty(array)) return null;
        return array(index -> readTuple((List<?>) array.get(index), schema.getElements().get(index)), array::size);
    }
}
