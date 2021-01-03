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
import io.art.core.model.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.art.rsocket.payload.*;
import io.art.rsocket.state.*;
import io.art.server.specification.*;
import io.art.value.immutable.*;
import io.rsocket.*;
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
import static io.art.value.constants.ValueConstants.*;
import static io.art.value.constants.ValueConstants.Keys.*;
import static io.art.value.immutable.Value.*;
import static io.art.value.mapping.ServiceIdMapping.*;
import static io.art.value.mime.MimeTypeDataFormatMapper.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.util.function.*;

public class ServingRsocket implements RSocket {
    private final RsocketPayloadReader reader;
    private final RsocketPayloadWriter writer;
    private final RSocket requesterSocket;
    private final RsocketModuleState moduleState = rsocketModule().state();
    private final RsocketSetupPayload setupPayload;
    private final RsocketServerConfiguration serverConfiguration;

    private volatile Runnable onDispose;
    private volatile ServiceMethodSpecification specification;

    public ServingRsocket(ConnectionSetupPayload payload, RSocket requesterSocket, RsocketServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
        moduleState.registerRequester(this.requesterSocket = requesterSocket);
        DataFormat defaultDataFormat = serverConfiguration.getDefaultDataFormat();
        DataFormat defaultMetaDataFormat = serverConfiguration.getDefaultMetaDataFormat();
        DataFormat dataFormat = fromMimeType(MimeType.valueOf(payload.dataMimeType()), defaultDataFormat);
        DataFormat metaDataFormat = fromMimeType(MimeType.valueOf(payload.metadataMimeType()), defaultMetaDataFormat);
        reader = new RsocketPayloadReader(dataFormat, metaDataFormat);
        writer = new RsocketPayloadWriter(dataFormat, metaDataFormat);
        RsocketPayloadValue payloadData = reader.readPayloadMetaData(payload);
        RsocketSetupPayloadBuilder setupPayloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(dataFormat)
                .metadataFormat(metaDataFormat);
        Entity serviceIdentifiers;
        if (isEntity(payloadData.getValue()) && nonNull(serviceIdentifiers = asEntity(asEntity(payloadData.getValue()).get(SERVICE_METHOD_IDENTIFIERS_KEY)))) {
            ServiceMethodIdentifier serviceMethodId = toServiceMethod(serviceIdentifiers);
            setupPayloadBuilder.serviceMethod(serviceMethodId);
            initializeSpecification(serviceMethodId);
        }
        setupPayload = setupPayloadBuilder.build();
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        Flux<Value> input = addContext(isNull(payloadValue) ? Flux.empty() : Flux.just(payloadValue.getValue()));
        return specification.serve(input).then();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        Flux<Value> input = addContext(isNull(payloadValue) ? Flux.empty() : Flux.just(payloadValue.getValue()));
        return specification.serve(input).map(writer::writePayloadData).last();
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        Flux<Value> input = addContext(isNull(payloadValue) ? Flux.empty() : Flux.just(payloadValue.getValue()));
        return specification.serve(input).map(writer::writePayloadData);
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        EmitterProcessor<Value> inputProcessor = EmitterProcessor.create();
        FluxSink<Value> inputEmitter = inputProcessor.sink();
        Consumer<Payload> sendPayload = payload -> apply(reader.readPayloadData(payload), value -> inputEmitter.next(value.getValue()));
        Flux<Value> inputFlux = addContext(inputProcessor).doOnSubscribe(subscription -> {
            inputEmitter.onRequest(subscription::request);
            inputEmitter.onCancel(subscription::cancel);
            inputEmitter.onDispose(Flux.from(payloads).subscribe(sendPayload, inputEmitter::error, inputEmitter::complete));
        });
        return specification.serve(inputFlux).map(writer::writePayloadData);
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        RsocketPayloadValue payloadData = reader.readPayloadMetaData(payload);
        Entity serviceIdentifiers;
        if (isEntity(payloadData.getValue()) && nonNull(serviceIdentifiers = asEntity(asEntity(payloadData.getValue()).get(SERVICE_METHOD_IDENTIFIERS_KEY)))) {
            initializeSpecification(toServiceMethod(serviceIdentifiers));
        }
        return Mono.empty();
    }

    @Override
    public void dispose() {
        moduleState.disposeRequester(this);
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
                        .putNonNull(SPECIFICATION_KEY, specification)
                        .putNonNull(SETUP_PAYLOAD_KEY, setupPayload)));
    }

    private void initializeSpecification(ServiceMethodIdentifier identifier) {
        ServiceMethodIdentifier defaultServiceMethod = serverConfiguration.getDefaultServiceMethod();
        if (isNull(defaultServiceMethod)) {
            this.specification = specifications()
                    .findMethodById(identifier)
                    .orElseThrow(() -> new RsocketException(format(SPECIFICATION_NOT_FOUND, identifier)));
            return;
        }

        this.specification = specifications()
                .findMethodById(defaultServiceMethod)
                .orElseThrow(() -> new RsocketException(format(SPECIFICATION_NOT_FOUND, defaultServiceMethod)));
    }
}
