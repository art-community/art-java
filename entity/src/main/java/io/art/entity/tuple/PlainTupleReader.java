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

import lombok.experimental.*;
import io.art.entity.*;
import io.art.entity.constants.*;
import io.art.entity.tuple.schema.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.CollectionValuesFactory.*;
import static io.art.entity.Value.*;
import static io.art.entity.Entity.*;
import static io.art.entity.PrimitivesFactory.*;
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
            case COLLECTION:
                return readCollectionValue(tuple, (CollectionValueSchema) schema);
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    private static Primitive readPrimitive(ValueType type, Object value) {
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
        EntityBuilder entityBuilder = entityBuilder();
        if (isEmpty(entity)) return entityBuilder.build();
        List<EntitySchema.EntityFieldSchema> fieldsSchema = schema.getFieldsSchema();
        for (int i = 0, fieldsSchemaSize = fieldsSchema.size(); i < fieldsSchemaSize; i++) {
            EntitySchema.EntityFieldSchema fieldSchema = fieldsSchema.get(i);
            switch (fieldSchema.getType()) {
                case STRING:
                    entityBuilder.stringField(fieldSchema.getName(), (String) entity.get(i));
                    break;
                case LONG:
                    Number number = (Number) entity.get(i);
                    if (nonNull(number)) {
                        entityBuilder.longField(fieldSchema.getName(), number.longValue());
                        break;
                    }
                    entityBuilder.longField(fieldSchema.getName(), null);
                    break;
                case DOUBLE:
                    entityBuilder.doubleField(fieldSchema.getName(), (Double) entity.get(i));
                    break;
                case FLOAT:
                    entityBuilder.floatField(fieldSchema.getName(), (Float) entity.get(i));
                    break;
                case INT:
                    entityBuilder.intField(fieldSchema.getName(), (Integer) entity.get(i));
                    break;
                case BOOL:
                    entityBuilder.boolField(fieldSchema.getName(), (Boolean) entity.get(i));
                    break;
                case BYTE:
                    entityBuilder.byteField(fieldSchema.getName(), (Byte) entity.get(i));
                    break;
                case ENTITY:
                    entityBuilder.entityField(fieldSchema.getName(), readEntity((List<?>) entity.get(i), (EntitySchema) fieldSchema.getSchema()));
                    break;
                case COLLECTION:
                    entityBuilder.valueField(fieldSchema.getName(), readCollectionValue((List<?>) entity.get(i), (CollectionValueSchema) fieldSchema.getSchema()));
                    break;
            }
        }
        return entityBuilder.build();
    }

    private static CollectionValue<?> readCollectionValue(List<?> collection, CollectionValueSchema schema) {
        if (isNull(schema)) return null;
        if (isEmpty(collection)) return emptyCollection();
        List<?> elements = dynamicArrayOf();
        List<ValueSchema> elementsSchema = schema.getElementsSchema();
        switch (schema.getElementsType()) {
            case BYTE:
            case STRING:
            case BOOL:
            case DOUBLE:
            case FLOAT:
                return collectionValue(schema.getElementsType(), cast(collection));
            case LONG:
                return longCollection(collection.stream().filter(Objects::nonNull).map(element -> ((Number) element).longValue()).collect(toList()));
            case INT:
                return intCollection(collection.stream().filter(Objects::nonNull).map(element -> ((Number) element).intValue()).collect(toList()));
            case ENTITY:
                for (int i = 0; i < elementsSchema.size(); i++) {
                    elements.add(cast(readEntity((List<?>) collection.get(i), (EntitySchema) elementsSchema.get(i))));
                }
                return entityCollection(cast(elements));
            case COLLECTION:
                for (int i = 0; i < elementsSchema.size(); i++) {
                    elements.add(cast(readCollectionValue((List<?>) collection.get(i), (CollectionValueSchema) elementsSchema.get(i))));
                }
                return collectionOfCollections(cast(elements));
            case VALUE:
                for (int i = 0; i < elementsSchema.size(); i++) {
                    elements.add(cast(readTuple((List<?>) collection.get(i), elementsSchema.get(i))));
                }
                return valueCollection(cast(elements));

        }
        return emptyCollection();
    }
}
