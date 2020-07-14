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

package io.art.entity;

import io.art.entity.constants.*;
import io.art.entity.exception.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static io.art.entity.constants.ValueMappingExceptionMessages.*;

public interface PrimitivesFactory {
    static Primitive stringPrimitive(String value) {
        return new Primitive(value, ValueType.PrimitiveType.STRING);
    }

    static Primitive longPrimitive(Long value) {
        return new Primitive(value, ValueType.PrimitiveType.LONG);
    }

    static Primitive intPrimitive(Integer value) {
        return new Primitive(value, ValueType.PrimitiveType.INT);
    }

    static Primitive boolPrimitive(Boolean value) {
        return new Primitive(value, ValueType.PrimitiveType.BOOL);
    }

    static Primitive doublePrimitive(Double value) {
        return new Primitive(value, ValueType.PrimitiveType.DOUBLE);
    }

    static Primitive bytePrimitive(Byte value) {
        return new Primitive(value, ValueType.PrimitiveType.BYTE);
    }

    static Primitive floatPrimitive(Float value) {
        return new Primitive(value, ValueType.PrimitiveType.FLOAT);
    }


    static Primitive longPrimitive(long value) {
        return new Primitive(value, ValueType.PrimitiveType.LONG);
    }

    static Primitive intPrimitive(int value) {
        return new Primitive(value, ValueType.PrimitiveType.INT);
    }

    static Primitive boolPrimitive(boolean value) {
        return new Primitive(value, ValueType.PrimitiveType.BOOL);
    }

    static Primitive doublePrimitive(double value) {
        return new Primitive(value, ValueType.PrimitiveType.DOUBLE);
    }

    static Primitive bytePrimitive(byte value) {
        return new Primitive(value, ValueType.PrimitiveType.BYTE);
    }

    static Primitive floatPrimitive(float value) {
        return new Primitive(value, ValueType.PrimitiveType.FLOAT);
    }


    static Primitive primitiveFromString(String value, ValueType.PrimitiveType type) {
        if (isNull(type)) throw new ValueMappingException(PRIMITIVE_TYPE_IS_NULL_DURING_PARSING);
        switch (type) {
            case STRING:
                return stringPrimitive(value);
            case LONG:
                return longPrimitive(isNull(value) ? null : Long.valueOf(value));
            case DOUBLE:
                return doublePrimitive(isNull(value) ? null : Double.valueOf(value));
            case INT:
                return intPrimitive(isNull(value) ? null : Integer.valueOf(value));
            case BOOL:
                return boolPrimitive(isNull(value) ? null : Boolean.valueOf(value));
            case BYTE:
                return bytePrimitive(isNull(value) ? null : Byte.valueOf(value));
            case FLOAT:
                return floatPrimitive(isNull(value) ? null : Float.valueOf(value));
            default:
                throw new ValueMappingException(format(NOT_PRIMITIVE_TYPE, type));
        }
    }
}
