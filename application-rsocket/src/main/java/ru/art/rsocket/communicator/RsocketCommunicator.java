/*
 * ART Java
 *
 * Copyright 2019 ART
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
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.art.rsocket.constants.RsocketModuleConstants.RsocketTransport;
import ru.art.rsocket.exception.RsocketClientException;
import ru.art.rsocket.model.RsocketCommunicationTargetConfiguration;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.rsocket.RSocketFactory.ClientRSocketFactory;
import static io.rsocket.RSocketFactory.connect;
import static java.text.MessageFormat.format;
import static java.time.Duration.ofMillis;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Flux.empty;
import static reactor.core.publisher.Flux.from;
import static reactor.core.publisher.Mono.just;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.entity.Value.asEntity;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.INVALID_RSOCKET_COMMUNICATION_CONFIGURATION;
import static ru.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.UNSUPPORTED_TRANSPORT;
import static ru.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.RSOCKET_TCP_COMMUNICATOR_STARTED_MESSAGE;
import static ru.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.RSOCKET_WS_COMMUNICATOR_STARTED_MESSAGE;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_FUNCTION_SERVICE;
import static ru.art.rsocket.model.RsocketCommunicationTargetConfiguration.rsocketCommunicationTarget;
import static ru.art.rsocket.module.RsocketModule.rsocketModule;
import static ru.art.rsocket.processor.ResponseValueInterceptorProcessor.processResponseValueInterceptors;
import static ru.art.rsocket.reader.RsocketPayloadReader.readPayload;
import static ru.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.toMimeType;
import static ru.art.rsocket.writer.ServiceRequestPayloadWriter.writeServiceRequestPayload;
import static ru.art.service.factory.ServiceRequestFactory.newServiceRequest;
import static ru.art.service.mapping.ServiceRequestMapping.fromServiceRequest;
import static ru.art.service.mapping.ServiceResponseMapping.toServiceResponse;

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
    private List<ValueInterceptor<Entity, Entity>> requestValueInterceptors = linkedListOf(rsocketModule().getRequestValueInterceptors());
    private List<ValueInterceptor<Entity, Entity>> responseValueInterceptors = linkedListOf(rsocketModule().getResponseValueInterceptors());
    private ServiceMethodCommand command;

    private RsocketCommunicator(RsocketCommunicationTargetConfiguration configuration) {
        dataFormat = configuration.dataFormat();
        ClientRSocketFactory factory = connect();
        if (configuration.resumable()) {
            factory = factory.resume()
                    .resumeSessionDuration(ofMillis(configuration.resumeSessionDuration()));
        }
        rsocketModule().getClientInterceptors().forEach(factory::addRequesterPlugin);
        configuration.interceptors().forEach(factory::addRequesterPlugin);
        switch (configuration.transport()) {
            case TCP:
                socket = factory.dataMimeType(toMimeType(configuration.dataFormat()))
                        .metadataMimeType(toMimeType(configuration.dataFormat()))
                        .transport(TcpClientTransport.create(configuration.host(), configuration.tcpPort()))
                        .start()
                        .doOnSubscribe(subscription -> loggingModule()
                                .getLogger(RsocketCommunicator.class)
                                .info(format(RSOCKET_TCP_COMMUNICATOR_STARTED_MESSAGE, configuration.host(), configuration.tcpPort())));
                return;
            case WEB_SOCKET:
                socket = factory
                        .dataMimeType(toMimeType(configuration.dataFormat()))
                        .metadataMimeType(toMimeType(configuration.dataFormat()))
                        .transport(WebsocketClientTransport.create(configuration.host(), configuration.tcpPort()))
                        .start()
                        .doOnSubscribe(subscription -> loggingModule()
                                .getLogger(RsocketCommunicator.class)
                                .info(format(RSOCKET_WS_COMMUNICATOR_STARTED_MESSAGE, configuration.host(), configuration.webSocketPort())));
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
        if (isNotEmpty(methodId)) {
            command = new ServiceMethodCommand(serviceId, methodId);
        }
        return this;
    }

    public RsocketCommunicator methodId(String methodId) {
        this.methodId = validator.notEmptyField(methodId, "methodId");
        if (isNotEmpty(serviceId)) {
            command = new ServiceMethodCommand(serviceId, methodId);
        }
        return this;
    }

    public RsocketCommunicator functionId(String functionId) {
        this.serviceId = RSOCKET_FUNCTION_SERVICE;
        this.methodId = functionId;
        this.command = new ServiceMethodCommand(serviceId, methodId);
        return this;
    }

    public RsocketCommunicator addRequestValueInterceptor(ValueInterceptor<Entity, Entity> interceptor) {
        requestValueInterceptors.add(validator.notNullField(interceptor, "requestValueInterceptor"));
        return this;
    }

    public RsocketCommunicator addResponseValueInterceptor(ValueInterceptor<Entity, Entity> interceptor) {
        responseValueInterceptors.add(validator.notNullField(interceptor, "responseValueInterceptor"));
        return this;
    }

    public <RequestType> RsocketCommunicator requestMapper(ValueFromModelMapper<RequestType, ? extends Value> requestMapper) {
        this.requestMapper = validator.notEmptyField(requestMapper, "requestMapper");
        return this;
    }

    public <ResponseType> RsocketCommunicator responseMapper(ValueToModelMapper<ResponseType, ? extends Value> responseMapper) {
        this.responseMapper = validator.notEmptyField(responseMapper, "responseMapper");
        return this;
    }

    public Mono<Void> call() {
        validator.validate();
        validateRequiredFields();
        return socket.flatMap(rsocket -> rsocket
                .fireAndForget(writeServiceRequestPayload(fromServiceRequest()
                        .map(newServiceRequest(command)), requestValueInterceptors, dataFormat)));
    }

    public <ResponseType> Mono<ServiceResponse<ResponseType>> execute() {
        validator.validate();
        validateRequiredFields();
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        return socket.flatMap(rsocket -> rsocket
                .requestResponse(writeServiceRequestPayload(fromServiceRequest().map(newServiceRequest(command)),
                        requestValueInterceptors, dataFormat)))
                .map(responsePayload -> ofNullable(readPayload(responsePayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> processResponseValueInterceptors(asEntity(responseValue), responseValueInterceptors))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> cast(toServiceResponse(cast(responseModelMapper)).map(cast(responseValue))));
    }

    public <ResponseType> Flux<ServiceResponse<ResponseType>> stream() {
        validator.validate();
        validateRequiredFields();
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        Flux<Payload> requestStream = socket
                .flatMapMany(rsocket -> rsocket
                        .requestStream(writeServiceRequestPayload(fromServiceRequest().map(newServiceRequest(command)),
                                requestValueInterceptors, dataFormat)));
        if (isNull(responseModelMapper)) {
            return empty();
        }
        return requestStream
                .map(responsePayload -> ofNullable(readPayload(responsePayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> processResponseValueInterceptors(asEntity(responseValue), responseValueInterceptors))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> cast(toServiceResponse(cast(responseModelMapper)).map(cast(responseValue))));
    }

    public <RequestType> Mono<Void> call(RequestType request) {
        validator.validate();
        validateRequiredFields();
        ValueFromModelMapper<?, ? extends Value> requestModelMapper = cast(requestMapper);
        Payload requestPayload = writeServiceRequestPayload(fromServiceRequest(cast(requestModelMapper))
                .map(newServiceRequest(command, request)), requestValueInterceptors, dataFormat);
        return socket.flatMap(rsocket -> rsocket.fireAndForget(requestPayload));
    }

    @SuppressWarnings("Duplicates")
    public <RequestType, ResponseType> Mono<ServiceResponse<ResponseType>> execute(RequestType request) {
        validator.validate();
        validateRequiredFields();
        ValueFromModelMapper<?, ? extends Value> requestModelMapper = cast(requestMapper);
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        Payload requestPayload = writeServiceRequestPayload(fromServiceRequest(cast(requestModelMapper))
                .map(newServiceRequest(command, request)), requestValueInterceptors, dataFormat);
        Mono<Payload> requestResponse = socket.flatMap(rsocket -> rsocket.requestResponse(requestPayload));
        return requestResponse
                .map(responsePayload -> ofNullable(readPayload(responsePayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> processResponseValueInterceptors(asEntity(responseValue), responseValueInterceptors))
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
        Payload requestPayload = writeServiceRequestPayload(fromServiceRequest(cast(requestModelMapper))
                .map(newServiceRequest(command, request)), requestValueInterceptors, dataFormat);
        if (isNull(responseModelMapper)) {
            return empty();
        }
        return socket.flatMapMany(rsocket -> rsocket.requestStream(requestPayload)
                .map(responsePayload -> ofNullable(readPayload(responsePayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> processResponseValueInterceptors(asEntity(responseValue), responseValueInterceptors))
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
                .map(requestData -> writeServiceRequestPayload(fromServiceRequest(cast(requestModelMapper))
                        .map(newServiceRequest(command, requestData)), requestValueInterceptors, dataFormat)))
                .map(requestPayload -> ofNullable(readPayload(requestPayload, dataFormat)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> processResponseValueInterceptors(asEntity(responseValue), responseValueInterceptors))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(responseValue -> cast(toServiceResponse(cast(responseModelMapper)).map(cast(responseValue)))));
    }

    private void validateRequiredFields() {
        boolean serviceIdIsEmpty = isEmpty(serviceId);
        boolean methodIdIsEmpty = isEmpty(methodId);
        if (!serviceIdIsEmpty && !methodIdIsEmpty) {
            return;
        }
        if (serviceIdIsEmpty && methodIdIsEmpty) {
            throw new RsocketClientException(INVALID_RSOCKET_COMMUNICATION_CONFIGURATION + "serviceId,methodId");
        }
        if (serviceIdIsEmpty) {
            throw new RsocketClientException(INVALID_RSOCKET_COMMUNICATION_CONFIGURATION + "serviceId");
        }
        throw new RsocketClientException("methodId");
    }

}
