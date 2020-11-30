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
import io.art.core.collection.*;
import io.art.core.configuration.ContextConfiguration.*;
import io.art.core.context.*;
import io.art.core.lazy.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.json.module.*;
import io.art.logging.*;
import io.art.model.customizer.*;
import io.art.model.implementation.*;
import io.art.rsocket.module.*;
import io.art.server.module.*;
import io.art.tarantool.module.*;
import io.art.value.module.*;
import io.art.xml.module.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.colorizer.AnsiColorizer.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.launcher.ModuleLauncherConstants.*;
import static io.art.logging.LoggingModule.*;
import static io.art.model.constants.ModelConstants.Protocol.*;
import static java.util.Optional.*;
import java.util.concurrent.atomic.*;

@UtilityClass
public class ModuleLauncher {
    private final static AtomicBoolean launched = new AtomicBoolean(false);

    public static void launch(ModuleModel model) {
        if (launched.compareAndSet(false, true)) {
            ConfiguratorModule configurator = new ConfiguratorModule();
            ImmutableArray<ConfigurationSource> sources = configurator
                    .loadConfigurations()
                    .configuration()
                    .orderedSources();
            ConfiguratorCustomizer configuratorCustomizer = model.getConfiguratorCustomizer();
            ValueCustomizer valueCustomizer = configuratorCustomizer.value().apply(new ValueCustomizer());
            LoggingCustomizer loggingCustomizer = configuratorCustomizer.logging().apply(new LoggingCustomizer());
            ServerCustomizer serverCustomizer = configuratorCustomizer.server().apply(new ServerCustomizer());
            RsocketCustomizer rsocketCustomizer = configuratorCustomizer.rsocket().apply(new RsocketCustomizer());
            ImmutableArray.Builder<Module> modules = immutableArrayBuilder();
            modules.add(
                    configurator,
                    value(sources, valueCustomizer),
                    logging(sources, loggingCustomizer),
                    json(sources),
                    xml(sources),
                    server(sources, serverCustomizer),
                    communicator(sources),
                    rsocket(sources, model.getServerModel(), rsocketCustomizer),
                    tarantool(sources)
            );
            LazyValue<Logger> logger = lazy(() -> logger(Context.class));
            initialize(new DefaultContextConfiguration(), modules.build(), message -> logger.get().info(message));
            LAUNCHED_MESSAGES.forEach(message -> logger.get().info(success(message)));
            boolean needBlock = rsocketCustomizer.getConfiguration().isActivateServer();
            if (needBlock) {
                block();
            }
        }
    }

    private ValueModule value(ImmutableArray<ConfigurationSource> sources, ValueCustomizer valueCustomizer) {
        ValueModule value = new ValueModule();
        value.configure(configurator -> configurator.from(sources));
        ofNullable(valueCustomizer).ifPresent(model -> value.configure(configurator -> configurator.override(model.getConfiguration())));
        return value;
    }

    private LoggingModule logging(ImmutableArray<ConfigurationSource> sources, LoggingCustomizer loggingCustomizer) {
        LoggingModule logging = new LoggingModule();
        logging.configure(configurator -> configurator.from(sources));
        ofNullable(loggingCustomizer).ifPresent(model -> logging.configure(configurator -> configurator.override(model.getConfiguration())));
        return logging;
    }

    private JsonModule json(ImmutableArray<ConfigurationSource> sources) {
        JsonModule json = new JsonModule();
        json.configure(configurator -> configurator.from(sources));
        return json;
    }

    private XmlModule xml(ImmutableArray<ConfigurationSource> sources) {
        XmlModule xml = new XmlModule();
        xml.configure(configurator -> configurator.from(sources));
        return xml;
    }

    private ServerModule server(ImmutableArray<ConfigurationSource> sources, ServerCustomizer serverCustomizer) {
        ServerModule server = new ServerModule();
        ofNullable(serverCustomizer).ifPresent(model -> server.configure(configurator -> configurator.from(sources).override(model.getConfiguration())));
        return server;
    }

    private CommunicatorModule communicator(ImmutableArray<ConfigurationSource> sources) {
        CommunicatorModule communicator = new CommunicatorModule();
        communicator.configure(configurator -> configurator.from(sources));
        return communicator;
    }

    private RsocketModule rsocket(ImmutableArray<ConfigurationSource> sources, ServerModel serverModel, RsocketCustomizer rsocketCustomizer) {
        RsocketModule rsocket = new RsocketModule();
        if (serverModel.getServices().values().stream().anyMatch(service -> service.getProtocol() == RSOCKET)) {
            rsocketCustomizer.activateServer();
        }
        rsocket.configure(configurator -> configurator.from(sources).override(rsocketCustomizer.getConfiguration()));
        return rsocket;
    }

    private TarantoolModule tarantool(ImmutableArray<ConfigurationSource> sources) {
        TarantoolModule tarantool = new TarantoolModule();
        tarantool.configure(configurator -> configurator.from(sources));
        return tarantool;
    }
}
