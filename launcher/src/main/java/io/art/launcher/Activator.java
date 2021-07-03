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

package io.art.launcher;

import io.art.communicator.module.*;
import io.art.configurator.module.*;
import io.art.core.collection.*;
import io.art.core.extensions.*;
import io.art.core.module.*;
import io.art.http.module.*;
import io.art.json.module.*;
import io.art.logging.module.*;
import io.art.message.pack.module.*;
import io.art.meta.model.*;
import io.art.meta.module.*;
import io.art.rsocket.module.*;
import io.art.scheduler.module.*;
import io.art.server.module.*;
import io.art.storage.module.*;
import io.art.tarantool.module.*;
import io.art.value.module.*;
import io.art.yaml.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.launcher.LauncherConstants.*;
import java.util.*;
import java.util.function.*;

@Accessors(fluent = true)
public class Activator {
    private final static Activator activator = new Activator();

    private final Map<String, ModuleActivator> activators = map();

    @Getter
    private ModuleActivator configuratorActivator;

    @Getter
    private ModuleActivator metaActivator;

    @Getter
    private ModuleActivator loggingActivator;

    @Setter
    @Getter
    private String mainModuleId;

    @Setter
    @Getter
    private Runnable onLoad;

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

    @Setter
    @Getter
    private boolean quiet = false;

    public ImmutableMap<String, ModuleActivator> activators() {
        return immutableMapOf(activators);
    }

    public Activator module(ModuleActivator activator) {
        if (activator.getId().equals(CONFIGURATOR_MODULE_ID)) {
            configuratorActivator = activator;
            return this;
        }
        if (activator.getId().equals(LOGGING_MODULE_ID)) {
            loggingActivator = activator;
            return this;
        }
        activator.getDependencies().forEach(this::module);
        activators.putIfAbsent(activator.getId(), activator);
        return this;
    }

    public Activator kit() {
        return kit(new ModulesInitializer());
    }

    public Activator kit(ModulesInitializer initializer) {
        module(LoggingActivator.logging(initializer.logging()));
        module(ValueActivator.value(initializer.value()));
        module(SchedulerActivator.scheduler());
        module(JsonActivator.json());
        module(MessagePackActivator.messagePack());
        module(YamlActivator.yaml());
        module(CommunicatorActivator.communicator(initializer.communicator()));
        module(ServerActivator.server(initializer.server()));
        module(HttpActivator.http(initializer.http()));
        module(RsocketActivator.rsocket(initializer.rsocket()));
        module(StorageActivator.storage(initializer.storage()));
        module(TarantoolActivator.tarantool());
        return this;
    }


    public BlockingAction launch() {
        Launcher.launch(this);
        return new BlockingAction();
    }


    public static Activator activator(String[] arguments, Supplier<? extends MetaLibrary> metaFactory) {
        Activator.activator.metaActivator = MetaActivator.meta(metaFactory);
        Activator.activator.configuratorActivator = ConfiguratorActivator.configurator();
        return Activator.activator.arguments(arguments);
    }

    public static Activator activator(Supplier<? extends MetaLibrary> metaFactory) {
        return activator(EMPTY_STRINGS, metaFactory);
    }

    public static Activator activator(String[] arguments) {
        Activator.activator.configuratorActivator = ConfiguratorActivator.configurator();
        return Activator.activator.arguments(arguments);
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
