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
import io.grpc.ManagedChannelBuilder;
import lombok.NoArgsConstructor;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.grpc.servlet.GrpcResponse;
import ru.art.grpc.servlet.GrpcServlet;
import ru.art.grpc.servlet.GrpcServlet.GrpcServletBlockingStub;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;
import static io.grpc.ManagedChannelBuilder.forTarget;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.grpc.client.communicator.GrpcServiceResponseExtractor.extractServiceResponse;
import static ru.art.grpc.client.module.GrpcClientModule.grpcClientModule;
import static ru.art.grpc.servlet.GrpcRequest.newBuilder;
import static ru.art.protobuf.descriptor.ProtobufEntityWriter.writeProtobuf;
import static ru.art.service.factory.ServiceRequestFactory.newServiceRequest;
import static ru.art.service.mapping.ServiceRequestMapping.fromServiceRequest;
import java.util.concurrent.Executor;

@NoArgsConstructor(access = PRIVATE)
class GrpcCommunicationExecutor {
    @SuppressWarnings("Duplicates")
    static <ResponseType> ServiceResponse<ResponseType> execute(GrpcCommunicationConfiguration configuration) {
        ManagedChannelBuilder<?> channelBuilder = forTarget(configuration.getUrl()).usePlaintext();
        if (configuration.isUseSecuredTransport()) {
            channelBuilder.useTransportSecurity();
        }
        long deadlineTimeout = configuration.getDeadlineTimeout();
        GrpcServletBlockingStub stub = new GrpcServlet().newBlockingStub(channelBuilder.build(), emptyIfNull(configuration.getPath()))
                .withDeadlineAfter(deadlineTimeout > 0L ? deadlineTimeout : grpcClientModule().getTimeout(), MILLISECONDS)
                .withInterceptors(configuration.getInterceptors().toArray(new ClientInterceptor[0]));
        Executor executor;
        if (nonNull(executor = configuration.getOverrideExecutor()) || nonNull(executor = grpcClientModule().getOverridingExecutor())) {
            stub = stub.withExecutor(executor);
        }
        ServiceMethodCommand serviceMethodCommand = new ServiceMethodCommand(configuration.getServiceId(), configuration.getMethodId());
        ValueFromModelMapper<?, ? extends Value> requestMapper;
        Object request;
        if (nonNull(requestMapper = configuration.getRequestMapper()) && nonNull(request = configuration.getRequest())) {
            newServiceRequest(serviceMethodCommand, request);
            GrpcResponse response = stub.executeService(newBuilder()
                    .setServiceRequest(writeProtobuf(fromServiceRequest(cast(requestMapper)).map(newServiceRequest(serviceMethodCommand))))
                    .build());
            return extractServiceResponse(configuration, response);
        }
        GrpcResponse response = stub.executeService(newBuilder()
                .setServiceRequest(writeProtobuf(fromServiceRequest().map(newServiceRequest(serviceMethodCommand))))
                .build());
        return extractServiceResponse(configuration, response);
    }
}