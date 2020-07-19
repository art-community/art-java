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

package io.art.grpc.server.servlet;

import io.art.entity.immutable.*;
import io.grpc.stub.*;
import lombok.*;
import io.art.entity.interceptor.*;
import io.art.entity.mapper.ValueToModelMapper.*;
import io.art.grpc.server.exception.*;
import io.art.grpc.server.model.GrpcService.*;
import io.art.grpc.server.specification.*;
import io.art.grpc.servlet.*;
import io.art.logging.*;
import io.art.server.service.exception.*;
import io.art.server.model.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static org.apache.logging.log4j.ThreadContext.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.constants.InterceptionStrategy.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.entity.immutable.Value.*;
import static io.art.entity.mapper.ValueFromModelMapper.*;
import static io.art.grpc.server.constants.GrpcServerExceptionMessages.*;
import static io.art.grpc.server.constants.GrpcServerLoggingMessages.*;
import static io.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static io.art.logging.LoggingModule.*;
import static io.art.logging.LoggingModuleConstants.*;
import static io.art.logging.LoggingModuleConstants.LoggingParameters.*;
import static io.art.logging.LoggingContext.*;
import static io.art.protobuf.descriptor.ProtobufEntityReader.*;
import static io.art.protobuf.descriptor.ProtobufEntityWriter.*;
import static io.art.service.ServiceController.*;
import static io.art.server.module.ServerModule.serviceModuleState;
import static io.art.server.constants.RequestValidationPolicy.*;
import static io.art.service.factory.ServiceRequestFactory.*;
import static io.art.service.factory.ServiceResponseFactory.*;
import static io.art.service.mapping.ServiceRequestMapping.*;
import static io.art.service.mapping.ServiceResponseMapping.*;
import java.util.*;

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
        public void executeService(com.google.protobuf.Value grpcRequest, StreamObserver<com.google.protobuf.Value> responseObserver) {
            ServiceMethodCommand command = toServiceRequest()
                    .map(asEntity(readProtobuf(grpcRequest)))
                    .getServiceMethodCommand();
            String serviceId = command.getServiceId();
            String serviceMethodId = serviceId + DOT + command.getMethodId() + BRACKETS;
            clearServiceLoggingContext();
            putLoggingParameters(ServiceLoggingContext.builder()
                    .serviceId(serviceId)
                    .serviceMethodId(serviceMethodId)
                    .serviceMethodCommand(serviceMethodId + DOT + getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID))
                    .logEventType(GRPC_LOGGING_EVENT)
                    .loadedServices(serviceModuleState().getServiceRegistry().getServices().keySet())
                    .build());
            try {
                executeServiceChecked(grpcRequest, command, responseObserver);
                clearServiceLoggingContext();
            } catch (Throwable throwable) {
                loggingModule()
                        .getLogger(GrpcServletContainer.class)
                        .error(GRPC_SERVICE_EXCEPTION, throwable);
                responseObserver.onNext(writeProtobuf(fromServiceResponse().map(errorResponse(command, GRPC_SERVLET_ERROR, throwable))));
                responseObserver.onCompleted();
                clearServiceLoggingContext();
            }
        }

        private void executeServiceChecked(com.google.protobuf.Value grpcRequest, ServiceMethodCommand command, StreamObserver<com.google.protobuf.Value> responseObserver) {
            if (isNull(grpcRequest) || isNull(responseObserver)) {
                throw new GrpcServletException(GRPC_SERVLET_INPUT_PARAMETERS_NULL);
            }
            Entity serviceRequestEntity = asEntity(readProtobuf(grpcRequest));
            String serviceId = command.getServiceId();
            String serviceMethodId = command.getMethodId();
            GrpcServiceSpecification service = services.get(serviceId);
            if (isNull(service)) {
                sendServiceNotExistsError(responseObserver, serviceId);
                return;
            }
            if (!service.getServiceTypes().contains(GRPC_SERVICE_TYPE)) {
                sendServiceNotExistsError(responseObserver, serviceId);
                return;
            }
            Map<String, GrpcMethod> grpcMethods = service.getGrpcService().getGrpcMethods();
            GrpcMethod grpcMethod = grpcMethods.get(serviceMethodId);
            if (isNull(grpcMethod)) {
                sendMethodNotExistsError(responseObserver, serviceMethodId);
                return;
            }
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
                    if (valueIsNull(result.getOutValue())) {
                        responseObserver.onNext(writeProtobuf(fromServiceResponse().map(okResponse(command))));
                        responseObserver.onCompleted();
                        return;
                    }
                    responseObserver.onNext(writeProtobuf(result.getOutValue()));
                    responseObserver.onCompleted();
                    return;
                }
            }
            EntityToModelMapper<ServiceRequest<?>> toServiceRequest = cast(toServiceRequest(cast(grpcMethod.requestMapper())));
            ServiceRequest<?> mappedServiceRequest = toServiceRequest.map(asEntity(serviceRequestEntity));
            ServiceRequest<?> serviceRequest = isEmpty(serviceRequestEntity.get(REQUEST_DATA)) || isNull(grpcMethod.requestMapper())
                    ? newServiceRequest(command, getOrElse(grpcMethod.validationPolicy(), NON_VALIDATABLE))
                    : newServiceRequest(command, mappedServiceRequest.getRequestData(), getOrElse(grpcMethod.validationPolicy(), NON_VALIDATABLE));
            ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(serviceRequest);
            handleResponse(responseObserver, grpcMethod, serviceResponse);
        }

        private void sendServiceNotExistsError(StreamObserver<com.google.protobuf.Value> responseObserver, String serviceId) {
            String errorMessage = format(GRPC_SERVICE_NOT_EXISTS_MESSAGE, serviceId);
            loggingModule()
                    .getLogger(GrpcServletContainer.class)
                    .error(errorMessage);
            responseObserver.onNext(writeProtobuf(fromServiceResponse().map(errorResponse(GRPC_SERVICE_NOT_EXISTS_CODE, errorMessage))));
            responseObserver.onCompleted();
        }

        private void sendMethodNotExistsError(StreamObserver<com.google.protobuf.Value> responseObserver, String methodId) {
            String errorMessage = format(GRPC_METHOD_NOT_EXISTS_MESSAGE, methodId);
            loggingModule()
                    .getLogger(GrpcServletContainer.class)
                    .error(errorMessage);
            responseObserver.onNext(writeProtobuf(fromServiceResponse().map(errorResponse(GRPC_METHOD_NOT_EXISTS_CODE, errorMessage))));
            responseObserver.onCompleted();
        }

        private void handleResponse(StreamObserver<com.google.protobuf.Value> responseObserver, GrpcMethod grpcMethod, ServiceResponse<?> serviceResponse) {
            EntityFromModelMapper<ServiceResponse<?>> fromServiceResponse = cast(fromServiceResponse(cast(grpcMethod.responseMapper())));
            Entity serviceResponseEntity = fromServiceResponse.map(serviceResponse);
            ServiceExecutionException serviceException = serviceResponse.getServiceException();
            for (ValueInterceptor<Entity, Entity> responseValueInterceptor : grpcMethod.responseValueInterceptors()) {
                ValueInterceptionResult<Entity, Entity> result = responseValueInterceptor.intercept(serviceResponseEntity);
                if (isNull(result)) {
                    break;
                }
                serviceResponseEntity = result.getOutValue();
                if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                    break;
                }
                if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                    if (valueIsNull(result.getOutValue())) {
                        responseObserver.onNext(writeProtobuf(serviceResponseEntity));
                        responseObserver.onCompleted();
                        return;
                    }
                    responseObserver.onNext(writeProtobuf(serviceResponseEntity));
                    responseObserver.onCompleted();
                    return;
                }
            }
            if (nonNull(serviceException)) {
                loggingModule()
                        .getLogger(GrpcServletContainer.class)
                        .error(GRPC_SERVICE_EXCEPTION, serviceException);
            }
            responseObserver.onNext(writeProtobuf(serviceResponseEntity));
            responseObserver.onCompleted();
        }
    }
}
