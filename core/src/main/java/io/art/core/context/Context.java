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

import io.art.core.collection.*;
import io.art.core.configuration.*;
import io.art.core.exception.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.ContextConstants.*;
import static io.art.core.constants.ExceptionMessages.*;
import static io.art.core.constants.LoggingMessages.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.lang.Runtime.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class Context {
    private static volatile Context INSTANCE;
    private final Thread terminatorHookThread = newDaemon(TERMINATOR_THREAD, () -> ignoreException(INSTANCE::terminationHook));
    private final Thread terminatorThread = newDaemon(TERMINATOR_THREAD, this::awaitTermination);
    private final AtomicBoolean terminationScheduled = new AtomicBoolean(false);
    private final AtomicBoolean onShutdown = new AtomicBoolean(false);
    private final CountDownLatch terminatorSignal = new CountDownLatch(1);
    private final Map<String, Module<?, ?>> modules = map();
    private final ContextConfiguration configuration;
    private final Service service;

    private Context(ContextConfiguration configuration) {
        this.configuration = configuration;
        this.service = new Context.Service();
    }

    public static void prepareInitialization(ContextConfiguration configuration) {
        if (nonNull(INSTANCE)) {
            throw new InternalRuntimeException(CONTEXT_ALREADY_INITIALIZED);
        }
        INSTANCE = new Context(configuration);
    }

    public static void processInitialization(ImmutableSet<Module<?, ?>> modules) {
        if (isNull(INSTANCE)) {
            throw new InternalRuntimeException(CONTEXT_NOT_INITIALIZED);
        }
        if (isNotEmpty(INSTANCE.modules)) {
            throw new InternalRuntimeException(CONTEXT_ALREADY_INITIALIZED);
        }
        INSTANCE.load(modules);
        INSTANCE.terminatorThread.start();
        getRuntime().addShutdownHook(INSTANCE.terminatorHookThread);
    }

    public static Context context() {
        if (isNull(INSTANCE)) {
            throw new InternalRuntimeException(CONTEXT_NOT_INITIALIZED);
        }
        return INSTANCE;
    }

    public <C extends ModuleConfiguration> StatelessModuleProxy<C> getStatelessModule(String moduleId) {
        return new StatelessModuleProxy<>(cast(getModule(moduleId)));
    }

    public <C extends ModuleConfiguration, S extends ModuleState> StatefulModuleProxy<C, S> getStatefulModule(String moduleId) {
        return new StatefulModuleProxy<>(cast(getModule(moduleId)));
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


    private Module<?, ?> getModule(String moduleId) {
        Module<?, ?> module = modules.get(moduleId);
        if (nonNull(module)) {
            return module;
        }
        throw new InternalRuntimeException(format(MODULE_WAS_NOT_FOUND, moduleId));
    }

    private void load(ImmutableSet<Module<?, ?>> modules) {
        Set<String> messages = setOf(ART_BANNER);
        for (Module<?, ?> module : modules) {
            String moduleId = module.getId();
            messages.add(format(MODULE_LOADED_MESSAGE, moduleId));
            this.modules.put(moduleId, module);
        }
        messages.forEach(configuration.getPrinter());
        for (Module<?, ?> module : this.modules.values()) {
            module.onLoad(service);
            ifNotEmpty(module.print(), configuration.getPrinter());
        }
        apply(configuration.getOnLoad(), Runnable::run);
    }

    private void unload() {
        List<Module<?, ?>> modules = linkedListOf(this.modules.values());
        reverse(modules);
        for (Module<?, ?> module : modules) {
            configuration.getPrinter().accept(format(MODULE_UNLOADED_MESSAGE, module.getId()));
            module.onUnload(service);
            this.modules.remove(module.getId());
        }
        apply(configuration.getOnUnload(), Runnable::run);
        INSTANCE = null;
    }

    public static boolean active() {
        return nonNull(INSTANCE) && !INSTANCE.terminationScheduled.get();
    }

    public static void scheduleTermination() {
        if (isNull(INSTANCE) || INSTANCE.onShutdown.get()) {
            return;
        }
        if (INSTANCE.terminationScheduled.compareAndSet(false, true)) {
            getRuntime().removeShutdownHook(INSTANCE.terminatorHookThread);
            INSTANCE.terminatorSignal.countDown();
        }
    }

    public static void terminateImmediately() {
        shutdown();
        System.exit(0);
    }

    public static void shutdown() {
        if (isNull(INSTANCE) || INSTANCE.terminationScheduled.get()) {
            return;
        }
        if (INSTANCE.onShutdown.compareAndSet(false, true)) {
            getRuntime().removeShutdownHook(INSTANCE.terminatorHookThread);
            INSTANCE.terminatorThread.interrupt();
            INSTANCE.unload();
        }
    }

    private void awaitTermination() {
        try {
            terminatorSignal.await();
        } catch (InterruptedException interruptedException) {
            return;
        }
        if (isNull(INSTANCE)) {
            return;
        }
        INSTANCE.unload();
        System.exit(0);
    }

    private void terminationHook() {
        terminatorThread.interrupt();
        if (isNull(INSTANCE)) {
            return;
        }
        INSTANCE.unload();
    }

    public class Service {
        public void reload() {
            for (Map.Entry<String, Module<?, ?>> entry : modules.entrySet()) {
                Module<?, ?> module = entry.getValue();
                configuration.getPrinter().accept(format(MODULE_RELOADING_START_MESSAGE, module.getId()));
                module.beforeReload(service);
            }
            apply(configuration.getBeforeReload(), Runnable::run);

            for (Module<?, ?> module : modules.values()) {
                configuration.getReload().accept(module);

                module.afterReload(service);
                configuration.getPrinter().accept(format(MODULE_RELOADING_END_MESSAGE, module.getId()));
            }
            apply(configuration.getAfterReload(), Runnable::run);
        }
    }
}
