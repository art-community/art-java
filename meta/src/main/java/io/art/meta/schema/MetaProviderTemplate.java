/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.meta.schema;

import io.art.core.collection.*;
import io.art.meta.model.*;
import lombok.*;
import static lombok.AccessLevel.*;

@AllArgsConstructor
public class MetaProviderTemplate {
    private final ImmutableMap<String, MetaProperty<?>> propertyMap;
    private final ImmutableArray<MetaProperty<?>> propertyArray;

    public MetaProviderInstance instantiate(Object model) {
        return new MetaProviderInstance(model);
    }

    public ImmutableMap<String, MetaProperty<?>> propertyMap() {
        return propertyMap;
    }

    public ImmutableArray<MetaProperty<?>> propertyArray() {
        return propertyArray;
    }

    @AllArgsConstructor(access = PRIVATE)
    public class MetaProviderInstance {
        private final Object model;

        public Object getValue(MetaProperty<?> property) {
            return property.getter().invokeCatched(model);
        }

        public ImmutableMap<String, MetaProperty<?>> propertyMap() {
            return MetaProviderTemplate.this.propertyMap();
        }

        public ImmutableArray<MetaProperty<?>> propertyArray() {
            return MetaProviderTemplate.this.propertyArray();
        }
    }
}
