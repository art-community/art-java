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

package io.art.core.initializer;

import io.art.core.collection.*;
import io.art.core.configuration.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import io.art.core.singleton.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.configuration.ContextConfiguration.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.singleton.SingletonAction.*;
import static java.util.Arrays.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import java.util.*;

@UtilityClass
public class Initializer {
    private final static SingletonAction initialize = singletonAction();

    public static void initialize(ModuleFactory<?>... modules) {
        initialize(defaults(), modules);
    }

    public static void initialize(ContextConfiguration configuration, ModuleFactory<?>... modules) {
        initialize.run(() -> initializeModules(configuration, modules));
    }

    public static void initialize(ModuleActivator... modules) {
        initialize(defaults(), modules);
    }

    public static void initialize(ContextConfiguration configuration, ModuleActivator... modules) {
        initializeModules(configuration, modules);
    }

    private static void initializeModules(ContextConfiguration configuration, ModuleFactory<?>... modules) {
        prepareInitialization(configuration);
        processInitialization(stream(modules).map(ModuleFactory::get).collect(immutableSetCollector()));
    }

    private static void initializeModules(ContextConfiguration configuration, ModuleActivator... modules) {
        prepareInitialization(configuration);
        Map<String, ModuleInitializationOperator<?>> decorators = map();
        Map<String, ModuleActivator> modulesMap = stream(modules).collect(mapCollector(ModuleActivator::getId, identity()));
        for (ModuleActivator activator : modules) {
            ImmutableMap<String, ModuleInitializationOperator<?>> moduleDecorators = activator.decorators();
            for (Map.Entry<String, ModuleInitializationOperator<?>> entry : moduleDecorators.entrySet()) {
                if (modulesMap.containsKey(entry.getKey())) {
                    ModuleInitializationOperator<?> existed = orElse(decorators.get(entry.getKey()), ModuleInitializationOperator.identity());
                    decorators.put(entry.getKey(), value -> entry.getValue().apply(cast(existed.apply(cast(value)))));
                }
            }
        }
        Builder<Module<?, ?>> builder = immutableSetBuilder();
        for (ModuleActivator activator : modules) {
            Module<?, ?> module = activator.getFactory().get();
            ModuleInitializationProvider<?> initializer = activator.getInitializer();
            ModuleInitializationOperator<?> decorator = cast(orElse(decorators.get(module.getId()), ModuleInitializationOperator.identity()));
            if (nonNull(initializer)) {
                module.configure(configurator -> configurator.initialize(cast(decorator.apply(cast(initializer.get())).initialize(cast(module)))));
            }
            builder.add(module);
        }
        processInitialization(builder.build());
    }
}
