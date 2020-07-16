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

package io.art.entity.immutable;

import io.art.entity.constants.*;
import static io.art.entity.constants.ValueType.*;
import static java.util.Objects.*;

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

    static boolean isEmpty(Value value) {
        return isNull(value) || value.isEmpty();
    }

    static XmlEntity asXmlEntity(Value value) {
        return (XmlEntity) value;
    }

    static boolean isPrimitive(Value value) {
        if (isNull(value)) return false;
        return Value.isPrimitiveType(value.getType());
    }

    static boolean isEntity(Value value) {
        if (isNull(value)) return false;
        return Value.isEntityType(value.getType());
    }

    static boolean isArray(Value value) {
        if (isNull(value)) return false;
        return Value.isArrayType(value.getType());
    }

    static boolean isXml(Value value) {
        if (isNull(value)) return false;
        return Value.isXmlType(value.getType());
    }

    static boolean isBinary(Value value) {
        if (isNull(value)) return false;
        return Value.isBinaryType(value.getType());
    }

    static boolean isPrimitiveType(ValueType type) {
        if (isNull(type)) return false;

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
        if (isNull(type)) return false;
        return type == ENTITY;
    }

    static boolean isArrayType(ValueType type) {
        if (isNull(type)) return false;
        return type == ARRAY;
    }

    static boolean isXmlType(ValueType type) {
        if (isNull(type)) return false;
        return type == ENTITY;
    }

    static boolean isBinaryType(ValueType type) {
        if (isNull(type)) return false;
        return type == BINARY;
    }

    boolean isEmpty();

    ValueType getType();
}
