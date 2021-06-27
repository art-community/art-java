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
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@AllArgsConstructor(access = PRIVATE)
public class SupplierTransformer implements MetaTransformer<Supplier<?>> {
    private final MetaTransformer<?> parameterTransformer;

    @Override
    public List<?> toArray(Supplier<?> value) {
        return parameterTransformer.toArray(cast(value.get()));
    }

    @Override
    public Map<?, ?> toMap(Supplier<?> value) {
        return parameterTransformer.toMap(cast(value.get()));
    }

    @Override
    public byte[] toByteArray(Supplier<?> value) {
        return parameterTransformer.toByteArray(cast(value.get()));
    }

    @Override
    public String toString(Supplier<?> value) {
        return parameterTransformer.toString(cast(value.get()));
    }

    @Override
    public Integer toInteger(Supplier<?> value) {
        return parameterTransformer.toInteger(cast(value.get()));
    }

    @Override
    public Long toLong(Supplier<?> value) {
        return parameterTransformer.toLong(cast(value.get()));
    }

    @Override
    public Float toFloat(Supplier<?> value) {
        return parameterTransformer.toFloat(cast(value.get()));
    }

    @Override
    public Double toDouble(Supplier<?> value) {
        return parameterTransformer.toDouble(cast(value.get()));
    }

    @Override
    public Short toShort(Supplier<?> value) {
        return parameterTransformer.toShort(cast(value.get()));
    }

    @Override
    public Character toCharacter(Supplier<?> value) {
        return parameterTransformer.toCharacter(cast(value.get()));
    }

    @Override
    public Boolean toBoolean(Supplier<?> value) {
        return parameterTransformer.toBoolean(cast(value.get()));
    }

    @Override
    public Supplier<?> fromArray(List<?> value) {
        return () -> parameterTransformer.fromArray(value);
    }

    @Override
    public Supplier<?> fromMap(Map<?, ?> value) {
        return () -> parameterTransformer.fromMap(value);
    }

    @Override
    public Supplier<?> fromByteArray(byte[] value) {
        return () -> parameterTransformer.fromByteArray(value);
    }

    @Override
    public Supplier<?> fromString(String value) {
        return () -> parameterTransformer.fromString(value);
    }

    @Override
    public Supplier<?> fromInteger(Integer value) {
        return () -> parameterTransformer.fromInteger(value);
    }

    @Override
    public Supplier<?> fromLong(Long value) {
        return () -> parameterTransformer.fromLong(value);
    }

    @Override
    public Supplier<?> fromFloat(Float value) {
        return () -> parameterTransformer.fromFloat(value);
    }

    @Override
    public Supplier<?> fromDouble(Double value) {
        return () -> parameterTransformer.fromDouble(value);
    }

    @Override
    public Supplier<?> fromShort(Short value) {
        return () -> parameterTransformer.fromShort(value);
    }

    @Override
    public Supplier<?> fromCharacter(Character value) {
        return () -> parameterTransformer.fromCharacter(value);
    }

    @Override
    public Supplier<?> fromBoolean(Boolean value) {
        return () -> parameterTransformer.fromBoolean(value);
    }

    public static SupplierTransformer supplierTransformer(MetaTransformer<?> parameterTransformer) {
        return new SupplierTransformer(parameterTransformer);
    }
}
