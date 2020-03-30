package ru.art.grpc.client.communicator;

import io.grpc.*;
import static io.grpc.ManagedChannelBuilder.*;
import static java.util.concurrent.TimeUnit.*;
import static ru.art.core.context.Context.*;
import static ru.art.grpc.client.module.GrpcClientModule.*;

public class GrpcCommunicatorChannelFactory {
    static ManagedChannel createChannel(GrpcCommunicationConfiguration configuration) {
        ManagedChannelBuilder<?> channelBuilder = forTarget(configuration.getUrl()).usePlaintext();
        if (configuration.isUseSecuredTransport()) {
            channelBuilder.useTransportSecurity();
        }
        ManagedChannel channel = channelBuilder
                .keepAliveTime(configuration.getKeepAliveTimeNanos(), NANOSECONDS)
                .keepAliveTimeout(configuration.getKeepAliveTimeOutNanos(), NANOSECONDS)
                .keepAliveWithoutCalls(configuration.isKeepAliveWithoutCalls())
                .build();
        return grpcClientModuleState().registerChannel(channel);
    }
}
