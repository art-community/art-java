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

package io.art.meta.model;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

@ToString
@ForGenerator
@EqualsAndHashCode
public abstract class MetaPackage {
    private final String name;
    private final Map<String, MetaPackage> packages = map();
    private final Map<Class<?>, MetaClass<?>> classes = map();

    protected MetaPackage(String name) {
        this.name = name;
    }

    protected <T extends MetaPackage> T register(T metaPackage) {
        packages.put(metaPackage.name(), metaPackage);
        return metaPackage;
    }

    protected <T extends MetaClass<?>> T register(T metaClass) {
        classes.put(metaClass.definition().type(), metaClass);
        return metaClass;
    }

    protected void beginComputation() {
        packages.values().forEach(MetaPackage::beginComputation);
        classes.values().forEach(MetaClass::beginComputation);
    }

    protected void completeComputation() {
        packages.values().forEach(MetaPackage::completeComputation);
        classes.values().forEach(MetaClass::completeComputation);
    }

    public String name() {
        return name;
    }

    public ImmutableMap<String, MetaPackage> packages() {
        return immutableMapOf(packages);
    }

    public ImmutableMap<Class<?>, MetaClass<?>> classes() {
        return immutableMapOf(classes);
    }

    public MetaPackage packageOf(String name) {
        return cast(packages.get(name));
    }

    public <T> MetaClass<T> classOf(Class<T> type) {
        return cast(classes.get(type));
    }
}
