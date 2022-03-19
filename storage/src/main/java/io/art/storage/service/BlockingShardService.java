package io.art.storage.service;

import io.art.core.collection.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

public interface BlockingShardService<KeyType, ModelType> {
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

    ReactiveShardService<KeyType, ModelType> reactive();
}
