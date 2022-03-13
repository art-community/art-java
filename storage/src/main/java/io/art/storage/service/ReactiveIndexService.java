package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
@SuppressWarnings({VARARGS})
public interface ReactiveIndexService<ModelType> {
    Mono<ModelType> first(Tuple tuple);

    Flux<ModelType> select(Tuple tuple);

    Flux<ModelType> select(Tuple tuple, int offset, int limit);

    default Mono<ModelType> update(Tuple key, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(key, spaceUpdater);
    }

    Mono<ModelType> update(Tuple key, Updater<ModelType> updater);

    Flux<ModelType> update(Collection<? extends Tuple> keys, Updater<ModelType> updater);

    Flux<ModelType> update(ImmutableCollection<? extends Tuple> keys, Updater<ModelType> updater);

    default Flux<ModelType> update(Collection<? extends Tuple> keys, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default Flux<ModelType> update(ImmutableCollection<? extends Tuple> keys, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default Flux<ModelType> find(Tuple... keys) {
        return find(asList(keys));
    }

    Flux<ModelType> find(Collection<? extends Tuple> keys);

    Flux<ModelType> find(ImmutableCollection<? extends Tuple> keys);

    Mono<ModelType> delete(Tuple key);

    default Flux<ModelType> delete(Tuple... keys) {
        return delete(asList(keys));
    }

    Flux<ModelType> delete(Collection<? extends Tuple> keys);

    Flux<ModelType> delete(ImmutableCollection<? extends Tuple> keys);

    Mono<Long> count(Tuple tuple);

    ReactiveSpaceStream<ModelType> stream();

    ReactiveSpaceStream<ModelType> stream(Tuple baseKey);
}
