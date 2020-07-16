/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.entity.immutable;

import io.art.entity.constants.*;
import io.art.entity.constants.ValueType.*;
import lombok.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.entity.constants.ValueType.PrimitiveType.STRING;
import static java.util.Objects.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Primitive implements Value {
    private final Object value;
    private final PrimitiveType primitiveType;

    public String getString() {
        if (isNull(value)) return null;
        if (primitiveType != STRING) {
            return value.toString();
        }
        return (String) value;
    }

    public Integer getInt() {
        if (isNull(value)) return null;
        if (primitiveType == STRING) {
            return Integer.parseInt((String) value);
        }
        return ((Number) value).intValue();
    }

    public Double getDouble() {
        if (isNull(value)) return null;
        if (primitiveType == STRING) {
            return Double.parseDouble((String) value);
        }
        return ((Number) value).doubleValue();
    }

    public Float getFloat() {
        if (isNull(value)) return null;
        if (primitiveType == STRING) {
            return Float.parseFloat((String) value);
        }
        return ((Number) value).floatValue();
    }

    public Long getLong() {
        if (isNull(value)) return null;
        if (primitiveType == STRING) {
            return Long.parseLong((String) value);
        }
        return ((Number) value).longValue();
    }

    public Boolean getBool() {
        if (isNull(value)) return null;
        if (primitiveType == STRING) {
            return Boolean.parseBoolean((String) value);
        }
        return (Boolean) value;
    }

    public Byte getByte() {
        if (isNull(value)) return null;
        if (primitiveType == STRING) {
            return Byte.parseByte((String) value);
        }
        return ((Number) value).byteValue();
    }

    @Override
    public String toString() {
        if (isNull(value)) return EMPTY_STRING;
        return value.toString();
    }

    @Override
    public boolean isEmpty() {
        return isNull(value);
    }

    @Override
    public ValueType getType() {
        return PrimitiveType.asValueType(primitiveType);
    }
}
