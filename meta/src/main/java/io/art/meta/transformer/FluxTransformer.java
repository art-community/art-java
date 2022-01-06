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
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static java.util.function.Function.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class FluxTransformer implements MetaTransformer<Flux<?>> {
    private final MetaTransformer<?> parameterTransformer;

    @Override
    public List<?> toArray(Flux<?> value) {
        return dynamicArrayOf(value.toIterable());
    }

    @Override
    public Flux<?> fromLazyArray(ImmutableLazyArrayImplementation<?> value) {
        return Flux.fromStream(value.stream());
    }

    @Override
    public ImmutableLazyArrayImplementation<?> toLazyArray(Flux<?> value) {
        List<?> array = dynamicArrayOf(value.toIterable());
        return cast(immutableLazyArrayOf(array, identity()));
    }

    @Override
    public byte[] toByteArray(Flux<?> value) {
        return parameterTransformer.toByteArray(cast(value.blockFirst()));
    }

    @Override
    public String toString(Flux<?> value) {
        return parameterTransformer.toString(cast(value.blockFirst()));
    }

    @Override
    public Integer toInteger(Flux<?> value) {
        return parameterTransformer.toInteger(cast(value.blockFirst()));
    }

    @Override
    public Long toLong(Flux<?> value) {
        return parameterTransformer.toLong(cast(value.blockFirst()));
    }

    @Override
    public Float toFloat(Flux<?> value) {
        return parameterTransformer.toFloat(cast(value.blockFirst()));
    }

    @Override
    public Double toDouble(Flux<?> value) {
        return parameterTransformer.toDouble(cast(value.blockFirst()));
    }

    @Override
    public Short toShort(Flux<?> value) {
        return parameterTransformer.toShort(cast(value.blockFirst()));
    }

    @Override
    public Character toCharacter(Flux<?> value) {
        return parameterTransformer.toCharacter(cast(value.blockFirst()));
    }

    @Override
    public Boolean toBoolean(Flux<?> value) {
        return parameterTransformer.toBoolean(cast(value.blockFirst()));
    }

    @Override
    public Flux<?> fromArray(List<?> value) {
        return Flux.fromIterable(value);
    }

    @Override
    public Flux<?> fromMap(Map<?, ?> value) {
        return justOrEmpty(parameterTransformer.fromMap(value));
    }

    @Override
    public Flux<?> fromByteArray(byte[] value) {
        return justOrEmpty(parameterTransformer.fromByteArray(value));
    }

    @Override
    public Flux<?> fromString(String value) {
        return justOrEmpty(parameterTransformer.fromString(value));
    }

    @Override
    public Flux<?> fromInteger(Integer value) {
        return justOrEmpty(parameterTransformer.fromInteger(value));
    }

    @Override
    public Flux<?> fromLong(Long value) {
        return justOrEmpty(parameterTransformer.fromLong(value));
    }

    @Override
    public Flux<?> fromFloat(Float value) {
        return justOrEmpty(parameterTransformer.fromFloat(value));
    }

    @Override
    public Flux<?> fromDouble(Double value) {
        return justOrEmpty(parameterTransformer.fromDouble(value));
    }

    @Override
    public Flux<?> fromShort(Short value) {
        return justOrEmpty(parameterTransformer.fromShort(value));
    }

    @Override
    public Flux<?> fromCharacter(Character value) {
        return justOrEmpty(parameterTransformer.fromCharacter(value));
    }

    @Override
    public Flux<?> fromBoolean(Boolean value) {
        return justOrEmpty(parameterTransformer.fromBoolean(value));
    }

    @Override
    public Flux<?> fromLazy(Supplier<?> value) {
        return Flux.create(emitter -> emitter.next(value.get()));
    }

    @Override
    public Supplier<?> toLazy(Flux<?> value) {
        return value::blockFirst;
    }

    public static FluxTransformer fluxTransformer(MetaTransformer<?> parameterTransformer) {
        return new FluxTransformer(parameterTransformer);
    }
}
