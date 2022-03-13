package io.art.storage.service;

import io.art.core.annotation.*;
import lombok.*;
import reactor.core.publisher.*;

@Public
@RequiredArgsConstructor
public class ReactiveIndex4Service<ModelType, F1, F2, F3, F4> {
    private final ReactiveIndexService<ModelType> delegate;

    public Mono<ModelType> findFirst(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.first(key1, key2, key3, key4);
    }

    public Flux<ModelType> delete(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.delete(key1, key2, key3, key4);
    }

    public Flux<ModelType> findAll(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.findAll(key1, key2, key3, key4);
    }

    public Mono<Long> count() {
        return delegate.count();
    }
}
