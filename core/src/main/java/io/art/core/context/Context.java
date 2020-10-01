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

import io.art.core.configuration.*;
import io.art.core.configuration.ContextConfiguration.*;
import io.art.core.constants.*;
import io.art.core.exception.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.ContextState.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.constants.LoggingMessages.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.CollectionsFactory.*;
import static java.lang.Runtime.*;
import static java.lang.System.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class Context {
    private static Context INSTANCE;
    private ContextConfiguration configuration = new DefaultContextConfiguration();
    private ContextState state = READY;
    private final Map<String, Module> modules = mapOf();
    private final static List<String> messages = dynamicArrayOf();

    static {
        messages.add(ART_BANNER);
    }

    private Context() {
        if (nonNull(INSTANCE)) {
            return;
        }
        getRuntime().addShutdownHook(new Thread(this::unloadModules));
    }

    private Context(ContextConfiguration configuration) {
        if (nonNull(INSTANCE)) {
            return;
        }
        this.configuration = configuration;
        getRuntime().addShutdownHook(new Thread(this::unloadModules));
    }

    public static Context initContext(ContextConfiguration contextConfiguration) {
        return INSTANCE = new Context(contextConfiguration);
    }

    public static Context context() {
        return isNull(INSTANCE) ? INSTANCE = new Context() : INSTANCE;
    }

    public static ContextConfiguration contextConfiguration() {
        return context().configuration;
    }

    public <C extends ModuleConfiguration> StatelessModuleProxy<C> getStatelessModule(String moduleId) {
        if (isNull(INSTANCE) || state != READY) {
            throw new ContextInitializationException(CONTEXT_NOT_READY);
        }
        Module module = modules.get(moduleId);
        if (isNull(module)) throw new InternalRuntimeException(format(MODULE_WAS_NOT_FOUND, moduleId));
        return new StatelessModuleProxy<>(cast(module));
    }

    public <C extends ModuleConfiguration, S extends ModuleState> StatefulModuleProxy<C, S> getStatefulModule(String moduleId) {
        if (isNull(INSTANCE) || state != READY) {
            throw new ContextInitializationException(CONTEXT_NOT_READY);
        }
        Module module = modules.get(moduleId);
        if (isNull(module)) throw new InternalRuntimeException(format(MODULE_WAS_NOT_FOUND, moduleId));
        return new StatefulModuleProxy<>(cast(module));
    }

    public Context loadModule(Module module) {
        long timestamp = currentTimeMillis();
        ContextState currentState = state;
        state = LOADING;
        modules.put(module.getId(), module);
        state = currentState;
        module.onLoad();
        messages.add(format(MODULE_LOADED_MESSAGE, module.getId(), currentTimeMillis() - timestamp, module.getClass()));
        module.afterLoad();
        return this;
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

    public void printMessages(Consumer<String> printer) {
        messages.forEach(printer);
    }

    private void unloadModules() {
        state = UNLOADING;
        modules.values().forEach(Module::onUnload);
        modules.clear();
        state = EMPTY;
    }
}
