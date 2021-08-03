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

package io.art.rsocket.module;

import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.http.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.refresher.*;
import io.art.server.configurator.*;
import io.art.server.method.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import java.util.function.*;

public class RsocketInitializer implements ModuleInitializer<RsocketModuleConfiguration, RsocketModuleConfiguration.Configurator, RsocketModule> {
    private final RsocketServerConfigurator serverConfigurator = new RsocketServerConfigurator();
    private final RsocketCommunicatorConfigurator communicatorConfigurator = new RsocketCommunicatorConfigurator();

    public RsocketInitializer server(Function<RsocketServerConfigurator, ? extends ServerConfigurator> configurator) {
        configurator.apply(serverConfigurator);
        return this;
    }

    public RsocketInitializer communicator(Function<RsocketCommunicatorConfigurator, ? extends CommunicatorConfigurator> configurator) {
        configurator.apply(communicatorConfigurator);
        return this;
    }

    @Override
    public RsocketModuleConfiguration initialize(RsocketModule module) {
        Initial initial = new Initial(module.getRefresher());
        initial.enableTcpServer = serverConfigurator.enableTcp();
        initial.enableHttpServer = serverConfigurator.enableHttp();
        initial.tcpServerConfiguration = serverConfigurator.configure(initial.tcpServerConfiguration);
        initial.tcpServerConfiguration = serverConfigurator.configure(initial.tcpServerConfiguration);
        initial.httpServerConfiguration = serverConfigurator.configure(initial.httpServerConfiguration);
        initial.serviceMethodProvider = serverConfigurator.serviceMethods();
        initial.tcpConnectorConfigurations = communicatorConfigurator.configureTcp();
        initial.httpConnectorConfigurations = communicatorConfigurator.configureHttp();
        initial.connectorProvider = cast(communicatorConfigurator.connectors());
        initial.communicatorProvider = cast(communicatorConfigurator.communicators());
        return initial;
    }

    @Getter
    public static class Initial extends RsocketModuleConfiguration {
        private boolean enableTcpServer = super.isEnableTcpServer();
        private boolean enableHttpServer = super.isEnableHttpServer();
        private RsocketTcpServerConfiguration tcpServerConfiguration = super.getTcpServer();
        private RsocketHttpServerConfiguration httpServerConfiguration = super.getHttpServer();
        private LazyProperty<ImmutableArray<ServiceMethod>> serviceMethodProvider = super.getServiceMethods();
        private ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectorConfigurations = super.getTcpConnector();
        private ImmutableMap<String, RsocketHttpConnectorConfiguration> httpConnectorConfigurations = super.getHttpConnector();
        private LazyProperty<ImmutableMap<Class<?>, ? extends Connector>> connectorProvider = super.getConnectors();
        private LazyProperty<ImmutableMap<Class<?>, ? extends Communicator>> communicatorProvider = super.getCommunicators();

        public Initial(RsocketModuleRefresher refresher) {
            super(refresher);
        }
    }
}
