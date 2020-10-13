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

import io.art.core.configuration.*;
import io.art.core.context.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.socket.*;
import io.art.server.*;
import io.rsocket.core.*;
import io.rsocket.transport.netty.server.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.*;
import reactor.core.publisher.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.RsocketTransport.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static reactor.util.retry.Retry.*;
import java.time.*;
import java.util.concurrent.atomic.*;

@RequiredArgsConstructor
public class RsocketServer implements Server {
    private final RsocketTransport transport;
    private final AtomicReference<Disposable> disposable = new AtomicReference<>();

    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(RsocketServer.class);

    @Override
    public void start() {
        String message = transport == TCP ? RSOCKET_TCP_SERVER_STARTED_MESSAGE : RSOCKET_WS_SERVER_STARTED_MESSAGE;
        RsocketModuleConfiguration configuration = rsocketModule().configuration();
        RSocketServer server = RSocketServer.create((payload, socket) -> Mono.just(new ServerRsocket(payload, socket)));
        if (configuration.getFragmentationMtu() > 0) {
            server.fragment(configuration.getFragmentationMtu());
        }
        if (nonNull(configuration.getResume())) {
            Resume resume = new Resume()
                    .streamTimeout(configuration.getResume().getStreamTimeout())
                    .sessionDuration(configuration.getResume().getSessionDuration());
            switch (configuration.getResume().getRetryPolicy()) {
                case BACKOFF:
                    resume = resume.retry(backoff(12, Duration.ZERO));
                    break;
                case FIXED_DELAY:
                    resume = resume.retry(fixedDelay(12, Duration.ZERO));
                    break;
                case MAX:
                    resume = resume.retry(max(12));
                    break;
                case MAX_IN_A_ROW:
                    resume = resume.retry(maxInARow(12));
                    break;
                case INDEFINITELY:
                    resume = resume.retry(indefinitely());
                    break;
            }
            server.resume(resume);
        }
        server
                .bind(TcpServerTransport.create(configuration.getTcpPort()))
                .doOnSubscribe(channel -> getLogger().info(message))
                .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable))
                .subscribe(disposable::set);
    }

    @Override
    public void stop() {
        Disposable value;
        if (nonNull(value = disposable.getAndSet(null))) {
            value.dispose();
            getLogger().info(RSOCKET_STOPPED);
        }
    }

    @Override
    public void await() {
        block();
    }

    @Override
    public void restart() {
        stop();
        new RsocketServer(transport).start();
    }

    @Override
    public boolean available() {
        Disposable value;
        return nonNull(value = disposable.get()) && !value.isDisposed();
    }


    public static void main(String[] args) {
    }
}
