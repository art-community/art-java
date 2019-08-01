package ru.adk.grpc.client.handler;

import java.util.Optional;

@FunctionalInterface
public interface GrpcCommunicationExceptionHandler<RequestType> {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void failed(Optional<RequestType> request, Throwable exception);
}
