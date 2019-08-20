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

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;
import lombok.NoArgsConstructor;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptionResult;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.grpc.client.exception.GrpcClientException;
import ru.art.grpc.servlet.GrpcResponse;
import ru.art.grpc.servlet.GrpcServlet;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceRequest;
import ru.art.service.model.ServiceResponse;
import static com.google.common.util.concurrent.Futures.addCallback;
import static io.grpc.ManagedChannelBuilder.forTarget;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.art.core.constants.InterceptionStrategy.PROCESS_HANDLING;
import static ru.art.core.constants.InterceptionStrategy.STOP_HANDLING;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.entity.Value.asEntity;
import static ru.art.grpc.client.constants.GrpcClientExceptionMessages.RESPONSE_IS_NULL;
import static ru.art.grpc.client.module.GrpcClientModule.grpcClientModule;
import static ru.art.grpc.servlet.GrpcRequest.newBuilder;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.readProtobuf;
import static ru.art.protobuf.descriptor.ProtobufEntityWriter.writeProtobuf;
import static ru.art.service.factory.ServiceRequestFactory.newServiceRequest;
import static ru.art.service.factory.ServiceResponseFactory.okResponse;
import static ru.art.service.mapping.ServiceRequestMapping.fromServiceRequest;
import static ru.art.service.mapping.ServiceResponseMapping.toServiceResponse;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
        ListenableFuture<GrpcResponse> future = stub.executeService(newBuilder().setServiceRequest(writeProtobuf(requestValue)).build());
        addCallback(future, new FutureCallback<GrpcResponse>() {
            @Override
            public void onSuccess(GrpcResponse response) {
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
                Entity responseValue = asEntity(readProtobuf(response.getServiceResponse()));
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
        }, configuration.getAsynchronousFuturesExecutor());
        return supplyAsync(() -> executeGrpcFuture(configuration, future, command), configuration.getAsynchronousFuturesExecutor());
    }

    private static <ResponseType> ServiceResponse<ResponseType> executeGrpcFuture(GrpcCommunicationConfiguration configuration, ListenableFuture<GrpcResponse> future, ServiceMethodCommand command) {
        try {
            GrpcResponse grpcResponse = future.get();
            if (isNull(grpcResponse)) {
                return okResponse(command);
            }
            Entity responseValue = asEntity(readProtobuf(grpcResponse.getServiceResponse()));
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
