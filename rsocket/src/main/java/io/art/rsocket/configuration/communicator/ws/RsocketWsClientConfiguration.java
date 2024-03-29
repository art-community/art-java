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

package io.art.rsocket.configuration.communicator.ws;

import io.art.core.changes.*;
import io.art.core.source.*;
import io.art.rsocket.refresher.*;
import io.rsocket.transport.netty.client.*;
import lombok.*;
import reactor.netty.http.client.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CommonConfigurationKeys.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RsocketWsClientConfiguration {
    @EqualsAndHashCode.Include
    private String connector;
    @EqualsAndHashCode.Include
    private int port;
    @EqualsAndHashCode.Include
    private String host;
    private String path;
    private UnaryOperator<HttpClient> clientDecorator;
    private UnaryOperator<WebsocketClientTransport> transportDecorator;

    public static RsocketWsClientConfiguration wsClientConfiguration(String connector) {
        RsocketWsClientConfiguration configuration = RsocketWsClientConfiguration.builder().build();
        configuration.path = DEFAULT_WS_PATH;
        configuration.clientDecorator = identity();
        configuration.transportDecorator = identity();
        configuration.connector = connector;
        configuration.port = DEFAULT_PORT;
        configuration.host = LOCALHOST_IP_ADDRESS;
        return configuration;
    }

    public static RsocketWsClientConfiguration wsClientConfiguration(RsocketModuleRefresher refresher, RsocketWsClientConfiguration current, ConfigurationSource source) {
        RsocketWsClientConfiguration configuration = RsocketWsClientConfiguration.builder().build();
        ChangesListener listener = refresher.connectorListeners().listenerFor(current.connector);
        configuration.connector = current.connector;
        configuration.clientDecorator = current.clientDecorator;
        configuration.transportDecorator = current.transportDecorator;
        configuration.path = orElse(source.getString(WS_PATH_KEY), current.path);
        configuration.port = listener.emit(orElse(source.getInteger(PORT_KEY), current.port));
        configuration.host = listener.emit(orElse(source.getString(HOST_KEY), current.host));
        return configuration;
    }
}
