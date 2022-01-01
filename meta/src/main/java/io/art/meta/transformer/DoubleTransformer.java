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

import lombok.*;
import static java.lang.Double.*;
import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class DoubleTransformer implements MetaTransformer<Double> {
    @Override
    public String toString(Double value) {
        return value.toString();
    }

    @Override
    public Integer toInteger(Double value) {
        return value.intValue();
    }

    @Override
    public Long toLong(Double value) {
        return value.longValue();
    }

    @Override
    public Float toFloat(Double value) {
        return value.floatValue();
    }

    @Override
    public Double toDouble(Double value) {
        return value;
    }

    @Override
    public Short toShort(Double value) {
        return value.shortValue();
    }

    @Override
    public Double fromString(String value) {
        return parseDouble(value);
    }

    @Override
    public Double fromInteger(Integer value) {
        return value.doubleValue();
    }

    @Override
    public Double fromLong(Long value) {
        return value.doubleValue();
    }

    @Override
    public Double fromFloat(Float value) {
        return value.doubleValue();
    }

    @Override
    public Double fromDouble(Double value) {
        return value;
    }

    @Override
    public Double fromShort(Short value) {
        return value.doubleValue();
    }

    @Override
    public Double fromByte(Byte value) {
        return value.doubleValue();
    }

    @Override
    public Byte toByte(Double value) {
        return value.byteValue();
    }

    public static DoubleTransformer DOUBLE_TRANSFORMER = new DoubleTransformer();
}
