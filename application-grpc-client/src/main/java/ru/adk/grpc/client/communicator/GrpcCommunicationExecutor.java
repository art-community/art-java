package ru.adk.grpc.client.communicator;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;
import lombok.NoArgsConstructor;
import ru.adk.entity.Value;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.grpc.servlet.GrpcRequest;
import ru.adk.grpc.servlet.GrpcResponse;
import ru.adk.grpc.servlet.GrpcServlet;
import ru.adk.grpc.servlet.GrpcServlet.GrpcServletBlockingStub;
import ru.adk.service.model.ServiceResponse;
import static io.grpc.ManagedChannelBuilder.forTarget;
import static java.util.Objects.nonNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.adk.core.extension.StringExtensions.emptyIfNull;
import static ru.adk.grpc.client.communicator.GrpcServiceResponseExtractor.extractServiceResponse;
import static ru.adk.grpc.client.module.GrpcClientModule.grpcClientModule;
import static ru.adk.grpc.servlet.GrpcRequest.newBuilder;
import static ru.adk.protobuf.descriptor.ProtobufEntityWriter.writeProtobuf;
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
                .withInterceptors(ifEmpty(configuration.getInterceptors(), grpcClientModule().getInterceptors()).toArray(new ClientInterceptor[0]));
        Executor executor;
        if (nonNull(executor = configuration.getOverrideExecutor()) || nonNull(executor = grpcClientModule().getOverridingExecutor())) {
            stub = stub.withExecutor(executor);
        }
        GrpcRequest.Builder grpcRequestBuilder = newBuilder()
                .setServiceId(configuration.getServiceId())
                .setMethodId(configuration.getMethodId());
        ValueFromModelMapper<?, ? extends Value> requestMapper;
        Object request;
        if (nonNull(requestMapper = configuration.getRequestMapper()) && nonNull(request = configuration.getRequest())) {
            grpcRequestBuilder.setRequestData(writeProtobuf(requestMapper.map(cast(request))));
        }
        GrpcResponse response = stub.executeService(grpcRequestBuilder.build());
        return extractServiceResponse(configuration, response);
    }
}