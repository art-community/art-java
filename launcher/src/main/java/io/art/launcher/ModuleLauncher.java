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

import com.google.common.collect.*;
import io.art.communicator.module.*;
import io.art.configurator.module.*;
import io.art.core.configuration.ContextConfiguration.*;
import io.art.core.context.*;
import io.art.core.lazy.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.json.module.*;
import io.art.logging.*;
import io.art.model.configurator.*;
import io.art.model.module.*;
import io.art.model.server.*;
import io.art.rsocket.module.*;
import io.art.server.module.*;
import io.art.tarantool.module.*;
import io.art.value.module.*;
import io.art.xml.module.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static io.art.core.colorizer.AnsiColorizer.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.launcher.ModuleLauncherConstants.*;
import static io.art.logging.LoggingModule.*;
import static io.art.model.constants.ModelConstants.Protocol.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@UtilityClass
public class ModuleLauncher {
    private final static AtomicBoolean launched = new AtomicBoolean(false);

    public static void launch(ModuleModel model) {
        if (launched.compareAndSet(false, true)) {
            ConfiguratorModule configurator = new ConfiguratorModule();
            List<ConfigurationSource> sources = configurator
                    .loadConfigurations()
                    .configuration()
                    .orderedSources();
            ConfiguratorModel configuratorModel = model.getConfiguratorModel();
            ValueConfiguratorModel valueModel = configuratorModel.value().apply(new ValueConfiguratorModel());
            LoggingConfiguratorModel loggingModel = configuratorModel.logging().apply(new LoggingConfiguratorModel());
            ServerConfiguratorModel serverModel = configuratorModel.server().apply(new ServerConfiguratorModel());
            RsocketConfiguratorModel rsocketModel = configuratorModel.rsocket().apply(new RsocketConfiguratorModel());
            ImmutableList.Builder<Module> modules = ImmutableList.builder();
            modules.add(
                    configurator,
                    value(sources, valueModel),
                    logging(sources, loggingModel),
                    json(sources),
                    xml(sources),
                    server(sources, serverModel),
                    communicator(sources),
                    rsocket(sources, model.getServerModel(), rsocketModel),
                    tarantool(sources)
            );
            LazyValue<Logger> logger = lazy(() -> logger(Context.class));
            initialize(new DefaultContextConfiguration(), modules.build(), message -> logger.get().info(message));
            LAUNCHED_MESSAGES.forEach(message -> logger.get().info(success(message)));
            boolean needBlock = rsocketModel.getConfiguration().isActivateServer();
            if (needBlock) {
                block();
            }
        }
    }

    private ValueModule value(List<ConfigurationSource> sources, ValueConfiguratorModel valueModel) {
        ValueModule value = new ValueModule();
        value.configure(configurator -> configurator.from(sources));
        ofNullable(valueModel).ifPresent(model -> value.configure(configurator -> configurator.override(model.getConfiguration())));
        return value;
    }

    private LoggingModule logging(List<ConfigurationSource> sources, LoggingConfiguratorModel loggingModel) {
        LoggingModule logging = new LoggingModule();
        logging.configure(configurator -> configurator.from(sources));
        ofNullable(loggingModel).ifPresent(model -> logging.configure(configurator -> configurator.override(model.getConfiguration())));
        return logging;
    }

    private JsonModule json(List<ConfigurationSource> sources) {
        JsonModule json = new JsonModule();
        json.configure(configurator -> configurator.from(sources));
        return json;
    }

    private XmlModule xml(List<ConfigurationSource> sources) {
        XmlModule xml = new XmlModule();
        xml.configure(configurator -> configurator.from(sources));
        return xml;
    }

    private ServerModule server(List<ConfigurationSource> sources, ServerConfiguratorModel serverModel) {
        ServerModule server = new ServerModule();
        ofNullable(serverModel).ifPresent(model -> server.configure(configurator -> configurator.from(sources).override(model.getConfiguration())));
        return server;
    }

    private CommunicatorModule communicator(List<ConfigurationSource> sources) {
        CommunicatorModule communicator = new CommunicatorModule();
        communicator.configure(configurator -> configurator.from(sources));
        return communicator;
    }

    private RsocketModule rsocket(List<ConfigurationSource> sources, ServerModel serverModel, RsocketConfiguratorModel rsocketModel) {
        RsocketModule rsocket = new RsocketModule();
        if (serverModel.getServices().stream().anyMatch(service -> service.getProtocol() == RSOCKET)) {
            rsocketModel.activateServer();
        }
        rsocket.configure(configurator -> configurator.from(sources).override(rsocketModel.getConfiguration()));
        return rsocket;
    }

    private TarantoolModule tarantool(List<ConfigurationSource> sources) {
        TarantoolModule tarantool = new TarantoolModule();
        tarantool.configure(configurator -> configurator.from(sources));
        return tarantool;
    }
}
