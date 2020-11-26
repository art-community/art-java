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
import lombok.*;
import static io.art.value.constants.ValueConstants.ExceptionMessages.*;
import static java.text.MessageFormat.format;

public interface ValueConstants {
    interface ExceptionMessages {
        String UNKNOWN_VALUE_TYPE = "Unknown value type: ''{0}''";
        String TUPLE_NOT_SUPPORTED_VALUE_TYPE = "Value type: ''{0}'' not support for tuples";
        String NOT_PRIMITIVE_TYPE = "Not primitive type: ''{0}''";
        String XML_TAG_IS_EMPTY = "Xml tag is empty";
        String UNSUPPORTED_DATA_FORMAT = "Data format not supported: ''{0}''";
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

        public enum PrimitiveType {
            STRING,
            LONG,
            DOUBLE,
            FLOAT,
            INT,
            BOOL,
            BYTE;

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
