/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import io.art.core.model.*;
import io.art.meta.invoker.*;
import io.art.meta.model.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.art.rsocket.state.*;
import io.art.rsocket.state.RsocketModuleState.*;
import io.art.server.method.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.art.transport.payload.*;
import io.rsocket.*;
import io.rsocket.util.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.mime.MimeType.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.meta.Meta.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Errors.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.reader.RsocketPayloadReader.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static io.art.transport.payload.TransportPayloadReader.*;
import static io.art.transport.payload.TransportPayloadWriter.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;

public class ServingRsocket implements RSocket {
    private final TransportPayloadReader dataReader;
    private final TransportPayloadWriter dataWriter;
    private final ServiceMethod serviceMethod;
    private final ImmutableMap<ServiceMethodIdentifier, ServiceMethod> serviceMethods;
    private MetaType<?> inputMappingType;
    private MetaType<?> outputMappingType;
    private final RsocketModuleState state = rsocketModule().state();
    private final static MetaType<RsocketSetupPayload> payloadType = declaration(RsocketSetupPayload.class).definition();
    private final static Map<ServiceMethodIdentifier, ServingRsocket> cache = concurrentMap();

    public ServingRsocket(ConnectionSetupPayload payload, ImmutableMap<ServiceMethodIdentifier, ServiceMethod> serviceMethods, RsocketCommonServerConfiguration serverConfiguration) {
        this.serviceMethods = serviceMethods;
        DataFormat dataFormat = fromMimeType(parseMimeType(payload.dataMimeType(), toMimeType(serverConfiguration.getDefaultDataFormat())), serverConfiguration.getDefaultDataFormat());
        TransportPayload setupPayloadData = transportPayloadReader(dataFormat).read(payload.sliceData(), payloadType);
        if (!setupPayloadData.isEmpty()) {
            RsocketSetupPayload setupPayloadDataValue = (RsocketSetupPayload) setupPayloadData.getValue();
            if (nonNull(setupPayloadDataValue)) {
                ServiceMethodIdentifier serviceMethodId = serviceMethodId(setupPayloadDataValue.getServiceId(), setupPayloadDataValue.getMethodId());
                ServingRsocket cached = cache.get(serviceMethodId);
                if (nonNull(cached)) {
                    serviceMethod = cached.serviceMethod;
                    dataReader = cached.dataReader;
                    dataWriter = cached.dataWriter;
                    inputMappingType = cached.inputMappingType;
                    outputMappingType = cached.outputMappingType;
                    updateState(payload, setupPayloadDataValue);
                    return;
                }
                serviceMethod = findServiceMethod(serviceMethodId);
                dataReader = transportPayloadReader(dataFormat);
                dataWriter = transportPayloadWriter(dataFormat);
                inputMappingType = serviceMethod.getInputType();
                if (nonNull(inputMappingType) && (inputMappingType.internalKind() == MONO || inputMappingType.internalKind() == FLUX)) {
                    inputMappingType = inputMappingType.parameters().get(0);
                }
                outputMappingType = serviceMethod.getOutputType();
                if (nonNull(outputMappingType) && (outputMappingType.internalKind() == MONO || outputMappingType.internalKind() == FLUX)) {
                    outputMappingType = outputMappingType.parameters().get(0);
                }
                cache.put(serviceMethodId, this);
                updateState(payload, setupPayloadDataValue);
                return;
            }
        }

        ServiceMethodIdentifier defaultServiceMethod = serverConfiguration.getDefaultServiceMethod();
        if (nonNull(defaultServiceMethod)) {
            ServingRsocket cached = cache.get(defaultServiceMethod);
            if (nonNull(cached)) {
                serviceMethod = cached.serviceMethod;
                dataReader = cached.dataReader;
                dataWriter = cached.dataWriter;
                inputMappingType = cached.inputMappingType;
                outputMappingType = cached.outputMappingType;
                updateState(payload);
                return;
            }
            serviceMethod = findServiceMethod(defaultServiceMethod);
            dataReader = transportPayloadReader(dataFormat);
            dataWriter = transportPayloadWriter(dataFormat);
            inputMappingType = serviceMethod.getInputType();
            if (nonNull(inputMappingType) && (inputMappingType.internalKind() == MONO || inputMappingType.internalKind() == FLUX)) {
                inputMappingType = inputMappingType.parameters().get(0);
            }
            outputMappingType = serviceMethod.getOutputType();
            if (nonNull(outputMappingType) && (outputMappingType.internalKind() == MONO || outputMappingType.internalKind() == FLUX)) {
                outputMappingType = outputMappingType.parameters().get(0);
            }
            cache.put(defaultServiceMethod, this);
            updateState(payload);
            return;
        }

        throw new ImpossibleSituationException();
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, inputMappingType);
        Flux<Object> input = payloadValue.isEmpty() ? empty() : just(payloadValue.getValue());
        return serviceMethod.serve(input).then();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, inputMappingType);
        Flux<Object> input = payloadValue.isEmpty() ? empty() : let(payloadValue.getValue(), Flux::just, empty());
        if (isNull(outputMappingType) || outputMappingType.internalKind() == VOID) {
            return serviceMethod.serve(input).map(ignore -> EMPTY_PAYLOAD).last(EMPTY_PAYLOAD);
        }
        return serviceMethod
                .serve(input)
                .map(value -> ByteBufPayload.create(dataWriter.write(typed(outputMappingType, value))))
                .last(EMPTY_PAYLOAD);
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, inputMappingType);
        Flux<Object> input = payloadValue.isEmpty() ? empty() : just(payloadValue.getValue());
        if (isNull(outputMappingType) || outputMappingType.internalKind() == VOID) {
            return serviceMethod.serve(input).map(ignore -> EMPTY_PAYLOAD);
        }
        return serviceMethod
                .serve(input)
                .map(value -> ByteBufPayload.create(dataWriter.write(typed(outputMappingType, value))));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        Flux<Object> input = from(payloads)
                .map(data -> readRsocketPayload(dataReader, data, inputMappingType))
                .filter(data -> !data.isEmpty())
                .map(TransportPayload::getValue);
        if (isNull(outputMappingType) || outputMappingType.internalKind() == VOID) {
            return serviceMethod.serve(input).map(ignore -> EMPTY_PAYLOAD);
        }
        return serviceMethod
                .serve(input)
                .map(value -> ByteBufPayload.create(dataWriter.write(typed(outputMappingType, value))));
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        TransportPayload payloadValue = readRsocketPayload(dataReader, payload, inputMappingType);
        Flux<Object> input = payloadValue.isEmpty() ? empty() : just(payloadValue.getValue());
        return serviceMethod.serve(input).then();
    }

    @Override
    public void dispose() {
        MetaMethodInvoker invoker = serviceMethod.getInvoker();
        state.clearRsocketState(invoker.getOwner(), invoker.getDelegate());
    }

    private void updateState(ConnectionSetupPayload payload) {
        MetaMethodInvoker invoker = serviceMethod.getInvoker();
        state.rsocketState(invoker.getOwner(), invoker.getDelegate(), RsocketLocalState.builder()
                .setupPayloadRaw(payload)
                .build());
    }

    private void updateState(ConnectionSetupPayload payload, RsocketSetupPayload setupPayloadDataValue) {
        MetaMethodInvoker invoker = serviceMethod.getInvoker();
        state.rsocketState(invoker.getOwner(), invoker.getDelegate(), RsocketLocalState.builder()
                .setupPayloadModel(Optional.of(setupPayloadDataValue))
                .setupPayloadRaw(payload)
                .build());
    }

    private ServiceMethod findServiceMethod(ServiceMethodIdentifier serviceMethodId) {
        ServiceMethod serviceMethod = serviceMethods.get(serviceMethodId);
        return orThrow(serviceMethod, () -> new RsocketException(format(SERVICE_METHOD_NOT_FOUND, serviceMethodId)));
    }
}
