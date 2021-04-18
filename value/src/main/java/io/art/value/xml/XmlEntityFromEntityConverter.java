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

package io.art.value.xml;

import io.art.core.checker.*;
import io.art.value.immutable.*;
import io.art.value.immutable.Value;
import lombok.*;
import static io.art.value.factory.XmlEntityFactory.*;
import static io.art.value.immutable.Value.*;
import static io.art.value.immutable.XmlEntity.*;
import static lombok.AccessLevel.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
public final class XmlEntityFromEntityConverter {
    public static XmlEntity fromEntityAsTags(Entity entity) {
        if (valueIsNull(entity)) return null;
        XmlEntityBuilder builder = xmlEntityBuilder();
        if (valueIsEmpty(entity)) return emptyXmlEntity();
        Set<Primitive> keys = entity.asMap().keySet();
        for (Primitive key : keys) {
            if (valueIsNull(key)) continue;
            Value value = entity.get(key);
            if (valueIsNull(value)) continue;
            addFieldValue(builder, key.getString(), value);
        }
        return builder.create();
    }

    public static XmlEntity fromEntityAsAttributes(String tag, Entity entity) {
        if (EmptinessChecker.isEmpty(tag) || valueIsNull(entity)) {
            return null;
        }
        XmlEntityBuilder builder = xmlEntityBuilder().tag(tag);
        if (valueIsEmpty(entity)) {
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

    private static void addFieldValue(XmlEntityBuilder builder, String name, Value value) {
        if (EmptinessChecker.isEmpty(name) || valueIsNull(value)) {
            return;
        }
        builder = builder.child().tag(name);
        switch (value.getType()) {
            case ENTITY:
                builder.child(fromEntityAsTags(asEntity(value))).attach();
                return;
            case STRING:
            case LONG:
            case DOUBLE:
            case FLOAT:
            case INT:
            case BOOL:
            case BYTE:
                builder.value(value.toString()).attach();
                return;
            case ARRAY:
                addArrayValue(builder, (ArrayValue) value);
                builder.attach();
                break;
        }
    }

    private static void addArrayValue(XmlEntityBuilder builder, ArrayValue value) {
        if (valueIsNull(value)) return;
        Collection<Value> elements = value.asList();
        for (Value element : elements) {
            if (valueIsNull(element)) continue;
            switch (element.getType()) {
                case ENTITY:
                    builder.child(fromEntityAsTags(asEntity(element)));
                    break;
                case STRING:
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BOOL:
                case BYTE:
                    builder.child().tag(asPrimitive(element).getString()).attach();
                    break;
                case ARRAY:
                    addArrayValue(builder, (ArrayValue) element);
                    break;
            }
        }
    }
}
