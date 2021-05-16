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
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.configuration.*;
import io.art.core.context.*;
import io.art.core.exception.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.logging.logger.*;
import io.art.logging.module.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.launcher.LauncherConstants.Errors.*;
import static io.art.launcher.LauncherConstants.*;
import static io.art.logging.module.LoggingModule.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@UsedByGenerator
public class Launcher {
    private static final AtomicBoolean LAUNCHED = new AtomicBoolean(false);

    public static void launch(Activator activator) {
        if (LAUNCHED.compareAndSet(false, true)) {
            ImmutableMap<String, ModuleActivator> activators = activator.activators();

            LazyProperty<Logger> logger = lazy(() -> logger(Context.class));

            ModuleActivator configuratorActivator = activator.configuratorActivator();
            ConfiguratorModule configuratorModule = cast(configuratorActivator.getFactory().get());

            ContextConfiguration contextConfiguration = ContextConfiguration.builder()
                    .arguments(immutableArrayOf(activator.arguments()))
                    .onUnload(activator.onUnload())
                    .onLoad(activator.onLoad())
                    .beforeReload(activator.beforeReload())
                    .afterReload(activator.afterReload())
                    .mainModuleId(activator.mainModuleId())
                    .reload(module -> module.configure(configurator -> configurator.from(configuratorModule.orderedSources())))
                    .build();

            Consumer<String> printer = message -> {
                if (!activator.quiet() && activators.containsKey(LoggingModule.class.getSimpleName())) {
                    logger.get().info(message);
                }
            };

            prepareInitialization(contextConfiguration, printer);
            configuratorModule
                    .loadSources()
                    .configure(configurator -> configurator.initialize(cast(configuratorActivator.getInitializer().get().initialize(cast(configuratorModule)))));
            ImmutableSet.Builder<Module<?, ?>> builder = immutableSetBuilder();
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

            LAUNCHED_MESSAGES.forEach(printer);
            return;
        }
        throw new InternalRuntimeException(MODULES_ALREADY_LAUNCHED);
    }
}
