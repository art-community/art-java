/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.entity.xml;

import lombok.*;
import ru.art.core.checker.*;
import ru.art.entity.Value;
import ru.art.entity.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.entity.Value.*;
import static ru.art.entity.XmlEntity.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
public final class XmlEntityFromEntityConverter {
    public static XmlEntity fromEntityAsTags(Entity entity) {
        XmlEntity.XmlEntityBuilder xmlEntityBuilder = xmlEntityBuilder();
        if (Value.isEmpty(entity)) {
            return xmlEntityBuilder.create();
        }
        entity.getFields().forEach((key, value) -> addValue(xmlEntityBuilder, key, value));
        return xmlEntityBuilder.create();
    }

    public static XmlEntity fromEntityAsAttributes(String tag, Entity entity) {
        XmlEntity.XmlEntityBuilder builder = xmlEntityBuilder().tag(tag);
        if (Value.isEmpty(entity)) {
            return builder.create();
        }
        entity.getFields().entrySet()
                .stream()
                .filter(entry -> isPrimitive(entry.getValue()))
                .forEach(entry -> builder.stringAttributeField(entry.getKey(), asPrimitive(entry.getValue()).toString()));
        return builder.create();
    }

    private static void addValue(XmlEntity.XmlEntityBuilder builder, String name, Value value) {
        if (CheckerForEmptiness.isEmpty(name) || isNull(value)) {
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
            case COLLECTION:
                addCollectionValue(builder, (CollectionValue) value);
                builder.build();
                return;
            case STRING_PARAMETERS_MAP:
                builder.children(((StringParametersMap) value)
                        .getParameters()
                        .entrySet()
                        .stream()
                        .map(entry -> xmlEntityBuilder().tag(entry.getKey()).value(entry.getValue()).create())
                        .collect(toList()));
                builder.build();
                return;
            case MAP:
                addMapValue(builder, (MapValue) value);
                builder.build();
        }
    }

    private static void addCollectionValue(XmlEntity.XmlEntityBuilder builder, CollectionValue value) {
        if (isNull(value)) {
            return;
        }
        Collection<?> elements = cast(value.getElements());
        int index = 0;
        for (Object element : elements) {
            switch (value.getElementsType()) {
                case STRING:
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BOOL:
                case BYTE:
                    builder.child().tag(element.toString()).build();
                    continue;
                case ENTITY:
                case COLLECTION:
                case MAP:
                case STRING_PARAMETERS_MAP:
                case VALUE:
                    break;
            }
            Value elementValue = (Value) element;
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
                case COLLECTION:
                    builder = builder.child();
                    addCollectionValue(builder, (CollectionValue) elementValue);
                    builder = builder.build();
                    break;
                case STRING_PARAMETERS_MAP:
                    builder.children(((StringParametersMap) elementValue)
                            .getParameters()
                            .entrySet()
                            .stream()
                            .map(entry -> xmlEntityBuilder().tag(entry.getKey()).value(entry.getValue()).create())
                            .collect(toList()));
                    break;
                case MAP:
                    builder = builder.child();
                    addMapValue(builder, (MapValue) elementValue);
                    builder = builder.build();
                    break;
            }
        }
    }

    private static void addMapValue(XmlEntity.XmlEntityBuilder builder, MapValue value) {
        if (isNull(value)) {
            return;
        }
        Map<Value, Value> elements = cast(value.getElements());
        for (Map.Entry<Value, Value> element : elements.entrySet()) {
            Value elementKey = element.getKey();
            Value elementValue = element.getValue();
            if (!isPrimitive(elementKey)) {
                continue;
            }
            builder = builder.child().tag(asPrimitive(elementKey).toString());
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
                    builder.value(elementValue.toString());
                    break;
                case COLLECTION:
                    addCollectionValue(builder, (CollectionValue) elementValue);
                    break;
                case STRING_PARAMETERS_MAP:
                    builder.children(((StringParametersMap) elementValue)
                            .getParameters()
                            .entrySet()
                            .stream()
                            .map(entry -> xmlEntityBuilder().tag(entry.getKey()).value(entry.getValue()).create())
                            .collect(toList()));
                    break;
                case MAP:
                    addMapValue(builder, (MapValue) elementValue);
                    break;
            }
            builder.build();
        }
    }
}
