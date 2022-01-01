/*
 * ART
 *
 * Copyright 2019-2022 ART
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

import io.art.core.collection.*;
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
}
