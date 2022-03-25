package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import lombok.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.listCollector;
import static io.art.core.model.Tuple.*;
import java.util.*;
import java.util.function.*;

@Public
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ReactiveIndex2Service<SpaceType, F1, F2> {
    private final ReactiveIndexService<SpaceType> delegate;

    public Mono<SpaceType> first(F1 key1, F2 key2) {
        return delegate.first(tuple(key1, key2));
    }

    public Mono<SpaceType> update(F1 key1,F2 key2, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(tuple(key1, key2), updater);
    }

    public Flux<SpaceType> update(Collection<Tuple2<F1, F2>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<SpaceType> update(ImmutableCollection<Tuple2<F1, F2>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<SpaceType> select(F1 key1, F2 key2) {
        return delegate.select(tuple(key1, key2));
    }

    public Flux<SpaceType> select(F1 key1, F2 key2, int offset, int limit) {
        return delegate.select(tuple(key1, key2), offset, limit);
    }

    @SafeVarargs
    public final Flux<SpaceType> find(Tuple2<F1, F2>... keys) {
        return delegate.find(keys);
    }

    public Flux<SpaceType> find(Collection<Tuple2<F1, F2>> keys) {
        return delegate.find(keys);
    }

    public Flux<SpaceType> find(ImmutableCollection<Tuple2<F1, F2>> keys) {
        return delegate.find(keys);
    }

    public final Mono<SpaceType> delete(F1 key1, F2 key2) {
        return delegate.delete(tuple(key1, key2));
    }

    @SafeVarargs
    public final Flux<SpaceType> delete(Tuple2<F1, F2>... keys) {
        return delegate.delete(keys);
    }

    public Flux<SpaceType> delete(Collection<Tuple2<F1, F2>> keys) {
        return delegate.delete(keys);
    }

    public Flux<SpaceType> delete(ImmutableCollection<Tuple2<F1, F2>> keys) {
        return delegate.delete(keys);
    }

    public Mono<Long> count(F1 key1, F2 key2) {
        return delegate.count(tuple(key1, key2));
    }

    public ReactiveSpaceStream<SpaceType> stream() {
        return delegate.stream();
    }

    public ReactiveSpaceStream<SpaceType> stream(F1 key1, F2 key2) {
        return delegate.stream(tuple(key1, key2));
    }
}
