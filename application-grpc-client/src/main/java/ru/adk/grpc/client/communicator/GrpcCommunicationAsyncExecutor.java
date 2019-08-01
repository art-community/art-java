package ru.adk.grpc.client.communicator;

import com.google.common.util.concurrent.FutureCallback;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannelBuilder;
import lombok.NoArgsConstructor;
import ru.adk.entity.Value;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.grpc.client.exception.GrpcClientException;
import ru.adk.grpc.servlet.GrpcRequest;
import ru.adk.grpc.servlet.GrpcResponse;
import ru.adk.grpc.servlet.GrpcServlet;
import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static io.grpc.ManagedChannelBuilder.forTarget;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.adk.core.extension.StringExtensions.emptyIfNull;
import static ru.adk.grpc.client.communicator.GrpcServiceResponseExtractor.extractServiceResponse;
import static ru.adk.grpc.client.constants.GrpcClientExceptionMessages.RESPONSE_IS_NULL;
import static ru.adk.grpc.client.module.GrpcClientModule.grpcClientModule;
import static ru.adk.grpc.servlet.GrpcRequest.newBuilder;
import static ru.adk.protobuf.descriptor.ProtobufEntityWriter.writeProtobuf;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.Executor;

@NoArgsConstructor(access = PRIVATE)
class GrpcCommunicationAsyncExecutor {
    @SuppressWarnings("Duplicates")
    static void execute(GrpcCommunicationConfiguration configuration) {
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
        GrpcRequest.Builder grpcRequestBuilder = newBuilder()
                .setServiceId(configuration.getServiceId())
                .setMethodId(configuration.getMethodId());
        ValueFromModelMapper<?, ? extends Value> requestMapper;
        Object request;
        if (nonNull(requestMapper = configuration.getRequestMapper()) && nonNull(request = configuration.getRequest())) {
            grpcRequestBuilder.setRequestData(writeProtobuf(requestMapper.map(cast(request))));
        }
        addCallback(stub.executeService(grpcRequestBuilder.build()), new FutureCallback<GrpcResponse>() {
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
                configuration.getCompletionHandler()
                        .onComplete(ofNullable(cast(configuration.getRequest())), extractServiceResponse(configuration, response));
            }

            @Override
            @ParametersAreNonnullByDefault
            public void onFailure(Throwable exception) {
                if (isNull(configuration.getExceptionHandler())) {
                    return;
                }
                configuration.getExceptionHandler().failed(ofNullable(cast(configuration.getRequest())), exception);
            }
        }, directExecutor());
    }
}
