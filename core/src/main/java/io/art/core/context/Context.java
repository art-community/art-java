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

public class Context {
    private static Context INSTANCE;
    private volatile ContextConfiguration configuration = new DefaultContextConfiguration();
    private volatile ContextState state = READY;
    private volatile Long lastActionTimestamp = currentTimeMillis();
    private final Map<String, Module<?, ?, ?>> modules = concurrentHashMap();

    static {
        out.println(ART_BANNER);
    }

    private Context() {
        if (nonNull(INSTANCE)) {
            out.println(format(CONTEXT_CHANGED, configuration.getClass().getName()));
        }
        getRuntime().addShutdownHook(new Thread(this::unloadModules));
    }

    private Context(ContextConfiguration configuration) {
        if (nonNull(INSTANCE)) {
            out.println(format(CONTEXT_CHANGED, configuration.getClass().getName()));
        }
        this.configuration = configuration;
        getRuntime().addShutdownHook(new Thread(this::unloadModules));
    }

    public static Context initContext(ContextConfiguration contextConfiguration) {
        if (isNull(contextConfiguration))
            throw new ContextInitializationException(CONTEXT_INITIAL_CONFIGURATION_IS_NULL);
        return INSTANCE = new Context(contextConfiguration);
    }

    public static Context context() {
        return isNull(INSTANCE) ? INSTANCE = new Context() : INSTANCE;
    }

    public static ContextConfiguration contextConfiguration() {
        return context().configuration;
    }

    public <M extends Module<?, ?, ?>> M getModule(String moduleId) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        if (isNull(INSTANCE) || state != LOADING) {
            throw new ContextInitializationException(CONTEXT_NOT_READY);
        }
        return cast(modules.get(moduleId));
    }

    public Context loadModule(Module<?, ?, ?> module) {
        if (isNull(module)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ContextState currentState = state;
        state = LOADING;
        modules.put(module.getId(), module);
        out.println(format(MODULE_LOADED_MESSAGE, module.getId(), currentTimeMillis() - lastActionTimestamp, configuration.getClass().getName()));
        state = currentState;
        module.onLoad();
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    public Set<String> getModuleNames() {
        return modules.keySet();
    }

    public boolean hasModule(String moduleId) {
        lastActionTimestamp = currentTimeMillis();
        if (isEmpty(modules)) {
            return false;
        }
        return modules.containsKey(moduleId);
    }

    private void unloadModules() {
        state = UNLOADING;
        modules.values().forEach(module -> module.onUnload());
        modules.clear();
        state = EMPTY;
    }
}
