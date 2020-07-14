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

package io.art.rsocket.communicator;

import io.rsocket.*;
import io.rsocket.transport.netty.client.*;
import lombok.*;
import org.apache.logging.log4j.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import io.art.core.validator.*;
import io.art.entity.Value;
import io.art.entity.*;
import io.art.entity.interceptor.*;
import io.art.entity.mapper.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.art.service.model.*;
import static io.rsocket.RSocketFactory.*;
import static java.text.MessageFormat.*;
import static java.time.Duration.*;
import static java.util.Optional.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Flux.from;
import static reactor.core.publisher.Mono.just;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.CheckerForEmptiness.isEmpty;
import static io.art.core.checker.CheckerForEmptiness.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.Value.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.model.RsocketCommunicationTargetConfiguration.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.processor.ResponseValueInterceptorProcessor.*;
import static io.art.rsocket.reader.RsocketPayloadReader.*;
import static io.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.*;
import static io.art.rsocket.writer.ServiceRequestPayloadWriter.*;
import static io.art.service.factory.ServiceRequestFactory.*;
import static io.art.service.mapping.ServiceRequestMapping.*;
import static io.art.service.mapping.ServiceResponseMapping.*;
import java.util.*;

@SuppressWarnings("Duplicates")
@RequiredArgsConstructor
public class RsocketCommunicator {
    private final Mono<RSocket> rsocketMono;
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = loggingModule().getLogger(RsocketCommunicator.class);
    private String serviceId;
    private String methodId;
    private ValueFromModelMapper<?, ?> requestMapper;
    private ValueToModelMapper<?, ?> responseMapper;
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
                    .resumeSessionDuration(ofMillis(configuration.resumeSessionDuration()))
                    .resumeStreamTimeout(ofMillis(configuration.resumeStreamTimeout()));
        }
        rsocketModule().getClientInterceptors().forEach(factory::addRequesterPlugin);
        configuration.interceptors().forEach(factory::addRequesterPlugin);
        switch (configuration.transport()) {
            case TCP:
                rsocketMono = factory.dataMimeType(toMimeType(configuration.dataFormat()))
                        .metadataMimeType(toMimeType(configuration.dataFormat()))
                        .transport(TcpClientTransport.create(configuration.host(), configuration.tcpPort()))
                        .start()
                        .doOnNext(rsocketModuleState()::registerRsocket)
                        .doOnSubscribe(subscription -> getLogger().info(format(RSOCKET_TCP_COMMUNICATOR_STARTED_MESSAGE, configuration.host(), configuration.tcpPort())));
                getLogger().info(format(RSOCKET_TCP_COMMUNICATOR_CREATED_MESSAGE, configuration.host(), configuration.tcpPort()));
                return;
            case WEB_SOCKET:
                rsocketMono = factory
                        .dataMimeType(toMimeType(configuration.dataFormat()))
                        .metadataMimeType(toMimeType(configuration.dataFormat()))
                        .transport(WebsocketClientTransport.create(configuration.host(), configuration.tcpPort()))
                        .start()
                        .doOnNext(rsocketModuleState()::registerRsocket)
                        .doOnSubscribe(subscription -> getLogger().info(format(RSOCKET_WS_COMMUNICATOR_STARTED_MESSAGE, configuration.host(), configuration.webSocketPort())));
                getLogger().info(format(RSOCKET_WS_COMMUNICATOR_CREATED_MESSAGE, configuration.host(), configuration.tcpPort()));
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
        return rsocketMono.flatMap(rsocket -> rsocket
                .fireAndForget(writeServiceRequestPayload(fromServiceRequest()
                        .map(newServiceRequest(command)), requestValueInterceptors, dataFormat)));
    }

    public <ResponseType> Mono<ServiceResponse<ResponseType>> execute() {
        validator.validate();
        validateRequiredFields();
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        return rsocketMono.flatMap(rsocket -> rsocket
                .requestResponse(writeServiceRequestPayload(fromServiceRequest().map(newServiceRequest(command)),
                        requestValueInterceptors, dataFormat)))
                .map(responsePayload -> ofNullable(readPayloadData(responsePayload, dataFormat)))
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
        Flux<Payload> requestStream = rsocketMono
                .flatMapMany(rsocket -> rsocket
                        .requestStream(writeServiceRequestPayload(fromServiceRequest().map(newServiceRequest(command)),
                                requestValueInterceptors, dataFormat)));
        return requestStream
                .map(responsePayload -> ofNullable(readPayloadData(responsePayload, dataFormat)))
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
        return rsocketMono.flatMap(rsocket -> rsocket.fireAndForget(requestPayload));
    }

    @SuppressWarnings("Duplicates")
    public <RequestType, ResponseType> Mono<ServiceResponse<ResponseType>> execute(RequestType request) {
        validator.validate();
        validateRequiredFields();
        ValueFromModelMapper<?, ? extends Value> requestModelMapper = cast(requestMapper);
        ValueToModelMapper<?, ? extends Value> responseModelMapper = cast(responseMapper);
        Payload requestPayload = writeServiceRequestPayload(fromServiceRequest(cast(requestModelMapper))
                .map(newServiceRequest(command, request)), requestValueInterceptors, dataFormat);
        Mono<Payload> requestResponse = rsocketMono.flatMap(rsocket -> rsocket.requestResponse(requestPayload));
        return requestResponse
                .map(responsePayload -> ofNullable(readPayloadData(responsePayload, dataFormat)))
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
        return rsocketMono.flatMapMany(rsocket -> rsocket.requestStream(requestPayload)
                .map(responsePayload -> ofNullable(readPayloadData(responsePayload, dataFormat)))
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
        return rsocketMono.flatMapMany(rsocket -> rsocket.requestChannel(from(request)
                .filter(Objects::nonNull)
                .map(requestData -> writeServiceRequestPayload(fromServiceRequest(cast(requestModelMapper))
                        .map(newServiceRequest(command, requestData)), requestValueInterceptors, dataFormat)))
                .map(requestPayload -> ofNullable(readPayloadData(requestPayload, dataFormat)))
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
            throw new RsocketClientException(INVALID_RSOCKET_COMMUNICATION_CONFIGURATION + "serviceId,methodId or functionId");
        }
        if (serviceIdIsEmpty) {
            throw new RsocketClientException(INVALID_RSOCKET_COMMUNICATION_CONFIGURATION + "serviceId");
        }
        throw new RsocketClientException(INVALID_RSOCKET_COMMUNICATION_CONFIGURATION + "methodId");
    }

}
