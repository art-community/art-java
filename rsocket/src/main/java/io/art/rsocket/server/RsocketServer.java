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

package io.art.rsocket.server;

import io.art.core.atomic.*;
import io.art.core.caster.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.socket.*;
import io.art.server.*;
import io.rsocket.*;
import io.rsocket.core.*;
import io.rsocket.transport.*;
import io.rsocket.transport.netty.server.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.*;
import reactor.core.publisher.*;
import static io.art.core.atomic.AtomicValue.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.TransportMode.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

@RequiredArgsConstructor
public class RsocketServer implements Server {
    private final RsocketServerConfiguration configuration;

    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketServer.class);

    private final AtomicValue<Disposable> server = atomic();

    @Override
    public void start() {
        server.initialize(this::createServer);
    }

    @Override
    public void stop() {
        server.dispose(this::disposeServer);
    }

    @Override
    public boolean available() {
        Disposable value;
        return nonNull(value = server.get()) && !value.isDisposed();
    }

    private Disposable createServer() {
        TransportMode transportMode = configuration.getTransport();
        RSocketServer server = RSocketServer
                .create(this::createSocket)
                .fragment(configuration.getMaxInboundPayloadSize());
        if (configuration.getFragmentationMtu() > 0) {
            server.fragment(configuration.getFragmentationMtu());
        }
        Resume resume;
        if (nonNull(resume = configuration.getResume())) {
            server.resume(resume);
        }
        ServerTransport<CloseableChannel> transport = transportMode == TCP
                ? TcpServerTransport.create(configuration.getTcpServer(), configuration.getTcpMaxFrameLength())
                : WebsocketServerTransport.create(configuration.getHttpWebSocketServer());
        return server
                .interceptors(interceptorRegistry -> configuration.getInterceptorConfigurator().accept(interceptorRegistry))
                .payloadDecoder(configuration.getPayloadDecoder())
                .bind(transport)
                .doOnSubscribe(subscription -> getLogger().info(SERVER_STARTED))
                .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable))
                .subscribe();
    }

    private void disposeServer(Disposable value) {
        value.dispose();
        getLogger().info(SERVER_STOPPED);
    }

    private Mono<RSocket> createSocket(ConnectionSetupPayload payload, RSocket requesterSocket) {
        Mono<ServingRsocket> socket = Mono.create(emitter -> emitter.success(new ServingRsocket(payload, requesterSocket, configuration)));
        Logger logger = getLogger();
        if (configuration.isLogging()) {
            socket = socket
                    .doOnSuccess(servingSocket -> servingSocket.onDispose(() -> logger.info(SERVER_CLIENT_DISCONNECTED)))
                    .doOnSubscribe(subscription -> logger.info(SERVER_CLIENT_CONNECTED));
        }
        return socket.doOnError(throwable -> logger.error(throwable.getMessage(), throwable)).map(Caster::cast);
    }
}
