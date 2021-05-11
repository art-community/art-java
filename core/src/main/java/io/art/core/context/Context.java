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

package io.art.core.context;

import io.art.core.changes.*;
import io.art.core.collection.*;
import io.art.core.configuration.*;
import io.art.core.exception.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import io.art.core.property.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.changes.ChangesListener.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.constants.LoggingMessages.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.property.DisposableProperty.*;
import static io.art.core.property.LazyProperty.*;
import static java.lang.Runtime.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class Context {
    private static final Context DEFAULT_INSTANCE = new Context(ContextConfiguration.builder().build(), System.out::println);
    private static Context INSTANCE;
    private static final ChangesListener INITIALIZATION_LISTENER = changesListener();
    private static final ChangesListener DISPOSE_LISTENER = changesListener();
    private static final Map<String, LazyProperty<Module>> DEFAULTS_MODULES = map();
    private final Map<String, Module> modules = map();
    private final Map<String, ModuleDecorator<?>> configurators = map();
    private final ContextConfiguration configuration;
    private final Consumer<String> printer;
    private final Service service;

    private Context(ContextConfiguration configuration, Consumer<String> printer) {
        this.printer = printer;
        this.configuration = configuration;
        this.service = new Context.Service();
    }

    public static void initialize(ContextConfiguration configuration, ImmutableMap<ModuleFactory<?>, ModuleDecorator<?>> initializers, Consumer<String> printer) {
        if (nonNull(INSTANCE)) {
            throw new InternalRuntimeException(CONTEXT_ALREADY_INITIALIZED);
        }
        Context context = new Context(configuration, printer);
        context.load(initializers);
        getRuntime().addShutdownHook(new Thread(context::unload));
    }

    public static Context context() {
        if (isNull(INSTANCE)) {
            return DEFAULT_INSTANCE;
        }
        return INSTANCE;
    }

    public static <T extends ModuleConfiguration> void registerDefault(String id, ModuleFactory<ModuleConfigurationProvider<T>> module) {
        DEFAULTS_MODULES.put(id, lazy(module::get));
    }

    public <C extends ModuleConfiguration> StatelessModuleProxy<C> getStatelessModule(String moduleId) {
        DisposableProperty<Module> module = disposable(() -> getModule(moduleId)).initialize();
        INITIALIZATION_LISTENER.consume(module::dispose).consume(module::initialize);
        DISPOSE_LISTENER.consume(module::dispose);
        return new StatelessModuleProxy<>(cast(module));
    }

    public <C extends ModuleConfiguration, S extends ModuleState> StatefulModuleProxy<C, S> getStatefulModule(String moduleId) {
        DisposableProperty<Module> module = disposable(() -> getModule(moduleId)).initialize();
        INITIALIZATION_LISTENER.consume(module::dispose).consume(module::initialize);
        DISPOSE_LISTENER.consume(module::dispose);
        return new StatefulModuleProxy<>(cast(module));
    }

    public Set<String> getModuleNames() {
        return modules.keySet();
    }

    public boolean hasModule(String moduleId) {
        if (isEmpty(modules)) {
            return false;
        }
        return modules.containsKey(moduleId);
    }

    public ContextConfiguration configuration() {
        return configuration;
    }


    private Module getModule(String moduleId) {
        Module module = modules.get(moduleId);
        if (nonNull(module)) {
            return module;
        }
        module = cast(DEFAULTS_MODULES.get(moduleId).get());
        if (nonNull(module)) {
            return module;
        }
        throw new InternalRuntimeException(format(MODULE_WAS_NOT_FOUND, moduleId));
    }

    private void load(ImmutableMap<ModuleFactory<?>, ModuleDecorator<?>> modules) {
        INSTANCE = this;
        Set<String> messages = setOf(ART_BANNER);
        for (Map.Entry<ModuleFactory<?>, ModuleDecorator<?>> entry : modules.entrySet()) {
            Module module = entry.getKey().get();
            String moduleId = module.getId();
            ModuleDecorator<?> moduleDecorator = entry.getValue();
            messages.add(format(MODULE_LOADED_MESSAGE, moduleId));
            this.modules.put(moduleId, moduleDecorator.apply(cast(module)));
            this.configurators.put(moduleId, moduleDecorator);
        }
        INITIALIZATION_LISTENER.produce();
        messages.forEach(printer);
        for (Module module : this.modules.values()) {
            module.onLoad(service);
            ifNotEmpty(module.print(), printer);
        }
        apply(configuration.getOnLoad(), Runnable::run);
    }

    private void unload() {
        List<LazyProperty<Module>> defaultModules = linkedListOf(DEFAULTS_MODULES.values());
        reverse(defaultModules);
        for (LazyProperty<Module> module : defaultModules) {
            if (!module.initialized()) continue;
            Module defaultModule = module.get();
            printer.accept(format(MODULE_UNLOADED_MESSAGE, defaultModule.getId()));
            defaultModule.onUnload(service);
            DEFAULTS_MODULES.remove(defaultModule.getId());
        }
        List<Module> modules = linkedListOf(this.modules.values());
        reverse(modules);
        for (Module module : modules) {
            printer.accept(format(MODULE_UNLOADED_MESSAGE, module.getId()));
            module.onUnload(service);
            this.modules.remove(module.getId());
        }
        DISPOSE_LISTENER.produce();
        apply(configuration.getOnUnload(), Runnable::run);
        INSTANCE = null;
    }

    public class Service {
        public void reload() {
            for (Map.Entry<String, Module> entry : modules.entrySet()) {
                Module module = entry.getValue();
                printer.accept(format(MODULE_RELOADING_START_MESSAGE, module.getId()));
                module.beforeReload(service);
                configurators.get(entry.getKey()).apply(cast(module));
            }
            apply(configuration.getBeforeReload(), Runnable::run);

            for (Module module : modules.values()) {
                module.afterReload(service);
                printer.accept(format(MODULE_RELOADING_END_MESSAGE, module.getId()));
            }
            apply(configuration.getAfterReload(), Runnable::run);
        }
    }
}
