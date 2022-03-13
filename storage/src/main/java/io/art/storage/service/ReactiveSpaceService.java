package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.storage.index.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
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

    Mono<Long> count(KeyType key);

    Mono<Long> size();

    Mono<Void> truncate();

    ReactiveSpaceStream<ModelType> stream();

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
}
