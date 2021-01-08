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

import io.art.core.caster.*;
import io.art.core.lazy.*;
import io.art.core.runnable.*;
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
import static io.art.core.lazy.ManagedValue.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.TransportMode.*;
import static io.art.rsocket.manager.RsocketManager.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

@RequiredArgsConstructor
public class RsocketServer implements Server {
    private final RsocketServerConfiguration configuration;

    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketServer.class);

    private final ManagedValue<CloseableChannel> channel = managed(this::createServer);

    private final ManagedValue<Mono<Void>> onClose = managed(this::onClose);

    @Override
    public void initialize() {
        channel.initialize();
    }

    @Override
    public void dispose() {
        channel.dispose(this::disposeServer);
    }

    @Override
    public boolean available() {
        return this.channel.initialized();
    }

    private CloseableChannel createServer() {
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
                .doOnTerminate(() -> getLogger().info(SERVER_STOPPED))
                .block();
    }

    private void disposeServer(Disposable server) {
        disposeRsocket(server);
        onClose.dispose(Mono::block);
    }

    private Mono<RSocket> createSocket(ConnectionSetupPayload payload, RSocket requesterSocket) {
        Logger logger = getLogger();
        Mono<ServingRsocket> socket = Mono.create(emitter -> {
            ExceptionRunnable createRsocket = () -> emitter.success(new ServingRsocket(payload, requesterSocket, configuration));
            ignoreException(createRsocket, throwable -> logger.error(throwable.getMessage(), throwable));
        });
        if (configuration.isLogging()) {
            socket = socket
                    .doOnSuccess(servingSocket -> servingSocket.onDispose(() -> logger.info(SERVER_CLIENT_DISCONNECTED)))
                    .doOnSubscribe(subscription -> logger.info(SERVER_CLIENT_CONNECTED));
        }
        return socket.doOnError(throwable -> logger.error(throwable.getMessage(), throwable)).map(Caster::cast);
    }

    private Mono<Void> onClose() {
        return channel.get().onClose();
    }
}
