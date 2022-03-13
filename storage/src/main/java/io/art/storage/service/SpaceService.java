package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.storage.index.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface SpaceService<KeyType, ModelType> {
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

    SpaceStream<ModelType> stream();

    ReactiveSpaceService<KeyType, ModelType> reactive();

    default <F1> Index1Service<ModelType, F1> index(Index1<ModelType, F1> index) {
        return new Index1Service<>(index((Index) index));
    }

    default <F1, F2> Index2Service<ModelType, F1, F2> index(Index2<ModelType, F1, F2> index) {
        return new Index2Service<>(index((Index) index));
    }

    default <F1, F2, F3> Index3Service<ModelType, F1, F2, F3> index(Index3<ModelType, F1, F2, F3> index) {
        return new Index3Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4> Index4Service<ModelType, F1, F2, F3, F4> index(Index4<ModelType, F1, F2, F3, F4> index) {
        return new Index4Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4, F5> Index5Service<ModelType, F1, F2, F3, F4, F5> index(Index5<ModelType, F1, F2, F3, F4, F5> index) {
        return new Index5Service<>(index((Index) index));
    }

    IndexService<ModelType> index(Index index);
}
