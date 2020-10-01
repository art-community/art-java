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

import io.art.rsocket.exception.*;
import io.art.server.*;
import io.rsocket.core.*;
import io.rsocket.transport.netty.server.*;
import lombok.*;
import org.apache.logging.log4j.Logger;
import reactor.core.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.apply;
import static io.art.core.checker.NullityChecker.let;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.*;
import static java.lang.System.*;
import static java.lang.Thread.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

public class RsocketServer implements Server {
    @Getter
    private final RsocketTransport transport;
    @Getter
    private final Mono<CloseableChannel> serverChannel;
    private Disposable serverDisposable;
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(RsocketServer.class);

    private RsocketServer(RsocketTransport transport) {
        this.transport = transport;
        serverChannel = createServer();
    }

    private Mono<CloseableChannel> createServer() {
        RSocketServer.create()
    }

    public static RsocketServer rsocketTcpServer() {
        return new RsocketServer(TCP);
    }

    public static RsocketServer rsocketWebSocketServer() {
        return new RsocketServer(WEB_SOCKET);
    }

    public static RsocketServer startRsocketTcpServer() {
        RsocketServer rsocketServer = new RsocketServer(TCP);
        rsocketServer.start();
        return rsocketServer;
    }

    public static RsocketServer startRsocketWebSocketServer() {
        RsocketServer rsocketServer = new RsocketServer(WEB_SOCKET);
        rsocketServer.start();
        return rsocketServer;
    }

    @Override
    public void start() {
        final long timestamp = currentTimeMillis();
        serverDisposable = serverChannel.subscribe(serverChannel -> getLogger().info(format(transport == TCP
                        ? RSOCKET_TCP_ACCEPTOR_STARTED_MESSAGE
                        : RSOCKET_WS_ACCEPTOR_STARTED_MESSAGE,
                currentTimeMillis() - timestamp)));
    }

    @Override
    public void stop() {
        apply(serverDisposable, Disposable::dispose);
    }

    @Override
    public void await() {
        try {
            currentThread().join();
        } catch (InterruptedException throwable) {
            throw new RsocketServerException(throwable);
        }
    }

    @Override
    public void restart() {
        long millis = currentTimeMillis();
        try {
            if (nonNull(serverDisposable)) {
                serverDisposable.dispose();
            }
            new RsocketServer(transport).start();
            getLogger().info(format(RSOCKET_RESTARTED_MESSAGE, currentTimeMillis() - millis));
        } catch (Throwable throwable) {
            getLogger().error(RSOCKET_RESTART_FAILED);
        }
    }

    public boolean available() {
        return nonNull(serverDisposable);
    }
}
