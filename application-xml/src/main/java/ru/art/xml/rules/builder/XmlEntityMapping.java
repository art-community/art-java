package ru.art.xml.rules.builder;

import lombok.Getter;
import ru.art.xml.constants.XmlEntityMappingTargets;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.xml.constants.XmlEntityMappingTargets.*;
import java.util.Map;
import java.util.Set;

@Getter
public class XmlEntityMapping {
    private final Map<XmlEntityMappingTargets, Set<XmlEntityRule>> mapping = mapOf();

    XmlEntityMapping(Set<XmlEntityRule> tagValueRules, Set<XmlEntityRule> attributeValueRules, Set<XmlEntityRule> namespaceValueRules) {
        mapping.put(TAG_VALUE, tagValueRules);
        mapping.put(ATTRIBUTE_VALUE, attributeValueRules);
        mapping.put(NAMESPACE_VALUE, namespaceValueRules);
    }
}
