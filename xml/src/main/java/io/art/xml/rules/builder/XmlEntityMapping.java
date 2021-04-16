/*
 * ART
 *
 * Copyright 2019-2021 ART
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
import static io.art.core.factory.MapFactory.map;
import static io.art.xml.constants.XmlEntityMappingTargets.*;
import java.util.*;

@Getter
public class XmlEntityMapping {
    private final Map<XmlEntityMappingTargets, Set<XmlEntityRule>> mapping = map();

    XmlEntityMapping(Set<XmlEntityRule> tagValueRules, Set<XmlEntityRule> attributeValueRules, Set<XmlEntityRule> namespaceValueRules) {
        mapping.put(TAG_VALUE, tagValueRules);
        mapping.put(ATTRIBUTE_VALUE, attributeValueRules);
        mapping.put(NAMESPACE_VALUE, namespaceValueRules);
    }
}
