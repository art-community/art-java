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
import ru.art.core.factory.*;
import ru.art.entity.constants.*;
import ru.art.entity.constants.ValueType.*;
import ru.art.entity.exception.*;
import static java.lang.Long.*;
import static java.text.MessageFormat.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.constants.ArrayConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.StringExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.core.parser.PrimitiveParser.*;
import static ru.art.entity.PrimitivesFactory.*;
import static ru.art.entity.Value.*;
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
import java.util.*;

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
        if (isEmpty()) return emptyList();
        if (collectionMode == PRIMITIVE_ARRAY) {
            switch (elementsType) {
                case LONG:
                    return cast(arrayOf(longElements));
                case DOUBLE:
                    return cast(arrayOf(doubleElements));
                case FLOAT:
                    return cast(arrayOf(floatElements));
                case INT:
                    return cast(arrayOf(intElements));
                case BOOL:
                    return cast(arrayOf(boolElements));
                case BYTE:
                    return cast(arrayOf(byteElements));
            }
        }
        return fixedArrayOf(elements);
    }

    public Set<T> getSet() {
        if (isEmpty()) return emptySet();
        if (collectionMode == PRIMITIVE_ARRAY) {
            switch (elementsType) {
                case LONG:
                    return cast(setOf(longElements));
                case DOUBLE:
                    return cast(setOf(doubleElements));
                case FLOAT:
                    return cast(setOf(floatElements));
                case INT:
                    return cast(setOf(intElements));
                case BOOL:
                    return cast(setOf(boolElements));
                case BYTE:
                    return cast(setOf(byteElements));
            }
        }
        return cast(setOf((Collection<?>) elements));
    }

    public Queue<T> getQueue() {
        if (isEmpty()) return queueOf();
        if (collectionMode == PRIMITIVE_ARRAY) {
            switch (elementsType) {
                case LONG:
                    return cast(queueOf(longElements));
                case DOUBLE:
                    return cast(queueOf(doubleElements));
                case FLOAT:
                    return cast(queueOf(floatElements));
                case INT:
                    return cast(queueOf(intElements));
                case BOOL:
                    return cast(queueOf(boolElements));
                case BYTE:
                    return cast(queueOf(byteElements));
            }
        }
        return cast(queueOf((Collection<?>) elements));
    }

    public List<Value> getValueList() {
        if (isEmpty()) return emptyList();
        List<Value> list = dynamicArrayOf();
        switch (elementsType) {
            case STRING:
                return elements
                        .stream()
                        .map(element -> stringPrimitive(cast(element)))
                        .collect(toList());
            case LONG: {
                if (collectionMode == PRIMITIVE_ARRAY) {
                    for (long element : longElements) {
                        list.add(longPrimitive(element));
                    }
                    return list;
                }
                return elements.stream().map(element -> longPrimitive(((Number) element).longValue())).collect(toList());
            }
            case DOUBLE: {
                if (collectionMode == PRIMITIVE_ARRAY) {
                    for (double element : doubleElements) {
                        list.add(doublePrimitive(element));
                    }
                    return list;
                }
                return elements.stream().map(element -> doublePrimitive(((Number) element).doubleValue())).collect(toList());
            }
            case FLOAT: {
                if (collectionMode == PRIMITIVE_ARRAY) {
                    for (float element : floatElements) {
                        list.add(floatPrimitive(element));
                    }
                    return list;
                }
                return elements.stream().map(element -> floatPrimitive(((Number) element).floatValue())).collect(toList());
            }
            case INT: {
                if (collectionMode == PRIMITIVE_ARRAY) {
                    for (int element : intElements) {
                        list.add(intPrimitive(element));
                    }
                    return list;
                }
                return elements.stream().map(element -> intPrimitive(((Number) element).intValue())).collect(toList());
            }
            case BOOL: {
                if (collectionMode == PRIMITIVE_ARRAY) {
                    for (boolean element : boolElements) {
                        list.add(boolPrimitive(element));
                    }
                    return list;
                }
                return elements.stream().map(element -> boolPrimitive(cast(element))).collect(toList());
            }
            case BYTE: {
                if (collectionMode == PRIMITIVE_ARRAY) {
                    for (byte element : byteElements) {
                        list.add(bytePrimitive(element));
                    }
                    return list;
                }
                return elements.stream().map(element -> bytePrimitive(((Number) element).byteValue())).collect(toList());
            }
            case ENTITY:
                return cast(getEntityList());
            case COLLECTION:
                return cast(getCollectionsList());
            case MAP:
                return cast(getMapValueList());
            case STRING_PARAMETERS_MAP:
                return cast(getStringParametersList());
            case VALUE:
                return cast(getList());
        }
        return cast(getList());
    }

    public List<MapValue> getMapValueList() {
        if (isEmpty()) return emptyList();
        if (elementsType != MAP && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, MAP.toString(), elementsType.toString()));
        }
        return cast(getList());
    }

    public List<CollectionValue<?>> getCollectionsList() {
        if (isEmpty()) return emptyList();
        if (elementsType != CollectionElementsType.COLLECTION && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, COLLECTION.toString(), elementsType.toString()));
        }
        return cast(getList());
    }

    public List<StringParametersMap> getStringParametersList() {
        if (isEmpty()) return emptyList();
        if (elementsType != STRING_PARAMETERS_MAP && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, STRING_PARAMETERS_MAP.toString(), elementsType.toString()));
        }
        return cast(getList());
    }

    public List<Entity> getEntityList() {
        if (isEmpty()) return emptyList();
        if (elementsType != ENTITY && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, ENTITY.toString(), elementsType.toString()));
        }
        return cast(getList());
    }

    public List<Long> getLongList() {
        if (isEmpty()) return emptyList();
        switch (elementsType) {
            case VALUE:
                return elements.stream()
                        .filter(element -> isPrimitive(cast(element)))
                        .map(element -> asPrimitive(cast(element)).getLong())
                        .collect(toList());
            case STRING:
                return elements.stream()
                        .map(element -> parseLong(cast(element)))
                        .collect(toList());
            case LONG:
                return collectionMode == PRIMITIVE_ARRAY ? arrayOf(longElements) : cast(elements);
            case DOUBLE:
                return (collectionMode == PRIMITIVE_ARRAY ? arrayOf(doubleElements).stream() : elements.stream())
                        .map(element -> ((Number) element).longValue())
                        .collect(toList());
            case FLOAT:
                return (collectionMode == PRIMITIVE_ARRAY ? arrayOf(floatElements).stream() : elements.stream())
                        .map(element -> ((Number) element).longValue())
                        .collect(toList());
            case INT:
                return (collectionMode == PRIMITIVE_ARRAY ? arrayOf(intElements).stream() : elements.stream())
                        .map(element -> ((Number) element).longValue())
                        .collect(toList());
            case BYTE:
                return (collectionMode == PRIMITIVE_ARRAY ? arrayOf(byteElements).stream() : elements.stream())
                        .map(element -> ((Number) element).longValue())
                        .collect(toList());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, LONG.toString(), elementsType.toString()));
    }

    public List<String> getStringList() {
        if (isEmpty()) return emptyList();
        if (collectionMode == PRIMITIVE_ARRAY) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, STRING.toString(), elementsType.toString()));
        }
        return getList().stream().map(Object::toString).collect(toList());
    }

    public List<Boolean> getBoolList() {
        if (isEmpty()) return emptyList();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != BOOL) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BOOL.toString(), elementsType.toString()));
            }
            List<Boolean> list = dynamicArrayOf();
            for (boolean element : boolElements) {
                list.add(element);
            }
            return list;
        }
        switch (elementsType) {
            case VALUE:
                return getList()
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getBool())
                        .collect(toList());
            case STRING:
                return getStringList().stream()
                        .map(Boolean::parseBoolean)
                        .collect(toList());
            case BOOL:
                return cast(getList());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BOOL.toString(), elementsType.toString()));
    }

    public List<Integer> getIntList() {
        if (isEmpty()) return emptyList();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != INT) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, INT.toString(), elementsType.toString()));
            }
            List<Integer> list = dynamicArrayOf();
            for (int element : intElements) {
                list.add(element);
            }
            return list;
        }
        switch (elementsType) {
            case VALUE:
                return getList()
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getInt())
                        .collect(toList());
            case STRING:
                return getStringList().stream()
                        .map(Integer::parseInt)
                        .collect(toList());
            case INT:
                return cast(getList());
            case LONG:
            case DOUBLE:
            case FLOAT:
            case BYTE:
                return getList().stream()
                        .map(element -> ((Number) element).intValue())
                        .collect(toList());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, INT.toString(), elementsType.toString()));
    }

    public List<Byte> getByteList() {
        if (isEmpty()) return emptyList();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != BYTE) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BYTE.toString(), elementsType.toString()));
            }
            List<Byte> list = dynamicArrayOf();
            for (byte element : byteElements) {
                list.add(element);
            }
            return list;
        }
        switch (elementsType) {
            case VALUE:
                return getList()
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getByte())
                        .collect(toList());
            case STRING:
                return getStringList().stream()
                        .map(Byte::parseByte)
                        .collect(toList());
            case BYTE:
                return cast(getList());
            case LONG:
            case DOUBLE:
            case FLOAT:
            case INT:
                return getList().stream()
                        .map(element -> ((Number) element).byteValue())
                        .collect(toList());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BYTE.toString(), elementsType.toString()));
    }

    public List<Double> getDoubleList() {
        if (isEmpty()) return emptyList();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != DOUBLE) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, DOUBLE.toString(), elementsType.toString()));
            }
            List<Double> list = dynamicArrayOf();
            for (double element : doubleElements) {
                list.add(element);
            }
            return list;
        }
        switch (elementsType) {
            case VALUE:
                return getList()
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getDouble())
                        .collect(toList());
            case STRING:
                return getStringList().stream()
                        .map(Double::parseDouble)
                        .collect(toList());
            case DOUBLE:
                return cast(getList());
            case LONG:
            case FLOAT:
            case INT:
            case BYTE:
                return getList().stream()
                        .map(element -> ((Number) element).doubleValue())
                        .collect(toList());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, DOUBLE.toString(), elementsType.toString()));
    }

    public List<Float> getFloatList() {
        if (isEmpty()) return emptyList();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != FLOAT) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, FLOAT.toString(), elementsType.toString()));
            }
            List<Float> list = dynamicArrayOf();
            for (float element : floatElements) {
                list.add(element);
            }
            return list;
        }
        switch (elementsType) {
            case VALUE:
                return getList()
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getFloat())
                        .collect(toList());
            case STRING:
                return getStringList().stream()
                        .map(Float::parseFloat)
                        .collect(toList());
            case FLOAT:
                return cast(getList());
            case LONG:
            case DOUBLE:
            case INT:
            case BYTE:
                return getList().stream()
                        .map(element -> ((Number) element).floatValue())
                        .collect(toList());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, FLOAT.toString(), elementsType.toString()));
    }


    public Set<StringParametersMap> getStringParametersSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != STRING_PARAMETERS_MAP && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, STRING_PARAMETERS_MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<MapValue> getMapValueSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != MAP && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<CollectionValue<?>> getCollectionsSet() {
        if (isNull(elementsType)) return emptySet();
        if (elementsType != CollectionElementsType.COLLECTION && elementsType != VALUE) {
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
        if (elementsType != ENTITY && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, ENTITY.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }

    public Set<Long> getLongSet() {
        if (isEmpty()) return emptySet();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != LONG) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, LONG.toString(), elementsType.toString()));
            }
            Set<Long> set = setOf();
            for (long element : longElements) {
                set.add(element);
            }
            return set;
        }
        switch (elementsType) {
            case VALUE:
                return getList()
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getLong())
                        .collect(toSet());
            case STRING:
                return getStringList().stream()
                        .map(Long::parseLong)
                        .collect(toSet());
            case LONG:
                return cast(getSet());
            case DOUBLE:
            case FLOAT:
            case INT:
            case BYTE:
                return getList().stream()
                        .map(element -> ((Number) element).longValue())
                        .collect(toSet());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, LONG.toString(), elementsType.toString()));
    }

    public Set<String> getStringSet() {
        if (isEmpty()) return emptySet();
        if (collectionMode == PRIMITIVE_ARRAY) {
            throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, STRING.toString(), elementsType.toString()));
        }
        return getList().stream().map(Object::toString).collect(toSet());
    }

    public Set<Boolean> getBoolSet() {
        if (isEmpty()) return emptySet();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != BOOL) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BOOL.toString(), elementsType.toString()));
            }
            Set<Boolean> set = setOf();
            for (boolean element : boolElements) {
                set.add(element);
            }
            return set;
        }
        switch (elementsType) {
            case VALUE:
                return getList()
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getBool())
                        .collect(toSet());
            case STRING:
                return getStringList().stream()
                        .map(Boolean::parseBoolean)
                        .collect(toSet());
            case BOOL:
                return cast(getSet());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BOOL.toString(), elementsType.toString()));
    }

    public Set<Integer> getIntSet() {
        if (isEmpty()) return emptySet();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != INT) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, INT.toString(), elementsType.toString()));
            }
            Set<Integer> set = setOf();
            for (int element : intElements) {
                set.add(element);
            }
            return set;
        }
        switch (elementsType) {
            case VALUE:
                return getList()
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getInt())
                        .collect(toSet());
            case STRING:
                return getStringList().stream()
                        .map(Integer::parseInt)
                        .collect(toSet());
            case LONG:
                return cast(getSet());
            case DOUBLE:
            case FLOAT:
            case INT:
            case BYTE:
                return getList().stream()
                        .map(element -> ((Number) element).intValue())
                        .collect(toSet());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, INT.toString(), elementsType.toString()));
    }

    public Set<Double> getDoubleSet() {
        if (isEmpty()) return emptySet();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != DOUBLE) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, DOUBLE.toString(), elementsType.toString()));
            }
            Set<Double> set = setOf();
            for (double element : doubleElements) {
                set.add(element);
            }
            return set;
        }
        switch (elementsType) {
            case VALUE:
                return getList()
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getDouble())
                        .collect(toSet());
            case STRING:
                return getStringList().stream()
                        .map(Double::parseDouble)
                        .collect(toSet());
            case DOUBLE:
                return cast(getSet());
            case LONG:
            case FLOAT:
            case INT:
            case BYTE:
                return getList().stream()
                        .map(element -> ((Number) element).doubleValue())
                        .collect(toSet());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, DOUBLE.toString(), elementsType.toString()));
    }

    public Set<Byte> getByteSet() {
        if (isEmpty()) return emptySet();
        if (collectionMode == PRIMITIVE_ARRAY) {
            if (elementsType != BYTE) {
                throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BYTE.toString(), elementsType.toString()));
            }
            Set<Byte> set = setOf();
            for (byte element : byteElements) {
                set.add(element);
            }
            return set;
        }
        switch (elementsType) {
            case VALUE:
                return elements
                        .stream()
                        .map(element -> asPrimitive(cast(element)).getByte())
                        .collect(toSet());
            case STRING:
                return elements.stream()
                        .map(element -> (String) element)
                        .map(Byte::parseByte)
                        .collect(toSet());
            case BYTE:
                return cast(getSet());
            case LONG:
            case DOUBLE:
            case FLOAT:
            case INT:
                return getList().stream()
                        .map(element -> ((Number) element).byteValue())
                        .collect(toSet());
        }
        throw new ValueMappingException(format(REQUEST_LIST_ELEMENTS_TYPE_INVALID, BYTE.toString(), elementsType.toString()));
    }

    public Set<Float> getFloatSet() {
        if (isNull(elementsType)) return emptySet();
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
        if (elementsType == VALUE) {
            if (isEmpty()) return emptySet();
            return getList()
                    .stream()
                    .filter(element -> isPrimitive(cast(element)) && asPrimitive(cast(element)).getPrimitiveType() == PrimitiveType.FLOAT)
                    .map(element -> asPrimitive(cast(element)).getFloat())
                    .collect(toSet());
        }
        if (elementsType != FLOAT) {
            throw new ValueMappingException(format(REQUEST_SET_ELEMENTS_TYPE_INVALID, FLOAT.toString(), elementsType.toString()));
        }
        if (isEmpty()) return emptySet();
        return cast(getSet());
    }


    public Queue<StringParametersMap> getStringParametersQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != STRING_PARAMETERS_MAP && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, STRING_PARAMETERS_MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<MapValue> getMapValueQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != MAP && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, MAP.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<CollectionValue<?>> getCollectionsQueue() {
        if (isNull(elementsType)) return queueOf();
        if (elementsType != CollectionElementsType.COLLECTION && elementsType != VALUE) {
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
        if (elementsType != ENTITY && elementsType != VALUE) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, ENTITY.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Long> getLongQueue() {
        if (isNull(elementsType)) return queueOf();
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
        if (elementsType == VALUE) {
            if (isEmpty()) return queueOf();
            return getList()
                    .stream()
                    .filter(element -> isPrimitive(cast(element)) && asPrimitive(cast(element)).getPrimitiveType() == PrimitiveType.LONG)
                    .map(element -> asPrimitive(cast(element)).getLong())
                    .collect(toCollection(CollectionsFactory::queueOf));
        }
        if (elementsType != LONG) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, LONG.toString(), elementsType.toString()));
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
        if (elementsType == VALUE) {
            if (isEmpty()) return queueOf();
            return getList()
                    .stream()
                    .filter(element -> isPrimitive(cast(element)) && asPrimitive(cast(element)).getPrimitiveType() == PrimitiveType.BOOL)
                    .map(element -> asPrimitive(cast(element)).getBool())
                    .collect(toCollection(CollectionsFactory::queueOf));
        }
        if (elementsType != BOOL) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, BOOL.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Integer> getIntQueue() {
        if (isNull(elementsType)) return queueOf();
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
        if (elementsType == VALUE) {
            if (isEmpty()) return queueOf();
            return getList()
                    .stream()
                    .filter(element -> isPrimitive(cast(element)) && asPrimitive(cast(element)).getPrimitiveType() == PrimitiveType.INT)
                    .map(element -> asPrimitive(cast(element)).getInt())
                    .collect(toCollection(CollectionsFactory::queueOf));
        }
        if (elementsType != INT) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, INT.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Double> getDoubleQueue() {
        if (isNull(elementsType)) return queueOf();
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
        if (elementsType == VALUE) {
            if (isEmpty()) return queueOf();
            return getList()
                    .stream()
                    .filter(element -> isPrimitive(cast(element)) && asPrimitive(cast(element)).getPrimitiveType() == PrimitiveType.DOUBLE)
                    .map(element -> asPrimitive(cast(element)).getDouble())
                    .collect(toCollection(CollectionsFactory::queueOf));
        }
        if (elementsType != DOUBLE) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, DOUBLE.toString(), elementsType.toString()));
        }

        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Byte> getByteQueue() {
        if (isNull(elementsType)) return queueOf();
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
        if (elementsType == VALUE) {
            if (isEmpty()) return queueOf();
            return getList()
                    .stream()
                    .filter(element -> isPrimitive(cast(element)) && asPrimitive(cast(element)).getPrimitiveType() == PrimitiveType.BYTE)
                    .map(element -> asPrimitive(cast(element)).getByte())
                    .collect(toCollection(CollectionsFactory::queueOf));
        }
        if (elementsType != BYTE) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, BYTE.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }

    public Queue<Float> getFloatQueue() {
        if (isNull(elementsType)) return queueOf();
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
        if (elementsType == VALUE) {
            if (isEmpty()) return queueOf();
            return getList()
                    .stream()
                    .filter(element -> isPrimitive(cast(element)) && asPrimitive(cast(element)).getPrimitiveType() == PrimitiveType.FLOAT)
                    .map(element -> asPrimitive(cast(element)).getFloat())
                    .collect(toCollection(CollectionsFactory::queueOf));
        }
        if (elementsType != FLOAT) {
            throw new ValueMappingException(format(REQUEST_QUEUE_ELEMENTS_TYPE_INVALID, FLOAT.toString(), elementsType.toString()));
        }
        if (isEmpty()) return queueOf();
        return cast(getQueue());
    }


    public long[] getLongArray() {
        if (isEmpty()) return EMPTY_LONGS;
        long[] values = new long[elements.size()];
        int i = 0;
        if (collectionMode == PRIMITIVE_ARRAY) {
            switch (elementsType) {
                case LONG:
                    return longElements;
                case DOUBLE: {
                    for (double element : doubleElements) {
                        values[i] = (long) element;
                        i++;
                    }
                    break;
                }
                case FLOAT: {
                    for (float element : floatElements) {
                        values[i] = (long) element;
                        i++;
                    }
                    break;
                }
                case INT: {
                    for (int element : intElements) {
                        values[i] = element;
                        i++;
                    }
                    break;
                }
                case BYTE: {
                    for (byte element : byteElements) {
                        values[i] = element;
                        i++;
                    }
                    break;
                }
            }
            return copyOf(values, i);
        }
        for (Object element : elements) {
            switch (elementsType) {
                case STRING:
                    Long value = tryParseLong(cast(element));
                    if (nonNull(value)) {
                        values[i] = value;
                        i++;
                    }
                    break;
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BYTE:
                    if (nonNull(element)) {
                        values[i] = ((Number) element).longValue();
                        i++;
                    }
                    break;
                case VALUE:
                    if (isPrimitive(cast(element))) {
                        values[i] = asPrimitive(cast(element)).getLong();
                        i++;
                    }
                    break;
            }
        }
        return copyOf(values, i);
    }

    public boolean[] getBoolArray() {
        if (isEmpty()) return EMPTY_BOOLEANS;
        boolean[] values = new boolean[elements.size()];
        int i = 0;
        if (collectionMode == PRIMITIVE_ARRAY && elementsType == BOOL) {
            return boolElements;
        }
        for (Object element : elements) {
            switch (elementsType) {
                case STRING:
                    Boolean value = tryParseBoolean(cast(element));
                    if (nonNull(value)) {
                        values[i] = value;
                        i++;
                    }
                    break;
                case VALUE:
                    if (isPrimitive(cast(element))) {
                        values[i] = asPrimitive(cast(element)).getBool();
                        i++;
                    }
                    break;
            }
        }
        return copyOf(values, i);
    }

    public int[] getIntArray() {
        if (isEmpty()) return EMPTY_INTS;
        int[] values = new int[elements.size()];
        int i = 0;
        if (collectionMode == PRIMITIVE_ARRAY) {
            switch (elementsType) {
                case LONG: {
                    for (long element : longElements) {
                        values[i] = (int) element;
                        i++;
                    }
                    break;
                }
                case DOUBLE: {
                    for (double element : doubleElements) {
                        values[i] = (int) element;
                        i++;
                    }
                    break;
                }
                case FLOAT: {
                    for (float element : floatElements) {
                        values[i] = (int) element;
                        i++;
                    }
                    break;
                }
                case INT:
                    return intElements;
                case BYTE: {
                    for (byte element : byteElements) {
                        values[i] = element;
                        i++;
                    }
                    break;
                }
            }
            return copyOf(values, i);
        }
        for (Object element : elements) {
            switch (elementsType) {
                case STRING:
                    Integer value = tryParseInt(cast(element));
                    if (nonNull(value)) {
                        values[i] = value;
                        i++;
                    }
                    break;
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BYTE:
                    if (nonNull(element)) {
                        values[i] = ((Number) element).intValue();
                        i++;
                    }
                    break;
                case VALUE:
                    if (isPrimitive(cast(element))) {
                        values[i] = asPrimitive(cast(element)).getInt();
                        i++;
                    }
                    break;
            }
        }
        return copyOf(values, i);
    }

    public byte[] getByteArray() {
        if (isEmpty()) return EMPTY_BYTES;
        byte[] values = new byte[elements.size()];
        int i = 0;
        if (collectionMode == PRIMITIVE_ARRAY) {
            switch (elementsType) {
                case LONG: {
                    for (long element : longElements) {
                        values[i] = (byte) element;
                        i++;
                    }
                    break;
                }
                case DOUBLE: {
                    for (double element : doubleElements) {
                        values[i] = (byte) element;
                        i++;
                    }
                    break;
                }
                case FLOAT: {
                    for (float element : floatElements) {
                        values[i] = (byte) element;
                        i++;
                    }
                    break;
                }
                case INT: {
                    for (int element : intElements) {
                        values[i] = (byte) element;
                        i++;
                    }
                    break;
                }
                case BYTE:
                    return byteElements;
            }
            return copyOf(values, i);
        }
        for (Object element : elements) {
            switch (elementsType) {
                case STRING:
                    Byte value = tryParseByte(cast(element));
                    if (nonNull(value)) {
                        values[i] = value;
                        i++;
                    }
                    break;
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BYTE:
                    if (nonNull(element)) {
                        values[i] = ((Number) element).byteValue();
                        i++;
                    }
                    break;
                case VALUE:
                    if (isPrimitive(cast(element))) {
                        values[i] = asPrimitive(cast(element)).getByte();
                        i++;
                    }
                    break;
            }
        }
        return copyOf(values, i);
    }

    public double[] getDoubleArray() {
        if (isEmpty()) return EMPTY_DOUBLES;
        double[] values = new double[elements.size()];
        int i = 0;
        if (collectionMode == PRIMITIVE_ARRAY) {
            switch (elementsType) {
                case LONG: {
                    for (long element : longElements) {
                        values[i] = (double) element;
                        i++;
                    }
                    break;
                }
                case DOUBLE:
                    return doubleElements;
                case FLOAT: {
                    for (float element : floatElements) {
                        values[i] = (double) element;
                        i++;
                    }
                    break;
                }
                case INT: {
                    for (int element : intElements) {
                        values[i] = element;
                        i++;
                    }
                    break;
                }
                case BYTE: {
                    for (byte element : byteElements) {
                        values[i] = element;
                        i++;
                    }
                    break;
                }
            }
            return copyOf(values, i);
        }
        for (Object element : elements) {
            switch (elementsType) {
                case STRING:
                    Double value = tryParseDouble(cast(element));
                    if (nonNull(value)) {
                        values[i] = value;
                        i++;
                    }
                    break;
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BYTE:
                    if (nonNull(element)) {
                        values[i] = ((Number) element).doubleValue();
                        i++;
                    }
                    break;
                case VALUE:
                    if (isPrimitive(cast(element))) {
                        values[i] = asPrimitive(cast(element)).getDouble();
                        i++;
                    }
                    break;
            }
        }
        return copyOf(values, i);
    }

    public float[] getFloatArray() {
        if (isEmpty()) return EMPTY_FLOATS;
        float[] values = new float[elements.size()];
        int i = 0;
        if (collectionMode == PRIMITIVE_ARRAY) {
            switch (elementsType) {
                case LONG: {
                    for (long element : longElements) {
                        values[i] = (float) element;
                        i++;
                    }
                    break;
                }
                case DOUBLE: {
                    for (double element : doubleElements) {
                        values[i] = (float) element;
                        i++;
                    }
                    break;
                }
                case FLOAT:
                    return floatElements;
                case INT: {
                    for (int element : intElements) {
                        values[i] = element;
                        i++;
                    }
                    break;
                }
                case BYTE: {
                    for (byte element : byteElements) {
                        values[i] = element;
                        i++;
                    }
                    break;
                }
            }
            return copyOf(values, i);
        }
        for (Object element : elements) {
            switch (elementsType) {
                case STRING:
                    Float value = tryParseFloat(cast(element));
                    if (nonNull(value)) {
                        values[i] = value;
                        i++;
                    }
                    break;
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BYTE:
                    if (nonNull(element)) {
                        values[i] = ((Number) element).floatValue();
                        i++;
                    }
                    break;
                case VALUE:
                    if (isPrimitive(cast(element))) {
                        values[i] = asPrimitive(cast(element)).getFloat();
                        i++;
                    }
                    break;
            }
        }
        return copyOf(values, i);
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
                    case FLOAT:
                        return CheckerForEmptiness.isEmpty(floatElements);
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