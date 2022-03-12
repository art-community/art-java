package io.art.storage.service;

import io.art.core.annotation.*;
import lombok.*;
import reactor.core.publisher.*;

@Public
@RequiredArgsConstructor
public class ReactiveIndex1Service<ModelType, F1> {
    private final ReactiveIndexService<ModelType> delegate;

    public Mono<ModelType> findFirst(F1 key1) {
        return delegate.findFirst(key1);
    }

    public Flux<ModelType> delete(F1 key1) {
        return delegate.delete(key1);
    }

    public Flux<ModelType> findAll(F1 key1) {
        return delegate.findAll(key1);
    }

    public Mono<Long> count() {
        return delegate.count();
    }
}
