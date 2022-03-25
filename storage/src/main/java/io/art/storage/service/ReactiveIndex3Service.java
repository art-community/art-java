package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import lombok.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import static io.art.core.model.Tuple.*;
import java.util.*;
import java.util.function.*;

@Public
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ReactiveIndex3Service<SpaceType, F1, F2, F3> {
    private final ReactiveIndexService<SpaceType> delegate;

    public Mono<SpaceType> first(F1 key1, F2 key2, F3 key3) {
        return delegate.first(tuple(key1, key2, key3));
    }

    public Mono<SpaceType> update(F1 key1, F2 key2, F3 key3, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(tuple(key1, key2, key3), updater);
    }

    public Flux<SpaceType> update(Collection<Tuple3<F1, F2, F3>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<SpaceType> update(ImmutableCollection<Tuple3<F1, F2, F3>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<SpaceType> select(F1 key1, F2 key2, F3 key3) {
        return delegate.select(tuple(key1, key2, key3));
    }

    public Flux<SpaceType> select(F1 key1, F2 key2, F3 key3, int offset, int limit) {
        return delegate.select(tuple(key1, key2, key3), offset, limit);
    }

    @SafeVarargs
    public final Flux<SpaceType> find(Tuple3<F1, F2, F3>... keys) {
        return delegate.find(keys);
    }

    public Flux<SpaceType> find(Collection<Tuple3<F1, F2, F3>> keys) {
        return delegate.find(keys);
    }

    public Flux<SpaceType> find(ImmutableCollection<Tuple3<F1, F2, F3>> keys) {
        return delegate.find(keys);
    }

    public final Mono<SpaceType> delete(F1 key1, F2 key2, F3 key3) {
        return delegate.delete(tuple(key1, key2, key3));
    }

    @SafeVarargs
    public final Flux<SpaceType> delete(Tuple3<F1, F2, F3>... keys) {
        return delegate.delete(keys);
    }

    public Flux<SpaceType> delete(Collection<Tuple3<F1, F2, F3>> keys) {
        return delegate.delete(keys);
    }

    public Flux<SpaceType> delete(ImmutableCollection<Tuple3<F1, F2, F3>> keys) {
        return delegate.delete(keys);
    }

    public Mono<Long> count(F1 key1, F2 key2, F3 key3) {
        return delegate.count(tuple(key1, key2, key3));
    }

    public ReactiveSpaceStream<SpaceType> stream() {
        return delegate.stream();
    }

    public ReactiveSpaceStream<SpaceType> stream(F1 key1, F2 key2, F3 key3) {
        return delegate.stream(tuple(key1, key2, key3));
    }
}
