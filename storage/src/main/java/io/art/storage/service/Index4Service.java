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
public class Index4Service<ModelType, F1, F2, F3, F4> {
    private final IndexService<ModelType> delegate;
    private final ReactiveIndex4Service<ModelType, F1, F2, F3, F4> reactive;

    public Index4Service(IndexService<ModelType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex4Service<>(delegate.reactive());
    }

    public ModelType first(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.first(tuple(key1, key2, key3, key4));
    }

    public ModelType update(F1 key1, F2 key2, F3 key3, F4 key4, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(tuple(key1, key2, key3, key4), updater);
    }

    public ImmutableArray<ModelType> update(Collection<Tuple4<F1, F2, F3, F4>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<ModelType> update(ImmutableCollection<Tuple4<F1, F2, F3, F4>> keys, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<ModelType> select(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.select(tuple(key1, key2, key3, key4));
    }

    public ImmutableArray<ModelType> select(F1 key1, F2 key2, F3 key3, F4 key4, int offset, int limit) {
        return delegate.select(tuple(key1, key2, key3, key4), offset, limit);
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> find(Tuple4<F1, F2, F3, F4>... keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<ModelType> find(Collection<Tuple4<F1, F2, F3, F4>> keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<ModelType> find(ImmutableCollection<Tuple4<F1, F2, F3, F4>> keys) {
        return delegate.find(keys);
    }

    public ModelType delete(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.delete(tuple(key1, key2, key3, key4));
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> delete(Tuple4<F1, F2, F3, F4>... keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<ModelType> delete(Collection<Tuple4<F1, F2, F3, F4>> keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<ModelType> delete(ImmutableCollection<Tuple4<F1, F2, F3, F4>> keys) {
        return delegate.delete(keys);
    }

    public long count(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.count(tuple(key1, key2, key3, key4));
    }

    public SpaceStream<ModelType> stream() {
        return delegate.stream();
    }

    public SpaceStream<ModelType> stream(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.stream(tuple(key1, key2, key3, key4));
    }
}
