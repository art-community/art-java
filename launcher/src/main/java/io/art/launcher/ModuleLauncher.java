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
import io.art.core.configuration.*;
import io.art.core.context.*;
import io.art.core.module.*;
import io.art.core.property.*;
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
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.colorizer.AnsiColorizer.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.core.singleton.SingletonsRegistry.*;
import static io.art.launcher.ModuleLauncherConstants.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@UtilityClass
@UsedByGenerator
public class ModuleLauncher {
    private static final AtomicBoolean LAUNCHED = new AtomicBoolean(false);

    public static void launch(ModuleModel model) {
        if (LAUNCHED.compareAndSet(false, true)) {
            ConfiguratorModule configurator = new ConfiguratorModule();
            ModuleCustomizer moduleCustomizer = model.getCustomizer();
            UnaryOperator<ConfiguratorCustomizer> configuratorCustomizer = moduleCustomizer.configurator();
            UnaryOperator<ValueCustomizer> value = moduleCustomizer.value();
            UnaryOperator<LoggingCustomizer> logging = moduleCustomizer.logging();
            UnaryOperator<ServerCustomizer> server = moduleCustomizer.server();
            UnaryOperator<CommunicatorCustomizer> communicator = moduleCustomizer.communicator();
            UnaryOperator<RsocketCustomizer> rsocket = moduleCustomizer.rsocket();
            ModuleInitializerRegistry modules = new ModuleInitializerRegistry();
            ModuleConfiguringState state = new ModuleConfiguringState(configurator.configure(), model);
            modules
                    .put(() -> configurator, module -> configurator(module, singleton(ConfiguratorCustomizer.class, () -> configuratorCustomizer.apply(new ConfiguratorCustomizer()))))
                    .put(ValueModule::new, module -> value(module, state, singleton(ValueCustomizer.class, () -> value.apply(new ValueCustomizer()))))
                    .put(LoggingModule::new, module -> logging(module, state, singleton(LoggingCustomizer.class, () -> logging.apply(new LoggingCustomizer()))))
                    .put(SchedulerModule::new, module -> scheduler(module, state))
                    .put(JsonModule::new, module -> json(module, state))
                    .put(YamlModule::new, module -> yaml(module, state))
                    .put(XmlModule::new, module -> xml(module, state))
                    .put(ServerModule::new, module -> server(module, state, singleton(ServerCustomizer.class, () -> server.apply(new ServerCustomizer(module)))))
                    .put(CommunicatorModule::new, module -> communicator(module, state, singleton(CommunicatorCustomizer.class, () -> communicator.apply(new CommunicatorCustomizer()))))
                    .put(RsocketModule::new, module -> rsocket(module, state, singleton(RsocketCustomizer.class, () -> rsocket.apply(new RsocketCustomizer(module)))))
                    .put(TarantoolModule::new, module -> tarantool(module, state));
            LazyProperty<Logger> logger = lazy(() -> logger(Context.class));
            ContextConfiguration contextConfiguration = ContextConfiguration.builder()
                    .onUnload(model.getOnUnload())
                    .onLoad(model.getOnLoad())
                    .beforeReload(model.getBeforeReload())
                    .afterReload(model.getAfterReload())
                    .mainModuleId(model.getMainModuleId())
                    .build();
            initialize(contextConfiguration, modules.get(), message -> logger.get().info(message));
            LAUNCHED_MESSAGES.forEach(message -> logger.get().info(success(message)));
            if (needBlock()) block();
        }
    }

    private static ConfiguratorModule configurator(ConfiguratorModule configuratorModule, ConfiguratorCustomizer configuratorCustomizer) {
        ofNullable(configuratorCustomizer)
                .ifPresent(customizer -> configuratorModule
                        .configure(configurator -> configurator
                                .override(customizer.configure(configuratorModule.orderedSources()))));
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
        if (isNotEmpty(let(serverModel, ServerModuleModel::getRsocketServices))) {
            rsocketCustomizer.activateServer();
        }
        if (isNotEmpty(let(communicatorModel, CommunicatorModuleModel::getRsocketCommunicators))) {
            rsocketCustomizer.activateCommunicator();
        }
        rsocket.configure(configurator -> configurator
                .from(state.getConfigurator().orderedSources())
                .override(rsocketCustomizer.getConfiguration()));
        return rsocket;
    }

    private static TarantoolModule tarantool(TarantoolModule tarantool, ModuleConfiguringState state) {
        tarantool.configure(configurator -> configurator.from(state.getConfigurator().orderedSources()));
        return tarantool;
    }

    private static boolean needBlock() {
        return rsocketModule().configuration().isActivateServer();
    }
}
