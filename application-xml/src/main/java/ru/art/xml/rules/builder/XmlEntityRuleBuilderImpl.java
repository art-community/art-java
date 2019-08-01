package ru.art.xml.rules.builder;

import lombok.RequiredArgsConstructor;
import ru.art.xml.constants.XmlEntityMappingTargets;
import static ru.art.core.factory.CollectionsFactory.setOf;
import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
class XmlEntityRuleBuilderImpl implements XmlEntityRuleBuilder {
    private final XmlEntityMappingBuilderImpl mappingBuilder;
    private final Set<XmlEntityMappingTargets> parsedParts;
    private final Set<String> inputs = setOf();

    @Override
    public XmlEntityRuleBuilder input(String oldValue) {
        inputs.add(oldValue);
        return this;
    }

    @Override
    public XmlEntityRuleBuilder input(Collection<String> oldValues) {
        inputs.addAll(oldValues);
        return this;
    }

    @Override
    public XmlEntityMappingBuilder output(String newValue) {
        XmlEntityRule rule = XmlEntityRule.builder()
                .inputs(inputs)
                .output(newValue)
                .build();
        mappingBuilder.add(rule, parsedParts);
        return mappingBuilder;
    }
}
