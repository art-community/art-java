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

import io.art.xml.constants.*;
import lombok.*;
import static io.art.core.factory.SetFactory.set;
import java.util.*;

@RequiredArgsConstructor
class XmlEntityRuleBuilderImplementation implements XmlEntityRuleBuilder {
    private final XmlEntityMappingBuilderImplementation mappingBuilder;
    private final Set<XmlEntityMappingTargets> parsedParts;
    private final Set<String> inputs = set();

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
