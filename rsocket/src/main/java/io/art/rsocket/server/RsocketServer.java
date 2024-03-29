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

package io.art.rsocket.server;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.core.runnable.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.common.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.refresher.*;
import io.art.rsocket.socket.*;
import io.art.server.*;
import io.art.server.method.*;
import io.netty.handler.ssl.*;
import io.rsocket.*;
import io.rsocket.core.*;
import io.rsocket.plugins.*;
import io.rsocket.transport.*;
import io.rsocket.transport.netty.server.*;
import lombok.*;
import reactor.core.*;
import reactor.core.publisher.*;
import reactor.netty.http.server.*;
import reactor.netty.tcp.SslProvider;
import reactor.netty.tcp.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.property.LazyProperty.lazy;
import static io.art.core.property.Property.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.rsocket.configuration.server.RsocketCommonServerConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Messages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.manager.RsocketManager.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static java.util.function.Function.*;
import java.io.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public class RsocketServer implements Server {
    private static final LazyProperty<Logger> logger = lazy(() -> Logging.logger(RSOCKET_SERVER_LOGGER));

    private final RsocketModuleConfiguration configuration;
    private final Property<CloseableChannel> tcpChannel;
    private final Property<CloseableChannel> wsChannel;

    private ImmutableMap<ServiceMethodIdentifier, ServiceMethod> serviceMethods;
    private volatile Mono<Void> tcpCloser;
    private volatile Mono<Void> wsCloser;

    public RsocketServer(RsocketModuleRefresher refresher, RsocketModuleConfiguration configuration) {
        this.configuration = configuration;
        tcpChannel = property(this::createTcpServer, this::disposeTcpServer)
                .listenConsumer(refresher.consumer()::serverConsumer)
                .initialized(this::setupTcpCloser);
        wsChannel = property(this::createWsServer, this::disposeWsServer)
                .listenConsumer(refresher.consumer()::serverConsumer)
                .initialized(this::setupWsCloser);
    }

    @Override
    public void initialize() {
        serviceMethods = configuration.getServer()
                .getMethods()
                .get()
                .values()
                .stream()
                .collect(immutableMapCollector(ServiceMethod::getId, identity()));

        if (configuration.isEnableTcpServer()) {
            tcpChannel.initialize();
        }

        if (configuration.isEnableWsServer()) {
            wsChannel.initialize();
        }
    }

    @Override
    public void dispose() {
        tcpChannel.dispose();
        wsChannel.dispose();
    }

    @Override
    public boolean available() {
        return tcpChannel.initialized() || wsChannel.initialized();
    }

    private CloseableChannel createTcpServer() {
        RsocketTcpServerConfiguration tcp = this.configuration.getTcpServer();
        RsocketCommonServerConfiguration common = fromTcp(tcp);
        UnaryOperator<TcpServer> tcpDecorator = tcp.getTcpDecorator();
        TcpServer server = TcpServer.create().port(common.getPort());
        RsocketSslConfiguration ssl = common.getSsl();
        if (nonNull(ssl)) createSslContext(ssl).ifPresent(server::secure);
        ServerTransport<CloseableChannel> transport = TcpServerTransport.create(tcpDecorator.apply(server), tcp.getMaxFrameLength());
        return createServer(common, transport);
    }

    private CloseableChannel createWsServer() {
        RsocketWsServerConfiguration ws = this.configuration.getWsServer();
        RsocketCommonServerConfiguration common = fromWs(ws);
        UnaryOperator<HttpServer> wsDecorator = ws.getWsDecorator();
        HttpServer server = HttpServer.create().port(common.getPort());
        RsocketSslConfiguration ssl = common.getSsl();
        if (nonNull(ssl)) createSslContext(ssl).ifPresent(server::secure);
        ServerTransport<CloseableChannel> transport = WebsocketServerTransport.create(wsDecorator.apply(server));
        return createServer(common, transport);
    }

    private CloseableChannel createServer(RsocketCommonServerConfiguration serverConfiguration, ServerTransport<CloseableChannel> transport) {
        int fragmentationMtu = serverConfiguration.getFragmentationMtu();
        RSocketServer server = RSocketServer
                .create((payload, requester) -> createAcceptor(payload, serverConfiguration))
                .maxInboundPayloadSize(serverConfiguration.getMaxInboundPayloadSize());
        if (fragmentationMtu > 0) {
            server.fragment(fragmentationMtu);
        }
        apply(serverConfiguration.getResume(), resume -> server.resume(resume.toResume()));
        Mono<CloseableChannel> bind = serverConfiguration.getDecorator().apply(server)
                .interceptors(registry -> configureInterceptors(serverConfiguration, registry))
                .payloadDecoder(serverConfiguration.getPayloadDecoder())
                .bind(transport);
        if (withLogging()) {
            bind = bind.doOnError(throwable -> logger.get().error(throwable.getMessage(), throwable));
        }
        return block(bind);
    }

    private void configureInterceptors(RsocketCommonServerConfiguration serverConfiguration, InterceptorRegistry registry) {
        UnaryOperator<InterceptorRegistry> interceptors = serverConfiguration.getInterceptors();
        if (withLogging()) {
            interceptors.apply(registry
                    .forResponder(new RsocketServerLoggingInterceptor(configuration, serverConfiguration))
                    .forRequester(new RsocketServerLoggingInterceptor(configuration, serverConfiguration)));
        }
    }

    private Optional<SslProvider> createSslContext(RsocketSslConfiguration ssl) {
        try {
            File certificate = ssl.getCertificate();
            File key = ssl.getKey();
            if (isNull(certificate) || !certificate.exists() || isNull(key) || !key.exists()) {
                return empty();
            }
            SslContextBuilder sslBuilder = SslContextBuilder.forServer(certificate, key);
            String password = ssl.getPassword();
            if (isNotEmpty(password)) {
                sslBuilder = SslContextBuilder.forServer(certificate, key, password);
            }
            return of(SslProvider.builder()
                    .sslContext(sslBuilder.build())
                    .build());
        } catch (Throwable throwable) {
            throw new RsocketException(throwable);
        }
    }

    private void disposeTcpServer(Disposable server) {
        if (!configuration.isEnableTcpServer()) return;
        disposeRsocket(server);
        tcpCloser.block();
    }

    private void disposeWsServer(Disposable server) {
        if (!configuration.isEnableWsServer()) return;
        disposeRsocket(server);
        wsCloser.block();
    }

    private Mono<RSocket> createAcceptor(ConnectionSetupPayload payload, RsocketCommonServerConfiguration serverConfiguration) {
        Mono<RSocket> socket = Mono.create(emitter -> setupSocket(payload, serverConfiguration, emitter));
        if (withLogging()) {
            return socket.doOnError(throwable -> logger.get().error(throwable.getMessage(), throwable));
        }
        return socket;
    }

    private void setupSocket(ConnectionSetupPayload payload, RsocketCommonServerConfiguration serverConfiguration, MonoSink<RSocket> emitter) {
        ExceptionRunnable createRsocket = () -> emitter.success(new ServingRsocket(payload, serviceMethods, serverConfiguration));
        ignoreException(createRsocket, throwable -> withLogging(() -> logger.get().error(throwable)));
    }

    private void setupTcpCloser(CloseableChannel channel) {
        this.tcpCloser = channel.onClose();
        RsocketTcpServerConfiguration serverConfiguration = configuration.getTcpServer();
        if (withLogging() && serverConfiguration.isVerbose()) {
            String message = format(RSOCKET_SERVER_STOPPED, TCP_SERVER_TYPE, serverConfiguration.getHost(), EMPTY_STRING + serverConfiguration.getPort());
            this.tcpCloser = channel.onClose().doOnSuccess(ignore -> logger.get().info(message));
        }
    }

    private void setupWsCloser(CloseableChannel channel) {
        this.wsCloser = channel.onClose();
        RsocketWsServerConfiguration serverConfiguration = configuration.getWsServer();
        if (withLogging() && serverConfiguration.isVerbose()) {
            String message = format(RSOCKET_SERVER_STOPPED, WS_SERVER_TYPE, serverConfiguration.getHost(), EMPTY_STRING + serverConfiguration.getPort());
            this.wsCloser = channel.onClose().doOnSuccess(ignore -> logger.get().info(message));
        }
    }

}
