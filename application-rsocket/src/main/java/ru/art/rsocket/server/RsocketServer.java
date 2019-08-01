package ru.art.rsocket.server;

import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Mono;
import ru.art.rsocket.exception.RsocketServerException;
import ru.art.rsocket.socket.AcceptorRsocket;
import ru.art.rsocket.specification.RsocketServiceSpecification;
import static io.rsocket.RSocketFactory.ServerTransportAcceptor;
import static io.rsocket.RSocketFactory.receive;
import static java.lang.System.currentTimeMillis;
import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;
import static reactor.core.publisher.Mono.just;
import static ru.art.core.extension.ThreadExtensions.thread;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.RSOCKET_RESTART_FAILED;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.UNSUPPORTED_TRANSPORT;
import static ru.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.TCP;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.WEB_SOCKET;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;
import static ru.art.rsocket.module.RsocketModule.rsocketModuleState;
import static ru.art.service.ServiceModule.serviceModule;

public class RsocketServer {
    @Getter
    private final Mono<CloseableChannel> server;
    private final long timestamp = currentTimeMillis();
    private final RsocketTransport transport;
    private final Logger logger = loggingModule().getLogger(RsocketServer.class);

    private RsocketServer(RsocketTransport transport) {
        this.transport = transport;
        this.server = createServer();
        rsocketModuleState().setServer(this);
    }

    public static RsocketServer rsocketTcpServer() {
        return new RsocketServer(TCP);
    }

    public static RsocketServer rsocketWebSocketServer() {
        return new RsocketServer(WEB_SOCKET);
    }

    private Mono<CloseableChannel> createServer() {
        ServerTransportAcceptor acceptor = receive()
                .resume()
                .acceptor((setup, sendingSocket) -> just(new AcceptorRsocket(sendingSocket, setup)));
        switch (transport) {
            case TCP:
                return acceptor.transport(TcpServerTransport.create(rsocketModule().getAcceptorHost(), rsocketModule().getAcceptorTcpPort())).start().onTerminateDetach();
            case WEB_SOCKET:
                return acceptor.transport(WebsocketServerTransport.create(rsocketModule().getAcceptorHost(), rsocketModule().getAcceptorWebSocketPort())).start().onTerminateDetach();
        }
        throw new RsocketServerException(format(UNSUPPORTED_TRANSPORT, transport));
    }

    public static RsocketServer rsocketTcpServerInSeparatedThread() {
        RsocketServer rsocketServer = rsocketTcpServer();
        thread(RSOCKET_SERVER_THREAD, rsocketServer::await);
        return rsocketServer;
    }

    public static RsocketServer rsocketWebSocketServerInSeparatedThread() {
        RsocketServer rsocketServer = rsocketWebSocketServer();
        thread(RSOCKET_SERVER_THREAD, rsocketServer::await);
        return rsocketServer;
    }

    public void await() {
        server.doOnSubscribe(subscription -> serviceModule()
                .getServiceRegistry()
                .getServices()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().getServiceTypes().contains(RSOCKET_SERVICE_TYPE))
                .forEach(entry -> logger.info(format(RSOCKET_LOADED_SERVICE_MESSAGE,
                        rsocketModule().getAcceptorHost(),
                        transport == TCP ? rsocketModule().getAcceptorTcpPort() : rsocketModule().getAcceptorWebSocketPort(),
                        entry.getKey(),
                        ((RsocketServiceSpecification) entry.getValue()).getRsocketService().getRsocketMethods().keySet()))))
                .subscribe(channel -> logger
                        .info(format(transport == TCP ? RSOCKET_TCP_ACCEPTOR_STARTED_MESSAGE : RSOCKET_WS_ACCEPTOR_STARTED_MESSAGE, currentTimeMillis() - timestamp)));
    }

    public void restart() {
        long millis = currentTimeMillis();
        try {
            CloseableChannel channel = server.block();
            if (nonNull(channel)) {
                channel.dispose();
            }
            new RsocketServer(transport).await();
            logger.info(format(RSOCKET_RESTARTED_MESSAGE, currentTimeMillis() - millis));
        } catch (Exception e) {
            logger.error(RSOCKET_RESTART_FAILED);
        }

    }
}
