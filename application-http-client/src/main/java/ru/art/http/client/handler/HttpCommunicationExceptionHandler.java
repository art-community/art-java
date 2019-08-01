package ru.art.http.client.handler;

import java.util.Optional;

@FunctionalInterface
public interface HttpCommunicationExceptionHandler<T>  {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void failed(Optional<T> request, Exception exception);
}
