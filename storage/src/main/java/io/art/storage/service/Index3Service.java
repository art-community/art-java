package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import static io.art.core.model.Tuple.*;
import java.util.*;

@Public
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

    public ImmutableArray<ModelType> select(F1 key1, F2 key2, F3 key3) {
        return delegate.select(tuple(key1, key2, key3));
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

    public ReactiveIndex3Service<ModelType, F1, F2, F3> reactive() {
        return reactive;
    }
}
