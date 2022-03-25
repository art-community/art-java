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
public class BlockingIndex5Service<SpaceType, F1, F2, F3, F4, F5> {
    private final BlockingIndexService<SpaceType> delegate;
    private final ReactiveIndex5Service<SpaceType, F1, F2, F3, F4, F5> reactive;

    public BlockingIndex5Service(BlockingIndexService<SpaceType> delegate) {
        this.delegate = delegate;
        reactive = new ReactiveIndex5Service<>(delegate.reactive());
    }

    public SpaceType first(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.first(tuple(key1, key2, key3, key4, key5));
    }

    public SpaceType update(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(tuple(key1, key2, key3, key4, key5), updater);
    }

    public ImmutableArray<SpaceType> update(Collection<Tuple5<F1, F2, F3, F4, F5>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<SpaceType> update(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys, UnaryOperator<Updater<SpaceType>> updater) {
        return delegate.update(keys, updater);
    }

    public ImmutableArray<SpaceType> select(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.select(tuple(key1, key2, key3, key4, key5));
    }

    public ImmutableArray<SpaceType> select(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5, int offset, int limit) {
        return delegate.select(tuple(key1, key2, key3, key4, key5), offset, limit);
    }

    @SafeVarargs
    public final ImmutableArray<SpaceType> find(Tuple5<F1, F2, F3, F4, F5>... keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<SpaceType> find(Collection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.find(keys);
    }

    public ImmutableArray<SpaceType> find(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.find(keys);
    }

    public SpaceType delete(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.delete(tuple(key1, key2, key3, key4, key5));
    }

    @SafeVarargs
    public final ImmutableArray<SpaceType> delete(Tuple5<F1, F2, F3, F4, F5>... keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<SpaceType> delete(Collection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.delete(keys);
    }

    public ImmutableArray<SpaceType> delete(ImmutableCollection<Tuple5<F1, F2, F3, F4, F5>> keys) {
        return delegate.delete(keys);
    }

    public long count(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.count(tuple(key1, key2, key3, key4, key5));
    }

    public BlockingSpaceStream<SpaceType> stream() {
        return delegate.stream();
    }

    public BlockingSpaceStream<SpaceType> stream(F1 key1, F2 key2, F3 key3, F4 key4, F5 key5) {
        return delegate.stream(tuple(key1, key2, key3, key4, key5));
    }
}
