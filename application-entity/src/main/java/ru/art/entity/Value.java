/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.entity;

import ru.art.entity.constants.ValueType;
import static java.util.Objects.isNull;
import static ru.art.entity.constants.ValueType.COLLECTION;
import static ru.art.entity.constants.ValueType.ENTITY;

public interface Value {
    static Primitive asPrimitive(Value value) {
        return (Primitive) value;
    }

    static Entity asEntity(Value value) {
        return (Entity) value;
    }

    @SuppressWarnings("unchecked")
    static <C> CollectionValue<C> asCollection(Value value) {
        return (CollectionValue<C>) value;
    }

    static MapValue asMap(Value value) {
        return (MapValue) value;
    }

    static StringParametersMap asStringParametersMap(Value value) {
        return (StringParametersMap) value;
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

    static boolean isCollection(Value value) {
        if (isNull(value)) return false;
        return isCollectionType(value.getType());
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

    static boolean isCollectionType(ValueType type) {
        if (isNull(type)) return false;
        return type == COLLECTION;
    }


    boolean isEmpty();

    ValueType getType();
}
