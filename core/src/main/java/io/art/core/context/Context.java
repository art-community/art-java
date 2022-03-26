/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import io.art.core.property.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.constants.ContextConstants.*;
import static io.art.core.constants.Errors.*;
import static io.art.core.constants.LoggingMessages.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.property.DisposableProperty.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.lang.Runtime.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
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
    private final Map<String, ManagedModule> modules = map();
    private final ContextConfiguration configuration;
    private final ContextService service;

    private Context(ContextConfiguration configuration) {
        this.configuration = configuration;
        this.service = new ContextService(configuration, lazy(() -> immutableMapOf(modules)));
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

        Map<String, Module<?, ?>> input = modules.stream().collect(mapCollector(Module::getId, identity()));
        Map<String, Module<?, ?>> sorted = map();
        Map<String, Module<?, ?>> postLoadingModules = map();

        for (String module : PRELOADED_MODULES) {
            if (input.containsKey(module)) {
                sorted.put(module, input.remove(module));
            }
        }

        List<String> postLoadingOrder = dynamicArrayOf(POST_LOADED_MODULES.toMutable());
        reverse(postLoadingOrder);
        for (String module : postLoadingOrder) {
            if (input.containsKey(module)) {
                postLoadingModules.put(module, input.remove(module));
            }
        }

        input.forEach(sorted::put);
        postLoadingModules.forEach(sorted::put);

        INSTANCE.load(sorted.values());
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
        DisposableProperty<ManagedModule> module = disposable(() -> INSTANCE.getModule(moduleId));
        return new StatelessModuleProxy<>(cast(module));
    }

    public <C extends ModuleConfiguration, S extends ModuleState> StatefulModuleProxy<C, S> getStatefulModule(String moduleId) {
        DisposableProperty<ManagedModule> module = disposable(() -> INSTANCE.getModule(moduleId));
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

    private ManagedModule getModule(String moduleId) {
        ManagedModule module = modules.get(moduleId);
        if (nonNull(module)) {
            return module;
        }
        throw new InternalRuntimeException(format(MODULE_WAS_NOT_FOUND, moduleId));
    }

    private void load(Collection<Module<?, ?>> modules) {
        for (Module<?, ?> module : modules) {
            String moduleId = module.getId();
            this.modules.put(moduleId, new ManagedModule(module));
        }

        for (ManagedModule module : this.modules.values()) {
            Module<?, ?> delegate = module.getModule();
            delegate.load(service);
            module.onLoad(service);
        }

        apply(configuration.getPrinter(), printer -> printer.accept(WELCOME_MESSAGE));
        apply(configuration.getOnLoad(), Runnable::run);

        for (ManagedModule module : this.modules.values()) {
            Module<?, ?> delegate = module.getModule();
            delegate.launch(service);
            module.onLaunch(service);
        }

        apply(configuration.getPrinter(), printer -> printer.accept(LAUNCHED_MESSAGE));
        apply(configuration.getOnLaunch(), Runnable::run);
    }

    private void unload() {
        List<ManagedModule> modules = linkedListOf(this.modules.values());

        reverse(modules);

        for (ManagedModule module : modules) {
            Module<?, ?> delegate = module.getModule();
            delegate.shutdown(service);
            module.onShutdown(service);
        }

        apply(configuration.getOnShutdown(), Runnable::run);
        apply(configuration.getPrinter(), printer -> printer.accept(SHUTDOWN_MESSAGE));

        for (ManagedModule module : modules) {
            Module<?, ?> delegate = module.getModule();
            delegate.unload(service);
            module.onUnload(service);
            this.modules.remove(delegate.getId());
        }

        apply(configuration.getOnUnload(), Runnable::run);
        INSTANCE = null;
    }
}
