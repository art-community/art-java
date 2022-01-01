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
import static java.lang.Integer.*;
import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class IntegerTransformer implements MetaTransformer<Integer> {
    @Override
    public String toString(Integer value) {
        return value.toString();
    }

    @Override
    public Integer toInteger(Integer value) {
        return value;
    }

    @Override
    public Long toLong(Integer value) {
        return value.longValue();
    }

    @Override
    public Float toFloat(Integer value) {
        return value.floatValue();
    }

    @Override
    public Double toDouble(Integer value) {
        return value.doubleValue();
    }

    @Override
    public Short toShort(Integer value) {
        return value.shortValue();
    }

    @Override
    public Integer fromString(String value) {
        return parseInt(value);
    }

    @Override
    public Integer fromInteger(Integer value) {
        return value;
    }

    @Override
    public Integer fromLong(Long value) {
        return value.intValue();
    }

    @Override
    public Integer fromFloat(Float value) {
        return value.intValue();
    }

    @Override
    public Integer fromDouble(Double value) {
        return value.intValue();
    }

    @Override
    public Integer fromShort(Short value) {
        return value.intValue();
    }

    @Override
    public Integer fromByte(Byte value) {
        return value.intValue();
    }

    @Override
    public Byte toByte(Integer value) {
        return value.byteValue();
    }

    public static IntegerTransformer INTEGER_TRANSFORMER = new IntegerTransformer();
}
