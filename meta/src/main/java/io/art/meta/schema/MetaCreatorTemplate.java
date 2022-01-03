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
import static io.art.meta.constants.MetaConstants.Errors.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Builder
public class MetaCreatorTemplate {
    private final MetaClass<?> owner;
    private final ImmutableMap<String, MetaProperty<?>> propertyMap;
    private final ImmutableArray<MetaProperty<?>> propertyArray;
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

    public MetaCreatorTemplate validate(Function<String, RuntimeException> exceptionFactory) {
        if (!isValid()) {
            throw exceptionFactory.apply(format(CLASS_CREATOR_INVALID, owner.definition().type()));
        }
        return this;
    }

    public ImmutableMap<String, MetaProperty<?>> propertyMap() {
        return propertyMap;
    }

    public ImmutableArray<MetaProperty<?>> propertyArray() {
        return propertyArray;
    }

    public MetaCreatorInstance instantiate() {
        return new MetaCreatorInstance();
    }

    @NoArgsConstructor(access = PRIVATE)
    public class MetaCreatorInstance {
        private int filledFields;
        private final Object[] values = new Object[propertyArray.size()];

        public ImmutableMap<String, MetaProperty<?>> propertyMap() {
            return MetaCreatorTemplate.this.propertyMap();
        }

        public ImmutableArray<MetaProperty<?>> propertyArray() {
            return MetaCreatorTemplate.this.propertyArray();
        }

        public MetaCreatorInstance putValue(MetaProperty<?> property, Object value) {
            values[property.index()] = value;
            filledFields++;
            return this;
        }

        public Object create() {
            if (filledFields == 0) {
                return noPropertiesConstructor.invokeCatched();
            }
            if (filledFields <= localPropertiesConstructor.parameters().size()) {
                return localPropertiesConstructor.invokeCatched(values);
            }
            return allPropertiesConstructor.invokeCatched(values);
        }
    }
}
