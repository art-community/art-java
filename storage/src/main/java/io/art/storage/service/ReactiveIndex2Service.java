package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.updater.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.listCollector;
import static io.art.core.model.Tuple.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class ReactiveIndex2Service<ModelType, F1, F2> {
    private final ReactiveIndexService<ModelType> delegate;

    public Mono<ModelType> first(F1 key1, F2 key2) {
        return delegate.first(tuple(key1, key2));
    }

    public Mono<ModelType> update(F1 key1,F2 key2, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(tuple(key1, key2), updater);
    }

    public Flux<ModelType> update(Collection<Tuple2<F1, F2>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<ModelType> update(ImmutableCollection<Tuple2<F1, F2>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public Flux<ModelType> select(F1 key1, F2 key2) {
        return delegate.select(tuple(key1, key2));
    }

    public Flux<ModelType> select(F1 key1, F2 key2, int offset, int limit) {
        return delegate.select(tuple(key1, key2), offset, limit);
    }

    @SafeVarargs
    public final Flux<ModelType> find(Tuple2<F1, F2>... keys) {
        return delegate.find(keys);
    }

    public Flux<ModelType> find(Collection<Tuple2<F1, F2>> keys) {
        return delegate.find(keys);
    }

    public Flux<ModelType> find(ImmutableCollection<Tuple2<F1, F2>> keys) {
        return delegate.find(keys);
    }

    public final Mono<ModelType> delete(F1 key1, F2 key2) {
        return delegate.delete(tuple(key1, key2));
    }

    @SafeVarargs
    public final Flux<ModelType> delete(Tuple2<F1, F2>... keys) {
        return delegate.delete(keys);
    }

    public Flux<ModelType> delete(Collection<Tuple2<F1, F2>> keys) {
        return delegate.delete(keys);
    }

    public Flux<ModelType> delete(ImmutableCollection<Tuple2<F1, F2>> keys) {
        return delegate.delete(keys);
    }

    public Mono<Long> count(F1 key1, F2 key2) {
        return delegate.count(tuple(key1, key2));
    }
}
