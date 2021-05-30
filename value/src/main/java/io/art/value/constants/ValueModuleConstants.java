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

package io.art.value.constants;

import io.art.core.collection.*;
import io.art.value.exception.*;
import io.art.value.factory.*;
import io.art.value.immutable.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import static io.art.value.constants.ValueModuleConstants.ExceptionMessages.*;
import static io.art.value.factory.PrimitivesFactory.*;
import static java.text.MessageFormat.*;
import java.lang.reflect.*;

public interface ValueModuleConstants {
    interface Fields {
        String EXCEPTION_KEY = "exception";
        String SERVICE_ID_KEY = "serviceId";
        String METHOD_ID_KEY = "methodId";
        String SERVICE_METHOD_IDENTIFIERS_KEY = "serviceMethodIdentifiers";
        String CLASS_KEY = "class";
        String MESSAGE_KEY = "message";
        String CAUSE_KEY = "cause";
        String STACK_TRACE_KEY = "stackTrace";
        String DECLARING_CLASS_KEY = "declaringClass";
        String METHOD_NAME_KEY = "methodName";
        String FILE_NAME_KEY = "fileName";
        String LINE_NUMBER_KEY = "lineNumber";
    }

    interface ExceptionMessages {
        String SERVICE_ID_NOT_PRESENTED = "Key 'serviceId' was not presented in: {0}";
        String METHOD_ID_NOT_PRESENTED = "Key 'methodId' was not presented in: {0}";
        String UNKNOWN_VALUE_TYPE = "Unknown emit type: ''{0}''";
        String TUPLE_NOT_SUPPORTED_VALUE_TYPE = "Value type: ''{0}'' not support for tuples";
        String NOT_PRIMITIVE_TYPE = "Not primitive type: ''{0}''";
        String INDEX_MAPPING_EXCEPTION = "Exception occurred during mapping array element ''{0}'':";
        String FIELD_MAPPING_EXCEPTION = "Exception occurred during mapping entity field ''{0}'':";
        String EMPTY_VALIDATION_EXPRESSIONS = "Please provide any validation expression";
    }


    enum ValueType {
        ENTITY,
        ARRAY,
        STRING,
        LONG,
        DOUBLE,
        FLOAT,
        INT,
        BOOL,
        BYTE,
        BINARY;

        @Getter
        @AllArgsConstructor
        public enum PrimitiveType {
            STRING,
            LONG,
            DOUBLE,
            FLOAT,
            INT,
            BOOL,
            BYTE;

            public Primitive defaultValue() {
                switch (this) {
                    case STRING:
                        return DEFAULT_STRING_PRIMITIVE;
                    case LONG:
                        return DEFAULT_LONG_PRIMITIVE;
                    case DOUBLE:
                        return DEFAULT_DOUBLE_PRIMITIVE;
                    case INT:
                        return DEFAULT_INT_PRIMITIVE;
                    case BOOL:
                        return DEFAULT_BOOL_PRIMITIVE;
                    case BYTE:
                        return DEFAULT_BYTE_PRIMITIVE;
                    case FLOAT:
                        return DEFAULT_FLOAT_PRIMITIVE;
                    default:
                        throw new ValueMappingException(format(NOT_PRIMITIVE_TYPE, this));
                }
            }
            public static ValueType asValueType(PrimitiveType primitiveType) {
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

            public static Primitive defaultValue(PrimitiveType primitiveType) {
                switch (primitiveType) {
                    case STRING:
                        return DEFAULT_STRING_PRIMITIVE;
                    case LONG:
                        return DEFAULT_INT_PRIMITIVE;
                    case DOUBLE:
                        return DEFAULT_LONG_PRIMITIVE;
                    case INT:
                        return DEFAULT_DOUBLE_PRIMITIVE;
                    case BOOL:
                        return DEFAULT_BOOL_PRIMITIVE;
                    case BYTE:
                        return DEFAULT_BYTE_PRIMITIVE;
                    case FLOAT:
                        return DEFAULT_FLOAT_PRIMITIVE;
                    default:
                        throw new ValueMappingException(format(NOT_PRIMITIVE_TYPE, primitiveType));
                }
            }
        }

        public enum XmlValueType {
            STRING,
            CDATA
        }
    }

    @Getter
    @AllArgsConstructor
    enum DataFormat {
        PROTOBUF("protobuf"),
        JSON("json"),
        YAML("yaml"),
        MESSAGE_PACK("messagePack");

        private final String format;

        public static DataFormat dataFormat(String format, DataFormat fallback) {
            if (PROTOBUF.format.equalsIgnoreCase(format)) return PROTOBUF;
            if (JSON.format.equalsIgnoreCase(format)) return JSON;
            if (YAML.format.equalsIgnoreCase(format)) return YAML;
            if (MESSAGE_PACK.format.equalsIgnoreCase(format)) return MESSAGE_PACK;
            return fallback;
        }
    }
}
