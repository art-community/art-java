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

package io.art.xml.rules.processor;

import io.art.entity.*;
import io.art.entity.XmlEntity.*;
import io.art.xml.constants.*;
import io.art.xml.exception.*;
import io.art.xml.rules.builder.*;
import static io.art.core.checker.CheckerForEmptiness.isEmpty;
import static io.art.core.extension.NullCheckingExtensions.*;
import static io.art.entity.XmlEntity.*;
import static io.art.xml.constants.XmlMappingExceptionMessages.*;
import java.util.*;
import java.util.Map.*;

public class XmlEntityRulesProcessor {
    public static XmlEntityMappingBuilder xmlMappingBuilder() {
        return new XmlEntityMappingBuilderImplementation();
    }

    public static XmlEntity map(XmlEntityMapping mappingRules, XmlEntity entity) {
        if (isEmpty(mappingRules)) throw new XmlMappingException(XML_MAPPING_RULES_ARE_EMPTY);
        if (isEmpty(entity)) throw new XmlMappingException(XML_ENTITY_IS_NULL);
        return map(entity, xmlEntityBuilder(), mappingRules.getMapping()).create();
    }

    private static void mapAttributes(XmlEntity entity, XmlEntityBuilder builder, Set<XmlEntityRule> rules) {
        Set<Entry<String, String>> attributes = entity.getAttributes().entrySet();
        for (Entry<String, String> attribute : attributes) {
            String mappedAttribute = getOrElse(getAttributeFromRules(attribute.getValue(), rules), attribute.getValue());
            builder.attributeField(attribute.getKey(), mappedAttribute);
        }
    }

    private static String getAttributeFromRules(String attribute, Set<XmlEntityRule> rules) {
        for (XmlEntityRule rule : rules) {
            if (rule.getInputs().contains(attribute)) {
                return rule.getOutput();
            }
        }
        return null;
    }

    private static void mapNamespaces(XmlEntity entity, XmlEntityBuilder builder, Set<XmlEntityRule> rules) {
        Set<Entry<String, String>> namespaces = entity.getNamespaces().entrySet();

        if (namespaces.isEmpty()) {
            builder.namespace(entity.getNamespace());
            return;
        }

        for (Entry<String, String> namespace : namespaces) {
            String namespaceValue = getOrElse(getNamespaceFromRules(namespace.getValue(), rules), namespace.getValue());
            builder.namespaceField(namespace.getKey(), namespaceValue);
            builder.namespace(namespaceValue);
        }
    }

    private static String getNamespaceFromRules(String namespace, Set<XmlEntityRule> rules) {
        for (XmlEntityRule rule : rules) {
            if (rule.getInputs().contains(namespace)) {
                return rule.getOutput();
            }
        }
        return null;
    }

    private static void mapTagValue(XmlEntity entity, XmlEntityBuilder builder, Set<XmlEntityRule> rules) {
        for (XmlEntityRule xmlEntityRule : rules) {
            if (xmlEntityRule.getInputs().contains(entity.getValue())) {
                builder.value(xmlEntityRule.getOutput());
                return;
            }
        }
        builder.value(entity.getValue());
    }

    private static XmlEntityBuilder map(XmlEntity entity, XmlEntityBuilder builder, Map<XmlEntityMappingTargets, Set<XmlEntityRule>> mapping) {
        builder.tag(entity.getTag());
        builder.prefix(entity.getPrefix());

        for (Entry<XmlEntityMappingTargets, Set<XmlEntityRule>> entry : mapping.entrySet()) {
            switch (entry.getKey()) {
                case TAG_VALUE:
                    mapTagValue(entity, builder, entry.getValue());
                    continue;
                case ATTRIBUTE_VALUE:
                    mapAttributes(entity, builder, entry.getValue());
                    continue;
                case NAMESPACE_VALUE:
                    mapNamespaces(entity, builder, entry.getValue());
            }
        }

        for (XmlEntity xmlEntity : entity.getChildren()) {
            builder.child(map(xmlEntity, xmlEntityBuilder(), mapping).create());
        }

        return builder;
    }
}
