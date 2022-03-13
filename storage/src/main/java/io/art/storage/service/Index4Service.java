package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import java.util.*;

@Public
public class Index4Service<ModelType, F1, F2, F3, F4> {
    private final IndexService<ModelType> delegate;
    private final ReactiveIndex4Service<ModelType, F1, F2, F3, F4> reactive;

    public Index4Service(IndexService<ModelType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex4Service<>(delegate.reactive());
    }

    public ModelType first(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.first(key1, key2, key3, key4);
    }

    public ImmutableArray<ModelType> select(F1 key1, F2 key2, F3 key3, F4 key4) {
        return delegate.select(key1, key2, key3, key4);
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
        return delegate.count(key1, key2, key3, key4);
    }

    public ReactiveIndex4Service<ModelType, F1, F2, F3, F4> reactive() {
        return reactive;
    }
}
