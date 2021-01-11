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

package io.art.launcher;

import io.art.communicator.module.*;
import io.art.configurator.module.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.configuration.*;
import io.art.core.context.*;
import io.art.core.lazy.*;
import io.art.core.module.*;
import io.art.json.module.*;
import io.art.logging.*;
import io.art.model.customizer.*;
import io.art.model.implementation.communicator.*;
import io.art.model.implementation.module.*;
import io.art.model.implementation.server.*;
import io.art.rsocket.module.*;
import io.art.scheduler.module.*;
import io.art.server.module.*;
import io.art.tarantool.module.*;
import io.art.value.module.*;
import io.art.xml.module.*;
import io.art.yaml.module.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.colorizer.AnsiColorizer.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.launcher.ModuleLauncherConstants.*;
import static io.art.launcher.ModuleModelProvider.*;
import static io.art.logging.LoggingModule.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.concurrent.atomic.*;

@UtilityClass
@UsedByGenerator
public class ModuleLauncher {
    private static final AtomicBoolean LAUNCHED = new AtomicBoolean(false);

    public static void launch() {
        launch(provide());
    }

    public static void launch(ModuleModel model) {
        if (LAUNCHED.compareAndSet(false, true)) {
            ConfiguratorModule configurator = new ConfiguratorModule();
            ModuleCustomizer moduleCustomizer = model.getCustomizer();
            ConfiguratorCustomizer configuratorCustomizer = moduleCustomizer.configurator().apply(new ConfiguratorCustomizer());
            ValueCustomizer valueCustomizer = moduleCustomizer.value().apply(new ValueCustomizer());
            LoggingCustomizer loggingCustomizer = moduleCustomizer.logging().apply(new LoggingCustomizer());
            ServerCustomizer serverCustomizer = moduleCustomizer.server().apply(new ServerCustomizer());
            CommunicatorCustomizer communicatorCustomizer = moduleCustomizer.communicator().apply(new CommunicatorCustomizer());
            RsocketCustomizer rsocketCustomizer = moduleCustomizer.rsocket().apply(new RsocketCustomizer());
            ImmutableMap.Builder<ModuleFactory, ModuleDecorator> modules = immutableMapBuilder();
            ModuleConfiguringState state = new ModuleConfiguringState(configurator.configure(), model);

            modules
                    .put(() -> configurator, module -> configurator(cast(module), configuratorCustomizer))
                    .put(ValueModule::new, module -> value(cast(module), state, valueCustomizer))
                    .put(LoggingModule::new, module -> logging(cast(module), state, loggingCustomizer))
                    .put(SchedulerModule::new, module -> scheduler(cast(module), state))
                    .put(JsonModule::new, module -> json(cast(module), state))
                    .put(YamlModule::new, module -> yaml(cast(module), state))
                    .put(XmlModule::new, module -> xml(cast(module), state))
                    .put(ServerModule::new, module -> server(cast(module), state, serverCustomizer))
                    .put(CommunicatorModule::new, module -> communicator(cast(module), state, communicatorCustomizer))
                    .put(RsocketModule::new, module -> rsocket(cast(module), state, rsocketCustomizer))
                    .put(TarantoolModule::new, module -> tarantool(cast(module), state));

            LazyValue<Logger> logger = lazy(() -> logger(Context.class));
            initialize(new ContextConfiguration.DefaultContextConfiguration(model.getMainModuleId()), modules.build(), message -> logger.get().info(message));
            LAUNCHED_MESSAGES.forEach(message -> logger.get().info(success(message)));
            model.getOnLoad().run();
            if (needBlock(rsocketCustomizer)) block();
        }
    }

    private static Module configurator(ConfiguratorModule configuratorModule, ConfiguratorCustomizer configuratorCustomizer) {
        ofNullable(configuratorCustomizer).ifPresent(customizer -> configuratorModule.configure(configurator -> configurator.override(customizer.configure(configuratorModule.orderedSources()))));
        return configuratorModule;
    }

    private static ValueModule value(ValueModule value, ModuleConfiguringState state, ValueCustomizer customizer) {
        value.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        if (isNull(customizer)) {
            return value;
        }
        value.configure(configurator -> configurator.override(customizer.getConfiguration()));
        return value;
    }

    private static SchedulerModule scheduler(SchedulerModule scheduler, ModuleConfiguringState state) {
        scheduler.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        return scheduler;
    }

    private static LoggingModule logging(LoggingModule logging, ModuleConfiguringState state, LoggingCustomizer customizer) {
        logging.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        if (isNull(customizer)) {
            return logging;
        }
        logging.configure(configurator -> configurator.override(customizer.getConfiguration()));
        return logging;
    }

    private static JsonModule json(JsonModule json, ModuleConfiguringState state) {
        json.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        return json;
    }

    private static YamlModule yaml(YamlModule yaml, ModuleConfiguringState state) {
        yaml.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        return yaml;
    }

    private static XmlModule xml(XmlModule xml, ModuleConfiguringState state) {
        xml.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        return xml;
    }

    private static ServerModule server(ServerModule server, ModuleConfiguringState state, ServerCustomizer customizer) {
        server.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        if (isNull(customizer)) {
            return server;
        }
        server.configure(configurator -> configurator.override(customizer.getConfiguration()));
        return server;
    }

    private static CommunicatorModule communicator(CommunicatorModule communicator, ModuleConfiguringState state, CommunicatorCustomizer customizer) {
        communicator.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        if (isNull(customizer)) {
            return communicator;
        }
        communicator.configure(configurator -> configurator.override(customizer.getConfiguration()));
        return communicator;
    }

    private static RsocketModule rsocket(RsocketModule rsocket, ModuleConfiguringState state, RsocketCustomizer rsocketCustomizer) {
        ServerModuleModel serverModel = state.getModel().getServerModel();
        CommunicatorModuleModel communicatorModel = state.getModel().getCommunicatorModel();
        if (isNotEmpty(serverModel.getRsocketServices())) {
            rsocketCustomizer.activateServer();
        }
        if (isNotEmpty(communicatorModel.getRsocketCommunicators())) {
            rsocketCustomizer.activateCommunicator();
        }
        rsocket.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()).override(rsocketCustomizer.getConfiguration()));
        return rsocket;
    }

    private static TarantoolModule tarantool(TarantoolModule tarantool, ModuleConfiguringState state) {
        tarantool.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        return tarantool;
    }

    private static boolean needBlock(RsocketCustomizer rsocketCustomizer) {
        return rsocketCustomizer.getConfiguration().isActivateServer();
    }
}
