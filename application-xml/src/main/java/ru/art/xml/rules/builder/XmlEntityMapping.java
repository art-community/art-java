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

import lombok.Getter;
import ru.art.xml.constants.XmlEntityMappingTargets;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.xml.constants.XmlEntityMappingTargets.*;
import java.util.Map;
import java.util.Set;

@Getter
public class XmlEntityMapping {
    private final Map<XmlEntityMappingTargets, Set<XmlEntityRule>> mapping = mapOf();

    XmlEntityMapping(Set<XmlEntityRule> tagValueRules, Set<XmlEntityRule> attributeValueRules, Set<XmlEntityRule> namespaceValueRules) {
        mapping.put(TAG_VALUE, tagValueRules);
        mapping.put(ATTRIBUTE_VALUE, attributeValueRules);
        mapping.put(NAMESPACE_VALUE, namespaceValueRules);
    }
}
