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

package io.art.rsocket.socket;

import io.art.core.mime.*;
import io.art.entity.immutable.Value;
import io.art.entity.immutable.*;
import io.art.entity.mapping.*;
import io.art.rsocket.model.*;
import io.art.rsocket.payload.*;
import io.art.server.module.*;
import io.art.server.specification.*;
import io.rsocket.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.entity.mime.MimeTypeDataFormatMapper.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Mono.*;

public class ServerRsocket implements RSocket {
    private final ServiceMethodSpecification specification;
    private final RsocketPayloadReader reader;
    private final RsocketPayloadWriter writer;
    private final RSocket connectedSocket;

    public ServerRsocket(ConnectionSetupPayload payload, RSocket socket) {
        reader = new RsocketPayloadReader(fromMimeType(MimeType.valueOf(payload.dataMimeType())));
        writer = new RsocketPayloadWriter(fromMimeType(MimeType.valueOf(payload.dataMimeType())));
        Entity setupEntity = Value.asEntity(reader.readPayloadData(payload).getValue());
        String serviceId = setupEntity.map("serviceId", PrimitiveMapping.toString);
        String methodId = setupEntity.map("methodId", PrimitiveMapping.toString);
        this.specification = ServerModule.specifications().get(serviceId).getMethods().get(methodId);
        this.connectedSocket = socket;
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        if (isNull(payloadValue)) {
            return Mono.never();
        }
        return specification.serve(Flux.just(payloadValue.getValue())).then();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        if (isNull(payloadValue)) {
            return Mono.empty();
        }
        return specification.serve(Flux.just(payloadValue.getValue())).map(writer::writePayloadData).last();
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        if (isNull(payloadValue)) {
            return Flux.empty();
        }
        return specification.serve(Flux.just(payloadValue.getValue())).map(writer::writePayloadData);
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return Flux.empty();
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        return never();
    }
}
