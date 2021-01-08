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

import io.art.core.exception.*;
import io.art.core.mime.*;
import io.art.core.model.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.art.rsocket.payload.*;
import io.art.rsocket.state.*;
import io.art.server.specification.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.rsocket.*;
import io.rsocket.util.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ContextKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.model.RsocketSetupPayload.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.*;
import static io.art.server.module.ServerModule.*;
import static io.art.value.constants.ValueModuleConstants.*;
import static io.art.value.constants.ValueModuleConstants.Keys.*;
import static io.art.value.immutable.Value.*;
import static io.art.value.mapping.ServiceMethodMapping.*;
import static io.art.value.mime.MimeTypeDataFormatMapper.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Flux.*;

public class ServingRsocket implements RSocket {
    private final RsocketPayloadReader reader;
    private final RsocketPayloadWriter writer;
    private final RSocket requesterSocket;
    private final RsocketModuleState moduleState = rsocketModule().state();
    private final RsocketSetupPayload setupPayload;
    private final ServiceMethodSpecification specification;

    private volatile Runnable onDispose;

    public ServingRsocket(ConnectionSetupPayload payload, RSocket requesterSocket, RsocketServerConfiguration serverConfiguration) {
        moduleState.registerRequester(this.requesterSocket = requesterSocket);
        DataFormat defaultDataFormat = serverConfiguration.getDefaultDataFormat();
        DataFormat defaultMetaDataFormat = serverConfiguration.getDefaultMetaDataFormat();
        DataFormat dataFormat = fromMimeType(MimeType.valueOf(payload.dataMimeType()), defaultDataFormat);
        DataFormat metaDataFormat = fromMimeType(MimeType.valueOf(payload.metadataMimeType()), defaultMetaDataFormat);
        reader = new RsocketPayloadReader(dataFormat, metaDataFormat);
        writer = new RsocketPayloadWriter(dataFormat, metaDataFormat);
        RsocketPayloadValue payloadMetaData = reader.readPayloadMetaData(payload);
        RsocketSetupPayloadBuilder setupPayloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(dataFormat)
                .metadataFormat(metaDataFormat);
        Entity serviceIdentifiers;
        if (isEntity(payloadMetaData.getValue()) && nonNull(serviceIdentifiers = asEntity(asEntity(payloadMetaData.getValue()).get(SERVICE_METHOD_IDENTIFIERS_KEY)))) {
            ServiceMethodIdentifier serviceMethodId = toServiceMethod(serviceIdentifiers);
            ServiceMethodIdentifier defaultServiceMethod = serverConfiguration.getDefaultServiceMethod();
            if (isNull(defaultServiceMethod)) {
                specification = specifications()
                        .findMethodById(serviceMethodId)
                        .orElseThrow(() -> new RsocketException(format(SPECIFICATION_NOT_FOUND, serviceMethodId)));
                setupPayload = setupPayloadBuilder.serviceMethod(serviceMethodId).build();
                specification.initialize();
                return;
            }
            specification = specifications()
                    .findMethodById(defaultServiceMethod)
                    .orElseThrow(() -> new RsocketException(format(SPECIFICATION_NOT_FOUND, defaultServiceMethod)));
            setupPayload = setupPayloadBuilder.serviceMethod(defaultServiceMethod).build();
            specification.initialize();
            return;
        }
        throw new ImpossibleSituation();
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        Flux<Value> input = addContext(payloadValue.isEmpty() ? empty() : just(payloadValue.getValue()));
        return specification.serve(input).then();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        Flux<Value> input = addContext(payloadValue.isEmpty() ? empty() : just(payloadValue.getValue()));
        return specification.serve(input).map(writer::writePayloadData).last(EmptyPayload.INSTANCE);
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        Flux<Value> input = addContext(payloadValue.isEmpty() ? empty() : just(payloadValue.getValue()));
        return specification.serve(input).map(writer::writePayloadData);
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        Flux<Value> input = addContext(from(payloads)
                .map(reader::readPayloadData)
                .filter(data -> !data.isEmpty())
                .map(RsocketPayloadValue::getValue));
        return specification.serve(input).map(writer::writePayloadData);
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadMetaData(payload);
        Flux<Value> input = addContext(isNull(payloadValue) ? empty() : just(payloadValue.getValue()));
        return specification.serve(input).then();
    }

    @Override
    public void dispose() {
        moduleState.disposeRequester(this);
        specification.dispose();
        apply(onDispose, Runnable::run);
    }

    public void onDispose(Runnable action) {
        this.onDispose = action;
    }

    private <T> Flux<T> addContext(Flux<T> flux) {
        return cast(flux
                .materialize()
                .doOnNext(signal -> moduleState.localState(fromContext(signal.getContext())))
                .dematerialize()
                .subscriberContext(context -> context
                        .putNonNull(REQUESTER_RSOCKET_KEY, requesterSocket)
                        .putNonNull(SETUP_PAYLOAD_KEY, setupPayload)));
    }
}
