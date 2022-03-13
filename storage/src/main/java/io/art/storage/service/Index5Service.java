package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.updater.*;
import static io.art.core.model.Tuple.*;
import java.util.*;
import java.util.function.*;

@Public
public class Index5Service<ModelType, F1, F2, F3, F4, F5> {
    private final IndexService<ModelType> delegate;
    private final ReactiveIndex5Service<ModelType, F1, F2, F3, F4, F5> reactive;

    public Index5Service(IndexService<ModelType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex5Service<>(delegate.reactive());
    }

    public ModelType first(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.first(tuple(key1, key2, key3, key4, key5));
    }

    public ModelType update(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(tuple(key1, key2, key3, key4, key5), updater);
    }

    public ImmutableArray<ModelType> select(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.select(tuple(key1, key2, key3, key4, key5));
    }

    public ImmutableArray<ModelType> select(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5, int offset, int limit) {
        return delegate.select(tuple(key1, key2, key3, key4, key5), offset, limit);
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> find(Tuple5<F1, F2, F3, F4, F5>... keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<ModelType> find(Collection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<ModelType> find(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.find(keys);
    }

    public ModelType delete(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.delete(tuple(key1, key2, key3, key4, key5));
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> delete(Tuple5<F1, F2, F3, F4, F5>... keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<ModelType> delete(Collection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<ModelType> delete(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.delete(keys);
    }

    public long count(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.count(tuple(key1, key2, key3, key4, key5));
    }

    public ReactiveIndex5Service<ModelType, F1, F2, F3, F4, F5> reactive() {
        return reactive;
    }
}
