package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import reactor.core.publisher.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
public interface ReactiveShardService<KeyType, ModelType> {
    Mono<ModelType> first(KeyType key);

    Flux<ModelType> select(KeyType key);

    Flux<ModelType> select(KeyType key, long offset, long limit);

    default Flux<ModelType> find(KeyType... keys) {
        return find(asList(keys));
    }

    Flux<ModelType> find(Collection<KeyType> keys);

    Flux<ModelType> find(ImmutableCollection<KeyType> keys);

    Mono<ModelType> delete(KeyType key);

    default Flux<ModelType> delete(KeyType... keys) {
        return delete(asList(keys));
    }

    Flux<ModelType> delete(Collection<KeyType> keys);

    Flux<ModelType> delete(ImmutableCollection<KeyType> keys);

    Mono<ModelType> insert(ModelType value);

    default Flux<ModelType> insert(ModelType... value) {
        return insert(Arrays.asList(value));
    }

    Flux<ModelType> insert(Collection<ModelType> value);

    Flux<ModelType> insert(ImmutableCollection<ModelType> value);

    Mono<ModelType> put(ModelType value);

    default Flux<ModelType> put(ModelType... value) {
        return insert(Arrays.asList(value));
    }

    Flux<ModelType> put(Collection<ModelType> value);

    Flux<ModelType> put(ImmutableCollection<ModelType> value);

    default Mono<ModelType> update(KeyType key, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(key, spaceUpdater);
    }

    Mono<ModelType> update(KeyType key, Updater<ModelType> updater);

    Mono<Void> upsert(ModelType model, Updater<ModelType> updater);

    default Mono<Void> upsert(ModelType model, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return upsert(model, spaceUpdater);
    }

    Flux<ModelType> update(Collection<KeyType> keys, Updater<ModelType> updater);

    Flux<ModelType> update(ImmutableCollection<KeyType> keys, Updater<ModelType> updater);

    default Flux<ModelType> update(Collection<KeyType> keys, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default Flux<ModelType> update(ImmutableCollection<KeyType> keys, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    Mono<Long> count(KeyType key);

    Mono<Long> size();

    Mono<Void> truncate();

    ReactiveSpaceStream<ModelType> stream();

    ReactiveSpaceStream<ModelType> stream(KeyType baseKey);
}
