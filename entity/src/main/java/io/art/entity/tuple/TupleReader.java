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
import io.art.entity.immutable.Value;
import io.art.entity.immutable.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.entity.factory.ArrayFactory.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.BinaryValue.*;
import static io.art.entity.immutable.Entity.*;
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
            case BINARY:
                return binary((byte[]) tuple.get(1));
            case ENTITY:
                return readEntity(tuple.subList(1, tuple.size())).value;
            case ARRAY:
                return readArray(tuple.subList(1, tuple.size())).value;
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
        for (int index = 0; index < size; index += 3) {
            String name = (String) entity.get(index + 1);
            ValueType type = ValueType.values()[(Integer) entity.get(index + 2)];
            switch (type) {
                case STRING:
                    entityBuilder.put(name, stringPrimitive((String) entity.get(index + 3)));
                    break;
                case LONG:
                    entityBuilder.put(name, longPrimitive((Long) entity.get(index + 3)));
                    break;
                case DOUBLE:
                    entityBuilder.put(name, doublePrimitive((Double) entity.get(index + 3)));
                    break;
                case FLOAT:
                    entityBuilder.put(name, floatPrimitive((Float) entity.get(index + 3)));
                    break;
                case INT:
                    entityBuilder.put(name, intPrimitive((Integer) entity.get(index + 3)));
                    break;
                case BOOL:
                    entityBuilder.put(name, boolPrimitive((Boolean) entity.get(index + 3)));
                    break;
                case BYTE:
                    entityBuilder.put(name, bytePrimitive((Byte) entity.get(index + 3)));
                    break;
                case BINARY:
                    entityBuilder.put(name, binary((byte[]) entity.get(index + 3)));
                    break;
                case ENTITY:
                    TupleReadingResult<Entity> entityField = readEntity(entity.subList(index + 3, entity.size()));
                    entityBuilder.put(name, entityField.value);
                    index += entityField.offset;
                    break;
                case ARRAY:
                    TupleReadingResult<ArrayValue> array = readArray(entity.subList(index + 3, entity.size()));
                    entityBuilder.put(name, array.value);
                    index += array.offset;
                    break;
            }
        }
        return TupleReadingResult.<Entity>builder()
                .offset(size)
                .value(entityBuilder.build())
                .build();
    }

    private static TupleReadingResult<ArrayValue> readArray(List<?> array) {
        int size = (Integer) array.get(0);
        return TupleReadingResult.<ArrayValue>builder()
                .offset(size)
                .value(array(index -> readTuple((List<?>) array.get(index)), array::size))
                .build();
    }

    @Builder
    private static class TupleReadingResult<T> {
        T value;
        int offset;
    }
}
