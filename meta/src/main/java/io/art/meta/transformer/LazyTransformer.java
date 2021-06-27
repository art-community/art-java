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

import io.art.core.property.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.property.LazyProperty.*;
import static lombok.AccessLevel.*;
import java.util.*;

@AllArgsConstructor(access = PRIVATE)
public class LazyTransformer implements MetaTransformer<LazyProperty<?>> {
    private final MetaTransformer<?> parameterTransformer;

    @Override
    public List<?> toArray(LazyProperty<?> value) {
        return parameterTransformer.toArray(cast(value.get()));
    }

    @Override
    public Map<?, ?> toMap(LazyProperty<?> value) {
        return parameterTransformer.toMap(cast(value.get()));
    }

    @Override
    public byte[] toByteArray(LazyProperty<?> value) {
        return parameterTransformer.toByteArray(cast(value.get()));
    }

    @Override
    public String toString(LazyProperty<?> value) {
        return parameterTransformer.toString(cast(value.get()));
    }

    @Override
    public Integer toInteger(LazyProperty<?> value) {
        return parameterTransformer.toInteger(cast(value.get()));
    }

    @Override
    public Long toLong(LazyProperty<?> value) {
        return parameterTransformer.toLong(cast(value.get()));
    }

    @Override
    public Float toFloat(LazyProperty<?> value) {
        return parameterTransformer.toFloat(cast(value.get()));
    }

    @Override
    public Double toDouble(LazyProperty<?> value) {
        return parameterTransformer.toDouble(cast(value.get()));
    }

    @Override
    public Short toShort(LazyProperty<?> value) {
        return parameterTransformer.toShort(cast(value.get()));
    }

    @Override
    public Character toCharacter(LazyProperty<?> value) {
        return parameterTransformer.toCharacter(cast(value.get()));
    }

    @Override
    public Boolean toBoolean(LazyProperty<?> value) {
        return parameterTransformer.toBoolean(cast(value.get()));
    }

    @Override
    public LazyProperty<?> fromArray(List<?> value) {
        return lazy(() -> parameterTransformer.fromArray(value));
    }

    @Override
    public LazyProperty<?> fromMap(Map<?, ?> value) {
        return lazy(() -> parameterTransformer.fromMap(value));
    }

    @Override
    public LazyProperty<?> fromByteArray(byte[] value) {
        return lazy(() -> parameterTransformer.fromByteArray(value));
    }

    @Override
    public LazyProperty<?> fromString(String value) {
        return lazy(() -> parameterTransformer.fromString(value));
    }

    @Override
    public LazyProperty<?> fromInteger(Integer value) {
        return lazy(() -> parameterTransformer.fromInteger(value));
    }

    @Override
    public LazyProperty<?> fromLong(Long value) {
        return lazy(() -> parameterTransformer.fromLong(value));
    }

    @Override
    public LazyProperty<?> fromFloat(Float value) {
        return lazy(() -> parameterTransformer.fromFloat(value));
    }

    @Override
    public LazyProperty<?> fromDouble(Double value) {
        return lazy(() -> parameterTransformer.fromDouble(value));
    }

    @Override
    public LazyProperty<?> fromShort(Short value) {
        return lazy(() -> parameterTransformer.fromShort(value));
    }

    @Override
    public LazyProperty<?> fromCharacter(Character value) {
        return lazy(() -> parameterTransformer.fromCharacter(value));
    }

    @Override
    public LazyProperty<?> fromBoolean(Boolean value) {
        return lazy(() -> parameterTransformer.fromBoolean(value));
    }

    public static LazyTransformer lazyTransformer(MetaTransformer<?> parameterTransformer) {
        return new LazyTransformer(parameterTransformer);
    }
}
