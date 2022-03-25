package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import lombok.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.model.Tuple.*;
import java.util.*;
import java.util.function.*;

@Public
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ReactiveIndex1Service<SpaceType, F1> {
    private final ReactiveIndexService<SpaceType> delegate;

    public Mono<SpaceType> first(F1 key1) {
        return delegate.first(tuple(key1));
    }

    public Mono<SpaceType> update(F1 key1, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(tuple(key1), updater);
    }

    public Flux<SpaceType> update(Collection<F1> keys1, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys1.stream().map(Tuple1::new).collect(listCollector()), updater);
    }

    public Flux<SpaceType> update(ImmutableCollection<F1> keys1, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys1.stream().map(Tuple1::new).collect(listCollector()), updater);
    }

    public Flux<SpaceType> select(F1 key1) {
        return delegate.select(tuple(key1));
    }

    public Flux<SpaceType> select(F1 key1, int offset, int limit) {
        return delegate.select(tuple(key1), offset, limit);
    }

    @SafeVarargs
    public final Flux<SpaceType> find(F1... keys) {
        return delegate.find(Arrays.stream(keys).map(Tuple1::new).collect(listCollector()));
    }

    public Flux<SpaceType> find(Collection<F1> keys) {
        return delegate.find(keys.stream().map(Tuple1::new).collect(listCollector()));
    }

    public Flux<SpaceType> find(ImmutableCollection<F1> keys) {
        return delegate.find(keys.stream().map(Tuple1::new).collect(listCollector()));
    }

    public final Mono<SpaceType> delete(F1 key1) {
        return delegate.delete(tuple(key1));
    }

    @SafeVarargs
    public final Flux<SpaceType> delete(F1... keys) {
        return delegate.delete(Arrays.stream(keys).map(Tuple1::new).collect(listCollector()));
    }

    public Flux<SpaceType> delete(Collection<F1> keys) {
        return delegate.delete(keys.stream().map(Tuple1::new).collect(listCollector()));
    }

    public Flux<SpaceType> delete(ImmutableCollection<F1> keys) {
        return delegate.delete(keys.stream().map(Tuple1::new).collect(listCollector()));
    }

    public Mono<Long> count(F1 key1) {
        return delegate.count(tuple(key1));
    }

    public ReactiveSpaceStream<SpaceType> stream() {
        return delegate.stream();
    }

    public ReactiveSpaceStream<SpaceType> stream(F1 key1) {
        return delegate.stream(tuple(key1));
    }
}
