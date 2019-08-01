package ru.adk.grpc.client.model;

import lombok.*;
import lombok.experimental.Accessors;
import static ru.adk.grpc.client.module.GrpcClientModule.grpcClientModule;

@Getter
@Accessors(fluent = true)
@Builder(toBuilder = true)
public class GrpcCommunicationTargetConfiguration {
    private final String serviceId;
    @Builder.Default
    private final String host = grpcClientModule().getBalancerHost();
    @Builder.Default
    private final Integer port = grpcClientModule().getBalancerPort();
    @Builder.Default
    private final long timeout = grpcClientModule().getTimeout();
    @Setter
    private String url;
    @Setter
    private String path;
    @Setter
    private boolean secured;
}
