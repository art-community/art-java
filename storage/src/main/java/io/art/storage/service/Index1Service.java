package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;

@Public
public class Index1Service<ModelType, F1> {
    private final IndexService<ModelType> delegate;
    private final ReactiveIndex1Service<ModelType, F1> reactive;

    public Index1Service(IndexService<ModelType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex1Service<>(delegate.reactive());
    }

    public ModelType findFirst(F1 key1) {
        return delegate.findFirst(key1);
    }

    public ImmutableArray<ModelType> findAll(F1 key1) {
        return delegate.findAll(key1);
    }

    public ImmutableArray<ModelType> delete(F1 key1) {
        return delegate.delete(key1);
    }

    public long count() {
        return delegate.count();
    }

    public ReactiveIndex1Service<ModelType, F1> reactive() {
        return reactive;
    }
}
