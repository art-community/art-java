/*
 * ART
 *
 * Copyright 2019-2021 ART
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

import io.art.core.collection.*;
import io.art.core.exception.*;
import io.art.core.mime.*;
import io.art.core.model.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.art.rsocket.state.*;
import io.art.server.method.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import io.rsocket.*;
import io.rsocket.util.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Errors.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.reader.RsocketPayloadReader.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;
import java.util.function.*;

public class ServingRsocket implements RSocket {
    private final TransportPayloadReader dataReader;
    private final TransportPayloadWriter dataWriter;
    private final RsocketModuleState moduleState = rsocketModule().state();
    private final ServiceMethod serviceMethod;
    private final ImmutableMap<ServiceMethodIdentifier, ServiceMethod> serviceMethods;

    public ServingRsocket(ConnectionSetupPayload payload, ImmutableMap<ServiceMethodIdentifier, ServiceMethod> serviceMethods, RsocketCommonServerConfiguration serverConfiguration) {
        this.serviceMethods = serviceMethods;
        DataFormat dataFormat = fromMimeType(MimeType.valueOf(payload.dataMimeType()), serverConfiguration.getDefaultDataFormat());
        Function<DataFormat, TransportPayloadReader> setupReader = serverConfiguration.getSetupReader();
        TransportPayload setupPayloadData = setupReader.apply(dataFormat).read(payload.sliceData(), declaration(RsocketSetupPayload.class).definition());
        if (!setupPayloadData.isEmpty()) {
            RsocketSetupPayload setupPayloadDataValue = (RsocketSetupPayload) setupPayloadData.getValue();
            if (nonNull(setupPayloadDataValue)) {
                ServiceMethodIdentifier serviceMethodId = new ServiceMethodIdentifier(setupPayloadDataValue.getServiceId(), setupPayloadDataValue.getMethodId());
                serviceMethod = findServiceMethod(serviceMethodId);
                dataReader = serviceMethod.getReader().apply(dataFormat);
                dataWriter = serviceMethod.getWriter().apply(dataFormat);
                return;
            }
        }

        ServiceMethodIdentifier defaultServiceMethod = serverConfiguration.getDefaultServiceMethod();
        if (nonNull(defaultServiceMethod)) {
            serviceMethod = findServiceMethod(defaultServiceMethod);
            dataReader = serviceMethod.getReader().apply(dataFormat);
            dataWriter = serviceMethod.getWriter().apply(dataFormat);
            return;
        }
        throw new ImpossibleSituationException();
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, serviceMethod.getInputType());
        Flux<Object> input = payloadValue.isEmpty() ? empty() : just(payloadValue.getValue());
        return serviceMethod.serve(input).then();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, serviceMethod.getInputType());
        Flux<Object> input = payloadValue.isEmpty() ? empty() : let(payloadValue.getValue(), Flux::just, empty());
        return serviceMethod
                .serve(input)
                .map(value -> dataWriter.write(typed(serviceMethod.getOutputType(), value)))
                .filter(Objects::nonNull)
                .map(ByteBufPayload::create)
                .last(EMPTY_PAYLOAD);
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, serviceMethod.getInputType());
        Flux<Object> input = payloadValue.isEmpty() ? empty() : just(payloadValue.getValue());
        return serviceMethod
                .serve(input)
                .map(value -> ByteBufPayload.create(dataWriter.write(typed(serviceMethod.getOutputType(), value))));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        Flux<Object> input = from(payloads)
                .map(data -> readRsocketPayload(dataReader, data, serviceMethod.getInputType()))
                .filter(data -> !data.isEmpty())
                .map(TransportPayload::getValue);
        return serviceMethod
                .serve(input)
                .map(value -> ByteBufPayload.create(dataWriter.write(typed(serviceMethod.getOutputType(), value))));
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, serviceMethod.getInputType());
        Flux<Object> input = payloadValue.isEmpty() ? empty() : just(payloadValue.getValue());
        return serviceMethod.serve(input).then();
    }

    @Override
    public void dispose() {
        moduleState.removeRequester(this);
    }

    private ServiceMethod findServiceMethod(ServiceMethodIdentifier serviceMethodId) {
        return orThrow(serviceMethods.get(serviceMethodId), () -> new RsocketException(format(SERVICE_METHOD_NOT_FOUND, serviceMethodId)));
    }
}
