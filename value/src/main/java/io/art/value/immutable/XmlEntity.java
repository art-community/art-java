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

package io.art.value.immutable;

import io.art.core.checker.*;
import io.art.core.collection.*;
import io.art.core.extensions.*;
import io.art.value.constants.ValueModuleConstants.*;
import io.art.value.exception.*;
import lombok.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.value.constants.ValueModuleConstants.ExceptionMessages.*;
import static io.art.value.constants.ValueModuleConstants.ValueType.*;
import static io.art.value.factory.XmlEntityFactory.*;
import static java.util.Map.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = PRIVATE)
public class XmlEntity implements Value {
    private final ValueType type = XML;
    private String tag;
    private String prefix;
    private XmlValue<?> value;
    private String namespace;
    private ImmutableMap<String, String> attributes;
    private ImmutableMap<String, String> namespaces;
    private ImmutableArray<XmlEntity> children;
    private boolean cData;

    public static XmlEntityBuilder xmlEntityBuilder() {
        return new XmlEntityBuilder();
    }

    public static XmlEntity createMandatoryChild(String prefix, String namespace, String tag, Object value) {
        return createChild(prefix, namespace, tag, emptyIfNull(value));
    }

    public static XmlEntity createChild(String prefix, String namespace, String tag, Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return xmlEntityBuilder()
                .prefix(prefix)
                .namespace(namespace)
                .namespaceField(prefix, namespace)
                .tag(tag)
                .value(value.toString())
                .create();
    }

    public static XmlEntity createEntity(String prefix, String namespace, String tag, XmlEntity value) {
        if (Objects.isNull(value)) {
            return null;
        }
        return xmlEntityBuilder()
                .prefix(prefix)
                .namespace(namespace)
                .namespaceField(prefix, namespace)
                .tag(tag)
                .cData(value)
                .create();
    }

    public String getValue() {
        return (Objects.isNull(value)) ? null : emptyIfNull(value.getValue());
    }

    public XmlValue<?> getXmlValue() {
        return value;
    }

    public XmlEntity find(String tag) {
        if (EmptinessChecker.isEmpty(tag)) {
            return emptyXmlEntity();
        }
        XmlEntity found = findRecursive(tag);
        if (Objects.isNull(found)) {
            return emptyXmlEntity();
        }
        return found;
    }

    private XmlEntity findRecursive(String tag) {
        if (tag.equalsIgnoreCase(this.tag)) {
            return this;
        }

        if (!Value.valueIsEmpty(this) || Objects.isNull(this.children)) {
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

    public ImmutableArray<XmlEntity> getChildren(String tagName) {
        XmlEntity xmlEntity = find(tagName);
        if (!Value.valueIsEmpty(xmlEntity)) {
            return xmlEntity.getChildren();
        }
        return emptyImmutableArray();
    }

    public ImmutableArray<String> getChildTags(String tagName) {
        return getChildren(tagName).stream().map(XmlEntity::getTag).collect(immutableArrayCollector());
    }

    public ImmutableArray<String> getChildValues(String tagName) {
        return getChildren(tagName).stream().map(XmlEntity::getValue).collect(immutableArrayCollector());
    }

    public ImmutableMap<String, String> getAttributes(String tagName) {
        XmlEntity xmlEntity = find(tagName);
        if (!Value.valueIsEmpty(xmlEntity)) {
            return xmlEntity.getAttributes();
        }
        return emptyImmutableMap();
    }

    public ImmutableMap<String, String> getNamespaces(String tagName) {
        XmlEntity xmlEntity = find(tagName);
        if (!Value.valueIsEmpty(xmlEntity)) {
            return xmlEntity.getNamespaces();
        }
        return emptyImmutableMap();
    }

    public String getValueByTag(String tag) {
        XmlEntity xmlEntity = find(tag);
        if (!Value.valueIsEmpty(xmlEntity)) {
            return xmlEntity.getValue();
        }
        return EMPTY_STRING;
    }

    public String getNamespaceByTag(String tag) {
        XmlEntity xmlEntity = find(tag);
        if (!Value.valueIsEmpty(xmlEntity)) {
            return xmlEntity.getNamespace();
        }
        return EMPTY_STRING;
    }

    public String getPrefixByTag(String tag) {
        XmlEntity xmlEntity = find(tag);
        if (!Value.valueIsEmpty(xmlEntity)) {
            return xmlEntity.getPrefix();
        }
        return EMPTY_STRING;
    }

    public boolean exists(String tag) {
        return !Value.valueIsEmpty(find(tag));
    }

    public static final XmlEntity EMPTY = xmlEntityBuilder().create();

    @NoArgsConstructor(access = PRIVATE)
    public static class XmlEntityBuilder {
        private final ImmutableMap.Builder<String, String> attributes = immutableMapBuilder();
        private final ImmutableMap.Builder<String, String> namespaces = immutableMapBuilder();
        private final ImmutableArray.Builder<XmlEntity> children = immutableArrayBuilder();
        private XmlEntityBuilder parent;
        private String tag;
        private XmlValue<?> value;
        private String prefix;
        private String namespace;
        private boolean cData;
        private boolean ignoreEmpty = false;

        XmlEntityBuilder(XmlEntityBuilder parent) {
            this.parent = parent;
        }

        public XmlEntityBuilder ignoreEmpty() {
            this.ignoreEmpty = true;
            return this;
        }

        public XmlEntityBuilder boolAttributeStringFormat(String name, Boolean value) {
            attributes.put(name, emptyIfNull(value));
            return this;
        }

        public XmlEntityBuilder boolAttributeNumericFormat(String name, Boolean value) {
            attributes.put(name, value ? TRUE_NUMERIC : FALSE_NUMERIC);
            return this;
        }

        public XmlEntityBuilder attribute(String name, Object value) {
            attributes.put(name, emptyIfNull(value));
            return this;
        }

        public XmlEntityBuilder stringAttributes(ImmutableMap<String, String> attributes) {
            this.attributes.putAll(attributes);
            return this;
        }

        public XmlEntityBuilder attributes(Map<String, ?> attributes) {
            this.attributes.putAll(attributes
                    .entrySet()
                    .stream()
                    .collect(mapCollector(Entry::getKey, StringExtensions::emptyIfNull)));
            return this;
        }

        public XmlEntityBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public XmlEntityBuilder value(Object value) {
            return value(emptyIfNull(value));
        }

        public XmlEntityBuilder value(String value) {
            this.value = new StringXmlValue(value);
            return this;
        }


        public XmlEntityBuilder boolValueStringFormat(Boolean value) {
            return value(emptyIfNull(value));
        }

        public XmlEntityBuilder boolValueNumericFormat(Boolean value) {
            return value(value ? TRUE_NUMERIC : FALSE_NUMERIC);
        }

        public XmlEntityBuilder cData(XmlEntity value) {
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
            child.ignoreEmpty = ignoreEmpty;
            return child;
        }

        public XmlEntityBuilder namespaceField(String prefix, String url) {
            namespaces.put(prefix, url);
            return this;
        }

        public XmlEntityBuilder namespaceFields(ImmutableMap<String, String> namespaces) {
            this.namespaces.putAll(namespaces);
            return this;
        }

        public XmlEntityBuilder namespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public XmlEntity create() {
            if (EmptinessChecker.isEmpty(tag) && EmptinessChecker.isEmpty(children)) {
                throw new XmlEntityCreationException(XML_TAG_IS_EMPTY);
            }
            XmlEntity entity = new XmlEntity();
            entity.namespace = namespace;
            entity.namespaces = namespaces.build();
            entity.children = children.build();
            entity.tag = tag;
            entity.prefix = prefix;
            entity.value = value;
            entity.attributes = attributes.build();
            entity.cData = cData;
            return entity;
        }

        public XmlEntityBuilder attach() {
            if (Objects.isNull(tag)) {
                throw new XmlEntityCreationException(XML_TAG_IS_EMPTY);
            }
            if (ignoreEmpty && EmptinessChecker.isEmpty(value.getValue())) {
                return parent;
            }
            XmlEntity entity = new XmlEntity();
            entity.namespace = namespace;
            entity.namespaces = namespaces.build();
            entity.children = children.build();
            entity.tag = tag;
            entity.prefix = prefix;
            entity.value = value;
            entity.attributes = attributes.build();
            return parent.child(entity);
        }
    }
}
