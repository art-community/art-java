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

package ru.art.entity.tuple;

import lombok.*;
import ru.art.entity.*;
import ru.art.entity.Value;
import ru.art.entity.StringParametersMap.*;
import ru.art.entity.constants.*;
import static lombok.AccessLevel.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.CollectionValuesFactory.*;
import static ru.art.entity.Entity.isPrimitiveType;
import static ru.art.entity.Entity.*;
import static ru.art.entity.PrimitivesFactory.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
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
            case COLLECTION:
                return readCollectionValue(tuple.subList(1, tuple.size())).value;
            case MAP:
                return readMapValue(tuple.subList(1, tuple.size())).value;
            case STRING_PARAMETERS_MAP:
                return readStringParameters(tuple.subList(1, tuple.size())).value;
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
                case COLLECTION:
                    TupleReadingResult<CollectionValue<Value>> collection = readCollectionValue(entity.subList(i + 3, entity.size()));
                    entityBuilder.valueField(name, collection.value);
                    i += collection.offset;
                    break;
                case MAP:
                    TupleReadingResult<MapValue> mapValue = readMapValue(entity.subList(i + 3, entity.size()));
                    entityBuilder.valueField(name, mapValue.value);
                    i += mapValue.offset;
                    break;
                case STRING_PARAMETERS_MAP:
                    TupleReadingResult<StringParametersMap> stringParameters = readStringParameters(entity.subList(i + 3, entity.size()));
                    entityBuilder.valueField(name, stringParameters.value);
                    i += stringParameters.offset;
                    break;
            }
        }
        return TupleReadingResult.<Entity>builder()
                .offset(size)
                .value(entityBuilder.build())
                .build();
    }

    private static TupleReadingResult<CollectionValue<Value>> readCollectionValue(List<?> collection) {
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
                case COLLECTION:
                    TupleReadingResult<CollectionValue<Value>> collectionElement = readCollectionValue(collection.subList(i + 2, collection.size()));
                    elements.add(collectionElement.value);
                    i += collectionElement.offset;
                    break;
                case MAP:
                    TupleReadingResult<MapValue> mapElement = readMapValue(collection.subList(i + 2, collection.size()));
                    elements.add(mapElement.value);
                    i += mapElement.offset;
                    break;
                case STRING_PARAMETERS_MAP:
                    TupleReadingResult<StringParametersMap> stringParametersElement = readStringParameters(collection.subList(i + 2, collection.size()));
                    elements.add(stringParametersElement.value);
                    i += stringParametersElement.offset;
                    break;
            }
        }
        return TupleReadingResult.<CollectionValue<Value>>builder()
                .offset(size)
                .value(valueCollection(elements))
                .build();
    }

    private static TupleReadingResult<StringParametersMap> readStringParameters(List<?> map) {
        Integer size = (Integer) map.get(0);
        StringParametersMapBuilder stringParametersMapBuilder = StringParametersMap.builder();
        for (int i = 0; i < size; i += 2) {
            stringParametersMapBuilder.parameter((String) map.get(i + 1), (String) map.get(i + 2));
        }
        return TupleReadingResult.<StringParametersMap>builder()
                .offset(size)
                .value(stringParametersMapBuilder.build())
                .build();
    }

    private static TupleReadingResult<MapValue> readMapValue(List<?> map) {
        Integer size = (Integer) map.get(0);
        MapValue.MapValueBuilder mapValueBuilder = MapValue.builder();
        for (int i = 0; i < size; i += 4) {
            Primitive key = readPrimitive(ValueType.values()[(Integer) map.get(i + 1)], map.get(i + 2));
            ValueType valueType = ValueType.values()[(Integer) map.get(i + 3)];
            if (isPrimitiveType(valueType)) {
                mapValueBuilder.element(key, readPrimitive(valueType, map.get(i + 4)));
                return TupleReadingResult.<MapValue>builder()
                        .offset(size)
                        .value(mapValueBuilder.build())
                        .build();
            }
            switch (valueType) {
                case ENTITY:
                    TupleReadingResult<Entity> entity = readEntity(map.subList(i + 4, map.size()));
                    mapValueBuilder.element(key, entity.value);
                    i += entity.offset;
                    break;
                case COLLECTION:
                    TupleReadingResult<CollectionValue<Value>> collectionValue = readCollectionValue(map.subList(i + 4, map.size()));
                    mapValueBuilder.element(key, collectionValue.value);
                    i += collectionValue.offset;
                    break;
                case MAP:
                    TupleReadingResult<MapValue> mapValue = readMapValue(map.subList(i + 4, map.size()));
                    mapValueBuilder.element(key, mapValue.value);
                    i += mapValue.offset;
                    break;
                case STRING_PARAMETERS_MAP:
                    TupleReadingResult<StringParametersMap> stringParameters = readStringParameters(map.subList(i + 4, map.size()));
                    mapValueBuilder.element(key, stringParameters.value);
                    i += stringParameters.offset;
                    break;
            }
        }
        return TupleReadingResult.<MapValue>builder()
                .offset(size)
                .value(mapValueBuilder.build())
                .build();
    }

    @Builder
    private static class TupleReadingResult<T> {
        T value;
        int offset;
    }
}
