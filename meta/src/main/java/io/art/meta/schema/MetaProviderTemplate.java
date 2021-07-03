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
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

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

        public Object getValue(MetaProperty<?> property) throws Throwable {
            return property.getter().invoke(model);
        }

        public String getString(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toString(cast(notNull)));
        }

        public Integer getInteger(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toInteger(cast(notNull)));
        }

        public Long getLong(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toLong(cast(notNull)));
        }

        public Double getDouble(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toDouble(cast(notNull)));
        }

        public Short getShort(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toShort(cast(notNull)));
        }

        public Float getFloat(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toFloat(cast(notNull)));
        }

        public Byte getByte(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toByte(cast(notNull)));
        }

        public Character getCharacter(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toCharacter(cast(notNull)));
        }

        public byte[] getByteArray(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toByteArray(cast(notNull)));
        }

        public Boolean getBoolean(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toBoolean(cast(notNull)));
        }

        public List<?> getArray(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toArray(cast(notNull)));
        }

        public Map<?, ?> getMap(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toMap(cast(notNull)));
        }

        public Supplier<?> getLazy(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toLazy(cast(notNull)));
        }

        public ImmutableLazyArrayImplementation<?> getLazyArray(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toLazyArray(cast(notNull)));
        }

        public ImmutableLazyMapImplementation<?, ?> getLazyMap(MetaProperty<?> property) throws Throwable {
            return let(property.getter().invoke(model), notNull -> property.type().outputTransformer().toLazyMap(cast(notNull)));
        }

        public Object getEntity(MetaProperty<?> property) throws Throwable {
            return property.getter().invoke(model);
        }

        public ImmutableMap<String, MetaProperty<?>> properties() {
            return MetaProviderTemplate.this.propertyMap();
        }
    }
}
