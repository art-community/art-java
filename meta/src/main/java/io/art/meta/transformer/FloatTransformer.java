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
import static java.lang.Float.*;
import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class FloatTransformer implements MetaTransformer<Float> {
    @Override
    public String toString(Float value) {
        return value.toString();
    }

    @Override
    public Integer toInteger(Float value) {
        return value.intValue();
    }

    @Override
    public Long toLong(Float value) {
        return value.longValue();
    }

    @Override
    public Float toFloat(Float value) {
        return value;
    }

    @Override
    public Double toDouble(Float value) {
        return value.doubleValue();
    }

    @Override
    public Short toShort(Float value) {
        return value.shortValue();
    }

    @Override
    public Float fromString(String value) {
        return parseFloat(value);
    }

    @Override
    public Float fromInteger(Integer value) {
        return value.floatValue();
    }

    @Override
    public Float fromLong(Long value) {
        return value.floatValue();
    }

    @Override
    public Float fromFloat(Float value) {
        return value;
    }

    @Override
    public Float fromDouble(Double value) {
        return value.floatValue();
    }

    @Override
    public Float fromShort(Short value) {
        return value.floatValue();
    }

    public static FloatTransformer FLOAT_TRANSFORMER = new FloatTransformer();
}
