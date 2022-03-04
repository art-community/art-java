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

package io.art.rsocket.configuration;

import io.art.communicator.configuration.*;
import io.art.communicator.model.*;
import io.art.communicator.refresher.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.refresher.*;
import io.art.server.configuration.*;
import io.art.server.refresher.*;
import lombok.*;
import static io.art.communicator.configuration.CommunicatorConfiguration.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpConnectorConfiguration.tcpConnectorConfiguration;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsConnectorConfiguration.wsConnectorConfiguration;
import static io.art.rsocket.configuration.server.RsocketTcpServerConfiguration.*;
import static io.art.rsocket.configuration.server.RsocketWsServerConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.server.configuration.ServerConfiguration.*;
import static io.art.server.constants.ServerConstants.ConfigurationKeys.*;
import static java.util.Optional.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class RsocketModuleConfiguration implements ModuleConfiguration {
    private final RsocketModuleRefresher refresher;
    private final ServerRefresher serverRefresher;
    private final CommunicatorRefresher communicatorRefresher;

    @Getter
    private RsocketModuleRefresher.Consumer consumer;

    @Getter
    private ServerConfiguration server;

    @Getter
    private boolean enableTcpServer;

    @Getter
    private boolean enableWsServer;

    @Getter
    private RsocketTcpServerConfiguration tcpServer;

    @Getter
    private RsocketWsServerConfiguration wsServer;

    @Getter
    private CommunicatorConfiguration communicator;

    @Getter
    private ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectors;

    @Getter
    private ImmutableMap<String, RsocketWsConnectorConfiguration> wsConnectors;

    public RsocketTcpConnectorConfiguration tcpConnector(ConnectorIdentifier id) {
        return tcpConnectors.get(id.id());
    }

    public RsocketWsConnectorConfiguration wsConnector(ConnectorIdentifier id) {
        return wsConnectors.get(id.id());
    }

    public RsocketModuleConfiguration(RsocketModuleRefresher refresher) {
        this.refresher = refresher;
        serverRefresher = new ServerRefresher();
        communicatorRefresher = new CommunicatorRefresher();
        consumer = refresher.consumer();

        server = serverConfiguration(serverRefresher);
        tcpServer = tcpServerConfiguration();
        wsServer = wsServerConfiguration();
        enableTcpServer = false;
        enableWsServer = false;

        communicator = communicatorConfiguration(communicatorRefresher);
        tcpConnectors = emptyImmutableMap();
        wsConnectors = emptyImmutableMap();
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<RsocketModuleConfiguration, Configurator> {
        private final RsocketModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            Optional<NestedConfiguration> serverSection = ofNullable(source.getNested(RSOCKET_SECTION))
                    .map(rsocket -> rsocket.getNested(SERVER_SECTION));
            serverSection
                    .map(this::tcpServer)
                    .ifPresent(server -> configuration.tcpServer = server);
            serverSection
                    .map(this::wsServer)
                    .ifPresent(server -> configuration.wsServer = server);
            serverSection
                    .map(this::server)
                    .ifPresent(server -> configuration.server = server);

            Optional<NestedConfiguration> communicatorSection = ofNullable(source.getNested(RSOCKET_SECTION))
                    .map(rsocket -> rsocket.getNested(COMMUNICATOR_SECTION));
            communicatorSection
                    .map(this::tcpConnectors)
                    .ifPresent(connectors -> configuration.tcpConnectors = merge(configuration.tcpConnectors, connectors));
            communicatorSection
                    .map(this::wsConnectors)
                    .ifPresent(connectors -> configuration.wsConnectors = merge(configuration.wsConnectors, connectors));
            communicatorSection
                    .map(this::communicator)
                    .ifPresent(communicator -> configuration.communicator = communicator);

            configuration.refresher.produce();
            configuration.serverRefresher.produce();
            configuration.communicatorRefresher.produce();
            return this;
        }

        private ServerConfiguration server(NestedConfiguration server) {
            return serverConfiguration(configuration.serverRefresher, configuration.server, server);
        }

        private RsocketWsServerConfiguration wsServer(NestedConfiguration server) {
            return wsServerConfiguration(configuration.refresher, configuration.wsServer, server);
        }

        private RsocketTcpServerConfiguration tcpServer(NestedConfiguration server) {
            return tcpServerConfiguration(configuration.refresher, configuration.tcpServer, server);
        }


        private CommunicatorConfiguration communicator(NestedConfiguration communicator) {
            return communicatorConfiguration(configuration.communicatorRefresher, configuration.communicator, communicator);
        }

        private ImmutableMap<String, RsocketWsConnectorConfiguration> wsConnectors(NestedConfiguration communicator) {
            return communicator.getNestedMap(CONNECTORS_SECTION, nested -> wsConnector(nested, configuration.wsConnectors.get(nested.getParent())));
        }

        private RsocketWsConnectorConfiguration wsConnector(NestedConfiguration nested, RsocketWsConnectorConfiguration current) {
            return wsConnectorConfiguration(configuration.refresher, current, nested);
        }

        private ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectors(NestedConfiguration communicator) {
            return communicator.getNestedMap(CONNECTORS_SECTION, nested -> tcpConnector(nested, configuration.tcpConnectors.get(nested.getParent())));
        }

        private RsocketTcpConnectorConfiguration tcpConnector(NestedConfiguration nested, RsocketTcpConnectorConfiguration current) {
            return tcpConnectorConfiguration(configuration.refresher, current, nested);
        }


        @Override
        public Configurator initialize(RsocketModuleConfiguration configuration) {
            this.configuration.enableTcpServer = configuration.isEnableTcpServer();
            this.configuration.enableWsServer = configuration.isEnableWsServer();

            this.configuration.server = configuration.getServer();
            this.configuration.tcpServer = configuration.getTcpServer();
            this.configuration.wsServer = configuration.getWsServer();

            this.configuration.communicator = configuration.getCommunicator();
            this.configuration.tcpConnectors = configuration.getTcpConnectors();
            this.configuration.wsConnectors = configuration.getWsConnectors();
            return this;
        }
    }

}
