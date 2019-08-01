package ru.art.http.client.handler;

import java.util.Optional;

@FunctionalInterface
public interface HttpCommunicationCancellationHandler<T> {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void cancelled(Optional<T> request);
}
