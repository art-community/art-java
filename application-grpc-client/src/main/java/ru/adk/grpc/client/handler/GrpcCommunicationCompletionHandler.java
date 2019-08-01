package ru.adk.grpc.client.handler;

import ru.adk.service.model.ServiceResponse;
import java.util.Optional;

@FunctionalInterface
public interface GrpcCommunicationCompletionHandler<RequestType, ResponseType> {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void onComplete(Optional<RequestType> request, ServiceResponse<ResponseType> response);
}
