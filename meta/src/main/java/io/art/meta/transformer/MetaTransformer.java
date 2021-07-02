/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.meta.transformer;

import io.art.core.caster.*;
import io.art.core.collection.*;
import io.art.core.exception.*;
import io.art.meta.constants.MetaConstants.*;
import io.art.meta.exception.*;
import static io.art.core.caster.Caster.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static java.text.MessageFormat.*;
import java.util.*;
import java.util.function.*;

public interface MetaTransformer<T> {
    default List<?> toArray(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Map<?, ?> toMap(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default byte[] toByteArray(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default String toString(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Integer toInteger(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Long toLong(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Float toFloat(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Double toDouble(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Short toShort(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Byte toByte(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Character toCharacter(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Boolean toBoolean(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default ImmutableLazyArrayImplementation<?> toLazyArray(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default ImmutableLazyMapImplementation<?, ?> toLazyMap(T value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default Supplier<?> toLazy(T value) {
        return () -> value;
    }

    default T fromArray(List<?> value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromMap(Map<?, ?> value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromByteArray(byte[] value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, Arrays.toString(value), getClass()));
    }

    default T fromString(String value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromInteger(Integer value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromLong(Long value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromFloat(Float value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromDouble(Double value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromShort(Short value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromCharacter(Character value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromBoolean(Boolean value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromByte(Byte value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromLazyArray(ImmutableLazyArrayImplementation<?> value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromLazyMap(ImmutableLazyMapImplementation<?, ?> value) {
        throw new TransformationException(format(TRANSFORMATION_NOT_AVAILABLE, value, getClass()));
    }

    default T fromLazy(Supplier<?> value) {
        return cast(value.get());
    }

    default Function<T, ?> toKind(MetaTypeExternalKind kind) {
        switch (kind) {
            case MAP:
                return this::toMap;
            case ARRAY:
                return this::toArray;
            case LAZY_MAP:
                return this::toLazyMap;
            case LAZY_ARRAY:
                return this::toLazyArray;
            case LAZY:
                return this::toLazy;
            case STRING:
                return this::toString;
            case LONG:
                return this::toLong;
            case DOUBLE:
                return this::toDouble;
            case FLOAT:
                return this::toFloat;
            case INTEGER:
                return this::toInteger;
            case BOOLEAN:
                return this::toBoolean;
            case CHARACTER:
                return this::toCharacter;
            case SHORT:
                return this::toShort;
            case BYTE:
                return this::toByte;
            case BINARY:
                return this::toByteArray;
            case ENTITY:
                return Caster::cast;
            case UNKNOWN:
                break;
        }
        throw new ImpossibleSituationException();
    }

    default Function<?, T> fromKind(MetaTypeExternalKind kind) {
        switch (kind) {
            case MAP:
                return value -> fromMap(cast(value));
            case ARRAY:
                return value -> fromArray(cast(value));
            case LAZY_MAP:
                return value -> fromLazyMap(cast(value));
            case LAZY_ARRAY:
                return value -> fromLazyArray(cast(value));
            case LAZY:
                return value -> fromLazy(cast(value));
            case STRING:
                return value -> fromString(cast(value));
            case LONG:
                return value -> fromLong(cast(value));
            case DOUBLE:
                return value -> fromDouble(cast(value));
            case FLOAT:
                return value -> fromFloat(cast(value));
            case INTEGER:
                return value -> fromInteger(cast(value));
            case BOOLEAN:
                return value -> fromBoolean(cast(value));
            case CHARACTER:
                return value -> fromCharacter(cast(value));
            case SHORT:
                return value -> fromShort(cast(value));
            case BYTE:
                return value -> fromByte(cast(value));
            case BINARY:
                return value -> fromByteArray(cast(value));
            case ENTITY:
                return Caster::cast;
            case UNKNOWN:
                break;
        }
        throw new ImpossibleSituationException();
    }
}
