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

package ru.art.entity;

import lombok.*;
import ru.art.core.checker.*;
import ru.art.entity.constants.*;
import ru.art.entity.constants.ValueType.*;
import ru.art.entity.exception.*;
import java.util.*;

import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.constants.ArrayConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.StringExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.entity.constants.CollectionMode.COLLECTION;
import static ru.art.entity.constants.CollectionMode.*;
import static ru.art.entity.constants.ValueMappingExceptionMessages.*;
import static ru.art.entity.constants.ValueType.CollectionElementsType.BOOL;
import static ru.art.entity.constants.ValueType.CollectionElementsType.BYTE;
import static ru.art.entity.constants.ValueType.CollectionElementsType.DOUBLE;
import static ru.art.entity.constants.ValueType.CollectionElementsType.ENTITY;
import static ru.art.entity.constants.ValueType.CollectionElementsType.FLOAT;
import static ru.art.entity.constants.ValueType.CollectionElementsType.INT;
import static ru.art.entity.constants.ValueType.CollectionElementsType.LONG;
import static ru.art.entity.constants.ValueType.CollectionElementsType.MAP;
import static ru.art.entity.constants.ValueType.CollectionElementsType.STRING;
import static ru.art.entity.constants.ValueType.CollectionElementsType.STRING_PARAMETERS_MAP;
import static ru.art.entity.constants.ValueType.CollectionElementsType.*;

@Getter
public class CollectionValue<T> implements Value {
    private final CollectionElementsType elementsType;
    private final Collection<T> elements;
    private final ValueType type = ValueType.COLLECTION;
    private final CollectionMode collectionMode;
    private byte[] byteElements;
    private int[] intElements;
    private long[] longElements;
    private double[] doubleElements;
    private boolean[] boolElements;
    private float[] floatElements;

    CollectionValue() {
        this.elements = emptyList();
        this.elementsType = null;
        collectionMode = COLLECTION;
    }

    CollectionValue(CollectionElementsType elementsType, Collection<T> elements) {
        this.elementsType = elementsType;
        this.elements = elements;
        collectionMode = COLLECTION;
    }

    CollectionValue(byte[] byteElements) {
        this.byteElements = byteElements;
        elementsType = BYTE;
        elements = emptyList();
        collectionMode = PRIMITIVE_ARRAY;
    }

    CollectionValue(int[] intElements) {
        this.intElements = intElements;
        elementsType = INT;
        elements = null;
        collectionMode = PRIMITIVE_ARRAY;
    }

    CollectionValue(long[] longElements) {
        this.longElements = longElements;
        elementsType = LONG;
        elements = null;
        collectionMode = PRIMITIVE_ARRAY;
    }

    CollectionValue(double[] doubleElements) {
        this.doubleElements = doubleElements;
        elementsType = DOUBLE;
        elements = null;
        collectionMode = PRIMITIVE_ARRAY;
    }

    CollectionValue(boolean[] boolElements) {
        this.boolElements = boolElements;
        elementsType = BOOL;
        elements = null;
        collectionMode = PRIMITIVE_ARRAY;
    }

    CollectionValue(float[] floatElements) {
        this.floatElements = floatElements;
        elementsType = FLOAT;
        elements = null;
        collectionMode = PRIMITIVE_ARRAY;
    }


    public List<T> getList() {
        if (isNull(elementsType)) return emptyList();
        if (CheckerForEmptiness.isEmpty(elements)) return emptyList();
        return fixedArrayOf(elements);
    }

    public Set<T> getSet() {
        if (isNull(elementsType)) return emptySet();
        if (CheckerForEmptiness.isEmpty(elements)) return emptySet();
        return cast(setOf((Collection<?>) elements));
    }

    public Queue<T> getQueue() {
        if (isNull(elementsType)) return queueOf();
        if (CheckerForEmptiness.isEmpty(elements)) return queueOf();
        return cast(queueOf((Collection<?>) elements));
    }

    public List<Value> getValueList() {
        if (isNull(elements)) return emptyList();
        if (elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, VALUE.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<MapValue> getMapValueList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != MAP) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<CollectionValue<?>> getCollectionsList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != CollectionElementsType.COLLECTION) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, COLLECTION.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<StringParametersMap> getStringParametersList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != STRING_PARAMETERS_MAP) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, STRING_PARAMETERS_MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<Entity> getEntityList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != ENTITY) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, ENTITY.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<Long> getLongList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != LONG) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, LONG.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(longElements)) {
                return emptyList();
            }
            List<Long> list = dynamicArrayOf();
            for (long longElement : longElements) {
                list.add(longElement);
            }
            return list;
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<String> getStringList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != STRING) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, STRING.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<Boolean> getBoolList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != BOOL) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BOOL.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(boolElements)) {
                return emptyList();
            }
            List<Boolean> list = dynamicArrayOf();
            for (boolean boolElement : boolElements) {
                list.add(boolElement);
            }
            return list;
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<Integer> getIntList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != INT) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, INT.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(intElements)) {
                return emptyList();
            }
            List<Integer> list = dynamicArrayOf();
            for (int intElement : intElements) {
                list.add(intElement);
            }
            return list;
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<Byte> getByteList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != BYTE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BYTE.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(byteElements)) {
                return emptyList();
            }
            List<Byte> list = dynamicArrayOf();
            for (byte byteElement : byteElements) {
                list.add(byteElement);
            }
            return list;
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<Double> getDoubleList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != DOUBLE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, DOUBLE.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(doubleElements)) {
                return emptyList();
            }
            List<Double> list = dynamicArrayOf();
            for (double doubleElement : doubleElements) {
                list.add(doubleElement);
            }
            return list;
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }

    public List<Float> getFloatList() {
        if (isNull(elementsType)) return emptyList();
        if (elementsType != FLOAT) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, FLOAT.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(floatElements)) {
                return emptyList();
            }
            List<Float> list = dynamicArrayOf();
            for (float floatElements : floatElements) {
                list.add(floatElements);
            }
            return list;
        }
        if (isEmpty()) return emptyList();
        return cast(getList());
    }


    public Set<StringParametersMap> getStringParametersSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != STRING_PARAMETERS_MAP) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, STRING_PARAMETERS_MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<MapValue> getMapValueSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != MAP) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<CollectionValue<?>> getCollectionsSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != CollectionElementsType.COLLECTION) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, COLLECTION.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<Value> getValueSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, VALUE.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<Entity> getEntitySet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != ENTITY) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, ENTITY.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<Long> getLongSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != LONG) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, LONG.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(longElements)) {
                return emptySet();
            }
            Set<Long> set = setOf();
            for (long longElement : longElements) {
                set.add(longElement);
            }
            return set;
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<String> getStringSet() {
        if (isNull(elementsType)) return emptySet();
        if (isEmpty()) return emptySet();
        if (elementsType != STRING) {
            throw new ValueMappingException(format(REQUEST_SET_ELEMENTS_TYPE_INVALID, STRING.toString(), elementsType.toString()));
        }
        return cast(getSet());
    }

    public Set<Boolean> getBoolSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != BOOL) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BOOL.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(boolElements)) {
                return emptySet();
            }
            Set<Boolean> set = setOf();
            for (boolean boolElement : boolElements) {
                set.add(boolElement);
            }
            return set;
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<Integer> getIntSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != INT) {
            throw new ValueMappingException(format(REQUEST_SET_ELEMENTS_TYPE_INVALID, INT.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(intElements)) {
                return emptySet();
            }
            Set<Integer> set = setOf();
            for (int intElement : intElements) {
                set.add(intElement);
            }
            return set;
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<Double> getDoubleSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != DOUBLE) {
            throw new ValueMappingException(format(REQUEST_SET_ELEMENTS_TYPE_INVALID, DOUBLE.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(doubleElements)) {
                return emptySet();
            }
            Set<Double> set = setOf();
            for (double doubleElement : doubleElements) {
                set.add(doubleElement);
            }
            return set;
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<Byte> getByteSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != BYTE) {
            throw new ValueMappingException(format(REQUEST_SET_ELEMENTS_TYPE_INVALID, BYTE.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(byteElements)) {
                return emptySet();
            }
            Set<Byte> set = setOf();
            for (byte byteElement : byteElements) {
                set.add(byteElement);
            }
            return set;
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<Float> getFloatSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != FLOAT) {
            throw new ValueMappingException(format(REQUEST_SET_ELEMENTS_TYPE_INVALID, FLOAT.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(floatElements)) {
                return emptySet();
            }
            Set<Float> set = setOf();
            for (float floatElement : floatElements) {
                set.add(floatElement);
            }
            return set;
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }


    public Queue<StringParametersMap> getStringParametersQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != STRING_PARAMETERS_MAP) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, STRING_PARAMETERS_MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<MapValue> getMapValueQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != MAP) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<CollectionValue<?>> getCollectionsQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != CollectionElementsType.COLLECTION) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, COLLECTION.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Value> getValueQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, VALUE.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Entity> getEntityQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != ENTITY) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, ENTITY.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Long> getLongQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != LONG) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, LONG.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(longElements)) {
                return queueOf();
            }
            Queue<Long> queue = queueOf();
            for (long longElement : longElements) {
                queue.add(longElement);
            }
            return queue;
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<String> getStringQueue() {
        if (isNull(elementsType)) return queueOf();
        if (isEmpty()) return queueOf();
        if (elementsType != STRING) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, STRING.toString(), elementsType.toString()));
        }
        return cast(getQueue());
    }

    public Queue<Boolean> getBoolQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != BOOL) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, BOOL.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(boolElements)) {
                return queueOf();
            }
            Queue<Boolean> queue = queueOf();
            for (boolean boolElement : boolElements) {
                queue.add(boolElement);
            }
            return queue;
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Integer> getIntQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != INT) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, INT.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(intElements)) {
                return queueOf();
            }
            Queue<Integer> queue = queueOf();
            for (int intElement : intElements) {
                queue.add(intElement);
            }
            return queue;
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Double> getDoubleQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != DOUBLE) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, DOUBLE.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(doubleElements)) {
                return queueOf();
            }
            Queue<Double> queue = queueOf();
            for (double doubleElement : doubleElements) {
                queue.add(doubleElement);
            }
            return queue;
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Byte> getByteQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != BYTE) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, BYTE.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(byteElements)) {
                return queueOf();
            }
            Queue<Byte> queue = queueOf();
            for (byte byteElement : byteElements) {
                queue.add(byteElement);
            }
            return queue;
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Float> getFloatQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != FLOAT) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, FLOAT.toString(), elementsType.toString()));
        }
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (CheckerForEmptiness.isEmpty(floatElements)) {
                return queueOf();
            }
            Queue<Float> queue = queueOf();
            for (float floatElement : floatElements) {
                queue.add(floatElement);
            }
            return queue;
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }


    public long[] getLongArray() {
        if (isEmpty()) return EMPTY_LONGS;
        if (elementsType != LONG) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, LONG.toString(), elementsType.toString()));
        }
        if (collectionMode == COLLECTION) {
            List<T> list = getList();
            long[] longs = new long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                longs[i] = (Long) list.get(i);
            }
            return longs;
        }
        return longElements;
    }

    public boolean[] getBoolArray() {
        if (isEmpty()) return EMPTY_BOOLEANS;
        if (elementsType != BOOL) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BOOL.toString(), elementsType.toString()));
        }
        if (collectionMode == COLLECTION) {
            List<T> list = getList();
            boolean[] booleans = new boolean[list.size()];
            for (int i = 0; i < list.size(); i++) {
                booleans[i] = (Boolean) list.get(i);
            }
            return booleans;
        }
        return boolElements;
    }

    public int[] getIntArray() {
        if (isEmpty()) return EMPTY_INTS;
        if (elementsType != INT) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, INT.toString(), elementsType.toString()));
        }
        if (collectionMode == COLLECTION) {
            List<T> list = getList();
            int[] ints = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                ints[i] = (Integer) list.get(i);
            }
            return ints;
        }
        return intElements;
    }

    public byte[] getByteArray() {
        if (isEmpty()) return EMPTY_BYTES;
        if (elementsType != BYTE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BYTE.toString(), elementsType.toString()));
        }
        if (collectionMode == COLLECTION) {
            List<T> list = getList();
            byte[] bytes = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                bytes[i] = (Byte) list.get(i);
            }
            return bytes;
        }
        return byteElements;
    }

    public double[] getDoubleArray() {
        if (isEmpty()) return EMPTY_DOUBLES;
        if (elementsType != DOUBLE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, DOUBLE.toString(), elementsType.toString()));
        }
        if (collectionMode == COLLECTION) {
            List<T> list = getList();
            double[] doubles = new double[list.size()];
            for (int i = 0; i < list.size(); i++) {
                doubles[i] = (Double) list.get(i);
            }
            return doubles;
        }
        return doubleElements;
    }

    public float[] getFloatArray() {
        if (isEmpty()) return EMPTY_FLOATS;
        if (elementsType != FLOAT) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, FLOAT.toString(), elementsType.toString()));
        }
        if (collectionMode == COLLECTION) {
            List<T> list = getList();
            float[] floats = new float[list.size()];
            for (int i = 0; i < list.size(); i++) {
                floats[i] = (Float) list.get(i);
            }
            return floats;
        }
        return floatElements;
    }


    @Override
    public String toString() {
        switch (collectionMode) {
            case PRIMITIVE_ARRAY:
                switch (elementsType) {
                    case LONG:
                        return emptyIfNull(Arrays.toString(longElements));
                    case DOUBLE:
                        return emptyIfNull(Arrays.toString(doubleElements));
                    case INT:
                        return emptyIfNull(Arrays.toString(intElements));
                    case BOOL:
                        return emptyIfNull(Arrays.toString(boolElements));
                    case BYTE:
                        return emptyIfNull(Arrays.toString(byteElements));
                }
            case COLLECTION:
                return emptyIfNull(Arrays.toString(elements.toArray()));
        }
        return EMPTY_STRING;
    }

    public String toStringWithoutBraces() {
        StringBuilder str = new StringBuilder();
        Iterator<T> iterator = elements.iterator();
        while (iterator.hasNext()) {
            str.append(iterator.next());
            if (iterator.hasNext()) {
                str.append(COMMA);
            }
        }
        return str.toString();
    }

    @Override
    public boolean isEmpty() {
        if (isNull(elementsType)) return true;
        switch (collectionMode) {
            case PRIMITIVE_ARRAY:
                switch (elementsType) {
                    case LONG:
                        return CheckerForEmptiness.isEmpty(longElements);
                    case DOUBLE:
                        return CheckerForEmptiness.isEmpty(doubleElements);
                    case INT:
                        return CheckerForEmptiness.isEmpty(intElements);
                    case BOOL:
                        return CheckerForEmptiness.isEmpty(boolElements);
                    case BYTE:
                        return CheckerForEmptiness.isEmpty(byteElements);
                }
            case COLLECTION:
                return CheckerForEmptiness.isEmpty(elements);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(getList(), ((CollectionValue<?>) o).getList());
    }

    @Override
    public int hashCode() {
        List<T> elements = getList();
        return elements != null ? elements.hashCode() : 0;
    }
}