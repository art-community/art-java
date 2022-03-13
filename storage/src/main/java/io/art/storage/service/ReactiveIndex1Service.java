package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.updater.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.model.Tuple.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class ReactiveIndex1Service<ModelType, F1> {
    private final ReactiveIndexService<ModelType> delegate;

    public Mono<ModelType> first(F1 key1) {
        return delegate.first(tuple(key1));
    }

    public Mono<ModelType> update(F1 key1, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(tuple(key1), updater);
    }

    public Flux<ModelType> update(Collection<F1> keys1, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys1.stream().map(Tuple1::new).collect(listCollector()), updater);
    }

    public Flux<ModelType> update(ImmutableCollection<F1> keys1, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys1.stream().map(Tuple1::new).collect(listCollector()), updater);
    }

    public Flux<ModelType> select(F1 key1) {
        return delegate.select(tuple(key1));
    }

    public Flux<ModelType> select(F1 key1, int offset, int limit) {
        return delegate.select(tuple(key1), offset, limit);
    }

    @SafeVarargs
    public final Flux<ModelType> find(F1... keys) {
        return delegate.find(stream(keys).map(Tuple1::new).collect(listCollector()));
    }

    public Flux<ModelType> find(Collection<F1> keys) {
        return delegate.find(keys.stream().map(Tuple1::new).collect(listCollector()));
    }

    public Flux<ModelType> find(ImmutableCollection<F1> keys) {
        return delegate.find(keys.stream().map(Tuple1::new).collect(listCollector()));
    }

    public final Mono<ModelType> delete(F1 key1) {
        return delegate.delete(tuple(key1));
    }

    @SafeVarargs
    public final Flux<ModelType> delete(F1... keys) {
        return delegate.delete(stream(keys).map(Tuple1::new).collect(listCollector()));
    }

    public Flux<ModelType> delete(Collection<F1> keys) {
        return delegate.delete(keys.stream().map(Tuple1::new).collect(listCollector()));
    }

    public Flux<ModelType> delete(ImmutableCollection<F1> keys) {
        return delegate.delete(keys.stream().map(Tuple1::new).collect(listCollector()));
    }

    public Mono<Long> count(F1 key1) {
        return delegate.count(tuple(key1));
    }
}
