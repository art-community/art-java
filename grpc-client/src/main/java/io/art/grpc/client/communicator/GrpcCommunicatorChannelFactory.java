package io.art.grpc.client.communicator;

import io.grpc.*;
import io.grpc.internal.*;
import io.grpc.util.*;
import io.netty.resolver.*;
import io.art.grpc.client.interceptor.*;
import static io.grpc.ManagedChannelBuilder.*;
import static java.util.concurrent.TimeUnit.*;
import static io.art.core.context.Context.*;
import static io.art.grpc.client.module.GrpcClientModule.*;

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
