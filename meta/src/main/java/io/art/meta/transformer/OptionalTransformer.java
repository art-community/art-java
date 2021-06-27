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

import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.OptionalExtensions.*;
import static java.util.Optional.*;
import static lombok.AccessLevel.*;
import java.util.*;

@AllArgsConstructor(access = PRIVATE)
public class OptionalTransformer implements MetaTransformer<Optional<?>> {
    private final MetaTransformer<?> parameterTransformer;

    @Override
    public List<?> toArray(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toArray(cast(element))));
    }

    @Override
    public Map<?, ?> toMap(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toMap(cast(element))));
    }

    @Override
    public byte[] toByteArray(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toByteArray(cast(element))));
    }

    @Override
    public String toString(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toString(cast(element))));
    }

    @Override
    public Integer toInteger(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toInteger(cast(element))));
    }

    @Override
    public Long toLong(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toLong(cast(element))));
    }

    @Override
    public Float toFloat(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toFloat(cast(element))));
    }

    @Override
    public Double toDouble(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toDouble(cast(element))));
    }

    @Override
    public Short toShort(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toShort(cast(element))));
    }

    @Override
    public Character toCharacter(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toCharacter(cast(element))));
    }

    @Override
    public Boolean toBoolean(Optional<?> value) {
        return unwrap(value.map(element -> parameterTransformer.toBoolean(cast(element))));
    }

    @Override
    public Optional<?> fromArray(List<?> value) {
        return ofNullable(parameterTransformer.fromArray(value));
    }

    @Override
    public Optional<?> fromMap(Map<?, ?> value) {
        return ofNullable(parameterTransformer.fromMap(value));
    }

    @Override
    public Optional<?> fromByteArray(byte[] value) {
        return ofNullable(parameterTransformer.fromByteArray(value));
    }

    @Override
    public Optional<?> fromString(String value) {
        return ofNullable(parameterTransformer.fromString(value));
    }

    @Override
    public Optional<?> fromInteger(Integer value) {
        return ofNullable(parameterTransformer.fromInteger(value));
    }

    @Override
    public Optional<?> fromLong(Long value) {
        return ofNullable(parameterTransformer.fromLong(value));
    }

    @Override
    public Optional<?> fromFloat(Float value) {
        return ofNullable(parameterTransformer.fromFloat(value));
    }

    @Override
    public Optional<?> fromDouble(Double value) {
        return ofNullable(parameterTransformer.fromDouble(value));
    }

    @Override
    public Optional<?> fromShort(Short value) {
        return ofNullable(parameterTransformer.fromShort(value));
    }

    @Override
    public Optional<?> fromCharacter(Character value) {
        return ofNullable(parameterTransformer.fromCharacter(value));
    }

    @Override
    public Optional<?> fromBoolean(Boolean value) {
        return ofNullable(parameterTransformer.fromBoolean(value));
    }

    public static OptionalTransformer optionalTransformer(MetaTransformer<?> parameterTransformer) {
        return new OptionalTransformer(parameterTransformer);
    }
}
