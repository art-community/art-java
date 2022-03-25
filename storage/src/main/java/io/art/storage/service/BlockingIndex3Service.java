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
public class BlockingIndex3Service<SpaceType, F1, F2, F3> {
    private final BlockingIndexService<SpaceType> delegate;
    private final ReactiveIndex3Service<SpaceType, F1, F2, F3> reactive;

    public BlockingIndex3Service(BlockingIndexService<SpaceType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex3Service<>(delegate.reactive());
    }

    public SpaceType first(F1 key1, F2 key2, F3 key3) {
        return delegate.first(tuple(key1, key2, key3));
    }

    public SpaceType update(F1 key1, F2 key2, F3 key3, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(tuple(key1, key2, key3), updater);
    }

    public ImmutableArray<SpaceType> update(Collection<Tuple3<F1, F2, F3>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<SpaceType> update(ImmutableCollection<Tuple3<F1, F2, F3>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<SpaceType> select(F1 key1, F2 key2, F3 key3) {
        return delegate.select(tuple(key1, key2, key3));
    }

    public ImmutableArray<SpaceType> select(F1 key1, F2 key2, F3 key3, int offset, int limit) {
        return delegate.select(tuple(key1, key2, key3), offset, limit);
    }

    @SafeVarargs
    public final ImmutableArray<SpaceType> find(Tuple3<F1, F2, F3>... keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<SpaceType> find(Collection<Tuple3<F1, F2, F3>> keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<SpaceType> find(ImmutableCollection<Tuple3<F1, F2, F3>> keys) {
        return delegate.find(keys);
    }

    public SpaceType delete(F1 key1, F2 key2, F3 key3) {
        return delegate.delete(tuple(key1, key2, key3));
    }

    @SafeVarargs
    public final ImmutableArray<SpaceType> delete(Tuple3<F1, F2, F3>... keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<SpaceType> delete(Collection<Tuple3<F1, F2, F3>> keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<SpaceType> delete(ImmutableCollection<Tuple3<F1, F2, F3>> keys) {
        return delegate.delete(keys);
    }

    public long count(F1 key1, F2 key2, F3 key3) {
        return delegate.count(tuple(key1, key2, key3));
    }

    public BlockingSpaceStream<SpaceType> stream() {
        return delegate.stream();
    }

    public BlockingSpaceStream<SpaceType> stream(F1 key1, F2 key2, F3 key3) {
        return delegate.stream(tuple(key1, key2, key3));
    }
}
