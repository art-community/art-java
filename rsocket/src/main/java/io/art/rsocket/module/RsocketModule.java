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

package io.art.rsocket.module;

import io.art.core.module.Module;
import io.art.json.descriptor.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.server.*;
import io.art.rsocket.state.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.rsocket.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.netty.http.client.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.configuration.RsocketModuleConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.*;
import static reactor.netty.ByteBufFlux.*;
import static reactor.netty.http.server.HttpServer.create;
import java.util.*;

@Getter
public class RsocketModule implements Module<RsocketModuleConfiguration, RsocketModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final RsocketModuleConfiguration rsocketModule = context().getModule(RSOCKET_MODULE_ID, RsocketModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private static final RsocketModuleState rsocketModuleState = context().getModuleState(RSOCKET_MODULE_ID, RsocketModule::new);
    private final String id = RSOCKET_MODULE_ID;
    private final RsocketModuleConfiguration defaultConfiguration = RsocketModuleDefaultConfiguration.DEFAULT_CONFIGURATION;
    private final RsocketModuleState state = new RsocketModuleState();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(RsocketModule.class);

    public static RsocketModuleConfiguration rsocketModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getRsocketModule();
    }

    public static RsocketModuleState rsocketModuleState() {
        return getRsocketModuleState();
    }

    @Override
    public void onUnload() {
        let(rsocketModuleState().getTcpServer(), RsocketServer::stop);
        let(rsocketModuleState().getWebSocketServer(), RsocketServer::stop);
        rsocketModuleState().getRsocketClients().stream().filter(rsocket -> !rsocket.isDisposed()).forEach(this::disposeRsocket);
    }

    private void disposeRsocket(RSocket rsocket) {
        if (rsocket.isDisposed()) {
            return;
        }
        getLogger().info(RSOCKET_CLIENT_DISPOSING);
        ignoreException(rsocket::dispose, getLogger()::error);
    }

    public static void main(String[] args) {
        HttpClient client = HttpClient.create();
        client
                .post()
                .send(fromString(just("Test")))
                .responseSingle((response, byteBufFlux) -> {
                    HttpHeaders httpHeaders = response.responseHeaders();
                    return byteBufFlux.asString().map();
                })
                .block();
        create()
                .compress(true)
                .route(routes -> routes
                        .get("/test", (request, response) -> {
                            HttpHeaders headers = request.requestHeaders();
                            Map<CharSequence, Set<Cookie>> cookies = request.cookies();
                            Map<String, String> params = request.params();
                            String path = request.path();
                            headers.get(CONTENT_TYPE);
                            return response.sendString(request.receive()
                                    .asByteArray()
                                    .map(JsonEntityReader::readJson)
                                    .map(JsonEntityWriter::writeJson));
                        }))
                .bindNow();
    }
}
