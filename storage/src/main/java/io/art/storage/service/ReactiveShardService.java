package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.storage.index.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.UNCHECKED;
import static io.art.core.constants.CompilerSuppressingWarnings.VARARGS;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface ReactiveShardService<KeyType, SpaceType> {
    Mono<SpaceType> first(KeyType key);

    Flux<SpaceType> select(KeyType key);

    Flux<SpaceType> select(KeyType key, long offset, long limit);

    default Flux<SpaceType> find(KeyType... keys) {
        return find(asList(keys));
    }

    Flux<SpaceType> find(Collection<KeyType> keys);

    Flux<SpaceType> find(ImmutableCollection<KeyType> keys);

    Mono<SpaceType> delete(KeyType key);

    default Flux<SpaceType> delete(KeyType... keys) {
        return delete(asList(keys));
    }

    Flux<SpaceType> delete(Collection<KeyType> keys);

    Flux<SpaceType> delete(ImmutableCollection<KeyType> keys);

    Mono<SpaceType> insert(SpaceType value);

    default Flux<SpaceType> insert(SpaceType... value) {
        return insert(Arrays.asList(value));
    }

    Flux<SpaceType> insert(Collection<SpaceType> value);

    Flux<SpaceType> insert(ImmutableCollection<SpaceType> value);

    Mono<SpaceType> put(SpaceType value);

    default Flux<SpaceType> put(SpaceType... value) {
        return insert(Arrays.asList(value));
    }

    Flux<SpaceType> put(Collection<SpaceType> value);

    Flux<SpaceType> put(ImmutableCollection<SpaceType> value);

    default Mono<SpaceType> update(KeyType key, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(key, spaceUpdater);
    }

    Mono<SpaceType> update(KeyType key, Updater<SpaceType> updater);

    Mono<Void> upsert(SpaceType model, Updater<SpaceType> updater);

    default Mono<Void> upsert(SpaceType model, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return upsert(model, spaceUpdater);
    }

    Flux<SpaceType> update(Collection<KeyType> keys, Updater<SpaceType> updater);

    Flux<SpaceType> update(ImmutableCollection<KeyType> keys, Updater<SpaceType> updater);

    default Flux<SpaceType> update(Collection<KeyType> keys, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default Flux<SpaceType> update(ImmutableCollection<KeyType> keys, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    Mono<Long> count(KeyType key);

    Mono<Long> size();

    Mono<Void> truncate();

    ReactiveSpaceStream<SpaceType> stream();

    ReactiveSpaceStream<SpaceType> stream(KeyType baseKey);

    default <F1> ReactiveIndex1Service<SpaceType, F1> index(Index1<SpaceType, F1> index) {
        return new ReactiveIndex1Service<>(index((Index) index));
    }

    default <F1, F2> ReactiveIndex2Service<SpaceType, F1, F2> index(Index2<SpaceType, F1, F2> index) {
        return new ReactiveIndex2Service<>(index((Index) index));
    }

    default <F1, F2, F3> ReactiveIndex3Service<SpaceType, F1, F2, F3> index(Index3<SpaceType, F1, F2, F3> index) {
        return new ReactiveIndex3Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4> ReactiveIndex4Service<SpaceType, F1, F2, F3, F4> index(Index4<SpaceType, F1, F2, F3, F4> index) {
        return new ReactiveIndex4Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4, F5> ReactiveIndex5Service<SpaceType, F1, F2, F3, F4, F5> index(Index5<SpaceType, F1, F2, F3, F4, F5> index) {
        return new ReactiveIndex5Service<>(index((Index) index));
    }

    ReactiveIndexService<SpaceType> index(Index index);
}
