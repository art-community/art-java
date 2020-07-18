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
import io.art.entity.mapper.ValueFromModelMapper.*;
import io.art.entity.mapper.ValueToModelMapper.*;
import static io.art.entity.constants.ValueType.*;
import static io.art.entity.immutable.Entity.*;
import static io.art.entity.immutable.Value.Model.*;
import static io.art.entity.mapping.ArrayMapping.*;
import static io.art.entity.mapping.PrimitiveMapping.toString;
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

    class Model {
        String value;

        public static EntityFromModelMapper<Model> fromModel = model -> isNull(model)
                ? null
                : entityBuilder().put("value", model.value, fromString).build();

        public static EntityToModelMapper<Model> toModel = entity -> {
            if (isNull(entity)) return null;
            Model model = new Model();
            model.value = Value.asPrimitive(entity.get("value")).getString();
            return model;
        };
    }

    class Request {
        List<String> list;
        Set<String> set;
        Map<String, String> stringMap;
        Map<String, Model> map;
        Model model;

        public static EntityFromModelMapper<Request> fromRequest = request -> isNull(request)
                ? null
                : entityBuilder()
                .put("list", request.list, fromList(fromString))
                .put("set", request.set, fromSet(fromString))
                .put("stringMap", entityBuilder().putAllStrings(request.stringMap, fromString).build())
                .put("map", entityBuilder().putAllStrings(request.map, fromModel).build())
                .build();

        public static EntityToModelMapper<Request> toRequest = entity -> {
            if (isNull(entity)) return null;
            Request request = new Request();
            request.list = toList(toString).map(Value.asArray(entity.get("list")));
            request.set = toSet(toString).map(Value.asArray(entity.get("list")));
            request.stringMap = Value.asEntity(entity.get("stringMap")).asMap(toString, fromString, toString);
            request.map = Value.asEntity(entity.get("map")).asMap(toString, fromString, toModel);
            request.model = entity.map("model", toModel);
            return request;
        };
    }
}
