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
import static java.lang.Short.*;
import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class ShortTransformer implements MetaTransformer<Short> {
    @Override
    public String toString(Short value) {
        return value.toString();
    }

    @Override
    public Integer toInteger(Short value) {
        return value.intValue();
    }

    @Override
    public Long toLong(Short value) {
        return value.longValue();
    }

    @Override
    public Float toFloat(Short value) {
        return value.floatValue();
    }

    @Override
    public Double toDouble(Short value) {
        return value.doubleValue();
    }

    @Override
    public Short toShort(Short value) {
        return value;
    }

    @Override
    public Short fromString(String value) {
        return parseShort(value);
    }

    @Override
    public Short fromInteger(Integer value) {
        return value.shortValue();
    }

    @Override
    public Short fromLong(Long value) {
        return value.shortValue();
    }

    @Override
    public Short fromFloat(Float value) {
        return value.shortValue();
    }

    @Override
    public Short fromDouble(Double value) {
        return value.shortValue();
    }

    @Override
    public Short fromShort(Short value) {
        return value;
    }

    @Override
    public Short fromByte(Byte value) {
        return value.shortValue();
    }

    @Override
    public Byte toByte(Short value) {
        return value.byteValue();
    }

    public static ShortTransformer SHORT_TRANSFORMER = new ShortTransformer();
}
