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
