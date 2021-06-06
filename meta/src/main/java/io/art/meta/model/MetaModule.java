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
import static io.art.core.factory.SetFactory.*;
import static java.util.Arrays.*;
import java.util.*;

@ToString
@ForGenerator
@EqualsAndHashCode
public abstract class MetaModule {
    private final Map<String, MetaPackage> packages = map();
    private final Set<MetaClass<?>> rootClasses = set();
    private final List<MetaModule> dependencies;

    protected MetaModule(MetaModule... dependencies) {
        this.dependencies = asList(dependencies);
        rootClasses.forEach(MetaClass::compute);
        packages.values().forEach(MetaPackage::compute);
    }

    protected <T extends MetaPackage> T register(T metaPackage) {
        packages.put(metaPackage.name(), metaPackage);
        return metaPackage;
    }

    protected <T extends MetaClass<?>> T register(T metaClass) {
        rootClasses.add(metaClass);
        return metaClass;
    }

    public ImmutableMap<String, MetaPackage> packages() {
        return immutableMapOf(packages);
    }

    public ImmutableSet<MetaClass<?>> rootClasses() {
        return immutableSetOf(rootClasses);
    }

    public <T extends MetaPackage> T packageOf(String name) {
        return cast(packages.get(name));
    }
}
