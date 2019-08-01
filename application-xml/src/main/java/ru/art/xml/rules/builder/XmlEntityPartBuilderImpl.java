package ru.art.xml.rules.builder;

import lombok.RequiredArgsConstructor;
import ru.art.xml.constants.XmlEntityMappingTargets;
import static ru.art.core.factory.CollectionsFactory.setOf;
import static ru.art.xml.constants.XmlEntityMappingTargets.*;

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
