package ru.adk.xml.rules.builder;

import ru.adk.xml.constants.XmlEntityMappingTargets;
import ru.adk.xml.exception.XmlMappingException;
import static java.text.MessageFormat.format;
import static ru.adk.core.factory.CollectionsFactory.setOf;
import static ru.adk.xml.constants.XmlEntityMappingTargets.*;
import static ru.adk.xml.constants.XmlMappingExceptionMessages.SAME_INPUT_OUTPUT;
import static ru.adk.xml.constants.XmlMappingExceptionMessages.UNEXPECTED_PARSED_PART_IN_RULE;
import java.util.Set;

public class XmlEntityMappingBuilderImpl implements XmlEntityMappingBuilder {
    private final Set<XmlEntityRule> attributeValueRules = setOf();
    private final Set<XmlEntityRule> namespaceValueRules = setOf();
    private final Set<XmlEntityRule> tagValueRules = setOf();

    @Override
    public XmlEntityPartBuilder addRule() {
        return new XmlEntityPartBuilderImpl(this);
    }

    @Override
    public XmlEntityMapping build() {
        return new XmlEntityMapping(tagValueRules, attributeValueRules, namespaceValueRules);
    }

    void add(XmlEntityRule rule, Set<XmlEntityMappingTargets> changeableParts) {
        if (isIncorrectRule(rule)) {
            throw new XmlMappingException(format(SAME_INPUT_OUTPUT, rule));
        }

        for (XmlEntityMappingTargets part : changeableParts) {
            if (part != ATTRIBUTE_VALUE && part != TAG_VALUE && part != NAMESPACE_VALUE) {
                throw new XmlMappingException(format(UNEXPECTED_PARSED_PART_IN_RULE, part, rule));
            }
        }

        for (XmlEntityMappingTargets changeablePart : changeableParts) {
            switch (changeablePart) {
                case TAG_VALUE:
                    tagValueRules.add(rule);
                    continue;
                case ATTRIBUTE_VALUE:
                    attributeValueRules.add(rule);
                    continue;
                case NAMESPACE_VALUE:
                    namespaceValueRules.add(rule);
            }
        }
    }

    private boolean isIncorrectRule(XmlEntityRule rule) {
        for (XmlEntityRule xmlEntityRule : tagValueRules) {
            if (xmlEntityRule.hasIntersectionWithInputsOrOutputs(rule.getInputs(), rule.getOutput())) {
                return true;
            }
        }

        for (XmlEntityRule xmlEntityRule : attributeValueRules) {
            if (xmlEntityRule.hasIntersectionWithInputsOrOutputs(rule.getInputs(), rule.getOutput())) {
                return true;
            }
        }

        for (XmlEntityRule xmlEntityRule : namespaceValueRules) {
            if (xmlEntityRule.hasIntersectionWithInputsOrOutputs(rule.getInputs(), rule.getOutput())) {
                return true;
            }
        }

        return false;
    }

}
