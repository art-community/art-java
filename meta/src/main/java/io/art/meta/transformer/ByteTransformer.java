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
import static java.lang.Byte.*;
import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class ByteTransformer implements MetaTransformer<Byte> {
    @Override
    public String toString(Byte value) {
        return value.toString();
    }

    @Override
    public Byte toByte(Byte value) {
        return value;
    }

    @Override
    public Long toLong(Byte value) {
        return value.longValue();
    }

    @Override
    public Float toFloat(Byte value) {
        return value.floatValue();
    }

    @Override
    public Double toDouble(Byte value) {
        return value.doubleValue();
    }

    @Override
    public Short toShort(Byte value) {
        return value.shortValue();
    }

    @Override
    public Integer toInteger(Byte value) {
        return value.intValue();
    }

    @Override
    public Byte fromString(String value) {
        return parseByte(value);
    }

    @Override
    public Byte fromByte(Byte value) {
        return value;
    }

    @Override
    public Byte fromLong(Long value) {
        return value.byteValue();
    }

    @Override
    public Byte fromFloat(Float value) {
        return value.byteValue();
    }

    @Override
    public Byte fromDouble(Double value) {
        return value.byteValue();
    }

    @Override
    public Byte fromShort(Short value) {
        return value.byteValue();
    }

    @Override
    public Byte fromInteger(Integer value) {
        return value.byteValue();
    }

    public static ByteTransformer BYTE_TRANSFORMER = new ByteTransformer();
}
