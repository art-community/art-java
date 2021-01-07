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
import io.art.core.configuration.ContextConfiguration.*;
import io.art.core.context.*;
import io.art.core.lazy.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.json.module.*;
import io.art.logging.*;
import io.art.model.customizer.*;
import io.art.model.implementation.communicator.*;
import io.art.model.implementation.module.*;
import io.art.model.implementation.server.*;
import io.art.rsocket.module.*;
import io.art.server.module.*;
import io.art.tarantool.module.*;
import io.art.value.module.*;
import io.art.xml.module.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.colorizer.AnsiColorizer.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.launcher.ModuleLauncherConstants.*;
import static io.art.logging.LoggingModule.*;
import static java.util.Optional.*;
import java.util.concurrent.atomic.*;

@UtilityClass
@UsedByGenerator
public class ModuleLauncher {
    private static final AtomicBoolean launched = new AtomicBoolean(false);

    public static void launch(ModuleModel model) {
        if (launched.compareAndSet(false, true)) {
            ConfiguratorModule configurator = new ConfiguratorModule();
            ImmutableArray<ConfigurationSource> sources = configurator
                    .initializeConfigurator()
                    .configuration()
                    .orderedSources();

            ModuleCustomizer moduleCustomizer = model.getCustomizer();
            ConfiguratorCustomizer configuratorCustomizer = moduleCustomizer.configurator().apply(new ConfiguratorCustomizer());
            ValueCustomizer valueCustomizer = moduleCustomizer.value().apply(new ValueCustomizer());
            LoggingCustomizer loggingCustomizer = moduleCustomizer.logging().apply(new LoggingCustomizer());
            ServerCustomizer serverCustomizer = moduleCustomizer.server().apply(new ServerCustomizer());
            CommunicatorCustomizer communicatorCustomizer = moduleCustomizer.communicator().apply(new CommunicatorCustomizer());
            RsocketCustomizer rsocketCustomizer = moduleCustomizer.rsocket().apply(new RsocketCustomizer());
            ImmutableArray.Builder<Module> modules = immutableArrayBuilder();

            modules.add(
                    configurator(configurator, sources, configuratorCustomizer),
                    value(sources, valueCustomizer),
                    logging(sources, loggingCustomizer),
                    json(sources),
                    xml(sources),
                    server(sources, serverCustomizer),
                    communicator(sources, communicatorCustomizer),
                    rsocket(sources, model, rsocketCustomizer),
                    tarantool(sources)
            );
            LazyValue<Logger> logger = lazy(() -> logger(Context.class));
            initialize(new DefaultContextConfiguration(model.getMainModuleId()), modules.build(), message -> logger.get().info(message));
            LAUNCHED_MESSAGES.forEach(message -> logger.get().info(success(message)));
            model.getOnLoad().run();
            if (needBlock(rsocketCustomizer)) block();
        }
    }

    private static Module configurator(ConfiguratorModule configuratorModule, ImmutableArray<ConfigurationSource> sources, ConfiguratorCustomizer configuratorCustomizer) {
        ofNullable(configuratorCustomizer).ifPresent(customizer -> configuratorModule.configure(configurator -> configurator.override(customizer.configure(sources))));
        return configuratorModule;
    }

    private static ValueModule value(ImmutableArray<ConfigurationSource> sources, ValueCustomizer valueCustomizer) {
        ValueModule value = new ValueModule();
        value.configure(configurator -> configurator.from(sources));
        ofNullable(valueCustomizer).ifPresent(customizer -> value.configure(configurator -> configurator.override(customizer.getConfiguration())));
        return value;
    }

    private static LoggingModule logging(ImmutableArray<ConfigurationSource> sources, LoggingCustomizer loggingCustomizer) {
        LoggingModule logging = new LoggingModule();
        logging.configure(configurator -> configurator.from(sources));
        ofNullable(loggingCustomizer).ifPresent(customizer -> logging.configure(configurator -> configurator.override(customizer.getConfiguration())));
        return logging;
    }

    private static JsonModule json(ImmutableArray<ConfigurationSource> sources) {
        JsonModule json = new JsonModule();
        json.configure(configurator -> configurator.from(sources));
        return json;
    }

    private static XmlModule xml(ImmutableArray<ConfigurationSource> sources) {
        XmlModule xml = new XmlModule();
        xml.configure(configurator -> configurator.from(sources));
        return xml;
    }

    private static ServerModule server(ImmutableArray<ConfigurationSource> sources, ServerCustomizer serverCustomizer) {
        ServerModule server = new ServerModule();
        ofNullable(serverCustomizer).ifPresent(customizer -> server.configure(configurator -> configurator.from(sources).override(customizer.getConfiguration())));
        return server;
    }

    private static CommunicatorModule communicator(ImmutableArray<ConfigurationSource> sources, CommunicatorCustomizer communicatorCustomizer) {
        CommunicatorModule communicator = new CommunicatorModule();
        ofNullable(communicatorCustomizer).ifPresent(customizer -> communicator.configure(configurator -> configurator.from(sources).override(customizer.getConfiguration())));
        return communicator;
    }

    private static RsocketModule rsocket(ImmutableArray<ConfigurationSource> sources, ModuleModel model, RsocketCustomizer rsocketCustomizer) {
        RsocketModule rsocket = new RsocketModule();
        ServerModuleModel serverModel = model.getServerModel();
        CommunicatorModuleModel communicatorModel = model.getCommunicatorModel();
        if (isNotEmpty(serverModel.getRsocketServices())) {
            rsocketCustomizer.activateServer();
        }
        if (isNotEmpty(communicatorModel.getRsocketCommunicators())) {
            rsocketCustomizer.activateCommunicator();
        }
        rsocket.configure(configurator -> configurator.from(sources).override(rsocketCustomizer.getConfiguration()));
        return rsocket;
    }

    private static TarantoolModule tarantool(ImmutableArray<ConfigurationSource> sources) {
        TarantoolModule tarantool = new TarantoolModule();
        tarantool.configure(configurator -> configurator.from(sources));
        return tarantool;
    }

    private static boolean needBlock(RsocketCustomizer rsocketCustomizer) {
        return rsocketCustomizer.getConfiguration().isActivateServer();
    }
}
