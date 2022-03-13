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
public class ReactiveIndex1Service<ModelType, F1> {
    private final ReactiveIndexService<ModelType> delegate;

    public Mono<ModelType> first(F1 key1) {
        return delegate.first(key1);
    }

    public Flux<ModelType> select(F1 key1) {
        return delegate.select(key1);
    }

    @SafeVarargs
    public final Flux<ModelType> find(F1... keys1) {
        return delegate.find(stream(keys1).map(Tuple1::new).collect(listCollector()));
    }

    public Flux<ModelType> find(Collection<F1> keys1) {
        return delegate.find(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public Flux<ModelType> find(ImmutableCollection<F1> keys1) {
        return delegate.find(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    @SafeVarargs
    public final Flux<ModelType> delete(F1... keys1) {
        return delegate.delete(stream(keys1).map(Tuple1::new).collect(listCollector()));
    }

    public Flux<ModelType> delete(Collection<F1> keys1) {
        return delegate.delete(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public Flux<ModelType> delete(ImmutableCollection<F1> keys1) {
        return delegate.delete(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public Mono<Long> count() {
        return delegate.count();
    }
}
