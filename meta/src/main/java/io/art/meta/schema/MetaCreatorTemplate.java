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
import static io.art.core.factory.MapFactory.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.*;

@Builder
public class MetaCreatorTemplate {
    private final Map<String, MetaProperty<?>> propertyMap;
    private final MetaProperty<?>[] propertyArray;
    private final MetaConstructor<?> allPropertiesConstructor;
    private final MetaConstructor<?> localPropertiesConstructor;
    private final MetaConstructor<?> noPropertiesConstructor;

    public MetaConstructor<?> allPropertiesConstructor() {
        return allPropertiesConstructor;
    }

    public MetaConstructor<?> localPropertiesConstructor() {
        return localPropertiesConstructor;
    }

    public MetaConstructor<?> noPropertiesConstructor() {
        return noPropertiesConstructor;
    }

    public boolean isValid() {
        return nonNull(allPropertiesConstructor) || nonNull(localPropertiesConstructor) || nonNull(noPropertiesConstructor);
    }

    public ImmutableMap<String, MetaProperty<?>> properties() {
        return immutableMapOf(propertyMap);
    }

    public MetaCreatorInstance instantiate() {
        return new MetaCreatorInstance();
    }

    @NoArgsConstructor(access = PRIVATE)
    public class MetaCreatorInstance {
        private int filledFields;
        private final Object[] values = new Object[propertyArray.length];

        public ImmutableMap<String, MetaProperty<?>> properties() {
            return MetaCreatorTemplate.this.properties();
        }

        public MetaCreatorInstance put(MetaProperty<?> property, String value) {
            values[property.index()] = property.type().inputTransformer().fromString(value);
            filledFields++;
            return this;
        }

        public MetaCreatorInstance put(MetaProperty<?> property, List<?> value) {
            values[property.index()] = property.type().inputTransformer().fromArray(value);
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
}
