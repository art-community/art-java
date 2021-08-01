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
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.refresher.*;
import io.art.server.configurator.*;
import io.art.server.method.*;
import lombok.*;
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
        initial.serviceMethodProviders = serverConfigurator.serviceMethods();
        initial.communicatorProxyProviders = communicatorConfigurator.communicatorProxies();
        initial.tcpConnectorConfigurations = communicatorConfigurator.configureTcp();
        initial.httpConnectorConfigurations = communicatorConfigurator.configureHttp();
        return initial;
    }

    @Getter
    public static class Initial extends RsocketModuleConfiguration {
        private boolean enableTcpServer = super.isEnableTcpServer();
        private boolean enableHttpServer = super.isEnableHttpServer();
        private RsocketTcpServerConfiguration tcpServerConfiguration = super.getTcpServerConfiguration();
        private RsocketHttpServerConfiguration httpServerConfiguration = super.getHttpServerConfiguration();
        private LazyProperty<ImmutableArray<ServiceMethod>> serviceMethodProviders = super.getServiceMethodProviders();
        private LazyProperty<ImmutableMap<Class<?>, Object>> communicatorProxyProviders = super.getCommunicatorProxyProviders();
        private ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectorConfigurations = super.getTcpConnectorConfigurations();
        private ImmutableMap<String, RsocketHttpConnectorConfiguration> httpConnectorConfigurations = super.getHttpConnectorConfigurations();

        public Initial(RsocketModuleRefresher refresher) {
            super(refresher);
        }
    }
}
