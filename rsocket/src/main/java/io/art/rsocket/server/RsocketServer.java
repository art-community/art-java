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

package io.art.rsocket.server;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.core.runnable.*;
import io.art.logging.logger.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.refresher.*;
import io.art.rsocket.socket.*;
import io.art.server.*;
import io.art.server.method.*;
import io.rsocket.*;
import io.rsocket.core.*;
import io.rsocket.transport.*;
import io.rsocket.transport.netty.server.*;
import lombok.*;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.netty.http.server.*;
import reactor.netty.tcp.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.property.Property.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.module.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.manager.RsocketManager.*;
import static java.text.MessageFormat.*;
import static java.util.function.Function.*;
import static lombok.AccessLevel.*;

@RequiredArgsConstructor
public class RsocketServer implements Server {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketServer.class);

    private final RsocketModuleConfiguration configuration;
    private final Property<CloseableChannel> tcpChannel;
    private final Property<CloseableChannel> httpChannel;

    private ImmutableMap<ServiceMethodIdentifier, ServiceMethod> serviceMethods;
    private volatile Mono<Void> tcpCloser;
    private volatile Mono<Void> httpCloser;

    public RsocketServer(RsocketModuleRefresher refresher, RsocketModuleConfiguration configuration) {
        this.configuration = configuration;
        tcpChannel = property(this::createTcpServer, this::disposeTcpServer)
                .listenConsumer(refresher.consumer()::serverConsumer)
                .initialized(this::setupTcpCloser);
        httpChannel = property(this::createHttpServer, this::disposeHttpServer)
                .listenConsumer(refresher.consumer()::serverConsumer)
                .initialized(this::setupHttpCloser);
    }

    @Override
    public void initialize() {
        serviceMethods = configuration.getServiceMethodProvider()
                .get()
                .stream()
                .collect(immutableMapCollector(ServiceMethod::getId, identity()));

        if (configuration.isEnableTcpServer()) {
            tcpChannel.initialize();
        }

        if (configuration.isEnableHttpServer()) {
            httpChannel.initialize();
        }
    }

    @Override
    public void dispose() {
        tcpChannel.dispose();
        httpChannel.dispose();
    }

    @Override
    public boolean available() {
        return tcpChannel.initialized() || httpChannel.initialized();
    }

    private CloseableChannel createTcpServer() {
        RsocketTcpServerConfiguration tcp = this.configuration.getTcpServerConfiguration();
        RsocketCommonServerConfiguration common = tcp.getCommon();
        ServerTransport<CloseableChannel> transport = TcpServerTransport.create(TcpServer.create().port(common.getPort()), tcp.getMaxFrameLength());
        return createServer(common, transport);
    }

    private CloseableChannel createHttpServer() {
        RsocketHttpServerConfiguration http = this.configuration.getHttpServerConfiguration();
        RsocketCommonServerConfiguration common = http.getCommon();
        ServerTransport<CloseableChannel> transport = WebsocketServerTransport.create(HttpServer.create().port(common.getPort()));
        return createServer(common, transport);
    }

    private CloseableChannel createServer(RsocketCommonServerConfiguration serverConfiguration, ServerTransport<CloseableChannel> transport) {
        int fragmentationMtu = serverConfiguration.getFragmentationMtu();
        RSocketServer server = RSocketServer.create((payload, requester) -> createAcceptor(payload, requester, serverConfiguration)).maxInboundPayloadSize(serverConfiguration.getMaxInboundPayloadSize());
        if (fragmentationMtu > 0) {
            server.fragment(fragmentationMtu);
        }
        apply(serverConfiguration.getResume(), resume -> server.resume(resume.toResume()));
        Mono<CloseableChannel> bind = server
                .interceptors(registry -> serverConfiguration.getInterceptors().apply(registry
                        .forResponder(new RsocketServerLoggingInterceptor(configuration, serverConfiguration))
                        .forRequester(new RsocketServerLoggingInterceptor(configuration, serverConfiguration))))
                .payloadDecoder(serverConfiguration.getPayloadDecoder())
                .bind(transport);
        if (withLogging() && serverConfiguration.isLogging()) {
            bind = bind
                    .doOnSubscribe(subscription -> getLogger().info(format(SERVER_STARTED, serverConfiguration.getHost())))
                    .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable));
        }
        return block(bind);
    }

    private void disposeTcpServer(Disposable server) {
        if (!configuration.isEnableTcpServer()) return;
        disposeRsocket(server);
        tcpCloser.block();
    }

    private void disposeHttpServer(Disposable server) {
        if (!configuration.isEnableHttpServer()) return;
        disposeRsocket(server);
        httpCloser.block();
    }

    private Mono<RSocket> createAcceptor(ConnectionSetupPayload payload, RSocket requester, RsocketCommonServerConfiguration serverConfiguration) {
        Mono<RSocket> socket = Mono.create(emitter -> setupSocket(payload, serverConfiguration, emitter));
        return socket.doOnError(throwable -> withLogging(() -> getLogger().error(throwable.getMessage(), throwable)));
    }

    private void setupSocket(ConnectionSetupPayload payload, RsocketCommonServerConfiguration serverConfiguration, MonoSink<RSocket> emitter) {
        ExceptionRunnable createRsocket = () -> emitter.success(new ServingRsocket(payload, serviceMethods, serverConfiguration));
        ignoreException(createRsocket, throwable -> getLogger().error(throwable.getMessage(), throwable));
    }

    private void setupTcpCloser(CloseableChannel channel) {
        this.tcpCloser = channel.onClose();
        if (withLogging() && configuration.getTcpServerConfiguration().getCommon().isLogging()) {
            this.tcpCloser = channel.onClose().doOnSuccess(ignore -> getLogger().info(SERVER_STOPPED));
        }
    }

    private void setupHttpCloser(CloseableChannel channel) {
        this.httpCloser = channel.onClose();
        if (withLogging() && configuration.getHttpServerConfiguration().getCommon().isLogging()) {
            this.httpCloser = channel.onClose().doOnSuccess(ignore -> getLogger().info(SERVER_STOPPED));
        }
    }
}
