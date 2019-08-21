/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.entity;

import lombok.*;
import ru.art.entity.constants.*;
import ru.art.entity.constants.ValueType.*;
import ru.art.entity.exception.*;
import java.text.*;

import static java.util.Objects.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.StringExtensions.*;
import static ru.art.entity.constants.ValueMappingExceptionMessages.*;
import static ru.art.entity.constants.ValueType.PrimitiveType.BOOL;
import static ru.art.entity.constants.ValueType.PrimitiveType.BYTE;
import static ru.art.entity.constants.ValueType.PrimitiveType.DOUBLE;
import static ru.art.entity.constants.ValueType.PrimitiveType.FLOAT;
import static ru.art.entity.constants.ValueType.PrimitiveType.INT;
import static ru.art.entity.constants.ValueType.PrimitiveType.LONG;
import static ru.art.entity.constants.ValueType.PrimitiveType.STRING;


@Getter
@EqualsAndHashCode
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
        return ((Number) value).intValue();
    }

    public Double getDouble() {
        if (isNull(value)) return null;
        return ((Number) value).doubleValue();
    }

    public Float getFloat() {
        if (isNull(value)) return null;
        return ((Number) value).floatValue();
    }

    public Long getLong() {
        if (isNull(value)) return null;
        return ((Number) value).longValue();
    }

    public Boolean getBool() {
        return (Boolean) value;
    }

    public Byte getByte() {
        if (isNull(value)) return null;
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
}
    