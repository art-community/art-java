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
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.context.Context.*;
import static java.lang.Double.*;
import static java.lang.Float.*;
import static java.lang.Integer.*;
import static java.lang.Long.*;
import static java.lang.Short.*;
import static lombok.AccessLevel.*;

@NoArgsConstructor(access = PRIVATE)
public class StringTransformer implements MetaTransformer<String> {
    @Override
    public byte[] toByteArray(String value) {
        return value.getBytes(context().configuration().getCharset());
    }

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public Integer toInteger(String value) {
        return parseInt(value);
    }

    @Override
    public Long toLong(String value) {
        return parseLong(value);
    }

    @Override
    public Float toFloat(String value) {
        return parseFloat(value);
    }

    @Override
    public Double toDouble(String value) {
        return parseDouble(value);
    }

    @Override
    public Short toShort(String value) {
        return parseShort(value);
    }

    @Override
    public Character toCharacter(String value) {
        if (isEmpty(value)) return null;
        return value.charAt(0);
    }

    @Override
    public Boolean toBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    @Override
    public String fromByteArray(byte[] value) {
        return new String(value, context().configuration().getCharset());
    }

    @Override
    public String fromString(String value) {
        return value;
    }

    @Override
    public String fromInteger(Integer value) {
        return value.toString();
    }

    @Override
    public String fromLong(Long value) {
        return value.toString();
    }

    @Override
    public String fromFloat(Float value) {
        return value.toString();
    }

    @Override
    public String fromDouble(Double value) {
        return value.toString();
    }

    @Override
    public String fromShort(Short value) {
        return value.toString();
    }

    @Override
    public String fromCharacter(Character value) {
        return value.toString();
    }

    @Override
    public String fromBoolean(Boolean value) {
        return value.toString();
    }

    public static StringTransformer STRING_TRANSFORMER = new StringTransformer();
}
