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

import lombok.NoArgsConstructor;
import ru.art.entity.Entity;
import ru.art.entity.StringParametersMap;
import ru.art.entity.Value;
import ru.art.entity.XmlEntity;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.extension.CollectionExtensions.areAllUnique;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.entity.CollectionValuesFactory.valueCollection;
import static ru.art.entity.Entity.EntityBuilder;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.PrimitivesFactory.stringPrimitive;
import static ru.art.entity.Value.isEmpty;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = PRIVATE)
public final class XmlEntityToEntityConverter {
    public static Entity toEntityFromTags(XmlEntity xmlEntity) {
        if (isEmpty(xmlEntity)) {
            return entityBuilder().build();
        }
        EntityBuilder entityBuilder = entityBuilder();
        List<Value> values = dynamicArrayOf();
        String value = xmlEntity.getValue();
        if (isNotEmpty(value)) {
            entityBuilder.stringField(xmlEntity.getTag(), value);
        }
        if (isEmpty(xmlEntity.getChildren())) {
            return entityBuilder.build();
        }
        if (areAllUnique(xmlEntity.getChildren().stream().map(XmlEntity::getTag).collect(toList()))) {
            EntityBuilder innerEntityBuilder = entityBuilder();
            for (XmlEntity child : xmlEntity.getChildren()) {
                if (isEmpty(child.getChildren())) {
                    innerEntityBuilder.stringField(child.getTag(), child.getValue());
                    continue;
                }
                innerEntityBuilder.valueField(child.getTag(), toEntityFromTags(child).getValue(child.getTag()));
            }
            return entityBuilder.entityField(xmlEntity.getTag(), innerEntityBuilder.build()).build();
        }
        List<XmlEntity> childrenCollection = xmlEntity.getChildren()
                .stream()
                .collect(groupingBy(XmlEntity::getTag))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
                .values()
                .stream()
                .findFirst()
                .orElse(emptyList());
        List<Value> collection = dynamicArrayOf();
        for (XmlEntity child : childrenCollection) {
            if (isEmpty(child.getChildren()) && isEmpty(child.getValue())) {
                collection.add(stringPrimitive(child.getTag()));
                continue;
            }
            if (isEmpty(child.getChildren())) {
                collection.add(cast(entityBuilder().stringField(child.getTag(), child.getValue()).build()));
                continue;
            }
            collection.add(cast(entityBuilder().valueField(child.getTag(), toEntityFromTags(child).getValue(child.getTag())).build()));
        }
        return entityBuilder.valueField(xmlEntity.getTag(), valueCollection(cast(collection))).build();
    }

    public static Entity toEntityFromAttributes(XmlEntity xmlEntity) {
        if (isEmpty(xmlEntity)) {
            return entityBuilder().build();
        }
        return entityBuilder()
                .stringParametersField(xmlEntity.getTag(), StringParametersMap.builder().parameters(xmlEntity.getAttributes()).build())
                .build();
    }
}
