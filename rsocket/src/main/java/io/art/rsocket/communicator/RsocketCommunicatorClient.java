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

package io.art.rsocket.communicator;

import io.art.entity.immutable.Value;
import io.art.message.pack.descriptor.*;
import io.art.server.specification.*;
import io.netty.buffer.*;
import io.rsocket.*;
import io.rsocket.util.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import static io.art.message.pack.descriptor.MessagePackEntityReader.*;
import static io.art.server.module.ServerModule.*;
import static reactor.netty.http.server.HttpServer.*;

@RequiredArgsConstructor
public class RsocketCommunicatorClient implements CommunicatorClient {
    private final RSocket socket;

    @Override
    public void callBlocking() {
        socket.fireAndForget(EmptyPayload.INSTANCE);
    }

    @Override
    public void callBlocking(Value requestValue) {
        socket.fireAndForget(ByteBufPayload.create(MessagePackEntityWriter.writeMessagePackToBytes(requestValue)));
    }

    @Override
    public Mono<Void> callReactive() {
        return socket.fireAndForget(EmptyPayload.INSTANCE);
    }

    @Override
    public Mono<Void> callReactive(Value requestValue) {
        return null;
    }

    @Override
    public Value executeBlocking() {
        return null;
    }

    @Override
    public Value executeBlocking(Value requestValue) {
        return null;
    }

    @Override
    public Mono<Value> executeReactive() {
        return null;
    }

    @Override
    public Mono<Value> executeReactive(Value requestValue) {
        return null;
    }

    @Override
    public Mono<Value> executeReactive(Mono<Value> requestValue) {
        return null;
    }

    @Override
    public Flux<Value> stream() {
        return null;
    }

    @Override
    public Flux<Value> stream(Value requestValue) {
        return null;
    }

    @Override
    public Flux<Value> stream(Mono<Value> requestValue) {
        return null;
    }

    @Override
    public Flux<Value> channel(Flux<Value> requestValue) {
        ServiceMethodSpecification specification = specifications()
                .get("test")
                .getMethods()
                .get("test");

        create().route(routes -> {
            switch (specification.getRequestType()) {
                case VALUE:
                    routes.get("/test", (request, response) -> response
                            .sendByteArray(request
                                    .receiveContent()
                                    .map(content -> readMessagePack(new ByteBufInputStream(content.content())))
                                    .flatMap(specification::stream)
                                    .map(MessagePackEntityWriter::writeMessagePackToBytes)
                            )
                            .then());
                case MONO:
                case FLUX:
                    routes.get("/test", (request, response) -> response
                            .sendByteArray(specification.channel(request
                                    .receiveContent()
                                    .map(content -> readMessagePack(new ByteBufInputStream(content.content()))))
                                    .map(MessagePackEntityWriter::writeMessagePackToBytes)
                            )
                            .then());
            }
        });


        HttpClient.create().post().send().response().map(response -> response.)
        return null;
    }
}
