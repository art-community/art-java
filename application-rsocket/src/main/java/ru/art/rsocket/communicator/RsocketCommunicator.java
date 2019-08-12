/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.rsocket.communicator;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.art.core.validator.BuilderValidator;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport;
import ru.art.rsocket.exception.RsocketClientException;
import ru.art.rsocket.model.RsocketCommunicationTargetConfiguration;
import ru.art.service.model.ServiceResponse;
import static io.rsocket.RSocketFactory.ClientRSocketFactory;
import static io.rsocket.RSocketFactory.connect;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Flux.empty;
import static reactor.core.publisher.Flux.from;
import static reactor.core.publisher.Mono.just;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.INVALID_RSOCKET_COMMUNICATION_CONFIGURATION;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.UNSUPPORTED_TRANSPORT;
import static ru.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.RSOCKET_TCP_COMMUNICATOR_STARTED_MESSAGE;
import static ru.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.RSOCKET_WS_COMMUNICATOR_STARTED_MESSAGE;
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.art.rsocket.model.RsocketCommunicationTargetConfiguration.rsocketCommunicationTarget;
import static ru.art.rsocket.reader.RsocketPayloadReader.readPayload;
import static ru.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.toMimeType;
import static ru.art.rsocket.writer.ServiceRequestPayloadWriter.writeServiceRequestPayload;
import static ru.art.service.mapping.ServiceResponseMapping.toServiceResponse;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("Duplicates")
@RequiredArgsConstructor
public class RsocketCommunicator {
    private final Mono<RSocket> socket;
    private String serviceId;
    private String methodId;
    private ValueFromModelMapper requestMapper;
    private ValueToModelMapper responseMapper;
    private RsocketDataFormat dataFormat;
    private final BuilderValidator validator = new BuilderValidator(RsocketCommunicator.class.getName());

    private RsocketCommunicator(RsocketCommunicationTargetConfiguration configuration) {
        dataFormat = configuration.dataFormat();
        ClientRSocketFactory factory = connect();
        if (configuration.resumable()) {
            factory = factory.resume();
        }
        switch (configuration.transport()) {
            case TCP:
                socket = factory.dataMimeType(toMimeType(configuration.dataFormat()))
                        .metadataMimeType(toMimeType(configuration.dataFormat()))
                        .transport(TcpClientTransport.create(configuration.host(), configuration.tcpPort()))
                        .start()
                        .doOnSubscribe(subscription -> loggingModule()
                                .getLogger(RsocketCommunicator.class)
                                .info(RSOCKET_TCP_COMMUNICATOR_STARTED_MESSAGE));
                return;
            case WEB_SOCKET:
                socket = factory
                        .dataMimeType(toMimeType(configuration.dataFormat()))
                        .metadataMimeType(toMimeType(configuration.dataFormat()))
                        .transport(WebsocketClientTransport.create(configuration.host(), configuration.tcpPort()))
                        .start()
                        .doOnSubscribe(subscription -> loggingModule()
                                .getLogger(RsocketCommunicator.class)
                                .info(RSOCKET_WS_COMMUNICATOR_STARTED_MESSAGE));
                return;
        }
        throw new RsocketClientException(format(UNSUPPORTED_TRANSPORT, configuration.transport()));
    }

    public static RsocketCommunicator rsocketCommunicator(String host, int port) {
        return new RsocketCommunicator(rsocketCommunicationTarget().tcpPort(port).host(host).build());
    }

    public static RsocketCommunicator rsocketCommunicator(String host, int port, RsocketDataFormat dataFormat) {
        return new RsocketCommunicator(rsocketCommunicationTarget().tcpPort(port).host(host).dataFormat(dataFormat).build());
    }

    public static RsocketCommunicator rsocketCommunicator(String host, int port, RsocketTransport transport) {
        return new RsocketCommunicator(rsocketCommunicationTarget().tcpPort(port).host(host).transport(transport).build());
    }

    public static RsocketCommunicator rsocketCommunicator(RsocketCommunicationTargetConfiguration targetConfiguration) {
        return new RsocketCommunicator(targetConfiguration);
    }

    public static RsocketCommunicator rsocketCommunicator(RSocket socket) {
        return new RsocketCommunicator(just(socket));
    }

    public RsocketCommunicator serviceId(String serviceId) {
        this.serviceId = validator.notEmptyField(serviceId, "serviceId");
        return this;
    }

    public RsocketCommunicator methodId(String methodId) {
        this.methodId = validator.notEmptyField(methodId, "methodId");
        return this;
    }

    public RsocketCommunicator requestMapper(ValueFromModelMapper requestMapper) {
        this.requestMapper = validator.notEmptyField(requestMapper, "requestMapper");
        return this;
    }

    public RsocketCommunicator responseMapper(ValueToModelMapper responseMapper) {
        this.responseMapper = validator.notEmptyField(responseMapper, "responseMapper");
        return this;
    }

    public Mono<Void> call() {
        validator.validate();
        validateRequiredFields();
        return socket.flatMap(rsocket -> rsocket.fireAndForget(writeServiceRequestPayload(serviceId, methodId, dataFormat)));
    }

    public <ResponseType> Mono<ServiceResponse<ResponseType>> execute() {
        validator.validate();
        validateRequiredFields();
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        return socket.flatMap(rsocket -> rsocket.requestResponse(writeServiceRequestPayload(serviceId, methodId, dataFormat))
                .map(responsePayload -> ofNullable(readPayload(responsePayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> cast(toServiceResponse(cast(responseModelMapper)).map(cast(responseValue)))));
    }

    public <ResponseType> Flux<ServiceResponse<ResponseType>> stream() {
        validator.validate();
        validateRequiredFields();
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        Flux<Payload> requestStream = socket
                .flatMapMany(rsocket -> rsocket.requestStream(writeServiceRequestPayload(serviceId, methodId, dataFormat)));
        if (isNull(responseModelMapper)) {
            return empty();
        }
        return requestStream
                .map(responsePayload -> ofNullable(readPayload(responsePayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> cast(toServiceResponse(cast(responseModelMapper)).map(cast(responseValue))));
    }

    public <RequestType> Mono<Void> call(RequestType request) {
        validator.validate();
        validateRequiredFields();
        ValueFromModelMapper<?, ? extends Value> requestModelMapper = cast(requestMapper);
        Payload requestPayload = isNull(requestModelMapper)
                ? writeServiceRequestPayload(serviceId, methodId, dataFormat)
                : writeServiceRequestPayload(serviceId, methodId, requestModelMapper.map(cast(request)), dataFormat);
        return socket.flatMap(rsocket -> rsocket.fireAndForget(requestPayload));
    }

    @SuppressWarnings("Duplicates")
    public <RequestType, ResponseType> Mono<ServiceResponse<ResponseType>> execute(RequestType request) {
        validator.validate();
        validateRequiredFields();
        ValueFromModelMapper<?, ? extends Value> requestModelMapper = cast(requestMapper);
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        Payload requestPayload = isNull(requestModelMapper)
                ? writeServiceRequestPayload(serviceId, methodId, dataFormat)
                : writeServiceRequestPayload(serviceId, methodId, requestModelMapper.map(cast(request)), dataFormat);
        Mono<Payload> requestResponse = socket.flatMap(rsocket -> rsocket.requestResponse(requestPayload));
        return requestResponse
                .map(responsePayload -> ofNullable(readPayload(responsePayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> cast(toServiceResponse(cast(responseModelMapper)).map(cast(responseValue))));
    }

    @SuppressWarnings("Duplicates")
    public <RequestType, ResponseType> Flux<ServiceResponse<ResponseType>> stream(RequestType request) {
        validator.validate();
        validateRequiredFields();
        ValueFromModelMapper<?, ? extends Value> requestModelMapper = cast(requestMapper);
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        Payload requestPayload = isNull(requestModelMapper)
                ? writeServiceRequestPayload(serviceId, methodId, dataFormat)
                : writeServiceRequestPayload(serviceId, methodId, requestModelMapper.map(cast(request)), dataFormat);
        if (isNull(responseModelMapper)) {
            return empty();
        }
        return socket.flatMapMany(rsocket -> rsocket.requestStream(requestPayload)
                .map(responsePayload -> ofNullable(readPayload(responsePayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> cast(toServiceResponse(cast(responseModelMapper)).map(cast(responseValue)))));
    }

    public <RequestType, ResponseType> Flux<ServiceResponse<ResponseType>> channel(Publisher<RequestType> request) {
        validator.validate();
        validateRequiredFields();
        ValueFromModelMapper<?, ? extends Value> requestModelMapper = cast(requestMapper);
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        if (isNull(responseModelMapper)) {
            return empty();
        }
        return socket.flatMapMany(rsocket -> rsocket.requestChannel(from(request)
                .filter(Objects::nonNull)
                .map(requestPayload -> writeServiceRequestPayload(serviceId, methodId, isNull(requestModelMapper) ? null : requestModelMapper.map(cast(requestPayload)), dataFormat)))
                .map(responsePayload -> ofNullable(readPayload(responsePayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> cast(toServiceResponse(cast(responseModelMapper)).map(cast(responseValue)))));
    }

    private void validateRequiredFields() {
        boolean serviceIdIsEmpty = isEmpty(serviceId);
        boolean methodIdIsEmpty = isEmpty(methodId);
        if (serviceIdIsEmpty || methodIdIsEmpty) {
            String message = INVALID_RSOCKET_COMMUNICATION_CONFIGURATION;
            if (serviceIdIsEmpty) {
                message += "serviceId,";
            }
            if (methodIdIsEmpty) {
                message += "methodId";
            }
            throw new RsocketClientException(message);
        }

    }
}
