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

package io.art.launcher;

import io.art.configurator.module.*;
import io.art.core.collection.*;
import io.art.core.configuration.*;
import io.art.core.exception.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.logging.logger.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.constants.ContextConstants.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.launcher.LauncherConstants.*;
import static io.art.launcher.LauncherConstants.Errors.*;
import static io.art.logging.module.LoggingModule.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

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
        throw new InternalRuntimeException(MODULES_ALREADY_LAUNCHED);
    }

    private static void configuredLaunch(Activator activator, ModuleActivator configuratorActivator) {
        ImmutableMap<String, ModuleActivator> activators = activator.activators();

        LazyProperty<Logger> logger = lazy(() -> logger(LAUNCHER_LOGGER));

        ConfiguratorModule configuratorModule = cast(configuratorActivator.getFactory().get());

        ContextConfiguration.ContextConfigurationBuilder contextConfiguration = ContextConfiguration.builder()
                .arguments(immutableArrayOf(activator.arguments()))
                .onUnload(activator.onUnload())
                .onLoad(activator.onLoad())
                .beforeReload(activator.beforeReload())
                .afterReload(activator.afterReload())
                .main(orElse(activator.main(), DEFAULT_MAIN_MODULE_ID))
                .reload(module -> module.configure(configurator -> configurator.from(configuratorModule.orderedSources())));

        Consumer<String> printer = activator.activators().containsKey(LOGGING_MODULE_ID) ? message -> logger.get().info(message) : emptyConsumer();

        prepareInitialization(contextConfiguration.printer(printer).build());
        ModuleInitializationOperator<ConfiguratorInitializer> configuratorActivatorInitializer = cast(configuratorActivator.getInitializer());
        configuratorModule
                .loadSources()
                .configure(configurator -> configurator.initialize(configuratorActivatorInitializer.get().initialize(configuratorModule)));
        ImmutableSet.Builder<Module<?, ?>> builder = immutableSetBuilder();

        builder.add(configuratorModule);

        for (ModuleActivator moduleActivator : activators.values()) {
            Module<?, ?> module = moduleActivator.getFactory().get();
            ModuleInitializationOperator<?> initializer = moduleActivator.getInitializer();
            if (nonNull(initializer)) {
                module.configure(configurator -> configurator.initialize(cast(initializer.get().initialize(cast(module)))));
            }
            module.configure(configurator -> configurator.from(configuratorModule.orderedSources()));
            builder.add(module);
        }
        processInitialization(builder.build());

        printer.accept(format(CONFIGURED_BY_MESSAGE, configuratorModule.getConfiguration().getConfiguration().getPath()));
        LAUNCHED_MESSAGES.forEach(printer);
    }

    private static void defaultLaunch(Activator activator) {
        ImmutableMap<String, ModuleActivator> activators = activator.activators();

        LazyProperty<Logger> logger = lazy(() -> logger(LAUNCHER_LOGGER));

        ContextConfiguration.ContextConfigurationBuilder contextConfiguration = ContextConfiguration.builder()
                .arguments(immutableArrayOf(activator.arguments()))
                .onUnload(activator.onUnload())
                .onLoad(activator.onLoad())
                .beforeReload(activator.beforeReload())
                .afterReload(activator.afterReload())
                .main(orElse(activator.main(), DEFAULT_MAIN_MODULE_ID));

        Consumer<String> printer = activator.activators().containsKey(LOGGING_MODULE_ID) ? message -> logger.get().info(message) : emptyConsumer();

        initialize(contextConfiguration.printer(printer).build(), activators.values().toArray(new ModuleActivator[0]));

        printer.accept(DEFAULT_CONFIGURATION);
        LAUNCHED_MESSAGES.forEach(printer);
    }
}
