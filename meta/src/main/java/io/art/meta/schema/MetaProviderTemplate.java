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

package io.art.meta.schema;

import io.art.core.collection.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static lombok.AccessLevel.*;
import java.util.*;

@AllArgsConstructor
public class MetaProviderTemplate {
    private final Map<String, MetaProperty<?>> propertyMap;
    private final MetaProperty<?>[] propertyArray;

    public MetaProviderInstance instantiate(Object model) {
        return new MetaProviderInstance(model);
    }

    public ImmutableMap<String, MetaProperty<?>> propertyMap() {
        return immutableMapOf(propertyMap);
    }

    public ImmutableArray<MetaProperty<?>> propertyArray() {
        return immutableArrayOf(propertyArray);
    }

    @AllArgsConstructor(access = PRIVATE)
    public class MetaProviderInstance {
        private final Object model;

        public Object getValue(MetaProperty<?> property) {
            return property.getter().invokeCatched(model);
        }

        public ImmutableMap<String, MetaProperty<?>> properties() {
            return MetaProviderTemplate.this.propertyMap();
        }
    }
}
