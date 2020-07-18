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

package io.art.entity.xml;

import io.art.core.checker.*;
import io.art.entity.immutable.Value;
import io.art.entity.immutable.*;
import lombok.*;
import static io.art.entity.immutable.Value.*;
import static io.art.entity.immutable.XmlEntity.*;
import static lombok.AccessLevel.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
public final class XmlEntityFromEntityConverter {
    public static XmlEntity fromEntityAsTags(Entity entity) {
        if (valueIsNull(entity)) {
            return null;
        }
        XmlEntity.XmlEntityBuilder builder = xmlEntityBuilder();
        if (Value.valueIsEmpty(entity)) {
            return EMPTY;
        }
        Set<Primitive> keys = entity.asMap().keySet();
        for (Primitive key : keys) {
            if (valueIsNull(key)) continue;
            Value value = entity.get(key);
            if (valueIsNull(value)) continue;
            addValue(builder, key.getString(), value);
        }
        return builder.create();
    }

    public static XmlEntity fromEntityAsAttributes(String tag, Entity entity) {
        if (EmptinessChecker.isEmpty(tag) || valueIsNull(entity)) {
            return null;
        }
        XmlEntity.XmlEntityBuilder builder = xmlEntityBuilder().tag(tag);
        if (Value.valueIsEmpty(entity)) {
            return builder.create();
        }
        Set<Primitive> keys = entity.asMap().keySet();
        for (Primitive key : keys) {
            String keyAsString = key.getString();
            if (EmptinessChecker.isEmpty(keyAsString)) continue;
            Value value = entity.get(key);
            String valueAsString;
            if (valueIsNull(value) || !isPrimitive(value) || EmptinessChecker.isEmpty(valueAsString = asPrimitive(value).getString())) {
                continue;
            }
            builder.attribute(keyAsString, valueAsString);
        }
        return builder.create();
    }

    private static void addValue(XmlEntity.XmlEntityBuilder builder, String name, Value value) {
        if (EmptinessChecker.isEmpty(name) || valueIsNull(value)) {
            return;
        }
        builder = builder.child().tag(name);
        switch (value.getType()) {
            case ENTITY:
                builder.child(fromEntityAsTags(asEntity(value))).build();
                return;
            case STRING:
            case LONG:
            case DOUBLE:
            case FLOAT:
            case INT:
            case BOOL:
            case BYTE:
                builder.value(value.toString()).build();
                return;
            case ARRAY:
                addCollectionValue(builder, (ArrayValue) value);
                builder.build();
        }
    }

    private static void addCollectionValue(XmlEntity.XmlEntityBuilder builder, ArrayValue value) {
        if (valueIsNull(value)) {
            return;
        }
        Collection<?> elements = value.asList();
        for (Object element : elements) {
            Value elementValue = (Value) element;
            if (valueIsNull(elementValue)) continue;
            switch (elementValue.getType()) {
                case ENTITY:
                    builder.child(fromEntityAsTags((Entity) elementValue));
                    break;
                case STRING:
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BOOL:
                case BYTE:
                    break;
                case ARRAY:
                    builder = builder.child();
                    addCollectionValue(builder, (ArrayValue) elementValue);
                    builder = builder.build();
                    break;
            }
        }
    }
}
