package ru.art.xml.rules.builder;

import ru.art.xml.constants.XmlEntityMappingTargets;

public interface XmlEntityPartBuilder {
    XmlEntityRuleBuilder forAttributesValues();

    XmlEntityRuleBuilder forTagsValues();

    XmlEntityRuleBuilder forNamespacesValues();

    XmlEntityRuleBuilder forAll();

    XmlEntityRuleBuilder forTargets(XmlEntityMappingTargets... names);
}
