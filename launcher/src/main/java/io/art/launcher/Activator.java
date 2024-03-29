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

package io.art.launcher;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.extensions.*;
import io.art.core.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.http.module.HttpActivator.*;
import static io.art.meta.module.MetaActivator.*;
import java.util.*;

@Public
@Accessors(fluent = true)
public class Activator {
    private final static Activator activator = new Activator();
    private final Map<String, ModuleActivator> activators = map();

    @Setter
    @Getter
    private String main;

    @Setter
    @Getter
    private Runnable onLoad;

    @Setter
    @Getter
    private Runnable onLaunch;

    @Setter
    @Getter
    private Runnable onShutdown;

    @Setter
    @Getter
    private Runnable onUnload;

    @Setter
    @Getter
    private Runnable beforeReload;

    @Setter
    @Getter
    private Runnable afterReload;

    @Setter
    @Getter
    private String[] arguments;

    public ImmutableMap<String, ModuleActivator> activators() {
        return immutableMapOf(activators);
    }

    public ImmutableMap<String, ModuleInitializationOperator<?>> decorators() {
        Map<String, ModuleInitializationOperator<?>> decorators = map();
        for (Map.Entry<String, ModuleActivator> activatorEntry : activators.entrySet()) {
            for (Map.Entry<String, ModuleInitializationOperator<?>> decoratorEntry : activatorEntry.getValue().decorators().entrySet()) {
                if (activators.containsKey(decoratorEntry.getKey())) {
                    ModuleInitializationOperator<?> existed = orElse(decorators.get(decoratorEntry.getKey()), ModuleInitializationOperator.identity());
                    decorators.put(decoratorEntry.getKey(), value -> decoratorEntry.getValue().apply(cast(existed.apply(cast(value)))));
                }
            }
        }
        return immutableMapOf(decorators);
    }

    public Activator module(ModuleActivator activator) {
        activator.dependencies().forEach(this::module);
        activators.putIfAbsent(activator.getId(), activator);
        return this;
    }

    public BlockingAction launch() {
        Launcher.launch(this);
        return new BlockingAction();
    }

    public Activator web() {
        return module(meta()).module(http());
    }

    public static Activator activator(String[] arguments) {
        return activator.arguments(arguments);
    }

    public static Activator activator() {
        return activator(EMPTY_STRINGS);
    }

    public static class BlockingAction {
        public void block() {
            ThreadExtensions.block();
        }
    }
}
