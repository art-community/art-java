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
import reactor.util.context.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ContextKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.model.RsocketSetupPayload.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.reader.RsocketPayloadReader.*;
import static io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import java.util.function.*;

public class ServingRsocket implements RSocket {
    private final TransportPayloadReader dataReader;
    private final TransportPayloadWriter dataWriter;
    private final RSocket requesterSocket;
    private final RsocketModuleState moduleState = rsocketModule().state();
    private final RsocketSetupPayload setupPayload;
    private final ServiceMethod serviceMethod;

    public ServingRsocket(ConnectionSetupPayload payload, RSocket requesterSocket, RsocketServerConfiguration rsocketConfiguration) {
        moduleState.registerRequester(this.requesterSocket = requesterSocket);
        DataFormat dataFormat = fromMimeType(MimeType.valueOf(payload.dataMimeType()), rsocketConfiguration.getDefaultDataFormat());
        DataFormat metaDataFormat = fromMimeType(MimeType.valueOf(payload.metadataMimeType()), rsocketConfiguration.getDefaultMetaDataFormat());

        Function<DataFormat, TransportPayloadReader> setupReader = rsocketConfiguration.getServerConfiguration().getReader();

        TransportPayload setupPayloadData = setupReader.apply(dataFormat).read(payload.sliceData(), declaration(RsocketSetupPayload.class).definition());
        RsocketSetupPayloadBuilder setupPayloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(dataFormat)
                .metadataFormat(metaDataFormat);

        if (!setupPayloadData.isEmpty()) {
            ServiceMethodIdentifier serviceMethodId = ((RsocketSetupPayload)setupPayloadData.getValue()).getId();
            if (nonNull(serviceMethodId)) {
                setupPayload = setupPayloadBuilder.id(serviceMethodId).build();
                serviceMethod = findSpecification(serviceMethodId);

                ServerConfiguration configuration = serviceMethod.getConfiguration();
                dataReader = configuration.getReader(serviceMethodId, dataFormat);
                dataWriter = configuration.getWriter(serviceMethodId, dataFormat);
                return;
            }
        }


        ServiceMethodIdentifier defaultServiceMethod = rsocketConfiguration.getDefaultServiceMethod();
        if (nonNull(defaultServiceMethod)) {
            setupPayload = setupPayloadBuilder.id(defaultServiceMethod).build();
            serviceMethod = findSpecification(defaultServiceMethod);

            ServerConfiguration configuration = serviceMethod.getConfiguration();
            dataReader = configuration.getReader(defaultServiceMethod, dataFormat);
            dataWriter = configuration.getWriter(defaultServiceMethod, dataFormat);
            return;
        }
        throw new ImpossibleSituationException();
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, serviceMethod.getInputType());
        Flux<Object> input = addContext(payloadValue.isEmpty() ? empty() : just(payloadValue.getValue()));
        return serviceMethod.serve(input).then();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, serviceMethod.getInputType());
        Flux<Object> input = addContext(payloadValue.isEmpty() ? empty() : just(payloadValue.getValue()));
        return serviceMethod.serve(input).map(value -> ByteBufPayload.create(dataWriter.write(typed(serviceMethod.getOutputType(), value)))).last(EMPTY_PAYLOAD);
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, serviceMethod.getInputType());
        Flux<Object> input = addContext(payloadValue.isEmpty() ? empty() : just(payloadValue.getValue()));
        return serviceMethod.serve(input).map(value -> ByteBufPayload.create(dataWriter.write(typed(serviceMethod.getOutputType(), value))));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        Flux<Object> input = addContext(from(payloads)
                .map(data -> readRsocketPayload(dataReader, data, serviceMethod.getInputType()))
                .filter(data -> !data.isEmpty())
                .map(TransportPayload::getValue));
        return serviceMethod.serve(input).map(value -> ByteBufPayload.create(dataWriter.write(typed(serviceMethod.getOutputType(), value))));
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, serviceMethod.getInputType());
        Flux<Object> input = addContext(payloadValue.isEmpty() ? empty() : just(payloadValue.getValue()));
        return serviceMethod.serve(input).then();
    }

    @Override
    public void dispose() {
        moduleState.removeRequester(this);
    }

    private ServiceMethod findSpecification(ServiceMethodIdentifier serviceMethodId) {
//        return specifications()
//                .findMethodById(serviceMethodId)
//                .orElseThrow(() -> new RsocketException(format(SPECIFICATION_NOT_FOUND, serviceMethodId)));
        return null;
    }

    private <T> Flux<T> addContext(Flux<T> flux) {
        return flux.doOnEach(signal -> loadContext(signal.getContextView())).contextWrite(this::saveContext);
    }

    private void loadContext(ContextView context) {
        moduleState.localState(fromContext(context));
    }

    private Context saveContext(Context context) {
        return context
                .putNonNull(REQUESTER_RSOCKET_KEY, requesterSocket)
                .putNonNull(SETUP_PAYLOAD_KEY, setupPayload);
    }
}
