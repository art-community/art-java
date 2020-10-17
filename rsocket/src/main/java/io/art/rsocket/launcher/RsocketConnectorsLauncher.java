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
import io.art.rsocket.state.*;
import io.rsocket.core.*;
import io.rsocket.transport.netty.client.*;
import lombok.experimental.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.*;
import static io.art.core.lazy.LazyValue.*;
import static io.rsocket.core.RSocketClient.*;
import java.util.*;

@UtilityClass
public class RsocketConnectorsLauncher {
    public static void launchRsocketConnectors(RsocketCommunicatorConfiguration configuration, RsocketModuleState state) {
        for (Map.Entry<String, RsocketConnectorConfiguration> entry : configuration.getConnectors().entrySet()) {
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
                case WEB_SOCKET:
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
}

