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
import io.art.rsocket.interceptor.*;
import io.art.rsocket.socket.*;
import io.art.server.*;
import io.rsocket.core.*;
import io.rsocket.transport.*;
import io.rsocket.transport.netty.server.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.*;
import reactor.core.publisher.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.TransportMode.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.concurrent.atomic.*;

@RequiredArgsConstructor
public class RsocketServer implements Server {
    private final TransportMode transportMode;
    private final AtomicReference<Disposable> disposable = new AtomicReference<>();

    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(RsocketServer.class);

    @Override
    public void start() {
        String message = transportMode == TCP ? TCP_SERVER_STARTED_MESSAGE : WS_SERVER_STARTED_MESSAGE;
        RsocketModuleConfiguration configuration = rsocketModule().configuration();
        RSocketServer server = RSocketServer.create((payload, socket) -> Mono.just(new ServerRsocket(payload, socket)));
        if (configuration.getFragmentationMtu() > 0) {
            server.fragment(configuration.getFragmentationMtu());
        }
        if (configuration.getMaxInboundPayloadSize() > 0) {
            server.fragment(configuration.getMaxInboundPayloadSize());
        }
        Resume resume;
        if (nonNull(resume = configuration.getResume())) {
            server.resume(resume);
        }
        ServerTransport<CloseableChannel> transport = transportMode == TCP
                ? TcpServerTransport.create(configuration.getTcpServer())
                : WebsocketServerTransport.create(configuration.getHttpWebSocketServer());
        server
                .interceptors(interceptorRegistry -> configuration.getInterceptorConfigurer().accept(interceptorRegistry))
                .payloadDecoder(configuration.getPayloadDecoder())
                .bind(transport)
                .doOnSubscribe(channel -> getLogger().info(message))
                .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable))
                .subscribe(disposable::set);
    }

    @Override
    public void stop() {
        Disposable value;
        if (nonNull(value = disposable.getAndSet(null))) {
            value.dispose();
            getLogger().info(SERVER_STOPPED);
        }
    }

    @Override
    public void await() {
        block();
    }

    @Override
    public void restart() {
        stop();
        new RsocketServer(transportMode).start();
    }

    @Override
    public boolean available() {
        Disposable value;
        return nonNull(value = disposable.get()) && !value.isDisposed();
    }
}
