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

package io.art.entity;

import lombok.*;
import io.art.entity.constants.*;
import io.art.entity.constants.ValueType.*;
import io.art.entity.exception.*;
import static java.util.Objects.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extension.StringExtensions.*;
import static io.art.entity.constants.ValueMappingExceptionMessages.*;
import static io.art.entity.constants.ValueType.PrimitiveType.BOOL;
import static io.art.entity.constants.ValueType.PrimitiveType.BYTE;
import static io.art.entity.constants.ValueType.PrimitiveType.DOUBLE;
import static io.art.entity.constants.ValueType.PrimitiveType.FLOAT;
import static io.art.entity.constants.ValueType.PrimitiveType.INT;
import static io.art.entity.constants.ValueType.PrimitiveType.LONG;
import static io.art.entity.constants.ValueType.PrimitiveType.STRING;
import java.text.*;


@Getter
@AllArgsConstructor
public class Primitive implements Value {
    private Object value;
    private PrimitiveType primitiveType;

    public static Primitive parseStringByType(String value, PrimitiveType primitiveType) {
        switch (primitiveType) {
            case INT:
                return new Primitive(Integer.valueOf(value), INT);
            case STRING:
                return new Primitive(value, STRING);
            case LONG:
                return new Primitive(Long.valueOf(value), LONG);
            case BOOL:
                return new Primitive(Boolean.valueOf(value), BOOL);
            case BYTE:
                return new Primitive(Byte.valueOf(value), BYTE);
            case DOUBLE:
                return new Primitive(Double.valueOf(value), DOUBLE);
            case FLOAT:
                return new Primitive(Float.valueOf(value), FLOAT);
            default:
                throw new ValueMappingException(MessageFormat.format(NOT_PRIMITIVE_TYPE, primitiveType));
        }
    }

    public String getString() {
        if (primitiveType != STRING) {
            return emptyIfNull(value);
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

    public Integer parseInt() {
        if (isNull(value)) {
            return null;
        }
        switch (primitiveType) {
            case STRING:
                return Integer.parseInt((String) value);
            case INT:
                return (Integer) value;
            default:
                return null;
        }
    }

    public Double parseDouble() {
        if (isNull(value)) {
            return null;
        }
        switch (primitiveType) {
            case STRING:
                return Double.parseDouble((String) value);
            case DOUBLE:
                return (Double) value;
            default:
                return null;
        }
    }

    public Long parseLong() {
        if (isNull(value)) {
            return null;
        }
        switch (primitiveType) {
            case STRING:
                return Long.parseLong((String) value);
            case LONG:
                return (Long) value;
            default:
                return null;
        }
    }

    public Boolean parseBool() {
        if (isNull(value)) {
            return null;
        }
        switch (primitiveType) {
            case STRING:
                return Boolean.parseBoolean((String) value);
            case BOOL:
                return (Boolean) value;
            default:
                return null;
        }
    }

    public Byte parseByte() {
        if (isNull(value)) {
            return null;
        }
        switch (primitiveType) {
            case STRING:
                return Byte.parseByte((String) value);
            case BYTE:
                return (Byte) value;
            default:
                return null;
        }
    }

    public Float parseFloat() {
        if (isNull(value)) {
            return null;
        }
        switch (primitiveType) {
            case STRING:
                return Float.parseFloat((String) value);
            case FLOAT:
                return (Float) value;
            default:
                return null;
        }
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

    public boolean equals(final Object other) {
        if (other == this) return true;
        if (other instanceof Primitive) {
            Primitive otherPrimitive = (Primitive) other;
            if (isNull(this.value) && isNull(otherPrimitive.value)) {
                return true;
            }
            if (isNull(this.value)) {
                return false;
            }
            if (this.value == otherPrimitive.value) {
                return true;
            }
            return this.value.equals(otherPrimitive.value);
        }
        if (isNull(this.value) && isNull(other)) {
            return true;
        }
        if (isNull(this.value)) {
            return false;
        }
        if (this.value == other) {
            return true;
        }
        return this.value.equals(other);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $value = this.getValue();
        result = result * PRIME + ($value == null ? 43 : $value.hashCode());
        final Object $primitiveType = this.getPrimitiveType();
        result = result * PRIME + ($primitiveType == null ? 43 : $primitiveType.hashCode());
        return result;
    }
}
