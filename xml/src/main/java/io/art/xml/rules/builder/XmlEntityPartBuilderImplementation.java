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

package io.art.xml.rules.builder;

import lombok.*;
import io.art.xml.constants.*;
import static io.art.core.factory.SetFactory.setOf;
import static io.art.xml.constants.XmlEntityMappingTargets.*;

@RequiredArgsConstructor
class XmlEntityPartBuilderImplementation implements XmlEntityPartBuilder {
    private final XmlEntityMappingBuilderImplementation mappingBuilder;

    @Override
    public XmlEntityRuleBuilder forAttributesValues() {
        return new XmlEntityRuleBuilderImplementation(mappingBuilder, setOf(ATTRIBUTE_VALUE));
    }

    @Override
    public XmlEntityRuleBuilder forTagsValues() {
        return new XmlEntityRuleBuilderImplementation(mappingBuilder, setOf(TAG_VALUE));
    }

    @Override
    public XmlEntityRuleBuilder forNamespacesValues() {
        return new XmlEntityRuleBuilderImplementation(mappingBuilder, setOf(NAMESPACE_VALUE));
    }

    @Override
    public XmlEntityRuleBuilder forAll() {
        return new XmlEntityRuleBuilderImplementation(mappingBuilder, setOf(ATTRIBUTE_VALUE, TAG_VALUE, NAMESPACE_VALUE));
    }

    @Override
    public XmlEntityRuleBuilder forTargets(XmlEntityMappingTargets... names) {
        return new XmlEntityRuleBuilderImplementation(mappingBuilder, setOf(names));
    }
}
