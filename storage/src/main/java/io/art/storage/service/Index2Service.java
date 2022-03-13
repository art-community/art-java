package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import static io.art.core.model.Tuple.*;
import java.util.*;
import java.util.function.*;

@Public
public class Index2Service<ModelType, F1, F2> {
    private final IndexService<ModelType> delegate;
    private final ReactiveIndex2Service<ModelType, F1, F2> reactive;

    public Index2Service(IndexService<ModelType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex2Service<>(delegate.reactive());
    }

    public ModelType first(F1 key1, F2 key2) {
        return delegate.first(tuple(key1, key2));
    }

    public ModelType update(F1 key1, F2 key2, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(tuple(key1, key2), updater);
    }

    public ImmutableArray<ModelType> update(Collection<Tuple2<F1, F2>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<ModelType> update(ImmutableCollection<Tuple2<F1, F2>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<ModelType> select(F1 key1, F2 key2) {
        return delegate.select(tuple(key1, key2));
    }

    public ImmutableArray<ModelType> select(F1 key1, F2 key2, int offset, int limit) {
        return delegate.select(tuple(key1, key2), offset, limit);
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> find(Tuple2<F1, F2>... keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<ModelType> find(Collection<Tuple2<F1, F2>> keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<ModelType> find(ImmutableCollection<Tuple2<F1, F2>> keys) {
        return delegate.find(keys);
    }

    public final ModelType delete(F1 key1, F2 key2) {
        return delegate.delete(tuple(key1, key2));
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> delete(Tuple2<F1, F2>... keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<ModelType> delete(Collection<Tuple2<F1, F2>> keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<ModelType> delete(ImmutableCollection<Tuple2<F1, F2>> keys) {
        return delegate.delete(keys);
    }

    public long count(F1 key1, F2 key2) {
        return delegate.count(tuple(key1, key2));
    }

    public ReactiveIndex2Service<ModelType, F1, F2> reactive() {
        return reactive;
    }

    public SpaceStream<ModelType> stream() {
        return delegate.stream();
    }

    public SpaceStream<ModelType> stream(F1 key1, F2 key2) {
        return delegate.stream(tuple(key1, key2));
    }
}
