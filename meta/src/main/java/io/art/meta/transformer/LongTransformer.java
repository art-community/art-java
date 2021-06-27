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
import static java.lang.Long.*;
import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class LongTransformer implements MetaTransformer<Long> {
    @Override
    public String toString(Long value) {
        return value.toString();
    }

    @Override
    public Integer toInteger(Long value) {
        return value.intValue();
    }

    @Override
    public Long toLong(Long value) {
        return value;
    }

    @Override
    public Float toFloat(Long value) {
        return value.floatValue();
    }

    @Override
    public Double toDouble(Long value) {
        return value.doubleValue();
    }

    @Override
    public Short toShort(Long value) {
        return value.shortValue();
    }

    @Override
    public Long fromString(String value) {
        return parseLong(value);
    }

    @Override
    public Long fromInteger(Integer value) {
        return value.longValue();
    }

    @Override
    public Long fromLong(Long value) {
        return value;
    }

    @Override
    public Long fromFloat(Float value) {
        return value.longValue();
    }

    @Override
    public Long fromDouble(Double value) {
        return value.longValue();
    }

    @Override
    public Long fromShort(Short value) {
        return value.longValue();
    }

    public static LongTransformer LONG_TRANSFORMER = new LongTransformer();
}
