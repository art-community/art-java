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

package io.art.value.constants;

import io.art.value.exception.*;
import io.art.value.immutable.*;
import lombok.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.value.constants.ValueModuleConstants.ExceptionMessages.*;
import static io.art.value.factory.PrimitivesFactory.*;
import static java.text.MessageFormat.*;

public interface ValueModuleConstants {
    interface Keys {
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
        String UNKNOWN_VALUE_TYPE = "Unknown value type: ''{0}''";
        String TUPLE_NOT_SUPPORTED_VALUE_TYPE = "Value type: ''{0}'' not support for tuples";
        String NOT_PRIMITIVE_TYPE = "Not primitive type: ''{0}''";
        String XML_TAG_IS_EMPTY = "Xml tag is empty";
        String INDEX_MAPPING_EXCEPTION = "Exception occurred during mapping array element ''{0}'':";
        String FIELD_MAPPING_EXCEPTION = "Exception occurred during mapping entity field ''{0}'':";
    }


    enum ValueType {
        ENTITY,
        ARRAY,
        XML,
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
            STRING(DEFAULT_STRING_PRIMITIVE),
            LONG(DEFAULT_LONG_PRIMITIVE),
            DOUBLE(DEFAULT_DOUBLE_PRIMITIVE),
            FLOAT(DEFAULT_FLOAT_PRIMITIVE),
            INT(DEFAULT_INT_PRIMITIVE),
            BOOL(DEFAULT_BOOL_PRIMITIVE),
            BYTE(DEFAULT_BYTE_PRIMITIVE);

            private final Primitive defaultValue;

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
        XML("xml"),
        MESSAGE_PACK("messagePack");

        private final String format;

        public static DataFormat dataFormat(String format, DataFormat fallback) {
            if (PROTOBUF.format.equalsIgnoreCase(format)) return PROTOBUF;
            if (JSON.format.equalsIgnoreCase(format)) return JSON;
            if (XML.format.equalsIgnoreCase(format)) return XML;
            if (MESSAGE_PACK.format.equalsIgnoreCase(format)) return MESSAGE_PACK;
            return fallback;
        }
    }
}
