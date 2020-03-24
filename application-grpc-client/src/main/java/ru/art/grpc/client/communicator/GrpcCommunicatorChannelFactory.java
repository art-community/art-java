package ru.art.grpc.client.communicator;

import io.grpc.*;
import ru.art.grpc.client.module.*;
import static io.grpc.ManagedChannelBuilder.*;
import static java.util.concurrent.TimeUnit.*;
import static ru.art.grpc.client.module.GrpcClientModule.*;

public class GrpcCommunicatorChannelFactory {
    static ManagedChannel createChannel(GrpcCommunicationConfiguration configuration) {
        ManagedChannelBuilder<?> channelBuilder = forTarget(configuration.getUrl()).usePlaintext();
        if (configuration.isUseSecuredTransport()) {
            channelBuilder.useTransportSecurity();
        }
        return grpcClientModuleState().registerChannel(channelBuilder
                .keepAliveTime(configuration.getKeepAliveTimeNanos(), NANOSECONDS)
                .keepAliveTimeout(configuration.getKeepAliveTimeOutNanos(), NANOSECONDS)
                .keepAliveWithoutCalls(configuration.isKeepAliveWithoutCalls())
                .build());
    }
}
