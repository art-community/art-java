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

package ru.art.entity.constants;

import ru.art.entity.exception.*;

import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static ru.art.entity.constants.ValueMappingExceptionMessages.*;

public enum ValueType {
    ENTITY,
    COLLECTION,
    MAP,
    STRING_PARAMETERS_MAP,
    XML_ENTITY,
    STRING,
    LONG,
    DOUBLE,
    FLOAT,
    INT,
    BOOL,
    BYTE;

    public static PrimitiveType asPrimitiveType(ValueType valueType) {
        if (isNull(valueType)) throw new ValueMappingException(VALUE_TYPE_IS_NULL);
        switch (valueType) {
            case STRING:
                return PrimitiveType.STRING;
            case LONG:
                return PrimitiveType.LONG;
            case DOUBLE:
                return PrimitiveType.DOUBLE;
            case INT:
                return PrimitiveType.INT;
            case BOOL:
                return PrimitiveType.BOOL;
            case BYTE:
                return PrimitiveType.BYTE;
            case FLOAT:
                return PrimitiveType.FLOAT;
            default:
                throw new ValueMappingException(format(NOT_PRIMITIVE_TYPE, valueType));
        }
    }

    public static CollectionElementsType asCollectionElementsType(ValueType valueType) {
        if (isNull(valueType)) throw new ValueMappingException(VALUE_TYPE_IS_NULL);
        switch (valueType) {
            case ENTITY:
                return CollectionElementsType.ENTITY;
            case COLLECTION:
                return CollectionElementsType.COLLECTION;
            case MAP:
                return CollectionElementsType.MAP;
            case STRING_PARAMETERS_MAP:
                return CollectionElementsType.STRING_PARAMETERS_MAP;
            case STRING:
                return CollectionElementsType.STRING;
            case LONG:
                return CollectionElementsType.LONG;
            case DOUBLE:
                return CollectionElementsType.DOUBLE;
            case INT:
                return CollectionElementsType.INT;
            case BOOL:
                return CollectionElementsType.BOOL;
            case BYTE:
                return CollectionElementsType.BYTE;
            case FLOAT:
                return CollectionElementsType.FLOAT;
            default:
                throw new ValueMappingException(format(NFL_COLLECTIONS_ELEMENTS, valueType));
        }
    }

    public enum PrimitiveType {
        STRING(String.class.getName()),
        LONG(Long.class.getName()),
        DOUBLE(Double.class.getName()),
        FLOAT(Float.class.getName()),
        INT(Integer.class.getName()),
        BOOL(Boolean.class.getName()),
        BYTE(Byte.class.getName());

        private final String className;

        PrimitiveType(String className) {
            this.className = className;
        }

        public static PrimitiveType parseClassName(String className) {
            if (STRING.getClassName().equalsIgnoreCase(className)) return STRING;
            if (LONG.getClassName().equalsIgnoreCase(className)) return LONG;
            if (DOUBLE.getClassName().equalsIgnoreCase(className)) return DOUBLE;
            if (INT.getClassName().equalsIgnoreCase(className)) return INT;
            if (BOOL.getClassName().equalsIgnoreCase(className)) return BOOL;
            if (BYTE.getClassName().equalsIgnoreCase(className)) return BYTE;
            if (FLOAT.getClassName().equalsIgnoreCase(className)) return FLOAT;
            throw new ValueMappingException(format(NOT_PRIMITIVE_TYPE, className));
        }

        public static ValueType asValueType(PrimitiveType primitiveType) {
            if (isNull(primitiveType)) throw new ValueMappingException(PRIMITIVE_TYPE_IS_NULL);
            switch (primitiveType) {
                case STRING:
                    return ValueType.STRING;
                case LONG:
                    return ValueType.LONG;
                case DOUBLE:
                    return ValueType.DOUBLE;
                case INT:
                    return ValueType.INT;
                case BOOL:
                    return ValueType.BOOL;
                case BYTE:
                    return ValueType.BYTE;
                case FLOAT:
                    return ValueType.FLOAT;
                default:
                    throw new ValueMappingException(format(NOT_PRIMITIVE_TYPE, primitiveType));
            }
        }

        public String getClassName() {
            return className;
        }
    }

    public enum CollectionElementsType {
        STRING,
        LONG,
        DOUBLE,
        FLOAT,
        INT,
        BOOL,
        BYTE,
        ENTITY,
        COLLECTION,
        MAP,
        STRING_PARAMETERS_MAP,
        VALUE
    }


    public enum XmlValueType {
        STRING,
        CDATA
    }

}
