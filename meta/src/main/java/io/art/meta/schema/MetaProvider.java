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
import static io.art.meta.constants.MetaConstants.MetaTypeExternalKind.*;
import java.util.*;

public class MetaProvider {
    private Object model;
    private final Set<String> names;
    private final Map<String, MetaProperty<?>> propertiesMap;
    private final MetaProperty<?>[] propertiesArray;

    public MetaProvider(Map<String, MetaProperty<?>> properties) {
        this.propertiesMap = properties;
        propertiesArray = new MetaProperty[properties.size()];
        for (MetaProperty<?> property : properties.values()) {
            propertiesArray[property.index()] = property;
        }
        names = properties.keySet();
    }

    public MetaProvider(Object model, Map<String, MetaProperty<?>> properties) {
        this.model = model;
        this.propertiesMap = properties;
        propertiesArray = new MetaProperty[properties.size()];
        for (MetaProperty<?> property : properties.values()) {
            propertiesArray[property.index()] = property;
        }
        names = properties.keySet();
    }

    public Set<String> names() {
        return names;
    }

    public String getString(String name) throws Throwable {
        MetaProperty<?> property = propertiesMap.get(name);
        return (String) property.type().outputTransformers().stringTransformer().transform(property.getter().invoke(model));
    }

    public String getString(int index) throws Throwable {
        return (String) propertiesArray[index].getter().invoke(model);
    }
}
