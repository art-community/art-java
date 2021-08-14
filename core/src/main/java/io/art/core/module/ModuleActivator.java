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

package io.art.core.module;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import lombok.*;
import static io.art.core.factory.SetFactory.*;
import java.util.*;

@Builder
@ForUsing
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ModuleActivator {
    @Getter
    @EqualsAndHashCode.Include
    private final String id;
    @Getter
    private final ModuleFactory<?> factory;
    @Getter
    private final ModuleInitializationOperator<?> initializer;
    private final Set<ModuleActivator> dependencies = set();

    public static ModuleActivator module(Class<?> moduleClass, ModuleFactory<?> moduleFactory) {
        return module(moduleClass.getSimpleName(), moduleFactory);
    }

    public static ModuleActivator module(Class<?> moduleClass, ModuleFactory<?> moduleFactory, ModuleInitializationOperator<?> initializer) {
        return module(moduleClass.getSimpleName(), moduleFactory, initializer);
    }

    public static ModuleActivator module(String id, ModuleFactory<?> moduleFactory) {
        return ModuleActivator.builder()
                .id(id)
                .factory(moduleFactory)
                .build();
    }

    public static ModuleActivator module(String id, ModuleFactory<?> moduleFactory, ModuleInitializationOperator<?> initializer) {
        return ModuleActivator.builder()
                .id(id)
                .initializer(initializer)
                .factory(moduleFactory)
                .build();
    }

    public ImmutableSet<ModuleActivator> dependencies() {
        return immutableSetOf(dependencies);
    }

    public ModuleActivator with(ModuleActivator dependency) {
        dependencies.add(dependency);
        return this;
    }
}
