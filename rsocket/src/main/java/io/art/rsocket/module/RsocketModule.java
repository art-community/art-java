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

import io.art.core.collection.*;
import io.art.core.context.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.rsocket.manager.*;
import io.art.rsocket.refresher.*;
import io.art.rsocket.state.*;
import lombok.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.context.Context.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.logging.Logging.*;
import static io.art.rsocket.configuration.RsocketModuleConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.message.RsocketMessageBuilder.*;
import static reactor.core.publisher.Hooks.*;

@Getter
public class RsocketModule implements StatefulModule<RsocketModuleConfiguration, Configurator, RsocketModuleState> {
    private static final LazyProperty<StatefulModuleProxy<RsocketModuleConfiguration, RsocketModuleState>> rsocketModule = lazy(() -> context().getStatefulModule(RsocketModule.class.getSimpleName()));
    private final String id = RsocketModule.class.getSimpleName();
    private final RsocketModuleState state = new RsocketModuleState();
    private final RsocketModuleRefresher refresher = new RsocketModuleRefresher();
    private final RsocketModuleConfiguration configuration = new RsocketModuleConfiguration(refresher);
    private final RsocketManager manager = new RsocketManager(refresher, configuration);
    private final Configurator configurator = new Configurator(configuration);

    public static StatefulModuleProxy<RsocketModuleConfiguration, RsocketModuleState> rsocketModule() {
        return rsocketModule.get();
    }

    @Override
    public void launch(ContextService contextService) {
        onErrorDropped(emptyConsumer());
        boolean hasTcpServer = configuration.isEnableTcpServer();
        boolean hasWsServer = configuration.isEnableWsServer();
        if (hasTcpServer || hasWsServer) {
            manager.initializeServers();
        }
        ImmutableMap<String, RsocketWsConnectorConfiguration> wsConnectors = configuration.getWsConnectors();
        ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectors = configuration.getTcpConnectors();
        if (!tcpConnectors.isEmpty() || !wsConnectors.isEmpty()) {
            manager.initializeCommunicators();
        }
        withLogging(() -> logger(RSOCKET_LOGGER).info(rsocketLaunchedMessage(configuration)));
    }

    @Override
    public void shutdown(ContextService contextService) {
        if (configuration.isEnableTcpServer() || configuration.isEnableWsServer()) {
            manager.disposeServers();
        }
        if (!configuration.getWsConnectors().isEmpty() || !configuration.getTcpConnectors().isEmpty()) {
            manager.disposeCommunicators();
        }
    }
}
