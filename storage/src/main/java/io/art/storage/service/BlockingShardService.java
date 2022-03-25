package io.art.storage.service;

import io.art.core.collection.*;
import io.art.storage.index.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

public interface BlockingShardService<KeyType, SpaceType> {
    SpaceType first(KeyType key);

    ImmutableArray<SpaceType> select(KeyType key);

    ImmutableArray<SpaceType> select(KeyType key, long offset, long limit);

    default ImmutableArray<SpaceType> find(KeyType... keys) {
        return find(asList(keys));
    }

    ImmutableArray<SpaceType> find(Collection<KeyType> keys);

    ImmutableArray<SpaceType> find(ImmutableCollection<KeyType> keys);

    SpaceType delete(KeyType key);

    default ImmutableArray<SpaceType> delete(KeyType... keys) {
        return delete(asList(keys));
    }

    ImmutableArray<SpaceType> delete(Collection<KeyType> keys);

    ImmutableArray<SpaceType> delete(ImmutableCollection<KeyType> keys);

    long count(KeyType key);

    long size();

    void truncate();

    SpaceType insert(SpaceType value);

    default ImmutableArray<SpaceType> insert(SpaceType... value) {
        return insert(asList(value));
    }

    ImmutableArray<SpaceType> insert(Collection<SpaceType> value);

    ImmutableArray<SpaceType> insert(ImmutableCollection<SpaceType> value);

    SpaceType put(SpaceType value);

    default ImmutableArray<SpaceType> put(SpaceType... value) {
        return put(Arrays.asList(value));
    }

    ImmutableArray<SpaceType> put(Collection<SpaceType> value);

    ImmutableArray<SpaceType> put(ImmutableCollection<SpaceType> value);

    default SpaceType update(KeyType key, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(key, spaceUpdater);
    }

    SpaceType update(KeyType key, Updater<SpaceType> updater);

    void upsert(SpaceType model, Updater<SpaceType> updater);

    default void upsert(SpaceType model, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        upsert(model, spaceUpdater);
    }

    ImmutableArray<SpaceType> update(Collection<KeyType> keys, Updater<SpaceType> updater);

    ImmutableArray<SpaceType> update(ImmutableCollection<KeyType> keys, Updater<SpaceType> updater);

    default ImmutableArray<SpaceType> update(Collection<KeyType> keys, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default ImmutableArray<SpaceType> update(ImmutableCollection<KeyType> keys, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    BlockingSpaceStream<SpaceType> stream();

    BlockingSpaceStream<SpaceType> stream(KeyType baseKey);

    ReactiveShardService<KeyType, SpaceType> reactive();


    default <F1> BlockingIndex1Service<SpaceType, F1> index(Index1<SpaceType, F1> index) {
        return new BlockingIndex1Service<>(index((Index) index));
    }

    default <F1, F2> BlockingIndex2Service<SpaceType, F1, F2> index(Index2<SpaceType, F1, F2> index) {
        return new BlockingIndex2Service<>(index((Index) index));
    }

    default <F1, F2, F3> BlockingIndex3Service<SpaceType, F1, F2, F3> index(Index3<SpaceType, F1, F2, F3> index) {
        return new BlockingIndex3Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4> BlockingIndex4Service<SpaceType, F1, F2, F3, F4> index(Index4<SpaceType, F1, F2, F3, F4> index) {
        return new BlockingIndex4Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4, F5> BlockingIndex5Service<SpaceType, F1, F2, F3, F4, F5> index(Index5<SpaceType, F1, F2, F3, F4, F5> index) {
        return new BlockingIndex5Service<>(index((Index) index));
    }

    BlockingIndexService<SpaceType> index(Index index);
}
