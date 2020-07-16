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
import io.art.entity.mapper.*;
import lombok.*;
import static io.art.entity.constants.ValueType.*;
import static io.art.entity.immutable.Entity.*;
import static io.art.entity.mapping.PrimitiveMapping.*;
import static java.util.Objects.*;
import java.util.*;

public interface Value {
    static Primitive asPrimitive(Value value) {
        return (Primitive) value;
    }

    static Entity asEntity(Value value) {
        return (Entity) value;
    }

    static ArrayValue asCollection(Value value) {
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
        return isPrimitiveType(value.getType());
    }

    static boolean isEntity(Value value) {
        if (isNull(value)) return false;
        return isEntityType(value.getType());
    }

    static boolean isArray(Value value) {
        if (isNull(value)) return false;
        return isArrayType(value.getType());
    }

    static boolean isXml(Value value) {
        if (isNull(value)) return false;
        return isXmlType(value.getType());
    }

    static boolean isBinary(Value value) {
        if (isNull(value)) return false;
        return isBinaryType(value.getType());
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

    @Builder
    @lombok.Value
    class Request {
        String strValue;
        Map<String, String> inner;
        Map<Integer, String> inner2;


        public ValueToModelMapper<Request, Entity> toRequest = entity -> Request.builder()
                .strValue(asPrimitive(entity.get("strValue")).getString())
                .inner(asEntity(entity.get("strValue")).asStringMap(value -> asPrimitive(value).getString()))
                .inner2(asEntity(entity.get("strValue")).asIntMap(value -> asPrimitive(value).getString()))
                .build();

        public ValueFromModelMapper<Request, Entity> fromRequest = request -> entityBuilder()
                .put("strValue", request.strValue, fromString)
                .put("inner", entityBuilder().putAllStrings(request.inner, fromString).build())
                .put("inner2", entityBuilder().putAllInts(request.inner2, fromString).build())
                .build();
    }
}
