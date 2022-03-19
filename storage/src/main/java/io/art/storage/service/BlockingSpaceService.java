package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.index.*;
import io.art.storage.sharder.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
public interface BlockingSpaceService<KeyType, ModelType> {
    ModelType first(KeyType key);

    ImmutableArray<ModelType> select(KeyType key);

    ImmutableArray<ModelType> select(KeyType key, long offset, long limit);

    default ImmutableArray<ModelType> find(KeyType... keys) {
        return find(asList(keys));
    }

    ImmutableArray<ModelType> find(Collection<KeyType> keys);

    ImmutableArray<ModelType> find(ImmutableCollection<KeyType> keys);

    ModelType delete(KeyType key);

    default ImmutableArray<ModelType> delete(KeyType... keys) {
        return delete(asList(keys));
    }

    ImmutableArray<ModelType> delete(Collection<KeyType> keys);

    ImmutableArray<ModelType> delete(ImmutableCollection<KeyType> keys);

    long count(KeyType key);

    long size();

    void truncate();

    ModelType insert(ModelType value);

    default ImmutableArray<ModelType> insert(ModelType... value) {
        return insert(asList(value));
    }

    ImmutableArray<ModelType> insert(Collection<ModelType> value);

    ImmutableArray<ModelType> insert(ImmutableCollection<ModelType> value);

    ModelType put(ModelType value);

    default ImmutableArray<ModelType> put(ModelType... value) {
        return put(Arrays.asList(value));
    }

    ImmutableArray<ModelType> put(Collection<ModelType> value);

    ImmutableArray<ModelType> put(ImmutableCollection<ModelType> value);

    default ModelType update(KeyType key, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(key, spaceUpdater);
    }

    ModelType update(KeyType key, Updater<ModelType> updater);

    void upsert(ModelType model, Updater<ModelType> updater);

    default void upsert(ModelType model, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        upsert(model, spaceUpdater);
    }

    ImmutableArray<ModelType> update(Collection<KeyType> keys, Updater<ModelType> updater);

    ImmutableArray<ModelType> update(ImmutableCollection<KeyType> keys, Updater<ModelType> updater);

    default ImmutableArray<ModelType> update(Collection<KeyType> keys, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default ImmutableArray<ModelType> update(ImmutableCollection<KeyType> keys, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    BlockingSpaceStream<ModelType> stream();

    BlockingSpaceStream<ModelType> stream(KeyType baseKey);

    ReactiveSpaceService<KeyType, ModelType> reactive();

    default <F1> BlockingIndex1Service<ModelType, F1> index(Index1<ModelType, F1> index) {
        return new BlockingIndex1Service<>(index((Index) index));
    }

    default <F1, F2> BlockingIndex2Service<ModelType, F1, F2> index(Index2<ModelType, F1, F2> index) {
        return new BlockingIndex2Service<>(index((Index) index));
    }

    default <F1, F2, F3> BlockingIndex3Service<ModelType, F1, F2, F3> index(Index3<ModelType, F1, F2, F3> index) {
        return new BlockingIndex3Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4> BlockingIndex4Service<ModelType, F1, F2, F3, F4> index(Index4<ModelType, F1, F2, F3, F4> index) {
        return new BlockingIndex4Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4, F5> BlockingIndex5Service<ModelType, F1, F2, F3, F4, F5> index(Index5<ModelType, F1, F2, F3, F4, F5> index) {
        return new BlockingIndex5Service<>(index((Index) index));
    }

    BlockingIndexService<ModelType> index(Index index);

    default <P1> BlockingShardService<KeyType, ModelType> sharded(Sharder1<ModelType, P1> sharder, P1 input) {
        return sharded(sharder.shard(input));
    }

    default <P1, P2> BlockingShardService<KeyType, ModelType> sharded(Sharder2<ModelType, P1, P2> sharder, P1 input1, P2 input2) {
        return sharded(sharder.shard(input1, input2));
    }

    default <P1, P2, P3> BlockingShardService<KeyType, ModelType> sharded(Sharder3<ModelType, P1, P2, P3> sharder, P1 input1, P2 input2, P3 input3) {
        return sharded(sharder.shard(input1, input2, input3));
    }

    default <P1, P2, P3, P4> BlockingShardService<KeyType, ModelType> sharded(Sharder4<ModelType, P1, P2, P3, P4> sharder, P1 input1, P2 input2, P3 input3, P4 input4) {
        return sharded(sharder.shard(input1, input2, input3, input4));
    }

    default <P1, P2, P3, P4, P5> BlockingShardService<KeyType, ModelType> sharded(Sharder5<ModelType, P1, P2, P3, P4, P5> sharder, P1 input1, P2 input2, P3 input3, P4 input4, P5 input5) {
        return sharded(sharder.shard(input1, input2, input3, input4, input5));
    }

    default BlockingShardService<KeyType, ModelType> sharded(Sharder<ModelType> sharder, Tuple input) {
        return sharded(sharder.shard(input));
    }

    BlockingShardService<KeyType, ModelType> sharded(ShardRequest request);
}
