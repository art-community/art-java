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

package ru.art.grpc.server.servlet;

import io.grpc.stub.StreamObserver;
import lombok.Getter;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptionResult;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import ru.art.grpc.server.exception.GrpcServletException;
import ru.art.grpc.server.model.GrpcService.GrpcMethod;
import ru.art.grpc.server.specification.GrpcServiceSpecification;
import ru.art.grpc.servlet.GrpcRequest;
import ru.art.grpc.servlet.GrpcResponse;
import ru.art.grpc.servlet.GrpcServlet;
import ru.art.logging.ServiceCallLoggingParameters;
import ru.art.service.exception.ServiceExecutionException;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceRequest;
import ru.art.service.model.ServiceResponse;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.ThreadContext.get;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.InterceptionStrategy.PROCESS_HANDLING;
import static ru.art.core.constants.InterceptionStrategy.STOP_HANDLING;
import static ru.art.core.constants.StringConstants.BRACKETS;
import static ru.art.core.constants.StringConstants.DOT;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.entity.Value.asEntity;
import static ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import static ru.art.grpc.server.constants.GrpcServerExceptionMessages.*;
import static ru.art.grpc.server.constants.GrpcServerLoggingMessages.GRPC_LOGGING_EVENT;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.art.grpc.servlet.GrpcResponse.newBuilder;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.logging.LoggingModuleConstants.DEFAULT_REQUEST_ID;
import static ru.art.logging.LoggingModuleConstants.LoggingParameters.REQUEST_ID_KEY;
import static ru.art.logging.LoggingParametersManager.clearServiceCallLoggingParameters;
import static ru.art.logging.LoggingParametersManager.putServiceCallLoggingParameters;
import static ru.art.logging.ThreadContextExtensions.putIfNotNull;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.readProtobuf;
import static ru.art.protobuf.descriptor.ProtobufEntityWriter.writeProtobuf;
import static ru.art.service.ServiceController.executeServiceMethodUnchecked;
import static ru.art.service.constants.ServiceModuleConstants.REQUEST_VALUE_KEY;
import static ru.art.service.mapping.ServiceRequestMapping.toServiceRequest;
import static ru.art.service.mapping.ServiceResponseMapping.fromServiceResponse;
import java.util.Map;

public class GrpcServletContainer extends GrpcServlet {
    @Getter
    private final GrpcServletImplementation servlet;

    public GrpcServletContainer(String path, Map<String, GrpcServiceSpecification> services) {
        servlet = new GrpcServletImplementation(path, services);
    }

    public class GrpcServletImplementation extends GrpcServletImplBase {
        private final Map<String, GrpcServiceSpecification> services;

        private GrpcServletImplementation(String path, Map<String, GrpcServiceSpecification> services) {
            super(path);
            this.services = services;
        }

        @Override
        public void executeService(GrpcRequest grpcRequest, StreamObserver<GrpcResponse> responseObserver) {
            ServiceMethodCommand serviceMethodCommand = toServiceRequest()
                    .map(asEntity(readProtobuf(grpcRequest.getServiceRequest())))
                    .getServiceMethodCommand();
            String serviceId = serviceMethodCommand.getServiceId();
            String serviceMethodId = serviceId + DOT + serviceMethodCommand.getMethodId() + BRACKETS;
            clearServiceCallLoggingParameters();
            putServiceCallLoggingParameters(ServiceCallLoggingParameters.builder()
                    .serviceId(serviceId)
                    .serviceMethodId(serviceMethodId)
                    .serviceMethodCommand(serviceMethodId + DOT + getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID))
                    .loggingEventType(GRPC_LOGGING_EVENT)
                    .build());
            try {
                executeServiceChecked(grpcRequest, responseObserver);
                clearServiceCallLoggingParameters();
            } catch (Throwable e) {
                loggingModule()
                        .getLogger(GrpcServletContainer.class)
                        .error(GRPC_SERVICE_EXCEPTION, e);
                responseObserver.onNext(newBuilder()
                        .setServiceResponse(writeProtobuf(fromServiceResponse().map(ServiceResponse.builder()
                                .command(serviceMethodCommand)
                                .serviceException(new ServiceExecutionException(serviceMethodCommand, GRPC_SERVLET_ERROR, e))
                                .build())))
                        .build());
                responseObserver.onCompleted();
                clearServiceCallLoggingParameters();
            }
        }

        void executeServiceChecked(GrpcRequest grpcRequest, StreamObserver<GrpcResponse> responseObserver) {
            if (isNull(grpcRequest) || isNull(responseObserver)) {
                throw new GrpcServletException(GRPC_SERVLET_INPUT_PARAMETERS_NULL);
            }
            Entity serviceRequestEntity = asEntity(readProtobuf(grpcRequest.getServiceRequest()));
            putIfNotNull(REQUEST_VALUE_KEY, serviceRequestEntity);
            ServiceMethodCommand serviceMethodCommand = toServiceRequest().map(asEntity(serviceRequestEntity)).getServiceMethodCommand();
            String serviceId = serviceMethodCommand.getServiceId();
            String serviceMethodId = serviceId + DOT + serviceMethodCommand.getMethodId() + BRACKETS;
            GrpcServiceSpecification service = services.get(serviceId);
            if (isNull(service)) {
                sendServiceNotExistsError(responseObserver, serviceId);
                return;
            }
            if (!service.getServiceTypes().contains(GRPC_SERVICE_TYPE)) {
                sendServiceNotExistsError(responseObserver, serviceId);
                return;
            }
            Map<String, GrpcMethod> grpcMethods = service.getGrpcService().getMethods();
            GrpcMethod grpcMethod = grpcMethods.get(serviceMethodId);
            if (isNull(grpcMethod)) {
                sendMethodNotExistsError(responseObserver, serviceMethodId);
                return;
            }
            ValueFromModelMapper<?, ? extends Value> responseMapper = cast(grpcMethod.responseMapper());
            for (ValueInterceptor<Entity, Entity> requestValueInterceptor : grpcMethod.requestValueInterceptors()) {
                ValueInterceptionResult<Entity, Entity> result = requestValueInterceptor.intercept(serviceRequestEntity);
                if (isNull(result)) {
                    break;
                }
                serviceRequestEntity = result.getOutValue();
                if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                    break;
                }
                if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                    if (isNull(result.getOutValue()) || isNull(responseMapper)) {
                        responseObserver.onNext(newBuilder()
                                .setServiceResponse(writeProtobuf(fromServiceResponse().map(ServiceResponse.builder().build())))
                                .build());
                        responseObserver.onCompleted();
                        return;
                    }
                    GrpcResponse grpcResponse = newBuilder()
                            .setServiceResponse(writeProtobuf(result.getOutValue()))
                            .build();
                    responseObserver.onNext(grpcResponse);
                    responseObserver.onCompleted();
                    return;
                }
            }
            EntityToModelMapper<ServiceRequest<?>> toServiceRequest = cast(toServiceRequest(cast(grpcMethod.requestMapper())));
            ServiceRequest<?> serviceRequest = cast(toServiceRequest.map(asEntity(serviceRequestEntity)));
            ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(serviceRequest);
            EntityFromModelMapper<ServiceResponse<?>> fromServiceResponse = cast(fromServiceResponse(cast(grpcMethod.responseMapper())));
            Entity serviceResponseEntity = fromServiceResponse.map(serviceResponse);
            ServiceExecutionException serviceException = serviceResponse.getServiceException();
            for (ValueInterceptor<Entity, Entity> responseValueInterceptor : grpcMethod.responseValueInterceptors()) {
                ValueInterceptionResult<Entity, Entity> result = responseValueInterceptor.intercept(serviceRequestEntity);
                if (isNull(result)) {
                    break;
                }
                serviceResponseEntity = result.getOutValue();
                if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                    break;
                }
                if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                    if (isNull(result.getOutValue())) {
                        responseObserver.onNext(newBuilder().setServiceResponse(writeProtobuf(serviceResponseEntity)).build());
                        responseObserver.onCompleted();
                        return;
                    }
                    responseObserver.onNext(newBuilder().setServiceResponse(writeProtobuf(serviceResponseEntity)).build());
                    responseObserver.onCompleted();
                    return;
                }
            }
            GrpcResponse grpcResponse = newBuilder().setServiceResponse(writeProtobuf(serviceResponseEntity)).build();
            if (nonNull(serviceException)) {
                loggingModule()
                        .getLogger(GrpcServletContainer.class)
                        .error(GRPC_SERVICE_EXCEPTION, serviceException);
            }
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();
        }

        private void sendServiceNotExistsError(StreamObserver<GrpcResponse> responseObserver, String serviceId) {
            String errorMessage = format(GRPC_SERVICE_NOT_EXISTS_MESSAGE, serviceId);
            loggingModule()
                    .getLogger(GrpcServletContainer.class)
                    .error(errorMessage);
            responseObserver.onNext(newBuilder()
                    .setServiceResponse(writeProtobuf(fromServiceResponse().map(ServiceResponse.builder()
                            .serviceException(new ServiceExecutionException(GRPC_SERVICE_NOT_EXISTS_CODE, errorMessage))
                            .build())))
                    .build());
            responseObserver.onCompleted();
        }

        private void sendMethodNotExistsError(StreamObserver<GrpcResponse> responseObserver, String methodId) {
            String errorMessage = format(GRPC_METHOD_NOT_EXISTS_MESSAGE, methodId);
            loggingModule()
                    .getLogger(GrpcServletContainer.class)
                    .error(errorMessage);
            responseObserver.onNext(newBuilder()
                    .setServiceResponse(writeProtobuf(fromServiceResponse().map(ServiceResponse.builder()
                            .serviceException(new ServiceExecutionException(GRPC_METHOD_NOT_EXISTS_CODE, errorMessage))
                            .build())))
                    .build());
            responseObserver.onCompleted();
        }
    }
}