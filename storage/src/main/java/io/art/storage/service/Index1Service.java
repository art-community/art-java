package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import static io.art.core.collector.ArrayCollector.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
public class Index1Service<ModelType, F1> {
    private final IndexService<ModelType> delegate;
    private final ReactiveIndex1Service<ModelType, F1> reactive;

    public Index1Service(IndexService<ModelType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex1Service<>(delegate.reactive());
    }

    public ModelType first(F1 key1) {
        return delegate.first(key1);
    }

    public ImmutableArray<ModelType> select(F1 key1) {
        return delegate.select(key1);
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> find(F1... keys1) {
        return delegate.find(stream(keys1).map(Tuple1::new).collect(listCollector()));
    }

    public ImmutableArray<ModelType> find(Collection<F1> keys1) {
        return delegate.find(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public ImmutableArray<ModelType> find(ImmutableCollection<F1> keys1) {
        return delegate.find(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> delete(F1... keys1) {
        return delegate.delete(stream(keys1).map(Tuple1::new).collect(listCollector()));
    }

    public ImmutableArray<ModelType> delete(Collection<F1> keys1) {
        return delegate.delete(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public ImmutableArray<ModelType> delete(ImmutableCollection<F1> keys1) {
        return delegate.delete(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public long count(F1 key1) {
        return delegate.count(key1);
    }

    public ReactiveIndex1Service<ModelType, F1> reactive() {
        return reactive;
    }
}
