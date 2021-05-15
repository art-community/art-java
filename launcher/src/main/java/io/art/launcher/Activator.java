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

import io.art.configurator.module.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.communicator.module.CommunicatorActivator.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.http.module.HttpActivator.*;
import static io.art.json.module.JsonActivator.*;
import static io.art.logging.module.LoggingActivator.*;
import static io.art.message.pack.module.MessagePackActivator.*;
import static io.art.protobuf.module.ProtobufActivator.*;
import static io.art.rocks.db.module.RocksDbActivator.*;
import static io.art.rsocket.module.RsocketActivator.*;
import static io.art.scheduler.module.SchedulerActivator.*;
import static io.art.server.module.ServerActivator.*;
import static io.art.storage.module.StorageActivator.*;
import static io.art.tarantool.module.TarantoolActivator.*;
import static io.art.value.module.ValueActivator.*;
import static io.art.xml.module.XmlActivator.*;
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
        modules.putIfAbsent(activator.getId(), activator);
        return this;
    }

    public Activator kit() {
        return kit(new ModulesInitializer());
    }

    public Activator kit(ModulesInitializer initializer) {
        module(value(initializer.value()));
        module(scheduler());
        module(logging(initializer.logging()));
        module(json());
        module(protobuf());
        module(messagePack());
        module(xml());
        module(communicator(initializer.communicator()));
        module(server(initializer.server()));
        module(http(initializer.http()));
        module(rsocket(initializer.rsocket()));
        module(storage(initializer.storage()));
        module(tarantool());
        module(rocksdb());
        return this;
    }


    public void launch() {
        Launcher.launch(this);
    }


    public static Activator activator() {
        return activator.configurator();
    }
}
