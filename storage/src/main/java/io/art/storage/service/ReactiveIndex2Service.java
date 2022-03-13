package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class ReactiveIndex2Service<ModelType, F1, F2> {
    private final ReactiveIndexService<ModelType> delegate;

    public Mono<ModelType> first(F1 key1, F2 key2) {
        return delegate.first(key1, key2);
    }

    public Flux<ModelType> select(F1 key1, F2 key2) {
        return delegate.select(key1, key2);
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

    public Mono<Long> count() {
        return delegate.count();
    }
}