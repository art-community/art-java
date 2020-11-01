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

package io.art.value.immutable;

import io.art.value.constants.*;
import io.art.value.constants.ValueConstants.ValueType.*;
import io.art.value.constants.ValueConstants.ValueType.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.value.constants.ValueConstants.ValueType.PrimitiveType.STRING;
import static io.art.value.constants.ValueConstants.ValueType.PrimitiveType.STRING;
import java.util.*;

@Getter
@AllArgsConstructor
public class Primitive implements Value {
    @Getter(lazy = true)
    @Accessors(fluent = true)
    private final int hashCode = Objects.hashCode(getString());

    private final Object value;
    private final PrimitiveType primitiveType;

    public String getString() {
        if (Objects.isNull(value)) return null;
        if (primitiveType != STRING) {
            return value.toString();
        }
        return (String) value;
    }

    public Integer getInt() {
        if (Objects.isNull(value)) return null;
        if (primitiveType == STRING) {
            return Integer.parseInt((String) value);
        }
        return ((Number) value).intValue();
    }

    public Double getDouble() {
        if (Objects.isNull(value)) return null;
        if (primitiveType == STRING) {
            return Double.parseDouble((String) value);
        }
        return ((Number) value).doubleValue();
    }

    public Float getFloat() {
        if (Objects.isNull(value)) return null;
        if (primitiveType == STRING) {
            return Float.parseFloat((String) value);
        }
        return ((Number) value).floatValue();
    }

    public Long getLong() {
        if (Objects.isNull(value)) return null;
        if (primitiveType == STRING) {
            return Long.parseLong((String) value);
        }
        return ((Number) value).longValue();
    }

    public Boolean getBool() {
        if (Objects.isNull(value)) return null;
        if (primitiveType == STRING) {
            return Boolean.parseBoolean((String) value);
        }
        return (Boolean) value;
    }

    public Byte getByte() {
        if (Objects.isNull(value)) return null;
        if (primitiveType == STRING) {
            return Byte.parseByte((String) value);
        }
        return ((Number) value).byteValue();
    }

    @Override
    public String toString() {
        if (Objects.isNull(value)) return EMPTY_STRING;
        return value.toString();
    }

    @Override
    public ValueConstants.ValueType getType() {
        return PrimitiveType.asValueType(primitiveType);
    }

    public boolean equals(final Object other) {
        if (other == this) return true;
        if (other instanceof Primitive) {
            Primitive otherPrimitive = (Primitive) other;
            if (Objects.isNull(this.value) && Objects.isNull(otherPrimitive.value)) {
                return true;
            }
            if (Objects.isNull(this.value)) {
                return false;
            }
            if (this.value == otherPrimitive.value) {
                return true;
            }
            if (primitiveType == otherPrimitive.primitiveType) {
                return this.value.equals(otherPrimitive.value);
            }
            return getString().equals(otherPrimitive.getString());
        }
        return false;
    }
}
