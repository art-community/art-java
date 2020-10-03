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

package io.art.rsocket.configuration;

import com.google.common.collect.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.entity.interceptor.*;
import io.art.logging.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
import io.rsocket.RSocketFactory.*;
import io.rsocket.plugins.*;
import io.rsocket.transport.netty.server.*;
import lombok.*;
import reactor.netty.http.server.*;
import reactor.netty.tcp.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.network.selector.PortSelector.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat.*;
import static java.util.function.Function.*;
import java.util.function.*;

@Getter
public class RsocketModuleConfiguration implements ModuleConfiguration {
    private final RsocketDataFormat dataFormat = MESSAGE_PACK;
    private final String serverHost = BROADCAST_IP_ADDRESS;
    private final int serverTcpPort = findAvailableTcpPort();
    private final int serverWebSocketPort = findAvailableTcpPort();
    private final String balancerHost = LOCALHOST;
    private final int balancerTcpPort = DEFAULT_RSOCKET_TCP_PORT;
    private final int balancerWebSocketPort = DEFAULT_RSOCKET_WEB_SOCKET_PORT;
    private final boolean resumableServer = true;
    private final long serverResumeSessionDuration = DEFAULT_RSOCKET_RESUME_SESSION_DURATION;
    private final long serverResumeStreamTimeout = DEFAULT_RSOCKET_RESUME_STREAM_TIMEOUT;
    private final boolean resumableClient = true;
    private final long clientResumeSessionDuration = DEFAULT_RSOCKET_RESUME_SESSION_DURATION;
    private final long clientResumeStreamTimeout = DEFAULT_RSOCKET_RESUME_STREAM_TIMEOUT;
    private final boolean enableRawDataTracing = false;
    private final boolean enableValueTracing = false;
    private final int fragmentationMtu = 0;
    private final Function<? extends HttpServer, ? extends HttpServer> webSocketServerConfigurator = identity();
    private final Function<? extends TcpServer, ? extends TcpServer> tcpServerConfigurator = identity();
    private final Function<? extends WebsocketServerTransport, ? extends WebsocketServerTransport> webSocketServerTransportConfigurator = identity();
    private final Function<? extends TcpServerTransport, ? extends TcpServerTransport> tcpServerTransportConfigurator = identity();
    private final Function<? extends ServerRSocketFactory, ? extends ServerRSocketFactory> serverFactoryConfigurator = identity();
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
    private final ImmutableList<RSocketInterceptor> serverInterceptors = initializeInterceptors();
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
    private final ImmutableList<RSocketInterceptor> clientInterceptors = initializeInterceptors();
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
    private final ImmutableList<ValueInterceptor> requestValueInterceptors = initializeValueInterceptors();
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
    private final ImmutableList<ValueInterceptor> responseValueInterceptors = initializeValueInterceptors();

    private ImmutableList<RSocketInterceptor> initializeInterceptors() {
        return ImmutableList.of(new RsocketLoggingInterceptor());
    }

    private ImmutableList<ValueInterceptor> initializeValueInterceptors() {
        return ImmutableList.of(new LoggingValueInterceptor());
    }

    private final ImmutableMap<String, RsocketCommunicationTargetConfiguration> communicationTargets = ImmutableMap.of();

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<RsocketModuleConfiguration, Configurator> {
        private final RsocketModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            return this;
        }
    }

}
