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
import io.art.model.customizer.*;
import io.art.protobuf.module.*;
import io.art.resilience.module.*;
import io.art.rocks.db.module.*;
import io.art.rsocket.module.*;
import io.art.scheduler.module.*;
import io.art.server.module.*;
import io.art.storage.module.*;
import io.art.tarantool.module.*;
import io.art.value.module.*;
import io.art.xml.module.*;
import io.art.yaml.module.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Accessors(fluent = true)
public class Activator {
    private final static Activator singleton = new Activator();

    private final Map<String, ModuleActivator> modules = map();

    @Getter
    private ModuleActivator configuratorModuleActivator;

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
        configuratorModuleActivator = ModuleActivator.builder()
                .id(ConfiguratorModule.class.getSimpleName())
                .initializer(initializer.apply(new ConfiguratorInitializer()))
                .factory(ConfiguratorModule::new)
                .build();
        return this;
    }


    public Activator value() {
        return value(identity());
    }

    public Activator value(UnaryOperator<ValueInitializer> initializer) {
        return module(ValueModule.class, ValueModule::new, initializer.apply(new ValueInitializer()));
    }


    public Activator scheduler() {
        return module(SchedulerModule.class, SchedulerModule::new);
    }

    public Activator logging() {
        return logging(identity());
    }

    public Activator logging(UnaryOperator<LoggingInitializer> initializer) {
        return module(LoggingModule.class, LoggingModule::new, initializer.apply(new LoggingInitializer()));
    }


    public Activator server() {
        return server(identity());
    }

    public Activator server(UnaryOperator<ServerInitializer> initializer) {
        value();
        logging();
        scheduler();
        return module(ServerModule.class, ServerModule::new, initializer.apply(new ServerInitializer()));
    }


    public Activator communicator() {
        return communicator(identity());
    }

    public Activator communicator(UnaryOperator<CommunicatorInitializer> initializer) {
        value();
        logging();
        scheduler();
        resilience();
        return module(CommunicatorModule.class, CommunicatorModule::new, initializer.apply(new CommunicatorInitializer()));
    }


    public Activator rsocket() {
        return rsocket(identity());
    }

    public Activator rsocket(UnaryOperator<RsocketInitializer> initializer) {
        server();
        communicator();
        return module(RsocketModule.class, RsocketModule::new, initializer.apply(new RsocketInitializer()));
    }


    public Activator http() {
        return http(identity());
    }

    public Activator http(UnaryOperator<HttpInitializer> initializer) {
        server();
        communicator();
        return module(HttpModule.class, HttpModule::new, initializer.apply(new HttpInitializer()));
    }


    public Activator json() {
        return module(JsonModule.class, JsonModule::new);
    }

    public Activator protobuf() {
        return module(ProtobufModule.class, ProtobufModule::new);
    }

    public Activator yaml() {
        return module(YamlModule.class, YamlModule::new);
    }

    public Activator xml() {
        return module(XmlModule.class, XmlModule::new);
    }

    public Activator messagePack() {
        return module(MessagePackModule.class, MessagePackModule::new);
    }


    public Activator resilience() {
        return module(ResilienceModule.class, ResilienceModule::new);
    }


    public Activator storage() {
        return storage(identity());
    }

    public Activator storage(UnaryOperator<StorageInitializer> initializer) {
        value();
        logging();
        scheduler();
        return module(StorageModule.class, StorageModule::new, initializer.apply(new StorageInitializer()));
    }


    public Activator rocksdb() {
        storage();
        return module(RocksDbModule.class, RocksDbModule::new);
    }


    public Activator tarantool() {
        storage();
        return module(TarantoolModule.class, TarantoolModule::new);
    }


    public Activator kit() {
        return kit(new ModulesInitializer());
    }

    public Activator kit(ModulesInitializer initializer) {
        value(initializer.value());
        logging(initializer.logging());
        json();
        protobuf();
        messagePack();
        xml();
        communicator(initializer.communicator());
        server(initializer.server());
        http(initializer.http());
        rsocket(initializer.rsocket());
        storage(initializer.storage());
        return this;
    }


    public void launch() {
        Launcher.launch(this);
    }


    public static Activator art() {
        return singleton.configurator();
    }


    private Activator module(Class<?> moduleClass, ModuleFactory<?> moduleFactory) {
        modules.putIfAbsent(moduleClass.getSimpleName(), ModuleActivator.builder()
                .id(moduleClass.getSimpleName())
                .factory(moduleFactory)
                .build());
        return this;
    }

    private Activator module(Class<?> moduleClass, ModuleFactory<?> moduleFactory, ModuleInitializer<?, ?, ?> initializer) {
        modules.putIfAbsent(moduleClass.getSimpleName(), ModuleActivator.builder()
                .id(moduleClass.getSimpleName())
                .initializer(initializer)
                .factory(moduleFactory)
                .build());
        return this;
    }
}
