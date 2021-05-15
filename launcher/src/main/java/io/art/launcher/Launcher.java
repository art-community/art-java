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
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.context.Context.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.launcher.Activator.*;
import static io.art.launcher.LauncherConstants.Errors.*;
import static io.art.launcher.LauncherConstants.*;
import static io.art.launcher.ModulesInitializer.*;
import static io.art.logging.module.LoggingModule.*;
import static java.util.Objects.*;
import java.util.concurrent.atomic.*;

@UtilityClass
@UsedByGenerator
public class Launcher {
    private static final AtomicBoolean LAUNCHED = new AtomicBoolean(false);

    public static void launch(Activator activator) {
        if (LAUNCHED.compareAndSet(false, true)) {
            ImmutableSet.Builder<Module<?, ?>> builder = immutableSetBuilder();
            ImmutableSet<ModuleActivator> activators = activator.modules();
            ModuleActivator configuratorActivator = activator.configuratorActivator();
            ConfiguratorModule configuratorModule = cast(configuratorActivator.getFactory().get());
            configuratorModule.configure(configurator -> configurator.initialize(cast(configuratorActivator.getInitializer().initialize(cast(configuratorModule)))));
            for (ModuleActivator moduleActivator : activators) {
                Module<?, ?> module = moduleActivator.getFactory().get();
                ModuleInitializer<?, ?, ?> initializer = moduleActivator.getInitializer();
                module.configure(configurator -> configurator.from(configuratorModule.orderedSources()));
                if (nonNull(initializer)) {
                    module.configure(configurator -> configurator.initialize(cast(initializer.initialize(cast(module)))));
                }
                builder.add(module);
            }
            LazyProperty<Logger> logger = lazy(() -> logger(Context.class));
            ContextConfiguration contextConfiguration = ContextConfiguration.builder()
                    .onUnload(activator.onUnload())
                    .onLoad(activator.onLoad())
                    .beforeReload(activator.beforeReload())
                    .afterReload(activator.afterReload())
                    .mainModuleId(activator.mainModuleId())
                    .reload(module -> module.configure(configurator -> configurator.from(configuratorModule.orderedSources())))
                    .build();
            initialize(contextConfiguration, builder.build(), message -> logger.get().info(message));
            LAUNCHED_MESSAGES.forEach(message -> logger.get().info(message));
            return;
        }
        throw new InternalRuntimeException(MODULES_ALREADY_LAUNCHED);
    }

    public static void main(String[] args) {
        art()
                .kit(modules()
                        .logging(logging -> logging.colored(true)))
                .launch();
    }
}
