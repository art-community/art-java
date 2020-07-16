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

import io.art.entity.immutable.*;
import io.art.entity.immutable.Value;
import lombok.*;
import lombok.experimental.*;
import io.art.entity.constants.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.factory.ArrayValuesFactory.*;
import static io.art.entity.immutable.Entity.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import java.util.*;

@UtilityClass
public class TupleReader {
    public static Value readTuple(List<?> tuple) {
        if (isEmpty(tuple)) return null;
        if (tuple.size() == 2) {
            return readPrimitive(ValueType.values()[(Integer) tuple.get(0)], tuple.get(1));
        }
        int type = (Integer) tuple.get(0);
        switch (ValueType.values()[type]) {
            case ENTITY:
                return readEntity(tuple.subList(1, tuple.size())).value;
            case ARRAY:
                return readCollectionValue(tuple.subList(1, tuple.size())).value;
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    private static Primitive readPrimitive(ValueType type, Object value) {
        switch (type) {
            case STRING:
                return stringPrimitive((String) value);
            case LONG:
                return longPrimitive((Long) value);
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

    private static TupleReadingResult<Entity> readEntity(List<?> entity) {
        int size = (Integer) entity.get(0);
        EntityBuilder entityBuilder = entityBuilder();
        for (int i = 0; i < size; i += 3) {
            String name = (String) entity.get(i + 1);
            ValueType type = ValueType.values()[(Integer) entity.get(i + 2)];
            switch (type) {
                case STRING:
                    entityBuilder.stringField(name, (String) entity.get(i + 3));
                    break;
                case LONG:
                    entityBuilder.longField(name, (Long) entity.get(i + 3));
                    break;
                case DOUBLE:
                    entityBuilder.doubleField(name, (Double) entity.get(i + 3));
                    break;
                case FLOAT:
                    entityBuilder.floatField(name, (Float) entity.get(i + 3));
                    break;
                case INT:
                    entityBuilder.intField(name, (Integer) entity.get(i + 3));
                    break;
                case BOOL:
                    entityBuilder.boolField(name, (Boolean) entity.get(i + 3));
                    break;
                case BYTE:
                    entityBuilder.byteField(name, (Byte) entity.get(i + 3));
                    break;
                case ENTITY:
                    TupleReadingResult<Entity> entityField = readEntity(entity.subList(i + 3, entity.size()));
                    entityBuilder.valueField(name, entityField.value);
                    i += entityField.offset;
                    break;
                case ARRAY:
                    TupleReadingResult<ArrayValue<Value>> collection = readCollectionValue(entity.subList(i + 3, entity.size()));
                    entityBuilder.valueField(name, collection.value);
                    i += collection.offset;
                    break;
            }
        }
        return TupleReadingResult.<Entity>builder()
                .offset(size)
                .value(entityBuilder.build())
                .build();
    }

    private static TupleReadingResult<ArrayValue<Value>> readCollectionValue(List<?> collection) {
        int size = (Integer) collection.get(0);
        List<Value> elements = dynamicArrayOf();
        for (int i = 0; i < size; i += 2) {
            Integer elementsType = (Integer) collection.get(i + 1);
            ValueType collectionElementsType = ValueType.values()[elementsType];
            switch (collectionElementsType) {
                case STRING:
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BOOL:
                case BYTE:
                    elements.add(cast(collection.get(i + 2)));
                    break;
                case ENTITY:
                    TupleReadingResult<Entity> entityElement = readEntity(collection.subList(i + 2, collection.size()));
                    elements.add(entityElement.value);
                    i += entityElement.offset;
                    break;
                case ARRAY:
                    TupleReadingResult<ArrayValue<Value>> collectionElement = readCollectionValue(collection.subList(i + 2, collection.size()));
                    elements.add(collectionElement.value);
                    i += collectionElement.offset;
                    break;
            }
        }
        return TupleReadingResult.<ArrayValue<Value>>builder()
                .offset(size)
                .value(valueArray(elements))
                .build();
    }
    @Builder
    private static class TupleReadingResult<T> {
        T value;
        int offset;
    }
}
