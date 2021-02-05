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

import io.art.core.property.*;
import io.art.core.runnable.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.listener.*;
import io.art.rsocket.socket.*;
import io.art.server.*;
import io.rsocket.*;
import io.rsocket.core.*;
import io.rsocket.plugins.*;
import io.rsocket.transport.*;
import io.rsocket.transport.netty.server.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.*;
import reactor.core.publisher.*;
import static io.art.core.property.Property.*;
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
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketServer.class);

    private final RsocketModuleConfiguration configuration;
    private final Property<CloseableChannel> channel;

    public RsocketServer(RsocketModuleRefresher refresher, RsocketModuleConfiguration configuration) {
        this.configuration = configuration;
        channel = managed(this::createServer).listen(refresher.serverListener()).disposer(this::disposeServer);
    }

    @Override
    public void initialize() {
        channel.initialize();
    }

    @Override
    public void dispose() {
        channel.dispose();
    }

    @Override
    public boolean available() {
        return channel.initialized();
    }

    private CloseableChannel createServer() {
        RsocketServerConfiguration configuration = this.configuration.getServerConfiguration();
        TransportMode transportMode = configuration.getTransport();
        int fragmentationMtu = configuration.getFragmentationMtu();
        RSocketServer server = RSocketServer.create(this::createSocket).fragment(configuration.getMaxInboundPayloadSize());
        if (fragmentationMtu > 0) {
            server.fragment(fragmentationMtu);
        }
        Resume resume;
        if (nonNull(resume = configuration.getResume())) {
            server.resume(resume);
        }
        ServerTransport<CloseableChannel> transport = transportMode == TCP
                ? TcpServerTransport.create(configuration.getTcpServer(), configuration.getTcpMaxFrameLength())
                : WebsocketServerTransport.create(configuration.getHttpWebSocketServer());
        Mono<CloseableChannel> bind = server
                .interceptors(this::configureInterceptors)
                .payloadDecoder(configuration.getPayloadDecoder())
                .bind(transport);
        if (configuration.isLogging()) {
            bind = bind
                    .doOnSubscribe(subscription -> getLogger().info(SERVER_STARTED))
                    .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable));
        }
        return bind.block();
    }

    private void configureInterceptors(InterceptorRegistry registry) {
        registry
                .forResponder(new RsocketServerLoggingInterceptor())
                .forRequester(new RsocketServerLoggingInterceptor());
    }

    private void disposeServer(Disposable server) {
        disposeRsocket(server);
        Mono<Void> onClose = channel.get().onClose();
        if (configuration.getServerConfiguration().isLogging()) {
            onClose = onClose.doOnSuccess(ignore -> getLogger().info(SERVER_STOPPED));
        }
        onClose.block();
    }

    private Mono<RSocket> createSocket(ConnectionSetupPayload payload, RSocket requesterSocket) {
        Logger logger = getLogger();
        Mono<RSocket> socket = Mono.create(emitter -> processCreation(payload, requesterSocket, emitter));
        return socket.doOnError(throwable -> logger.error(throwable.getMessage(), throwable));
    }

    private void processCreation(ConnectionSetupPayload payload, RSocket requester, MonoSink<RSocket> emitter) {
        Logger logger = getLogger();
        ExceptionRunnable createRsocket = () -> emitter.success(new ServingRsocket(payload, requester, configuration.getServerConfiguration()));
        ignoreException(createRsocket, throwable -> logger.error(throwable.getMessage(), throwable));
    }
}
