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

import io.art.rsocket.configuration.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.socket.*;
import io.art.server.*;
import io.rsocket.core.*;
import io.rsocket.transport.netty.server.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.lang.Thread.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

@RequiredArgsConstructor
public class RsocketServer implements Server {
    @Getter
    private final RsocketTransport transport;

    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(RsocketServer.class);

    private Disposable disposable;

    @Override
    public void start() {
        String message = transport == TCP ? RSOCKET_TCP_ACCEPTOR_STARTED_MESSAGE : RSOCKET_WS_ACCEPTOR_STARTED_MESSAGE;
        RsocketModuleConfiguration configuration = rsocketModule().configuration();
        disposable = RSocketServer
                .create((payload, socket) -> Mono.just(new ServerRsocket(payload, socket)))
                .fragment(configuration.getFragmentationMtu())
                .bind(TcpServerTransport.create(configuration.getServerTcpPort()))
                .subscribe(serverChannel -> getLogger().info(message));
    }

    @Override
    public void stop() {
        apply(disposable, Disposable::dispose);
        getLogger().info(RSOCKET_STOPPED);
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
        try {
            stop();
            new RsocketServer(transport).start();
            getLogger().info(RSOCKET_RESTARTED_MESSAGE);
        } catch (Throwable throwable) {
            getLogger().error(RSOCKET_RESTART_FAILED);
        }
    }

    public boolean available() {
        return nonNull(disposable);
    }
}
