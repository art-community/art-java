package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.model.Tuple.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class ReactiveIndex3Service<ModelType, F1, F2, F3> {
    private final ReactiveIndexService<ModelType> delegate;

    public Mono<ModelType> first(F1 key1, F2 key2, F3 key3) {
        return delegate.first(tuple(key1, key2, key3));
    }

    public Mono<ModelType> update(F1 key1, F2 key2, F3 key3, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(tuple(key1, key2, key3), updater);
    }

    public Flux<ModelType> update(Collection<Tuple3<F1, F2, F3>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<ModelType> update(ImmutableCollection<Tuple3<F1, F2, F3>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<ModelType> select(F1 key1, F2 key2, F3 key3) {
        return delegate.select(tuple(key1, key2, key3));
    }

    public Flux<ModelType> select(F1 key1, F2 key2, F3 key3, int offset, int limit) {
        return delegate.select(tuple(key1, key2, key3), offset, limit);
    }

    @SafeVarargs
    public final Flux<ModelType> find(Tuple3<F1, F2, F3>... keys) {
        return delegate.find(keys);
    }

    public Flux<ModelType> find(Collection<Tuple3<F1, F2, F3>> keys) {
        return delegate.find(keys);
    }

    public Flux<ModelType> find(ImmutableCollection<Tuple3<F1, F2, F3>> keys) {
        return delegate.find(keys);
    }

    public final Mono<ModelType> delete(F1 key1, F2 key2, F3 key3) {
        return delegate.delete(tuple(key1, key2, key3));
    }

    @SafeVarargs
    public final Flux<ModelType> delete(Tuple3<F1, F2, F3>... keys) {
        return delegate.delete(keys);
    }

    public Flux<ModelType> delete(Collection<Tuple3<F1, F2, F3>> keys) {
        return delegate.delete(keys);
    }

    public Flux<ModelType> delete(ImmutableCollection<Tuple3<F1, F2, F3>> keys) {
        return delegate.delete(keys);
    }

    public Mono<Long> count(F1 key1, F2 key2, F3 key3) {
        return delegate.count(tuple(key1, key2, key3));
    }

    public ReactiveSpaceStream<ModelType> stream() {
        return delegate.stream();
    }

    public ReactiveSpaceStream<ModelType> stream(F1 key1, F2 key2, F3 key3) {
        return delegate.stream(tuple(key1, key2, key3));
    }
}
