package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.model.Tuple.*;
import java.util.*;
import java.util.function.*;

@Public
@Getter
@Accessors(fluent = true)
public class Index3Service<ModelType, F1, F2, F3> {
    private final IndexService<ModelType> delegate;
    private final ReactiveIndex3Service<ModelType, F1, F2, F3> reactive;

    public Index3Service(IndexService<ModelType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex3Service<>(delegate.reactive());
    }

    public ModelType first(F1 key1, F2 key2, F3 key3) {
        return delegate.first(tuple(key1, key2, key3));
    }

    public ModelType update(F1 key1, F2 key2, F3 key3, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(tuple(key1, key2, key3), updater);
    }

    public ImmutableArray<ModelType> update(Collection<Tuple3<F1, F2, F3>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<ModelType> update(ImmutableCollection<Tuple3<F1, F2, F3>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<ModelType> select(F1 key1, F2 key2, F3 key3) {
        return delegate.select(tuple(key1, key2, key3));
    }

    public ImmutableArray<ModelType> select(F1 key1, F2 key2, F3 key3, int offset, int limit) {
        return delegate.select(tuple(key1, key2, key3), offset, limit);
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> find(Tuple3<F1, F2, F3>... keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<ModelType> find(Collection<Tuple3<F1, F2, F3>> keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<ModelType> find(ImmutableCollection<Tuple3<F1, F2, F3>> keys) {
        return delegate.find(keys);
    }

    public ModelType delete(F1 key1, F2 key2, F3 key3) {
        return delegate.delete(tuple(key1, key2, key3));
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> delete(Tuple3<F1, F2, F3>... keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<ModelType> delete(Collection<Tuple3<F1, F2, F3>> keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<ModelType> delete(ImmutableCollection<Tuple3<F1, F2, F3>> keys) {
        return delegate.delete(keys);
    }

    public long count(F1 key1, F2 key2, F3 key3) {
        return delegate.count(tuple(key1, key2, key3));
    }

    public BlockingSpaceStream<ModelType> stream() {
        return delegate.stream();
    }

    public BlockingSpaceStream<ModelType> stream(F1 key1, F2 key2, F3 key3) {
        return delegate.stream(tuple(key1, key2, key3));
    }
}
