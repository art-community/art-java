package ru.adk.xml.rules.builder;

import lombok.RequiredArgsConstructor;
import ru.adk.xml.constants.XmlEntityMappingTargets;
import static ru.adk.core.factory.CollectionsFactory.setOf;
import static ru.adk.xml.constants.XmlEntityMappingTargets.*;

@RequiredArgsConstructor
class XmlEntityPartBuilderImpl implements XmlEntityPartBuilder {
    private final XmlEntityMappingBuilderImpl mappingBuilder;

    @Override
    public XmlEntityRuleBuilder forAttributesValues() {
        return new XmlEntityRuleBuilderImpl(mappingBuilder, setOf(ATTRIBUTE_VALUE));
    }

    @Override
    public XmlEntityRuleBuilder forTagsValues() {
        return new XmlEntityRuleBuilderImpl(mappingBuilder, setOf(TAG_VALUE));
    }

    @Override
    public XmlEntityRuleBuilder forNamespacesValues() {
        return new XmlEntityRuleBuilderImpl(mappingBuilder, setOf(NAMESPACE_VALUE));
    }

    @Override
    public XmlEntityRuleBuilder forAll() {
        return new XmlEntityRuleBuilderImpl(mappingBuilder, setOf(ATTRIBUTE_VALUE, TAG_VALUE, NAMESPACE_VALUE));
    }

    @Override
    public XmlEntityRuleBuilder forTargets(XmlEntityMappingTargets... names) {
        return new XmlEntityRuleBuilderImpl(mappingBuilder, setOf(names));
    }
}
