package ru.adk.http.client.handler;

import java.util.Optional;

@FunctionalInterface
public interface HttpCommunicationResponseHandler<RequestType, ResponseType> {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void completed(Optional<RequestType> request, Optional<ResponseType> response);
}
