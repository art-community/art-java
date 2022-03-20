package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.model.Tuple.*;
import java.util.*;
import java.util.function.*;

@Public
@Getter
@Accessors(fluent = true)
public class BlockingIndex1Service<ModelType, F1> {
    private final BlockingIndexService<ModelType> delegate;
    private final ReactiveIndex1Service<ModelType, F1> reactive;

    public BlockingIndex1Service(BlockingIndexService<ModelType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex1Service<>(delegate.reactive());
    }

    public ModelType first(F1 key1) {
        return delegate.first(tuple(key1));
    }

    public ModelType update(F1 key1, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(tuple(key1), updater);
    }

    public ImmutableArray<ModelType> update(Collection<F1> keys1, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys1.stream().map(Tuple1::new).collect(listCollector()), updater);
    }

    public ImmutableArray<ModelType> update(ImmutableCollection<F1> keys1, UnaryOperator<Updater<ModelType>> updater) {
        return delegate.update(keys1.stream().map(Tuple1::new).collect(listCollector()), updater);
    }

    public ImmutableArray<ModelType> select(F1 key1) {
        return delegate.select(tuple(key1));
    }

    public ImmutableArray<ModelType> select(F1 key1, int offset, int limit) {
        return delegate.select(tuple(key1), offset, limit);
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> find(F1... keys1) {
        return delegate.find(Arrays.stream(keys1).map(Tuple1::new).collect(listCollector()));
    }

    public ImmutableArray<ModelType> find(Collection<F1> keys1) {
        return delegate.find(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public ImmutableArray<ModelType> find(ImmutableCollection<F1> keys1) {
        return delegate.find(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public final ModelType delete(F1 key1) {
        return delegate.delete(tuple(key1));
    }

    @SafeVarargs
    public final ImmutableArray<ModelType> delete(F1... keys1) {
        return delegate.delete(Arrays.stream(keys1).map(Tuple1::new).collect(listCollector()));
    }

    public ImmutableArray<ModelType> delete(Collection<F1> keys1) {
        return delegate.delete(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public ImmutableArray<ModelType> delete(ImmutableCollection<F1> keys1) {
        return delegate.delete(keys1.stream().map(Tuple1::new).collect(listCollector()));
    }

    public long count(F1 key1) {
        return delegate.count(tuple(key1));
    }

    public BlockingSpaceStream<ModelType> stream() {
        return delegate.stream();
    }

    public BlockingSpaceStream<ModelType> stream(F1 key1) {
        return delegate.stream(tuple(key1));
    }
}