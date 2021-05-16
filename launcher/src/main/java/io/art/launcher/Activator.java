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
import io.art.core.module.*;
import io.art.http.module.*;
import io.art.json.module.*;
import io.art.logging.module.*;
import io.art.message.pack.module.*;
import io.art.protobuf.module.*;
import io.art.rocks.db.module.*;
import io.art.rsocket.module.*;
import io.art.scheduler.module.*;
import io.art.server.module.*;
import io.art.storage.module.*;
import io.art.tarantool.module.*;
import io.art.value.module.*;
import io.art.xml.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Accessors(fluent = true)
public class Activator {
    private final static Activator activator = new Activator();

    private final Map<String, ModuleActivator> modules = map();

    @Getter
    private ModuleActivator configuratorActivator;

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

    public ImmutableSet<ModuleActivator> modules() {
        return immutableSetOf(modules.values());
    }

    public Activator configurator() {
        return configurator(identity());
    }

    public Activator configurator(UnaryOperator<ConfiguratorInitializer> initializer) {
        configuratorActivator = ConfiguratorActivator.configurator(initializer);
        return this;
    }

    public Activator module(ModuleActivator activator) {
        activator.getDependencies().forEach(this::module);
        modules.putIfAbsent(activator.getId(), activator);
        return this;
    }

    public Activator kit() {
        return kit(new ModulesInitializer());
    }

    public Activator kit(ModulesInitializer initializer) {
        module(ValueActivator.value(initializer.value()));
        module(SchedulerActivator.scheduler());
        module(LoggingActivator.logging(initializer.logging()));
        module(JsonActivator.json());
        module(ProtobufActivator.protobuf());
        module(MessagePackActivator.messagePack());
        module(XmlActivator.xml());
        module(CommunicatorActivator.communicator(initializer.communicator()));
        module(ServerActivator.server(initializer.server()));
        module(HttpActivator.http(initializer.http()));
        module(RsocketActivator.rsocket(initializer.rsocket()));
        module(StorageActivator.storage(initializer.storage()));
        module(TarantoolActivator.tarantool());
        module(RocksDbActivator.rocksdb());
        return this;
    }


    public void launch() {
        Launcher.launch(this);
    }


    public static Activator activator(String[] arguments) {
        return activator.arguments(arguments).configurator();
    }

    public static Activator activator() {
        return activator(EMPTY_STRINGS);
    }
}
