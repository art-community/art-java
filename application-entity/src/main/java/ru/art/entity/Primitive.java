package ru.art.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.art.entity.constants.ValueType;
import ru.art.entity.constants.ValueType.PrimitiveType;
import ru.art.entity.exception.ValueMappingException;
import static java.util.Objects.isNull;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.entity.constants.ValueMappingExceptionMessages.NOT_PRIMITIVE_TYPE;
import static ru.art.entity.constants.ValueType.PrimitiveType.*;
import java.text.MessageFormat;


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
    