package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import lombok.*;
import reactor.core.publisher.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class ReactiveIndex4Service<ModelType, F1, F2, F3, F4> {
    private final ReactiveIndexService<ModelType> delegate;

    public Mono<ModelType> first(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.first(key1, key2, key3, key4);
    }

    public Flux<ModelType> select(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.select(key1, key2, key3, key4);
    }

    @SafeVarargs
    public final Flux<ModelType> find(Tuple4<F1, F2, F3, F4>... keys) {
        return delegate.find(keys);
    }

    public Flux<ModelType> find(Collection<Tuple4<F1, F2, F3, F4>> keys) {
        return delegate.find(keys);
    }

    public Flux<ModelType> find(ImmutableCollection<Tuple4<F1, F2, F3, F4>> keys) {
        return delegate.find(keys);
    }

    @SafeVarargs
    public final Flux<ModelType> delete(Tuple4<F1, F2, F3, F4>... keys) {
        return delegate.delete(keys);
    }

    public Flux<ModelType> delete(Collection<Tuple4<F1, F2, F3, F4>> keys) {
        return delegate.delete(keys);
    }

    public Flux<ModelType> delete(ImmutableCollection<Tuple4<F1, F2, F3, F4>> keys) {
        return delegate.delete(keys);
    }

    public Mono<Long> count() {
        return delegate.count();
    }
}
