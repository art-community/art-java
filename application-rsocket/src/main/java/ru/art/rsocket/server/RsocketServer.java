/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.rsocket.server;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Mono;
import ru.art.rsocket.exception.RsocketServerException;
import ru.art.rsocket.socket.RsocketAcceptor;
import ru.art.rsocket.specification.RsocketServiceSpecification;
import static io.rsocket.RSocketFactory.ServerTransportAcceptor;
import static io.rsocket.RSocketFactory.receive;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.currentThread;
import static java.text.MessageFormat.format;
import static java.time.Duration.ofMillis;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static reactor.core.publisher.Mono.just;
import static ru.art.core.constants.NetworkConstants.BROADCAST_IP_ADDRESS;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.RSOCKET_RESTART_FAILED;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.UNSUPPORTED_TRANSPORT;
import static ru.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_SERVICE_TYPE;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.TCP;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.WEB_SOCKET;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;
import static ru.art.rsocket.module.RsocketModule.rsocketModuleState;
import static ru.art.service.ServiceModule.serviceModule;

public class RsocketServer {
    @Getter
    private final Mono<CloseableChannel> channel;
    private final RsocketTransport transport;
    private final Logger logger = loggingModule().getLogger(RsocketServer.class);

    private RsocketServer(RsocketTransport transport) {
        this.transport = transport;
        this.channel = createServer();
        rsocketModuleState().setServer(this);
    }

    private Mono<CloseableChannel> createServer() {
        RSocketFactory.ServerRSocketFactory socketFactory = receive();
        if (rsocketModule().isResumableServer()) {
            socketFactory = socketFactory.resume().resumeSessionDuration(ofMillis(rsocketModule().getServerResumeSessionDuration()));
        }
        rsocketModule().getClientInterceptors().forEach(socketFactory::addResponderPlugin);
        ServerTransportAcceptor acceptor = socketFactory.acceptor((setup, sendingSocket) -> just(new RsocketAcceptor(sendingSocket, setup)));
        Mono<CloseableChannel> channel;
        switch (transport) {
            case TCP:
                channel = acceptor.transport(TcpServerTransport.create(rsocketModule().getServerHost(),
                        rsocketModule().getServerTcpPort()))
                        .start()
                        .onTerminateDetach();
                break;
            case WEB_SOCKET:
                channel = acceptor.transport(WebsocketServerTransport.create(rsocketModule().getServerHost(),
                        rsocketModule().getServerWebSocketPort()))
                        .start()
                        .onTerminateDetach();
                break;
            default:
                throw new RsocketServerException(format(UNSUPPORTED_TRANSPORT, transport));
        }
        return channel.doOnSubscribe(subscription -> serviceModule()
                .getServiceRegistry()
                .getServices()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getServiceTypes().contains(RSOCKET_SERVICE_TYPE))
                .forEach(entry -> logger.info(format(RSOCKET_LOADED_SERVICE_MESSAGE,
                        rsocketModule().getServerHost().equals(BROADCAST_IP_ADDRESS)
                                ? contextConfiguration().getIpAddress()
                                : rsocketModule().getServerHost(),
                        transport == TCP
                                ? rsocketModule().getServerTcpPort()
                                : rsocketModule().getServerWebSocketPort(),
                        entry.getKey(),
                        ((RsocketServiceSpecification) entry.getValue()).getRsocketService().getRsocketMethods().keySet()))));
    }

    public static RsocketServer rsocketTcpServer() {
        return new RsocketServer(TCP);
    }

    public static RsocketServer rsocketWebSocketServer() {
        return new RsocketServer(WEB_SOCKET);
    }

    public static RsocketServer startRsocketTcpServer() {
        RsocketServer rsocketServer = new RsocketServer(TCP);
        rsocketServer.subscribe();
        return rsocketServer;
    }

    public static RsocketServer startRsocketWebSocketServer() {
        RsocketServer rsocketServer = new RsocketServer(WEB_SOCKET);
        rsocketServer.subscribe();
        return rsocketServer;
    }

    public void subscribe() {
        final long timestamp = currentTimeMillis();
        channel.subscribe(serverChannel -> logger
                .info(format(transport == TCP
                                ? RSOCKET_TCP_ACCEPTOR_STARTED_MESSAGE
                                : RSOCKET_WS_ACCEPTOR_STARTED_MESSAGE,
                        currentTimeMillis() - timestamp)));
    }

    public void await() {
        try {
            currentThread().join();
        } catch (InterruptedException e) {
            throw new RsocketServerException(e);
        }
    }

    public void restart() {
        long millis = currentTimeMillis();
        try {
            CloseableChannel blockedChannel = this.channel.block();
            if (nonNull(blockedChannel)) {
                blockedChannel.dispose();
            }
            new RsocketServer(transport).subscribe();
            logger.info(format(RSOCKET_RESTARTED_MESSAGE, currentTimeMillis() - millis));
        } catch (Throwable e) {
            logger.error(RSOCKET_RESTART_FAILED);
        }

    }

    public boolean isWorking() {
        CloseableChannel blockedChannel = channel.block();
        if (isNull(blockedChannel)) return false;
        return !blockedChannel.isDisposed();
    }
}
