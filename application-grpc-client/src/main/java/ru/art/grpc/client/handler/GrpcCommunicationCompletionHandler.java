package ru.art.grpc.client.handler;

import ru.art.service.model.ServiceResponse;
import java.util.Optional;

@FunctionalInterface
public interface GrpcCommunicationCompletionHandler<RequestType, ResponseType> {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void onComplete(Optional<RequestType> request, ServiceResponse<ResponseType> response);
}
