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

package io.art.rsocket.launcher;


import io.art.core.lazy.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.server.*;
import io.art.rsocket.state.*;
import io.rsocket.core.*;
import io.rsocket.transport.netty.client.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.rsocket.core.RSocketClient.*;
import static lombok.AccessLevel.*;
import java.util.*;

@RequiredArgsConstructor
public class RsocketManager {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketManager.class);

    private final RsocketModuleConfiguration configuration;
    private final RsocketModuleState state;
    private RsocketServer server;

    public void startConnectors() {
        for (Map.Entry<String, RsocketConnectorConfiguration> entry : configuration.getCommunicatorConfiguration().getConnectors().entrySet()) {
            RsocketConnectorConfiguration connectorConfiguration = entry.getValue();
            RSocketConnector connector = connectorConfiguration.getConnector();
            switch (connectorConfiguration.getTransport()) {
                case TCP:
                    TcpClient tcpClient = connectorConfiguration.getTcpClient();
                    int tcpMaxFrameLength = connectorConfiguration.getTcpMaxFrameLength();
                    LazyValue<RSocketClient> client = lazy(() -> from(connector.connect(TcpClientTransport.create(tcpClient, tcpMaxFrameLength))));
                    if (!connectorConfiguration.isLazy()) {
                        client.initialize();
                    }
                    state.registerClient(entry.getKey(), client);
                    return;
                case WS:
                    HttpClient httpWebSocketClient = connectorConfiguration.getHttpWebSocketClient();
                    String httpWebSocketPath = connectorConfiguration.getHttpWebSocketPath();
                    client = lazy(() -> from(connector.connect(WebsocketClientTransport.create(httpWebSocketClient, httpWebSocketPath))));
                    if (!connectorConfiguration.isLazy()) {
                        client.initialize();
                    }
                    state.registerClient(entry.getKey(), client);
                    return;
            }
        }
    }

    public void startSever() {
        (server = new RsocketServer(configuration.getServerConfiguration())).start();
    }

    public void stopSever() {
        apply(server, RsocketServer::stop);
        state.getRequesters().forEach(this::disposeRsocket);
    }

    public void stopConnectors() {
        state.getClients()
                .values()
                .stream()
                .filter(LazyValue::initialized)
                .forEach(client -> disposeRsocket(client.get()));
    }

    private void disposeRsocket(Disposable rsocket) {
        if (rsocket.isDisposed()) {
            return;
        }
        getLogger().info(RSOCKET_DISPOSING);
        ignoreException(rsocket::dispose, getLogger()::error);
    }
}

