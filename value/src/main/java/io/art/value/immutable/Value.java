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

package io.art.value.immutable;

import io.art.value.constants.ValueModuleConstants.*;
import io.art.value.exception.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.value.constants.ValueModuleConstants.ExceptionMessages.*;
import static io.art.value.constants.ValueModuleConstants.ValueType.*;
import static java.text.MessageFormat.*;
import java.util.*;

public interface Value {
    static Primitive asPrimitive(Value value) {
        return (Primitive) value;
    }

    static Entity asEntity(Value value) {
        return (Entity) value;
    }

    static ArrayValue asArray(Value value) {
        return (ArrayValue) value;
    }

    static BinaryValue asBinary(Value value) {
        return (BinaryValue) value;
    }

    static XmlEntity asXml(Value value) {
        return (XmlEntity) value;
    }

    static boolean isPrimitive(Value value) {
        if (Objects.isNull(value)) return false;
        return Value.isPrimitiveType(value.getType());
    }

    static boolean isEntity(Value value) {
        if (Objects.isNull(value)) return false;
        return Value.isEntityType(value.getType());
    }

    static boolean isArray(Value value) {
        if (Objects.isNull(value)) return false;
        return Value.isArrayType(value.getType());
    }

    static boolean isXml(Value value) {
        if (Objects.isNull(value)) return false;
        return Value.isXmlType(value.getType());
    }

    static boolean isBinary(Value value) {
        if (Objects.isNull(value)) return false;
        return Value.isBinaryType(value.getType());
    }

    static boolean isPrimitiveType(ValueType type) {
        if (Objects.isNull(type)) return false;

        switch (type) {
            case INT:
            case LONG:
            case BYTE:
            case BOOL:
            case STRING:
            case DOUBLE:
            case FLOAT:
                return true;
        }
        return false;
    }

    static boolean isEntityType(ValueType type) {
        if (Objects.isNull(type)) return false;
        return type == ENTITY;
    }

    static boolean isArrayType(ValueType type) {
        if (Objects.isNull(type)) return false;
        return type == ARRAY;
    }

    static boolean isXmlType(ValueType type) {
        if (Objects.isNull(type)) return false;
        return type == XML;
    }

    static boolean isBinaryType(ValueType type) {
        if (Objects.isNull(type)) return false;
        return type == BINARY;
    }

    static boolean valueIsEmpty(Value value) {
        if (Objects.isNull(value)) {
            return true;
        }
        if (Value.isPrimitive(value)) {
            return isEmpty(Value.asPrimitive(value).getString());
        }
        if (Value.isEntity(value)) {
            return Value.asEntity(value).size() == 0;
        }
        if (Value.isArray(value)) {
            return Value.asArray(value).size() == 0;
        }
        if (Value.isBinary(value)) {
            return Value.asBinary(value).getContent() == EMPTY_BYTES;
        }
        if (Value.isXml(value)) {
            return isEmpty(Value.asXml(value).getTag()) && isEmpty(Value.asXml(value).getChildren());
        }
        throw new ValueMappingException(format(value.getType().name(), UNKNOWN_VALUE_TYPE));
    }

    static boolean valueIsNull(Value value) {
        if (Objects.isNull(value)) {
            return true;
        }
        if (Value.isPrimitive(value)) {
            return Objects.isNull(Value.asPrimitive(value).getValue());
        }
        return false;
    }

    ValueType getType();
}
