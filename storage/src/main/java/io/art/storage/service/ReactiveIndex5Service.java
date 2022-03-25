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
public class ReactiveIndex5Service<SpaceType, F1, F2, F3, F4, F5> {
    private final ReactiveIndexService<SpaceType> delegate;

    public Mono<SpaceType> first(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.first(tuple(key1, key2, key3, key4, key5));
    }

    public Mono<SpaceType> update(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(tuple(key1, key2, key3, key4, key5), updater);
    }

    public Flux<SpaceType> update(Collection<Tuple5<F1, F2, F3, F4, F5>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<SpaceType> update(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<SpaceType> select(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.select(tuple(key1, key2, key3, key4, key5));
    }

    public Flux<SpaceType> select(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5, int offset, int limit) {
        return delegate.select(tuple(key1, key2, key3, key4, key5), offset, limit);
    }

    @SafeVarargs
    public final Flux<SpaceType> find(Tuple5<F1, F2, F3, F4, F5>... keys) {
        return delegate.find(keys);
    }

    public Flux<SpaceType> find(Collection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.find(keys);
    }

    public Flux<SpaceType> find(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.find(keys);
    }

    public final Mono<SpaceType> delete(F1 key1, F2 key2, F3 key3, F4 key4, F4 key5) {
        return delegate.delete(tuple(key1, key2, key3, key4, key5));
    }

    @SafeVarargs
    public final Flux<SpaceType> delete(Tuple5<F1, F2, F3, F4, F5>... keys) {
        return delegate.delete(keys);
    }

    public Flux<SpaceType> delete(Collection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.delete(keys);
    }

    public Flux<SpaceType> delete(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.delete(keys);
    }

    public Mono<Long> count(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.count(tuple(key1, key2, key3, key4, key5));
    }

    public ReactiveSpaceStream<SpaceType> stream() {
        return delegate.stream();
    }

    public ReactiveSpaceStream<SpaceType> stream(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.stream(tuple(key1, key2, key3, key4, key5));
    }
}
