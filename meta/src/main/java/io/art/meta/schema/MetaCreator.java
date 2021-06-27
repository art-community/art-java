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

import io.art.meta.model.*;
import lombok.*;
import java.util.*;

@Builder(toBuilder = true)
public class MetaCreator {
    private int filledFields;
    private final Map<String, MetaProperty<?>> propertyMap;
    private final MetaProperty<?>[] propertyArray;
    private final MetaConstructor<?> allPropertiesConstructor;
    private final MetaConstructor<?> localPropertiesConstructor;

    @Builder.Default
    private final Object[] values = new Object[propertyArray.length];

    public MetaCreator prepare() {
        return toBuilder().values(new Object[propertyArray.length]).build();
    }

    public MetaCreator put(String name, Object value) {
        MetaProperty<?> property = propertyMap.get(name);
        values[property.index()] = property.type().inputTransformer().transform(value);
        filledFields++;
        return this;
    }

    public MetaCreator put(int index, Object value) {
        values[index] = propertyArray[index].type().inputTransformer().transform(value);
        filledFields++;
        return this;
    }

    public Object create() throws Throwable {
        if (filledFields <= localPropertiesConstructor.parameters().size()) {
            return localPropertiesConstructor.invoke(values);
        }
        return allPropertiesConstructor.invoke(values);
    }
}
