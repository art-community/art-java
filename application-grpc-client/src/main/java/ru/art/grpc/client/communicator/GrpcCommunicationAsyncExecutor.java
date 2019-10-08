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

import com.google.common.util.concurrent.*;
import io.grpc.*;
import lombok.*;
import ru.art.entity.Value;
import ru.art.entity.*;
import ru.art.entity.interceptor.*;
import ru.art.entity.mapper.*;
import ru.art.grpc.client.exception.*;
import ru.art.grpc.servlet.*;
import ru.art.service.model.*;
import static com.google.common.util.concurrent.Futures.*;
import static io.grpc.ManagedChannelBuilder.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static java.util.concurrent.CompletableFuture.*;
import static java.util.concurrent.TimeUnit.*;
import static lombok.AccessLevel.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.InterceptionStrategy.*;
import static ru.art.core.extension.StringExtensions.*;
import static ru.art.entity.Value.*;
import static ru.art.grpc.client.constants.GrpcClientExceptionMessages.*;
import static ru.art.grpc.client.module.GrpcClientModule.*;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.*;
import static ru.art.protobuf.descriptor.ProtobufEntityWriter.*;
import static ru.art.service.factory.ServiceRequestFactory.*;
import static ru.art.service.factory.ServiceResponseFactory.*;
import static ru.art.service.mapping.ServiceRequestMapping.*;
import static ru.art.service.mapping.ServiceResponseMapping.*;
import javax.annotation.*;
import java.util.*;
import java.util.concurrent.*;

@NoArgsConstructor(access = PRIVATE)
class GrpcCommunicationAsyncExecutor {
    @SuppressWarnings("Duplicates")
    static <ResponseType> CompletableFuture<ServiceResponse<ResponseType>> execute(GrpcCommunicationConfiguration configuration) {
        ManagedChannelBuilder<?> channelBuilder = forTarget(configuration.getUrl()).usePlaintext();
        if (configuration.isUseSecuredTransport()) {
            channelBuilder.useTransportSecurity();
        }
        long deadlineTimeout = configuration.getDeadlineTimeout();
        GrpcServlet.GrpcServletFutureStub stub = new GrpcServlet().newFutureStub(channelBuilder.build(), emptyIfNull(configuration.getPath()))
                .withDeadlineAfter(deadlineTimeout > 0L ? deadlineTimeout : grpcClientModule().getTimeout(), MILLISECONDS)
                .withInterceptors(ifEmpty(configuration.getInterceptors(), grpcClientModule().getInterceptors()).toArray(new ClientInterceptor[0]));
        Executor executor;
        if (nonNull(executor = configuration.getOverrideExecutor()) || nonNull(executor = grpcClientModule().getOverridingExecutor())) {
            stub = stub.withExecutor(executor);
        }
        ServiceMethodCommand command = new ServiceMethodCommand(configuration.getServiceId(), configuration.getMethodId());
        ValueFromModelMapper<?, ? extends Value> requestMapper = null;
        Object request;
        ServiceRequest<Object> serviceRequest = isNull(request = configuration.getRequest())
                || isNull(requestMapper = configuration.getRequestMapper())
                ? newServiceRequest(command)
                : newServiceRequest(command, request);
        Entity requestValue = fromServiceRequest(cast(requestMapper)).map(serviceRequest);
        List<ValueInterceptor<Entity, Entity>> requestValueInterceptors = configuration.getRequestValueInterceptors();
        for (ValueInterceptor<Entity, Entity> requestValueInterceptor : requestValueInterceptors) {
            ValueInterceptionResult<Entity, Entity> result = requestValueInterceptor.intercept(requestValue);
            if (isNull(result)) {
                break;
            }
            requestValue = result.getOutValue();
            if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                break;
            }
            if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                return completedFuture(okResponse(command));
            }
        }
        ListenableFuture<com.google.protobuf.Value> future = stub.executeService(writeProtobuf(requestValue));
        addCallback(future, createFutureCallback(configuration), configuration.getAsynchronousFuturesExecutor());
        return supplyAsync(() -> executeGrpcFuture(configuration, future, command), configuration.getAsynchronousFuturesExecutor());
    }

    private static FutureCallback<com.google.protobuf.Value> createFutureCallback(GrpcCommunicationConfiguration configuration) {
        return new FutureCallback<com.google.protobuf.Value>() {
            @Override
            public void onSuccess(com.google.protobuf.Value response) {
                if (isNull(response)) {
                    if (isNull(configuration.getExceptionHandler())) {
                        return;
                    }
                    configuration.getExceptionHandler().failed(empty(), new GrpcClientException(RESPONSE_IS_NULL));
                    return;
                }
                if (isNull(configuration.getCompletionHandler())) {
                    return;
                }
                Entity responseValue = asEntity(readProtobuf(response));
                List<ValueInterceptor<Entity, Entity>> responseValueInterceptors = configuration.getResponseValueInterceptors();
                for (ValueInterceptor<Entity, Entity> responseValueInterceptor : responseValueInterceptors) {
                    ValueInterceptionResult<Entity, Entity> result = responseValueInterceptor.intercept(responseValue);
                    if (isNull(result)) {
                        break;
                    }
                    responseValue = result.getOutValue();
                    if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                        break;
                    }
                    if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                        return;
                    }
                }
                configuration.getCompletionHandler()
                        .onComplete(ofNullable(cast(configuration.getRequest())),
                                cast(toServiceResponse(cast(configuration.getResponseMapper())).map(responseValue)));
            }

            @Override
            @ParametersAreNonnullByDefault
            public void onFailure(Throwable exception) {
                if (isNull(configuration.getExceptionHandler())) {
                    return;
                }
                configuration.getExceptionHandler().failed(ofNullable(cast(configuration.getRequest())), exception);
            }
        };
    }

    private static <ResponseType> ServiceResponse<ResponseType> executeGrpcFuture(GrpcCommunicationConfiguration configuration, ListenableFuture<com.google.protobuf.Value> future, ServiceMethodCommand command) {
        try {
            com.google.protobuf.Value grpcResponse = future.get();
            if (isNull(grpcResponse)) {
                return okResponse(command);
            }
            Entity responseValue = asEntity(readProtobuf(grpcResponse));
            List<ValueInterceptor<Entity, Entity>> responseValueInterceptors = configuration.getResponseValueInterceptors();
            for (ValueInterceptor<Entity, Entity> responseValueInterceptor : responseValueInterceptors) {
                ValueInterceptionResult<Entity, Entity> result = responseValueInterceptor.intercept(responseValue);
                if (isNull(result)) {
                    break;
                }
                responseValue = result.getOutValue();
                if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                    break;
                }
                if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                    return okResponse(command);
                }
            }
            return cast(toServiceResponse(cast(configuration.getResponseMapper())).map(responseValue));
        } catch (Exception e) {
            throw new GrpcClientException(e);
        }
    }
}
