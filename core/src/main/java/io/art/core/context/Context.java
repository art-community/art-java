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

import com.google.common.collect.*;
import io.art.core.checker.*;
import io.art.core.configuration.*;
import io.art.core.exception.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.constants.LoggingMessages.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.CollectionsFactory.*;
import static java.lang.Runtime.*;
import static java.lang.System.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class Context {
    private static Context INSTANCE;
    private final Map<String, Module> modules = mapOf();
    private final ContextConfiguration configuration;
    private final Consumer<String> printer;

    private Context(ContextConfiguration configuration, ImmutableList<Module> modules, Consumer<String> printer) {
        this.printer = printer;
        this.configuration = configuration;
        load(modules);
        getRuntime().addShutdownHook(new Thread(this::unload));
    }

    public static void initialize(ContextConfiguration configuration, ImmutableList<Module> modules, Consumer<String> printer) {
        if (nonNull(INSTANCE)) {
            throw new InternalRuntimeException(CONTEXT_ALREADY_INITIALIZED);
        }
        new Context(configuration, modules, printer);
    }

    public static Context context() {
        if (isNull(INSTANCE)) {
            throw new InternalRuntimeException(CONTEXT_NOT_INITIALIZED);
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


    private void load(ImmutableList<Module> modules) {
        INSTANCE = this;
        Set<String> messages = setOf(ART_BANNER);
        for (Module module : modules) {
            module.beforeLoad();
            messages.add(format(MODULE_LOADED_MESSAGE, module.getId()));
            module.afterLoad();
            this.modules.put(module.getId(), module);
        }
        this.modules.values()
                .stream()
                .map(Module::print)
                .filter(EmptinessChecker::isNotEmpty)
                .reduce((current, next) -> current + NEW_LINE + next)
                .filter(EmptinessChecker::isNotEmpty)
                .ifPresent(messages::add);
        messages.forEach(printer);
    }

    private void unload() {
        List<Module> modules = linkedListOf(this.modules.values());
        reverse(modules);
        for (Module module : modules) {
            long timestamp = currentTimeMillis();
            module.beforeUnload();
            printer.accept(format(MODULE_UNLOADED_MESSAGE, module.getId(), currentTimeMillis() - timestamp, module.getClass()));
            module.afterUnload();
            this.modules.remove(module.getId());
        }
        INSTANCE = null;
    }
}
