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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.art.core.checker.CheckerForEmptiness;
import ru.art.core.extension.StringExtensions;
import ru.art.entity.constants.ValueType;
import ru.art.entity.exception.ValueMappingException;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.Value.*;
import static ru.art.entity.constants.ValueMappingExceptionMessages.XML_TAG_IS_UNFILLED;
import static ru.art.entity.constants.ValueType.XML_ENTITY;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = PRIVATE)
public class XmlEntity implements Value {
    private final ValueType type = XML_ENTITY;
    private String tag;
    private String prefix;
    private XmlValue<?> value;
    private String namespace;
    private Map<String, String> attributes;
    private Map<String, String> namespaces;
    private List<XmlEntity> children;
    private boolean cData;

    public static XmlEntityBuilder xmlEntityBuilder() {
        return new XmlEntityBuilder();
    }

    public static XmlEntity createMandatoryChild(String prefix, String namespace, String tag, Object value) {
        return createChild(prefix, namespace, tag, emptyIfNull(value));
    }

    public static XmlEntity createChild(String prefix, String namespace, String tag, Object value) {
        return value == null ? null : xmlEntityBuilder()
                .prefix(prefix)
                .namespace(namespace)
                .namespaceField(prefix, namespace)
                .tag(tag)
                .value(value.toString())
                .create();
    }

    public static XmlEntity createEntity(String prefix, String namespace, String tag, XmlEntity value) {
        return value == null ? null : xmlEntityBuilder()
                .prefix(prefix)
                .namespace(namespace)
                .namespaceField(prefix, namespace)
                .tag(tag)
                .cDataValue(value)
                .create();
    }

    public String getValue() {
        return (isNull(value)) ? null : emptyIfNull(value.getValue());
    }

    public XmlValue<?> getXmlValue() {
        return value;
    }

    public XmlEntity find(String tag) {
        if (CheckerForEmptiness.isEmpty(tag)) {
            return xmlEntityBuilder().emptyXmlEntity();
        }
        XmlEntity found = findRecursive(tag);
        if (isNull(found)) {
            return xmlEntityBuilder().emptyXmlEntity();
        }
        return found;
    }

    private XmlEntity findRecursive(String tag) {
        if (tag.equalsIgnoreCase(this.tag)) {
            return this;
        }

        if (this.isEmpty() || isNull(this.children)) {
            return null;
        }

        for (XmlEntity child : children) {
            XmlEntity xmlEntity = child.findRecursive(tag);
            if (nonNull(xmlEntity)) {
                return xmlEntity;
            }
        }
        return null;
    }

    public List<XmlEntity> getChildren(String tagName) {
        XmlEntity xmlEntity = find(tagName);
        if (!xmlEntity.isEmpty()) {
            return xmlEntity.getChildren();
        }
        return emptyList();
    }

    public List<String> getChildTags(String tagName) {
        return getChildren(tagName).stream().map(XmlEntity::getTag).collect(toList());
    }

    public List<String> getChildValues(String tagName) {
        return getChildren(tagName).stream().map(XmlEntity::getValue).collect(toList());
    }

    public Map<String, String> getAttributes(String tagName) {
        XmlEntity xmlEntity = find(tagName);
        if (!xmlEntity.isEmpty()) {
            return xmlEntity.getAttributes();
        }
        return emptyMap();
    }

    public Map<String, String> getNamespaces(String tagName) {
        XmlEntity xmlEntity = find(tagName);
        if (!xmlEntity.isEmpty()) {
            return xmlEntity.getNamespaces();
        }
        return emptyMap();
    }

    public String getValueByTag(String tag) {
        XmlEntity xmlEntity = find(tag);
        if (!xmlEntity.isEmpty()) {
            return xmlEntity.getValue();
        }
        return EMPTY_STRING;
    }

    public String getNamespaceByTag(String tag) {
        XmlEntity xmlEntity = find(tag);
        if (!xmlEntity.isEmpty()) {
            return xmlEntity.getNamespace();
        }
        return EMPTY_STRING;
    }

    public String getPrefixByTag(String tag) {
        XmlEntity xmlEntity = find(tag);
        if (!xmlEntity.isEmpty()) {
            return xmlEntity.getPrefix();
        }
        return EMPTY_STRING;
    }

    public boolean existTag(String tag) {
        return !find(tag).isEmpty();
    }

    @Override
    public boolean isEmpty() {
        return CheckerForEmptiness.isEmpty(tag);
    }

    public Entity toEntityFromTags() {
        Entity.EntityBuilder entityBuilder = entityBuilder();
        String value = getValue();
        if (isNotEmpty(tag) && isNotEmpty(value)) {
            entityBuilder.stringField(tag, value);
        }
        if (isNotEmpty(children)) {
            children.forEach(child -> entityBuilder.entityField(child.tag, child.toEntityFromTags()));
        }
        return entityBuilder.build();
    }

    public Entity toEntityFromAttributes() {
        Entity.EntityBuilder entityBuilder = entityBuilder();
        if (CheckerForEmptiness.isEmpty(attributes)) {
            return entityBuilder.build();
        }
        attributes.forEach(entityBuilder::stringField);
        return entityBuilder.build();
    }

    public static XmlEntity fromValueAsTags(Entity entity) {
        XmlEntityBuilder xmlEntityBuilder = xmlEntityBuilder();
        if (Value.isEmpty(entity)) {
            return xmlEntityBuilder.create();
        }
        entity.getFields().forEach((key, value1) -> addValue(xmlEntityBuilder, key, value1));
        return xmlEntityBuilder.create();
    }

    private static void addValue(XmlEntityBuilder builder, String name, Value value) {
        if (CheckerForEmptiness.isEmpty(name) || Value.isEmpty(value)) {
            return;
        }
        switch (value.getType()) {
            case ENTITY:
                builder.child(fromValueAsTags(asEntity(value)));
                return;
            case STRING:
            case LONG:
            case DOUBLE:
            case FLOAT:
            case INT:
            case BOOL:
            case BYTE:
                builder.tag(name).value(value.toString());
                return;
            case COLLECTION:
                addCollectionValue(builder, (CollectionValue) value);
        }
    }

    private static void addCollectionValue(XmlEntityBuilder builder, CollectionValue value) {
        if (Value.isEmpty(value)) {
            return;
        }
        Collection<?> elements = value.getElements();
        int index = 0;
        for (Object element : elements) {
            builder.tag(EMPTY_STRING + index++);
            switch (value.getElementsType()) {
                case ENTITY:
                    builder.child(fromValueAsTags((Entity) element));
                case STRING:
                case LONG:
                case DOUBLE:
                case FLOAT:
                case INT:
                case BOOL:
                case BYTE:
                    builder.value(element.toString());
                    return;
                case COLLECTION:
                    addCollectionValue(builder, (CollectionValue) element);
            }
        }
    }

    public static XmlEntity fromEntityAsAttributes(String tag, Entity entity) {
        XmlEntityBuilder builder = xmlEntityBuilder().tag(tag);
        if (Value.isEmpty(entity)) {
            return builder.create();
        }
        entity.getFields().entrySet()
                .stream()
                .filter(entry -> isPrimitive(entry.getValue()))
                .forEach(entry -> builder.stringAttributeField(entry.getKey(), asPrimitive(entry.getValue()).toString()));
        return builder.create();
    }

    @NoArgsConstructor(access = PRIVATE)
    public static class XmlEntityBuilder {
        private final Map<String, String> attributes = mapOf();
        private final Map<String, String> namespaces = mapOf();
        private final List<XmlEntity> children = dynamicArrayOf();
        private XmlEntityBuilder parent;
        private String tag;
        private XmlValue<?> value;
        private String prefix;
        private String namespace;
        private boolean cData;
        private boolean deleteEmptyValueAttribute = false;

        XmlEntityBuilder(XmlEntityBuilder parent) {
            this.parent = parent;
        }

        public XmlEntityBuilder deleteEmptyValueAttribute() {
            this.deleteEmptyValueAttribute = true;
            return this;
        }

        public XmlEntityBuilder stringAttributeField(String name, String value) {
            attributes.put(name, value);
            return this;
        }

        public XmlEntityBuilder intAttributeField(String name, int value) {
            attributes.put(name, emptyIfNull(value));
            return this;
        }

        public XmlEntityBuilder longAttributeField(String name, long value) {
            attributes.put(name, emptyIfNull(value));
            return this;
        }

        public XmlEntityBuilder doubleAttributeField(String name, double value) {
            attributes.put(name, emptyIfNull(value));
            return this;
        }

        public XmlEntityBuilder booleanAttributeFieldStringFormat(String name, boolean value) {
            attributes.put(name, emptyIfNull(value));
            return this;
        }

        public XmlEntityBuilder booleanAttributeFieldNumericFormat(String name, boolean value) {
            attributes.put(name, value ? TRUE_NUMERIC : FALSE_NUMERIC);
            return this;
        }

        public <T> XmlEntityBuilder attributeField(String name, T value) {
            attributes.put(name, emptyIfNull(value));
            return this;
        }


        public XmlEntityBuilder stringAttributeFields(Map<String, String> attributes) {
            this.attributes.putAll(attributes);
            return this;
        }

        public <T> XmlEntityBuilder attributeFields(Map<String, T> attributes) {
            this.attributes
                    .putAll(attributes.entrySet().stream().collect(toMap(Map.Entry::getKey, StringExtensions::emptyIfNull)));
            return this;
        }

        public XmlEntityBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public XmlEntityBuilder stringValue(String value) {
            return value(emptyIfNull(value));
        }

        public XmlEntityBuilder intValue(int value) {
            return value(emptyIfNull(value));
        }

        public XmlEntityBuilder doubleValue(double value) {
            return value(emptyIfNull(value));
        }

        public XmlEntityBuilder longValue(long value) {
            return value(emptyIfNull(value));
        }

        public XmlEntityBuilder booleanValueStringFormat(boolean value) {
            return value(emptyIfNull(value));
        }

        public XmlEntityBuilder booleanValueNumericFormat(boolean value) {
            return value(value ? TRUE_NUMERIC : FALSE_NUMERIC);
        }

        public XmlEntityBuilder value(String value) {
            this.value = new StringXmlValue(value);
            return this;
        }

        public XmlEntityBuilder cDataValue(XmlEntity value) {
            this.cData = true;
            this.value = new CdataXmlValue(value);
            return this;
        }

        public XmlEntityBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public XmlEntityBuilder child(XmlEntity child) {
            children.add(child);
            return this;
        }

        public XmlEntityBuilder children(List<XmlEntity> childList) {
            children.addAll(childList);
            return this;
        }

        public XmlEntityBuilder child() {
            XmlEntityBuilder child = new XmlEntityBuilder(this);
            child.deleteEmptyValueAttribute = deleteEmptyValueAttribute;
            return child;
        }

        public XmlEntityBuilder namespaceField(String prefix, String url) {
            namespaces.put(prefix, url);
            return this;
        }

        public XmlEntityBuilder namespaceFields(Map<String, String> namespaces) {
            this.namespaces.putAll(namespaces);
            return this;
        }

        public XmlEntityBuilder namespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        @SuppressWarnings("Duplicates")
        public XmlEntity create() {
            XmlEntity entity = new XmlEntity();
            entity.namespace = namespace;
            entity.namespaces = namespaces;
            entity.children = children;
            entity.tag = tag;
            entity.prefix = prefix;
            entity.value = value;
            entity.attributes = attributes;
            entity.cData = cData;
            return entity;
        }

        public XmlEntity emptyXmlEntity() {
            return new XmlEntity();
        }

        @SuppressWarnings("Duplicates")
        public XmlEntityBuilder build() {
            if (isNull(tag)) {
                throw new ValueMappingException(XML_TAG_IS_UNFILLED);
            }
            if (deleteEmptyValueAttribute && CheckerForEmptiness.isEmpty(value.getValue())) {
                return parent;
            }
            XmlEntity entity = new XmlEntity();
            entity.namespace = namespace;
            entity.namespaces = namespaces;
            entity.children = children;
            entity.tag = tag;
            entity.prefix = prefix;
            entity.value = value;
            entity.attributes = attributes;
            return parent.child(entity);
        }
    }
}
