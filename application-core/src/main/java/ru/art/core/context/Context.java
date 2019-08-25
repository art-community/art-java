/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.core.context;

import ru.art.core.configuration.*;
import ru.art.core.configuration.ContextInitialConfiguration.*;
import ru.art.core.configurator.*;
import ru.art.core.exception.*;
import ru.art.core.module.*;
import ru.art.core.provider.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.function.*;

import static java.lang.Runtime.*;
import static java.lang.System.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.ExceptionMessages.*;
import static ru.art.core.constants.LoggingMessages.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.*;

public class Context {
    private static final ReentrantLock lock = new ReentrantLock();
    private static volatile Context DEFAULT_INSTANCE;
    private static volatile Context INSTANCE;
    private static volatile Consumer<Context> OUTSIDE_DEFAULT_CONTEXT_ACTION;
    private Map<String, ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState>> modules = mapOf();
    private ContextInitialConfiguration initialConfiguration = new ContextInitialDefaultConfiguration();
    private Long lastActionTimestamp = currentTimeMillis();

    private Context() {
        if (initialConfiguration.isUnloadModulesOnShutdown()) {
            getRuntime().addShutdownHook(new Thread(this::unloadModules));
        }
    }

    private Context(ContextInitialConfiguration initialConfiguration) {
        this.initialConfiguration = initialConfiguration;
        if (initialConfiguration.isUnloadModulesOnShutdown()) {
            getRuntime().addShutdownHook(new Thread(this::unloadModules));
        }
    }

    private Context(ContextInitialConfiguration contextInitialConfiguration, Map<String, ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState>> modules) {
        this.initialConfiguration = contextInitialConfiguration;
        this.modules = modules;
        if (initialConfiguration.isUnloadModulesOnShutdown()) {
            getRuntime().addShutdownHook(new Thread(this::unloadModules));
        }
    }

    public static Context initContext(ContextInitialConfiguration contextInitialConfiguration) {
        if (isNull(contextInitialConfiguration))
            throw new ContextInitializationException(CONTEXT_INITIAL_CONFIGURATION_IS_NULL);
        ReentrantLock lock = Context.lock;
        lock.lock();
        INSTANCE = new Context(contextInitialConfiguration);
        out.println(ART_BANNER);
        lock.unlock();
        return INSTANCE;
    }

    public static void recreateContext(ContextInitialConfiguration contextInitialConfiguration) {
        if (isNull(contextInitialConfiguration)) {
            throw new ContextInitializationException(CONTEXT_INITIAL_CONFIGURATION_IS_NULL);
        }
        ReentrantLock lock = Context.lock;
        lock.lock();
        long oldContextLastActionTimestamp = INSTANCE.lastActionTimestamp;
        final Map<String, ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState>> modules = INSTANCE.modules;
        INSTANCE = new Context(contextInitialConfiguration, modules);
        INSTANCE.refreshAndReloadModules();
        out.println(format(CONTEXT_RELOADED_MESSAGE, currentTimeMillis() - oldContextLastActionTimestamp));
        INSTANCE.lastActionTimestamp = currentTimeMillis();
        lock.unlock();
    }

    public static Context context() {
        Context localInstance = INSTANCE;
        if (isNull(localInstance)) {
            ReentrantLock lock = Context.lock;
            lock.lock();
            localInstance = INSTANCE;
            if (isNull(localInstance)) {
                INSTANCE = new Context();
            }
            lock.unlock();
        }
        return INSTANCE;
    }

    public static ContextInitialConfiguration contextConfiguration() {
        return context().initialConfiguration;
    }

    public static void withDefaultContext(Consumer<Context> action) {
        Context context = defaultContext();
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = context;
        lock.unlock();
        action.accept(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
        if (nonNull(OUTSIDE_DEFAULT_CONTEXT_ACTION)) {
            OUTSIDE_DEFAULT_CONTEXT_ACTION.accept(INSTANCE);
        }
    }

    public static <T> T withDefaultContext(Function<Context, T> action) {
        Context context = defaultContext();
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = context;
        lock.unlock();
        T result = action.apply(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
        if (nonNull(OUTSIDE_DEFAULT_CONTEXT_ACTION)) {
            OUTSIDE_DEFAULT_CONTEXT_ACTION.accept(INSTANCE);
        }
        return result;
    }

    public <C extends ModuleConfiguration, S extends ModuleState> C getModule(String moduleId, Module<C, S> toLoadIfNotExists) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        PreconfiguredModuleProvider preconfiguredModulesProvider;
        if (nonNull(moduleContainer)) {
            return cast(moduleContainer.getConfiguration());
        }
        C configuration;
        if (nonNull(preconfiguredModulesProvider = contextConfiguration().getPreconfiguredModulesProvider())) {
            configuration = cast(preconfiguredModulesProvider.getModuleConfiguration(moduleId)
                    .orElse(toLoadIfNotExists.getDefaultConfiguration()));
            loadModule(toLoadIfNotExists, configuration);
            return cast(configuration);
        }
        loadModule(toLoadIfNotExists, (configuration = toLoadIfNotExists.getDefaultConfiguration()));
        return cast(configuration);
    }

    public <C extends ModuleConfiguration, S extends ModuleState> S getModuleState(String moduleId, Module<C, S> toLoadIfNotExists) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        PreconfiguredModuleProvider preconfiguredModulesProvider;
        if (nonNull(moduleContainer)) {
            return cast(moduleContainer.getModule().getState());
        }
        if (nonNull(preconfiguredModulesProvider = contextConfiguration().getPreconfiguredModulesProvider())) {
            C configuration = cast(preconfiguredModulesProvider.getModuleConfiguration(moduleId)
                    .orElse(toLoadIfNotExists.getDefaultConfiguration()));
            loadModule(toLoadIfNotExists, configuration);
            return cast(toLoadIfNotExists.getState());
        }
        loadModule(toLoadIfNotExists, toLoadIfNotExists.getDefaultConfiguration());
        return cast(toLoadIfNotExists.getState());
    }

    public <C extends ModuleConfiguration, S extends ModuleState> C getModule(String moduleId, Supplier<Module<C, S>> toLoadIfNotExists) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        PreconfiguredModuleProvider preconfiguredModulesProvider;
        if (nonNull(moduleContainer)) {
            return cast(moduleContainer.getConfiguration());
        }
        C configuration;
        Module<C, S> module = toLoadIfNotExists.get();
        if (nonNull(preconfiguredModulesProvider = contextConfiguration().getPreconfiguredModulesProvider())) {
            configuration = cast(preconfiguredModulesProvider.getModuleConfiguration(moduleId).orElse(module.getDefaultConfiguration()));
            loadModule(module, configuration);
            return cast(configuration);
        }
        loadModule(module, (configuration = module.getDefaultConfiguration()));
        return cast(configuration);
    }

    public <C extends ModuleConfiguration, S extends ModuleState> S getModuleState(String moduleId, Supplier<Module<C, S>> toLoadIfNotExists) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        PreconfiguredModuleProvider preconfiguredModulesProvider;
        if (nonNull(moduleContainer)) {
            return cast(moduleContainer.getModule().getState());
        }
        Module<C, S> module = toLoadIfNotExists.get();
        if (nonNull(preconfiguredModulesProvider = contextConfiguration().getPreconfiguredModulesProvider())) {
            C configuration = cast(preconfiguredModulesProvider.getModuleConfiguration(moduleId)
                    .orElse(module.getDefaultConfiguration()));
            loadModule(module, configuration);
            return cast(module.getState());
        }
        loadModule(module, module.getDefaultConfiguration());
        return cast(module.getState());
    }

    public <C extends ModuleConfiguration, S extends ModuleState> Context loadModule(Module<C, S> module) {
        if (isNull(module)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        modules.put(module.getId(), new ModuleContainer<>(module, cast(module.getDefaultConfiguration())));
        out.println(format(MODULE_LOADED_MESSAGE, module.getId(), currentTimeMillis() - lastActionTimestamp));
        module.onLoad();
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    public <C extends ModuleConfiguration, S extends ModuleState> Context loadModule(Module<C, S> module, ModuleConfigurator<C, S> moduleConfigurator) {
        if (isNull(module)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        modules.put(module.getId(), new ModuleContainer<>(module, cast(moduleConfigurator.configure(module))));
        out.println(format(MODULE_LOADED_MESSAGE, module.getId(), currentTimeMillis() - lastActionTimestamp));
        module.onLoad();
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    public <C extends ModuleConfiguration, S extends ModuleState> Context loadModule(Module<C, S> module, C customModuleConfiguration) {
        if (isNull(module)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        modules.put(module.getId(), new ModuleContainer<>(module, cast(nonNull(customModuleConfiguration) ? customModuleConfiguration : module.getDefaultConfiguration())));
        module.onLoad();
        out.println(format(MODULE_LOADED_MESSAGE, module.getId(), currentTimeMillis() - lastActionTimestamp));
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    @SuppressWarnings("Duplicates")
    public <C extends ModuleConfiguration> Context overrideModule(String moduleId, C customModuleConfiguration) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        if (isNull(customModuleConfiguration)) {
            throw new ContextInitializationException(CUSTOM_MODULE_CONFIGURATION_IS_NULL);
        }
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (isNull(moduleContainer)) return this;
        modules.put(moduleId, moduleContainer.overrideConfiguration(cast(customModuleConfiguration)));
        out.println(format(MODULE_OVERRIDDEN_MESSAGE, moduleId, currentTimeMillis() - lastActionTimestamp));
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    @SuppressWarnings("Duplicates")
    public Context reloadModule(String moduleId) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (isNull(moduleContainer)) return this;
        moduleContainer.reloadModule();
        out.println(format(MODULE_RELOADED_MESSAGE, moduleId, currentTimeMillis() - lastActionTimestamp));
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    @SuppressWarnings("Duplicates")
    public Context refreshModule(String moduleId) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (isNull(moduleContainer)) return this;
        moduleContainer.refreshConfiguration();
        out.println(format(MODULE_REFRESHED_MESSAGE, moduleId, currentTimeMillis() - lastActionTimestamp));
        lastActionTimestamp = currentTimeMillis();
        return this;
    }

    public Context refreshAndReloadModule(String moduleId) {
        if (isNull(moduleId)) throw new ContextInitializationException(MODULE_ID_IS_NULL);
        ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState> moduleContainer = modules.get(moduleId);
        if (isNull(moduleContainer)) return this;
        moduleContainer.reloadModule();
        moduleContainer.refreshConfiguration();
        out.println(format(MODULE_REFRESHED_AND_RELOADED_MESSAGE, moduleId, currentTimeMillis() - lastActionTimestamp));
        lastActionTimestamp = currentTimeMillis();
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

    public boolean initialized() {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context instance = INSTANCE;
        lock.unlock();
        return instance != null;
    }

    private void unloadModules() {
        modules.values().forEach(module -> module.getModule().onUnload());
    }

    public static Context initDefaultContext(ContextInitialConfiguration contextInitialConfiguration) {
        if (isNull(contextInitialConfiguration))
            throw new ContextInitializationException(CONTEXT_INITIAL_CONFIGURATION_IS_NULL);
        ReentrantLock lock = Context.lock;
        lock.lock();
        DEFAULT_INSTANCE = new Context(contextInitialConfiguration);
        lock.unlock();
        return DEFAULT_INSTANCE;
    }

    public static Context defaultContext() {
        Context localInstance = DEFAULT_INSTANCE;
        if (isNull(localInstance)) {
            ReentrantLock lock = Context.lock;
            lock.lock();
            localInstance = DEFAULT_INSTANCE;
            if (isNull(localInstance)) {
                DEFAULT_INSTANCE = new Context();
            }
            lock.unlock();
        }
        return DEFAULT_INSTANCE;
    }

    public static boolean insideDefaultContext() {
        if (INSTANCE == null) return false;
        if (DEFAULT_INSTANCE == null) return false;
        return INSTANCE == DEFAULT_INSTANCE;
    }

    public static <C extends ModuleConfiguration> C constructInsideDefaultContext(Supplier<C> constructor) {
        Context context1 = defaultContext();
        ReentrantLock lock1 = lock;
        lock1.lock();
        Context currentContext = INSTANCE;
        INSTANCE = context1;
        lock1.unlock();
        C result = ((Function<Context, C>) context -> constructor.get()).apply(INSTANCE);
        lock1.lock();
        INSTANCE = currentContext;
        lock1.unlock();
        if (DEFAULT_INSTANCE == context1 && nonNull(OUTSIDE_DEFAULT_CONTEXT_ACTION)) {
            OUTSIDE_DEFAULT_CONTEXT_ACTION.accept(INSTANCE);
        }
        return result;
    }

    public static <C extends ModuleConfiguration> C constructInsideDefaultContext(ContextInitialConfiguration configuration, Supplier<C> constructor) {
        initDefaultContext(configuration);
        Context context1 = defaultContext();
        ReentrantLock lock1 = lock;
        lock1.lock();
        Context currentContext = INSTANCE;
        INSTANCE = context1;
        lock1.unlock();
        C result = ((Function<Context, C>) context -> constructor.get()).apply(INSTANCE);
        lock1.lock();
        INSTANCE = currentContext;
        lock1.unlock();
        if (DEFAULT_INSTANCE == context1 && nonNull(OUTSIDE_DEFAULT_CONTEXT_ACTION)) {
            OUTSIDE_DEFAULT_CONTEXT_ACTION.accept(INSTANCE);
        }
        return result;
    }

    public static void outsideDefaultContext(Consumer<Context> action) {
        OUTSIDE_DEFAULT_CONTEXT_ACTION = action;
    }
}