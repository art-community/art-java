/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.xml.rules.builder;

import ru.art.xml.constants.*;
import ru.art.xml.exception.*;
import static java.text.MessageFormat.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.xml.constants.XmlEntityMappingTargets.*;
import static ru.art.xml.constants.XmlMappingExceptionMessages.*;
import java.util.*;

public class XmlEntityMappingBuilderImplementation implements XmlEntityMappingBuilder {
    private final Set<XmlEntityRule> attributeValueRules = setOf();
    private final Set<XmlEntityRule> namespaceValueRules = setOf();
    private final Set<XmlEntityRule> tagValueRules = setOf();

    @Override
    public XmlEntityPartBuilder addRule() {
        return new XmlEntityPartBuilderImplementation(this);
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
