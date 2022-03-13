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
public class ReactiveIndex5Service<ModelType, F1, F2, F3, F4, F5> {
    private final ReactiveIndexService<ModelType> delegate;

    public Mono<ModelType> first(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.first(tuple(key1, key2, key3, key4, key5));
    }

    public Mono<ModelType> update(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(tuple(key1, key2, key3, key4, key5), updater);
    }

    public Flux<ModelType> update(Collection<Tuple5<F1, F2, F3, F4, F5>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<ModelType> update(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<ModelType> select(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.select(tuple(key1, key2, key3, key4, key5));
    }

    public Flux<ModelType> select(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5, int offset, int limit) {
        return delegate.select(tuple(key1, key2, key3, key4, key5), offset, limit);
    }

    @SafeVarargs
    public final Flux<ModelType> find(Tuple5<F1, F2, F3, F4, F5>... keys) {
        return delegate.find(keys);
    }

    public Flux<ModelType> find(Collection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.find(keys);
    }

    public Flux<ModelType> find(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.find(keys);
    }

    public final Mono<ModelType> delete(F1 key1, F2 key2, F3 key3, F4 key4, F4 key5) {
        return delegate.delete(tuple(key1, key2, key3, key4, key5));
    }

    @SafeVarargs
    public final Flux<ModelType> delete(Tuple5<F1, F2, F3, F4, F5>... keys) {
        return delegate.delete(keys);
    }

    public Flux<ModelType> delete(Collection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.delete(keys);
    }

    public Flux<ModelType> delete(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.delete(keys);
    }

    public Mono<Long> count(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.count(tuple(key1, key2, key3, key4, key5));
    }

    public ReactiveSpaceStream<ModelType> stream() {
        return delegate.stream();
    }

    public ReactiveSpaceStream<ModelType> stream(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.stream(tuple(key1, key2, key3, key4, key5));
    }
}
