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
import static io.art.meta.constants.MetaConstants.Errors.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@Builder
public class MetaCreatorTemplate {
    private final MetaClass<?> owner;
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

    public MetaCreatorTemplate validate(Function<String, RuntimeException> exceptionFactory) {
        if (!isValid()) {
            throw exceptionFactory.apply(format(CLASS_CREATOR_INVALID, owner.definition().type()));
        }
        return this;
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
            return putValue(property, property.type().inputTransformer().fromString(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Integer value) {
            return putValue(property, property.type().inputTransformer().fromInteger(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Double value) {
            return putValue(property, property.type().inputTransformer().fromDouble(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Long value) {
            return putValue(property, property.type().inputTransformer().fromLong(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Short value) {
            return putValue(property, property.type().inputTransformer().fromShort(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Byte value) {
            return putValue(property, property.type().inputTransformer().fromByte(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Float value) {
            return putValue(property, property.type().inputTransformer().fromFloat(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Character value) {
            return putValue(property, property.type().inputTransformer().fromCharacter(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Boolean value) {
            return putValue(property, property.type().inputTransformer().fromBoolean(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, List<?> value) {
            return putValue(property, property.type().inputTransformer().fromArray(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Map<?, ?> value) {
            return putValue(property, property.type().inputTransformer().fromMap(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, byte[] value) {
            return putValue(property, property.type().inputTransformer().fromByteArray(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, Supplier<?> value) {
            return putValue(property, property.type().inputTransformer().fromLazy(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, ImmutableLazyMapImplementation<?, ?> value) {
            return putValue(property, property.type().inputTransformer().fromLazyMap(value));
        }

        public MetaCreatorInstance put(MetaProperty<?> property, ImmutableLazyArrayImplementation<?> value) {
            return putValue(property, property.type().inputTransformer().fromLazyArray(value));
        }

        public MetaCreatorInstance putValue(MetaProperty<?> property, Object value) {
            values[property.index()] = value;
            filledFields++;
            return this;
        }

        public MetaCreatorInstance putNull(MetaProperty<?> property) {
            values[property.index()] = null;
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
