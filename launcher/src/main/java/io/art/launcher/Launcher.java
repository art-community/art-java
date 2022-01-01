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

package io.art.launcher;

import io.art.configurator.module.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.configuration.*;
import io.art.core.exception.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.constants.ContextConstants.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FunctionExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.launcher.LauncherConstants.*;
import static io.art.launcher.LauncherConstants.Errors.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@Public
@UtilityClass
public class Launcher {
    private static final AtomicBoolean launched = new AtomicBoolean(false);

    public static void launch(Activator activator) {
        if (launched.compareAndSet(false, true)) {
            ModuleActivator configurator = activator.activators().get(CONFIGURATOR_MODULE_ID);
            if (nonNull(configurator)) {
                configuredLaunch(activator, configurator);
                return;
            }
            defaultLaunch(activator);
        }
    }

    private static void configuredLaunch(Activator activator, ModuleActivator configuratorActivator) {
        ImmutableMap<String, ModuleActivator> activators = activator.activators();
        ImmutableMap<String, ModuleInitializationOperator<?>> decorators = activator.decorators();

        LazyProperty<Logger> logger = lazy(() -> Logging.logger(LAUNCHER_LOGGER));

        ConfiguratorModule configuratorModule = cast(configuratorActivator.getFactory().get());
        Consumer<String> printer = activators.containsKey(LOGGING_MODULE_ID) ? message -> logger.get().info(message) : emptyConsumer();

        ContextConfiguration.ContextConfigurationBuilder contextConfiguration = ContextConfiguration.builder()
                .arguments(immutableArrayOf(activator.arguments()))
                .onUnload(activator.onUnload())
                .onLoad(activator.onLoad())
                .onLaunch(activator.onLaunch())
                .onShutdown(activator.onShutdown())
                .beforeReload(activator.beforeReload())
                .afterReload(activator.afterReload())
                .main(orElse(activator.main(), DEFAULT_MAIN_MODULE_ID))
                .reload(module -> module.configure(configurator -> configurator.from(configuratorModule.orderedSources())));

        prepareInitialization(contextConfiguration.printer(printer).build());
        ModuleInitializationProvider<ConfiguratorInitializer> configuratorInitializer = cast(configuratorActivator.getInitializer());
        ModuleInitializationOperator<ConfiguratorInitializer> configuratorDecorator = cast(orElse(decorators.get(CONFIGURATOR_MODULE_ID), ModuleInitializationOperator.identity()));
        configuratorModule
                .loadSources(immutableSetOf(activators.keySet()))
                .configure(configurator -> configurator.initialize(configuratorDecorator.apply(configuratorInitializer.get()).initialize(configuratorModule)));
        ImmutableSet.Builder<Module<?, ?>> builder = immutableSetBuilder();

        builder.add(configuratorModule);
        Map<String, ModuleActivator> mutableActivators = activators.toMutable();
        mutableActivators.remove(CONFIGURATOR_MODULE_ID);

        for (ModuleActivator moduleActivator : mutableActivators.values()) {
            Module<?, ?> module = moduleActivator.getFactory().get();
            ModuleInitializationOperator<?> decorator = cast(orElse(decorators.get(module.getId()), ModuleInitializationOperator.identity()));
            ModuleInitializationProvider<?> initializer = moduleActivator.getInitializer();
            if (nonNull(initializer)) {
                module.configure(configurator -> configurator.initialize(cast(decorator.apply(cast(initializer.get())).initialize(cast(module)))));
            }
            module.configure(configurator -> configurator.from(configuratorModule.orderedSources()));
            builder.add(module);
        }
        processInitialization(builder.build());
    }

    private static void defaultLaunch(Activator activator) {
        ImmutableMap<String, ModuleActivator> activators = activator.activators();

        LazyProperty<Logger> logger = lazy(() -> Logging.logger(LAUNCHER_LOGGER));
        Consumer<String> printer = activators.containsKey(LOGGING_MODULE_ID) ? message -> logger.get().info(message) : emptyConsumer();
        ContextConfiguration.ContextConfigurationBuilder contextConfiguration = ContextConfiguration.builder()
                .arguments(immutableArrayOf(activator.arguments()))
                .onUnload(activator.onUnload())
                .onLoad(before(() -> printer.accept(DEFAULT_CONFIGURATION), activator.onLoad()))
                .onLaunch(activator.onLaunch())
                .onShutdown(activator.onShutdown())
                .beforeReload(activator.beforeReload())
                .afterReload(activator.afterReload())
                .main(orElse(activator.main(), DEFAULT_MAIN_MODULE_ID));

        initialize(contextConfiguration.printer(printer).build(), activators.values().toArray(new ModuleActivator[0]));
    }
}
