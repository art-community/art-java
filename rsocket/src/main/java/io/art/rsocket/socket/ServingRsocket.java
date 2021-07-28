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

import io.art.core.exception.*;
import io.art.core.mime.*;
import io.art.core.model.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.art.rsocket.state.*;
import io.art.server.configuration.*;
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
import static io.art.rsocket.model.RsocketSetupPayload.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.reader.RsocketPayloadReader.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import java.util.function.*;

public class ServingRsocket implements RSocket {
    private final TransportPayloadReader dataReader;
    private final TransportPayloadWriter dataWriter;
    private final RsocketModuleState moduleState = rsocketModule().state();
    private final ServiceMethod serviceMethod;
    private final RsocketModuleConfiguration moduleConfiguration;

    public ServingRsocket(ConnectionSetupPayload payload, RSocket requesterSocket, RsocketModuleConfiguration configuration) {
        moduleConfiguration = configuration;
        RsocketServerConfiguration transportConfiguration = configuration.getServerTransportConfiguration();
        ServerConfiguration serverConfiguration = configuration.getServerConfiguration();
        //moduleState.registerRequester(this.requesterSocket = requesterSocket);

        DataFormat dataFormat = fromMimeType(MimeType.valueOf(payload.dataMimeType()), transportConfiguration.getDefaultDataFormat());
        DataFormat metaDataFormat = fromMimeType(MimeType.valueOf(payload.metadataMimeType()), transportConfiguration.getDefaultMetaDataFormat());

        Function<DataFormat, TransportPayloadReader> setupReader = serverConfiguration.getReader();

        TransportPayload setupPayloadData = setupReader.apply(dataFormat).read(payload.sliceData(), declaration(RsocketSetupPayload.class).definition());
        RsocketSetupPayloadBuilder setupPayloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(dataFormat)
                .metadataFormat(metaDataFormat);

        if (!setupPayloadData.isEmpty()) {
            ServiceMethodIdentifier serviceMethodId = ((RsocketSetupPayload) setupPayloadData.getValue()).getId();
            if (nonNull(serviceMethodId)) {
                //setupPayload = setupPayloadBuilder.id(serviceMethodId).build();
                serviceMethod = findServiceMethod(serviceMethodId);
                dataReader = serverConfiguration.getReader(serviceMethodId, dataFormat);
                dataWriter = serverConfiguration.getWriter(serviceMethodId, dataFormat);
                return;
            }
        }

        ServiceMethodIdentifier defaultServiceMethod = transportConfiguration.getDefaultServiceMethod();
        if (nonNull(defaultServiceMethod)) {
            //setupPayload = setupPayloadBuilder.id(defaultServiceMethod).build();
            serviceMethod = findServiceMethod(defaultServiceMethod);
            dataReader = serverConfiguration.getReader(defaultServiceMethod, dataFormat);
            dataWriter = serverConfiguration.getWriter(defaultServiceMethod, dataFormat);
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
        Flux<Object> input = payloadValue.isEmpty() ? empty() : just(payloadValue.getValue());
        return serviceMethod
                .serve(input)
                .map(value -> ByteBufPayload.create(dataWriter.write(typed(serviceMethod.getOutputType(), value))))
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
        return orThrow(moduleConfiguration.getServices().get(serviceMethodId), () -> new RsocketException(format(SERVICE_METHOD_NOT_FOUND, serviceMethodId)));
    }
}
