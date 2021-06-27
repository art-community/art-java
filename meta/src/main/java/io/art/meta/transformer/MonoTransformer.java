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
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Mono.*;
import java.util.*;

@AllArgsConstructor(access = PRIVATE)
public class MonoTransformer implements MetaTransformer<Mono<?>> {
    private final MetaTransformer<?> parameterTransformer;

    @Override
    public List<?> toArray(Mono<?> value) {
        return parameterTransformer.toArray(cast(value.block()));
    }

    @Override
    public Map<?, ?> toMap(Mono<?> value) {
        return parameterTransformer.toMap(cast(value.block()));
    }

    @Override
    public byte[] toByteArray(Mono<?> value) {
        return parameterTransformer.toByteArray(cast(value.block()));
    }

    @Override
    public String toString(Mono<?> value) {
        return parameterTransformer.toString(cast(value.block()));
    }

    @Override
    public Integer toInteger(Mono<?> value) {
        return parameterTransformer.toInteger(cast(value.block()));
    }

    @Override
    public Long toLong(Mono<?> value) {
        return parameterTransformer.toLong(cast(value.block()));
    }

    @Override
    public Float toFloat(Mono<?> value) {
        return parameterTransformer.toFloat(cast(value.block()));
    }

    @Override
    public Double toDouble(Mono<?> value) {
        return parameterTransformer.toDouble(cast(value.block()));
    }

    @Override
    public Short toShort(Mono<?> value) {
        return parameterTransformer.toShort(cast(value.block()));
    }

    @Override
    public Character toCharacter(Mono<?> value) {
        return parameterTransformer.toCharacter(cast(value.block()));
    }

    @Override
    public Boolean toBoolean(Mono<?> value) {
        return parameterTransformer.toBoolean(cast(value.block()));
    }

    @Override
    public Mono<?> fromArray(List<?> value) {
        return justOrEmpty(parameterTransformer.fromArray(value));
    }

    @Override
    public Mono<?> fromMap(Map<?, ?> value) {
        return justOrEmpty(parameterTransformer.fromMap(value));
    }

    @Override
    public Mono<?> fromByteArray(byte[] value) {
        return justOrEmpty(parameterTransformer.fromByteArray(value));
    }

    @Override
    public Mono<?> fromString(String value) {
        return justOrEmpty(parameterTransformer.fromString(value));
    }

    @Override
    public Mono<?> fromInteger(Integer value) {
        return justOrEmpty(parameterTransformer.fromInteger(value));
    }

    @Override
    public Mono<?> fromLong(Long value) {
        return justOrEmpty(parameterTransformer.fromLong(value));
    }

    @Override
    public Mono<?> fromFloat(Float value) {
        return justOrEmpty(parameterTransformer.fromFloat(value));
    }

    @Override
    public Mono<?> fromDouble(Double value) {
        return justOrEmpty(parameterTransformer.fromDouble(value));
    }

    @Override
    public Mono<?> fromShort(Short value) {
        return justOrEmpty(parameterTransformer.fromShort(value));
    }

    @Override
    public Mono<?> fromCharacter(Character value) {
        return justOrEmpty(parameterTransformer.fromCharacter(value));
    }

    @Override
    public Mono<?> fromBoolean(Boolean value) {
        return justOrEmpty(parameterTransformer.fromBoolean(value));
    }

    public static MonoTransformer monoTransformer(MetaTransformer<?> parameterTransformer) {
        return new MonoTransformer(parameterTransformer);
    }
}
