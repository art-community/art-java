package ru.adk.xml.rules.builder;

import ru.adk.xml.constants.XmlEntityMappingTargets;

public interface XmlEntityPartBuilder {
    XmlEntityRuleBuilder forAttributesValues();

    XmlEntityRuleBuilder forTagsValues();

    XmlEntityRuleBuilder forNamespacesValues();

    XmlEntityRuleBuilder forAll();

    XmlEntityRuleBuilder forTargets(XmlEntityMappingTargets... names);
}
