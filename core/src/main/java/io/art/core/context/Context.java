/*
 * ART
 *
 * Copyright 2020 ART
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

import io.art.core.collection.*;
import io.art.core.configuration.*;
import io.art.core.configuration.ContextConfiguration.*;
import io.art.core.exception.*;
import io.art.core.module.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.ContextConstants.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.constants.LoggingMessages.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static java.lang.Runtime.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class Context {
    private static final Context DEFAULT_INSTANCE = new Context(new DefaultContextConfiguration(DEFAULT_MAIN_MODULE_ID), System.out::println);
    private static Context INSTANCE;
    private final Map<String, Module> modules = map();
    private final Map<String, ModuleDecorator> configurators = map();
    private final ContextConfiguration configuration;
    private final Consumer<String> printer;

    private Context(ContextConfiguration configuration, Consumer<String> printer) {
        this.printer = printer;
        this.configuration = configuration;
    }

    public static void initialize(ContextConfiguration configuration, ImmutableMap<ModuleFactory, ModuleDecorator> initializers, Consumer<String> printer) {
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


    public <C extends ModuleConfiguration> StatelessModuleProxy<C> getStatelessModule(String moduleId) {
        Module module = modules.get(moduleId);
        if (isNull(module)) throw new InternalRuntimeException(format(MODULE_WAS_NOT_FOUND, moduleId));
        return new StatelessModuleProxy<>(cast(module));
    }

    public <C extends ModuleConfiguration, S extends ModuleState> StatefulModuleProxy<C, S> getStatefulModule(String moduleId) {
        Module module = modules.get(moduleId);
        if (isNull(module)) throw new InternalRuntimeException(format(MODULE_WAS_NOT_FOUND, moduleId));
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


    public void reload() {
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            Module module = entry.getValue();
            printer.accept(format(MODULE_RELOADING_START_MESSAGE, module.getId()));
            module.beforeReload();
            configurators.get(entry.getKey()).apply(module);
        }

        for (Module module : modules.values()) {
            module.afterReload();
            printer.accept(format(MODULE_RELOADING_END_MESSAGE, module.getId()));
        }
    }

    private void load(ImmutableMap<ModuleFactory, ModuleDecorator> modules) {
        INSTANCE = this;
        Set<String> messages = setOf(ART_BANNER);
        for (Map.Entry<ModuleFactory, ModuleDecorator> entry : modules.entrySet()) {
            Module module = entry.getKey().get();
            messages.add(format(MODULE_LOADED_MESSAGE, module.getId()));
            this.modules.put(module.getId(), entry.getValue().apply(module));
            this.configurators.put(module.getId(), entry.getValue());
        }
        messages.forEach(printer);
        for (Module module : this.modules.values()) {
            module.onLoad();
            ifNotEmpty(module.print(), printer);
        }
    }

    private void unload() {
        List<Module> modules = linkedListOf(this.modules.values());
        reverse(modules);
        for (Module module : modules) {
            printer.accept(format(MODULE_UNLOADED_MESSAGE, module.getId()));
            module.onUnload();
            this.modules.remove(module.getId());
        }

        INSTANCE = null;
    }
}
