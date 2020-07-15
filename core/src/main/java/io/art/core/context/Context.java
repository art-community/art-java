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
import io.art.core.provider.*;
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
import java.util.concurrent.locks.*;
import java.util.function.*;

public class Context {
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static volatile Context INSTANCE;
    private volatile ContextConfiguration configuration = new DefaultContextConfiguration();
    private volatile ContextState state = READY;
    private volatile Long lastActionTimestamp = currentTimeMillis();
    private Map<String, ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState>> modules = concurrentHashMap();

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

    private Context(ContextConfiguration contextConfiguration, Map<String, ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState>> modules) {
        if (nonNull(INSTANCE)) {
            out.println(format(CONTEXT_CHANGED, contextConfiguration.getClass().getName()));
        }
        this.configuration = contextConfiguration;
        this.modules = modules;
        getRuntime().addShutdownHook(new Thread(this::unloadModules));
    }

    public static Context initContext(ContextConfiguration contextConfiguration) {
        if (isNull(contextConfiguration))
            throw new ContextInitializationException(CONTEXT_INITIAL_CONFIGURATION_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        INSTANCE = new Context(contextConfiguration);
        lock.unlock();
        return INSTANCE;
    }

    public static Context context() {
        Context localInstance = INSTANCE;
        if (isNull(localInstance)) {
            ReentrantLock lock = Context.LOCK;
            lock.lock();
            localInstance = INSTANCE;
            if (isNull(localInstance)) {
                INSTANCE = new Context();
            }
            lock.unlock();
        }
        return INSTANCE;
    }

    public static ContextConfiguration contextConfiguration() {
        return context().configuration;
    }

    public <C extends ModuleConfiguration, S extends ModuleState> C getModule(String moduleId, Module<C, S> toLoadIfNotExists) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        if (isNull(INSTANCE) || state != READY) {
            return toLoadIfNotExists.getConfiguration();
        }
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (nonNull(moduleContainer)) {
            lock.unlock();
            return cast(moduleContainer.getConfiguration());
        }
        C configuration;
        Optional<PreconfiguredModuleProvider> provider = contextConfiguration().getPreconfiguredModulesProvider();
        if (provider.isPresent()) {
            lock.unlock();
            return loadModule(toLoadIfNotExists, provider.get());
        }
        lock.unlock();
        loadModule(toLoadIfNotExists, (configuration = toLoadIfNotExists.getConfiguration()));
        return cast(configuration);
    }

    public <C extends ModuleConfiguration, S extends ModuleState> S getModuleState(String moduleId, Module<C, S> toLoadIfNotExists) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        if (isNull(INSTANCE) || state == LOADING_MODULES) {
            lock.unlock();
            return toLoadIfNotExists.getState();
        }
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (nonNull(moduleContainer)) {
            lock.unlock();
            return cast(moduleContainer.getModule().getState());
        }
        Optional<PreconfiguredModuleProvider> provider = contextConfiguration().getPreconfiguredModulesProvider();
        if (provider.isPresent()) {
            lock.unlock();
            loadModule(toLoadIfNotExists, provider.get());
            return cast(toLoadIfNotExists.getState());
        }
        lock.unlock();
        loadModule(toLoadIfNotExists, toLoadIfNotExists.getConfiguration());
        return cast(toLoadIfNotExists.getState());
    }

    public <C extends ModuleConfiguration, S extends ModuleState> C getModule(String moduleId, Supplier<Module<C, S>> toLoadIfNotExists) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        if (isNull(INSTANCE) || state != READY) {
            lock.unlock();
            return toLoadIfNotExists.get().getConfiguration();
        }
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (nonNull(moduleContainer)) {
            lock.unlock();
            return cast(moduleContainer.getConfiguration());
        }
        C configuration;
        Module<C, S> module = toLoadIfNotExists.get();
        Optional<PreconfiguredModuleProvider> provider = contextConfiguration().getPreconfiguredModulesProvider();
        if (provider.isPresent()) {
            lock.unlock();
            return loadModule(module, provider.get());
        }
        lock.unlock();
        loadModule(module, (configuration = module.getConfiguration()));
        return cast(configuration);
    }

    public <C extends ModuleConfiguration, S extends ModuleState> S getModuleState(String moduleId, Supplier<Module<C, S>> toLoadIfNotExists) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        if (isNull(INSTANCE) || state == LOADING_MODULES) {
            lock.unlock();
            return toLoadIfNotExists.get().getState();
        }
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (nonNull(moduleContainer)) {
            lock.unlock();
            return cast(moduleContainer.getModule().getState());
        }
        Module<C, S> module = toLoadIfNotExists.get();
        Optional<PreconfiguredModuleProvider> provider = contextConfiguration().getPreconfiguredModulesProvider();
        if (provider.isPresent()) {
            lock.unlock();
            loadModule(module, provider.get());
            return module.getState();
        }
        lock.unlock();
        loadModule(module, module.getConfiguration());
        return cast(module.getState());
    }

    public <C extends ModuleConfiguration, S extends ModuleState> Context loadModule(Module<C, S> module) {
        if (isNull(module)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        ContextState currentState = state;
        state = LOADING_MODULES;
        C configuration = module.getConfiguration();
        modules.put(module.getId(), new ModuleContainer<>(module, cast(configuration)));
        out.println(format(MODULE_LOADED_MESSAGE, module.getId(), currentTimeMillis() - lastActionTimestamp, configuration.getClass().getName()));
        state = currentState;
        lock.unlock();
        module.onLoad();
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    public <C extends ModuleConfiguration, S extends ModuleState> Context loadModule(Module<C, S> module, ModuleConfigurator<C, S> moduleConfigurator) {
        if (isNull(module)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        ContextState currentState = state;
        state = LOADING_MODULES;
        C configuration = cast(moduleConfigurator.configure(module));
        modules.put(module.getId(), new ModuleContainer<>(module, configuration));
        out.println(format(MODULE_LOADED_MESSAGE, module.getId(), currentTimeMillis() - lastActionTimestamp, configuration.getClass().getName()));
        state = currentState;
        lock.unlock();
        module.onLoad();
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    public <C extends ModuleConfiguration, S extends ModuleState> Context loadModule(Module<C, S> module, C customModuleConfiguration) {
        if (isNull(module)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        ContextState currentState = state;
        state = LOADING_MODULES;
        C configuration = cast(nonNull(customModuleConfiguration) ? customModuleConfiguration : module.getConfiguration());
        modules.put(module.getId(), new ModuleContainer<>(module, configuration));
        state = currentState;
        lock.unlock();
        module.onLoad();
        out.println(format(MODULE_LOADED_MESSAGE, module.getId(), currentTimeMillis() - lastActionTimestamp, configuration.getClass().getName()));
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    private <C extends ModuleConfiguration, S extends ModuleState> C loadModule(Module<C, S> module, PreconfiguredModuleProvider preconfiguredModulesProvider) {
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        ContextState currentState = state;
        state = LOADING_MODULES;
        C configuration = cast(preconfiguredModulesProvider.getModuleConfiguration(module.getId()).orElse(module.getConfiguration()));
        state = currentState;
        lock.unlock();
        loadModule(module, configuration);
        return cast(configuration);
    }

    @SuppressWarnings("Duplicates")
    public Context reloadModule(String moduleId) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (isNull(moduleContainer)) {
            lock.unlock();
            return this;
        }
        ContextState currentState = state;
        state = RELOADING_MODULES;
        moduleContainer.reloadModule();
        out.println(format(MODULE_RELOADED_MESSAGE, moduleId, currentTimeMillis() - lastActionTimestamp));
        lastActionTimestamp = currentTimeMillis();
        state = currentState;
        lock.unlock();
        return this;
    }

    @SuppressWarnings("Duplicates")
    public Context refreshModule(String moduleId) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (isNull(moduleContainer)) {
            lock.unlock();
            return this;
        }
        ContextState currentState = state;
        state = REFRESHING_MODULES;
        moduleContainer.refreshConfiguration();
        out.println(format(MODULE_REFRESHED_MESSAGE, moduleId, currentTimeMillis() - lastActionTimestamp));
        lastActionTimestamp = currentTimeMillis();
        state = currentState;
        lock.unlock();
        return this;
    }

    public Context refreshAndReloadModule(String moduleId) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        ContextState currentState = state;
        state = REFRESHING_AND_RELOADING_MODULES;
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (isNull(moduleContainer)) {
            lock.unlock();
            return this;
        }
        moduleContainer.reloadModule();
        moduleContainer.refreshConfiguration();
        out.println(format(MODULE_REFRESHED_AND_RELOADED_MESSAGE, moduleId, currentTimeMillis() - lastActionTimestamp));
        lastActionTimestamp = currentTimeMillis();
        state = currentState;
        lock.unlock();
        return this;
    }

    public Context refreshAndReloadModules() {
        modules.keySet().forEach(this::refreshAndReloadModule);
        return this;
    }

    public Context reloadModules() {
        modules.keySet().forEach(this::reloadModule);
        return this;
    }

    public Context refreshModules() {
        modules.keySet().forEach(this::refreshModule);
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

    public static boolean contextIsNotReady() {
        ReentrantLock lock = Context.LOCK;
        lock.lock();
        Context instance = INSTANCE;
        lock.unlock();
        return isNull(instance) || instance.state != READY;
    }

    private void unloadModules() {
        modules.values().forEach(module -> module.getModule().onUnload());
        modules.clear();
    }
}
