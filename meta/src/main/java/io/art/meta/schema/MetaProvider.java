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

import io.art.meta.exception.*;
import io.art.meta.model.*;
import lombok.*;
import java.util.*;

@Builder(toBuilder = true)
public class MetaProvider {
    private final Object model;
    private final Set<String> names;
    private final Map<String, MetaProperty<?>> propertyMap;
    private final MetaProperty<?>[] propertyArray;

    public MetaProvider prepare(Object model) {
        return toBuilder().model(model).build();
    }

    public Set<String> names() {
        return names;
    }

    public Object getValue(String name) {
        return getValue(propertyMap.get(name).index());
    }

    public Object getValue(int index) {
        MetaProperty<?> property = propertyArray[index];
        try {
            return property.type().outputTransformer().transform(property.getter().invoke(model));
        } catch (Throwable throwable) {
            throw new TransformationException(throwable);
        }
    }
}
