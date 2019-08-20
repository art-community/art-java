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

package ru.art.grpc.client.communicator;

import io.grpc.ClientInterceptor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.grpc.client.exception.GrpcClientException;
import ru.art.grpc.client.handler.GrpcCommunicationCompletionHandler;
import ru.art.grpc.client.handler.GrpcCommunicationExceptionHandler;
import static lombok.AccessLevel.PACKAGE;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.grpc.client.constants.GrpcClientExceptionMessages.INVALID_GRPC_COMMUNICATION_CONFIGURATION;
import static ru.art.grpc.client.module.GrpcClientModule.grpcClientModule;
import java.util.List;
import java.util.concurrent.Executor;

@Getter(value = PACKAGE)
@Setter(value = PACKAGE)
@ToString(onlyExplicitlyIncluded = true)
public class GrpcCommunicationConfiguration {
    @ToString.Include
    private String serviceId;
    @ToString.Include
    private String methodId;
    @ToString.Include
    private String url;
    @ToString.Include
    private String path;
    private ValueFromModelMapper<?, ? extends Value> requestMapper;
    private ValueToModelMapper<?, ? extends Value> responseMapper;
    private GrpcCommunicationCompletionHandler<?, ?> completionHandler;
    private GrpcCommunicationExceptionHandler<?> exceptionHandler;
    private List<ClientInterceptor> interceptors = grpcClientModule().getInterceptors();
    private Executor overrideExecutor;
    @ToString.Include
    private Object request;
    @ToString.Include
    private long deadlineTimeout;
    @ToString.Include
    private boolean useSecuredTransport;
    private List<ValueInterceptor<Entity, Entity>> requestValueInterceptors = grpcClientModule().getRequestValueInterceptors();
    private List<ValueInterceptor<Entity, Entity>> responseValueInterceptors = grpcClientModule().getResponseValueInterceptors();
    private Executor asynchronousFuturesExecutor = grpcClientModule().getAsynchronousFuturesExecutor();

    void validateRequiredFields() {
        boolean serviceIdIsEmpty = isEmpty(serviceId);
        boolean methodIdIsEmpty = isEmpty(methodId);
        if (!serviceIdIsEmpty && !methodIdIsEmpty) {
            return;
        }
        if (serviceIdIsEmpty && methodIdIsEmpty) {
            throw new GrpcClientException(INVALID_GRPC_COMMUNICATION_CONFIGURATION + "serviceId,methodId");
        }
        if (serviceIdIsEmpty) {
            throw new GrpcClientException(INVALID_GRPC_COMMUNICATION_CONFIGURATION + "serviceId");
        }
        throw new GrpcClientException("methodId");
    }
}
