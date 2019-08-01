package ru.adk.xml.rules.processor;

import ru.adk.entity.XmlEntity;
import ru.adk.entity.XmlEntity.XmlEntityBuilder;
import ru.adk.xml.constants.XmlEntityMappingTargets;
import ru.adk.xml.exception.XmlMappingException;
import ru.adk.xml.rules.builder.XmlEntityMapping;
import ru.adk.xml.rules.builder.XmlEntityMappingBuilder;
import ru.adk.xml.rules.builder.XmlEntityMappingBuilderImpl;
import ru.adk.xml.rules.builder.XmlEntityRule;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.entity.XmlEntity.xmlEntityBuilder;
import static ru.adk.xml.constants.XmlMappingExceptionMessages.XML_ENTITY_IS_NULL;
import static ru.adk.xml.constants.XmlMappingExceptionMessages.XML_MAPPING_RULES_ARE_EMPTY;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class XmlEntityRulesProcessor {
    public static XmlEntityMappingBuilder xmlMappingBuilder() {
        return new XmlEntityMappingBuilderImpl();
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
