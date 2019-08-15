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

import ru.art.core.configuration.ContextInitialConfiguration;
import ru.art.core.configuration.ContextInitialConfiguration.ContextInitialDefaultConfiguration;
import ru.art.core.configurator.ModuleConfigurator;
import ru.art.core.exception.ContextInitializationException;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleConfiguration;
import ru.art.core.module.ModuleContainer;
import ru.art.core.module.ModuleState;
import ru.art.core.provider.PreconfiguredModuleProvider;
import static java.lang.Runtime.getRuntime;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ExceptionMessages.*;
import static ru.art.core.constants.LoggingMessages.*;
import static ru.art.core.constants.StringConstants.ADK_BANNER;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Context {
    private static final ReentrantLock lock = new ReentrantLock();
    private static volatile Context INSTANCE;
    private Map<String, ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState>> modules = mapOf();
    private ContextInitialConfiguration initialConfiguration = new ContextInitialDefaultConfiguration();
    private Long lastActionTimestamp = currentTimeMillis();

    private Context() {
        if (initialConfiguration.isUnloadModulesOnShutdown()) {
            getRuntime().addShutdownHook(new Thread(this::unloadModules));
        }
    }

    public Context(ContextInitialConfiguration initialConfiguration) {
        this.initialConfiguration = initialConfiguration;
        if (initialConfiguration.isUnloadModulesOnShutdown()) {
            getRuntime().addShutdownHook(new Thread(this::unloadModules));
        }
    }

    public Context(ContextInitialConfiguration contextInitialConfiguration, Map<String, ModuleContainer<? extends ModuleConfiguration, ? extends ModuleState>> modules) {
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
        out.println(ADK_BANNER);
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

    public static void withContext(Context context, Consumer<Context> action) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = context;
        lock.unlock();
        action.accept(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
    }

    public static void withContext(ContextInitialConfiguration contextInitialConfiguration, Consumer<Context> action) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context(contextInitialConfiguration);
        action.accept(INSTANCE);
        INSTANCE = currentContext;
        lock.unlock();
    }

    public static void withModules(Consumer<Context> action, Module<?, ?>... modules) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context();
        for (Module<?, ?> module : modules) {
            INSTANCE.loadModule(module);
        }
        lock.unlock();
        action.accept(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
    }

    public static void withModules(ContextInitialConfiguration contextInitialConfiguration, Consumer<Context> action, Module<?, ?>... modules) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context(contextInitialConfiguration);
        for (Module<?, ?> module : modules) {
            INSTANCE.loadModule(module);
        }
        lock.unlock();
        action.accept(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
    }

    @SafeVarargs
    public static void withModules(Consumer<Context> action, Supplier<Module<?, ?>>... modules) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context();
        for (Supplier<Module<?, ?>> module : modules) {
            INSTANCE.loadModule(module.get());
        }
        lock.unlock();
        action.accept(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
    }

    @SafeVarargs
    public static void withModules(ContextInitialConfiguration contextInitialConfiguration, Consumer<Context> action, Supplier<Module<?, ?>>... modules) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context(contextInitialConfiguration);
        for (Supplier<Module<?, ?>> module : modules) {
            INSTANCE.loadModule(module.get());
        }
        lock.unlock();
        action.accept(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
    }

    public static <T> T withContext(Context context, Function<Context, T> action) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = context;
        lock.unlock();
        T result = action.apply(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
        return result;
    }

    public static <T> T withContext(ContextInitialConfiguration contextInitialConfiguration, Function<Context, T> action) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context(contextInitialConfiguration);
        lock.unlock();
        T result = action.apply(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
        return result;
    }

    public static <T> T withModules(Function<Context, T> action, Module<?, ?>... modules) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context();
        for (Module<?, ?> module : modules) {
            INSTANCE.loadModule(module);
        }
        lock.unlock();
        T result = action.apply(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
        return result;
    }

    public static <T> T withModules(ContextInitialConfiguration contextInitialConfiguration, Function<Context, T> action, Module<?, ?>... modules) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context(contextInitialConfiguration);
        for (Module<?, ?> module : modules) {
            INSTANCE.loadModule(module);
        }
        lock.unlock();
        T result = action.apply(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
        return result;
    }

    @SafeVarargs
    public static <T> T withModules(Function<Context, T> action, Supplier<Module<?, ?>>... modules) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context();
        for (Supplier<Module<?, ?>> module : modules) {
            INSTANCE.loadModule(module.get());
        }
        lock.unlock();
        T result = action.apply(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
        return result;
    }

    @SafeVarargs
    public static <T> T withModules(ContextInitialConfiguration contextInitialConfiguration, Function<Context, T> action, Supplier<Module<?, ?>>... modules) {
        ReentrantLock lock = Context.lock;
        lock.lock();
        Context currentContext = INSTANCE;
        INSTANCE = new Context(contextInitialConfiguration);
        for (Supplier<Module<?, ?>> module : modules) {
            INSTANCE.loadModule(module.get());
        }
        lock.unlock();
        T result = action.apply(INSTANCE);
        lock.lock();
        INSTANCE = currentContext;
        lock.unlock();
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

}