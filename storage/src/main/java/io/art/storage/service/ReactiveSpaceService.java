package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.index.*;
import io.art.storage.sharder.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import reactor.core.publisher.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
public interface ReactiveSpaceService<KeyType, ModelType> {
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


    default <F1> ReactiveIndex1Service<ModelType, F1> index(Index1<ModelType, F1> index) {
        return new ReactiveIndex1Service<>(index((Index) index));
    }

    default <F1, F2> ReactiveIndex2Service<ModelType, F1, F2> index(Index2<ModelType, F1, F2> index) {
        return new ReactiveIndex2Service<>(index((Index) index));
    }

    default <F1, F2, F3> ReactiveIndex3Service<ModelType, F1, F2, F3> index(Index3<ModelType, F1, F2, F3> index) {
        return new ReactiveIndex3Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4> ReactiveIndex4Service<ModelType, F1, F2, F3, F4> index(Index4<ModelType, F1, F2, F3, F4> index) {
        return new ReactiveIndex4Service<>(index((Index) index));
    }

    default <F1, F2, F3, F4, F5> ReactiveIndex5Service<ModelType, F1, F2, F3, F4, F5> index(Index5<ModelType, F1, F2, F3, F4, F5> index) {
        return new ReactiveIndex5Service<>(index((Index) index));
    }

    ReactiveIndexService<ModelType> index(Index index);


    default <P1> ReactiveShardService<KeyType, ModelType> sharded(Sharder1<ModelType, P1> sharder, P1 input) {
        return sharded(sharder.shard(input));
    }

    default <P1, P2> ReactiveShardService<KeyType, ModelType> sharded(Sharder2<ModelType, P1, P2> sharder, P1 input1, P2 input2) {
        return sharded(sharder.shard(input1, input2));
    }

    default <P1, P2, P3> ReactiveShardService<KeyType, ModelType> sharded(Sharder3<ModelType, P1, P2, P3> sharder, P1 input1, P2 input2, P3 input3) {
        return sharded(sharder.shard(input1, input2, input3));
    }

    default <P1, P2, P3, P4> ReactiveShardService<KeyType, ModelType> sharded(Sharder4<ModelType, P1, P2, P3, P4> sharder, P1 input1, P2 input2, P3 input3, P4 input4) {
        return sharded(sharder.shard(input1, input2, input3, input4));
    }

    default <P1, P2, P3, P4, P5> ReactiveShardService<KeyType, ModelType> sharded(Sharder5<ModelType, P1, P2, P3, P4, P5> sharder, P1 input1, P2 input2, P3 input3, P4 input4, P5 input5) {
        return sharded(sharder.shard(input1, input2, input3, input4, input5));
    }

    default ReactiveShardService<KeyType, ModelType> sharded(Sharder<ModelType> sharder, Tuple input) {
        return sharded(sharder.shard(input));
    }

    ReactiveShardService<KeyType, ModelType> sharded(ShardRequest request);
}
