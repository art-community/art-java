/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.module;

import io.art.communicator.configuration.*;
import io.art.communicator.configurator.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.refresher.*;
import io.art.server.configuration.*;
import io.art.server.configurator.*;
import lombok.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.util.function.*;

@Public
public class RsocketInitializer implements ModuleInitializer<RsocketModuleConfiguration, RsocketModuleConfiguration.Configurator, RsocketModule> {
    private final RsocketServerConfigurator serverConfigurator = new RsocketServerConfigurator();
    private final RsocketCommunicatorConfigurator communicatorConfigurator = new RsocketCommunicatorConfigurator();

    public RsocketInitializer server(Function<RsocketServerConfigurator, ? extends ServerConfigurator<RsocketServerConfigurator>> configurator) {
        configurator.apply(serverConfigurator);
        return this;
    }

    public RsocketInitializer communicator(Function<RsocketCommunicatorConfigurator, ? extends CommunicatorConfigurator<RsocketCommunicatorConfigurator>> configurator) {
        configurator.apply(communicatorConfigurator);
        return this;
    }

    @Override
    public RsocketModuleConfiguration initialize(RsocketModule module) {
        Initial initial = new Initial(module.getRefresher());

        initial.enableTcpServer = serverConfigurator.enableTcp();
        initial.enableWsServer = serverConfigurator.enableWs();
        initial.tcpServer = serverConfigurator.configureTcp(initial.tcpServer);
        initial.wsServer = serverConfigurator.configureWs(initial.wsServer);
        initial.server = serverConfigurator.configure(lazy(() -> rsocketModule().configuration().getServer()), initial.server);

        initial.tcpConnectors = communicatorConfigurator.tcpConnectors();
        initial.wsConnectors = communicatorConfigurator.wsConnectors();
        initial.communicator = communicatorConfigurator.configure(lazy(() -> rsocketModule().configuration().getCommunicator()), initial.communicator);

        return initial;
    }

    @Getter
    public static class Initial extends RsocketModuleConfiguration {
        private boolean enableTcpServer = super.isEnableTcpServer();
        private boolean enableWsServer = super.isEnableWsServer();
        private RsocketTcpServerConfiguration tcpServer = super.getTcpServer();
        private RsocketWsServerConfiguration wsServer = super.getWsServer();
        private ServerConfiguration server = super.getServer();

        private ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectors = super.getTcpConnectors();
        private ImmutableMap<String, RsocketWsConnectorConfiguration> wsConnectors = super.getWsConnectors();
        private CommunicatorConfiguration communicator = super.getCommunicator();

        public Initial(RsocketModuleRefresher refresher) {
            super(refresher);
        }
    }
}
