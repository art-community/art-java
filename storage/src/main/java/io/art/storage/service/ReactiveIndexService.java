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
public interface ReactiveIndexService<SpaceType> {
    Mono<SpaceType> first(Tuple tuple);

    Flux<SpaceType> select(Tuple tuple);

    Flux<SpaceType> select(Tuple tuple, int offset, int limit);

    default Mono<SpaceType> update(Tuple key, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(key, spaceUpdater);
    }

    Mono<SpaceType> update(Tuple key, Updater<SpaceType> updater);

    Flux<SpaceType> update(Collection<? extends Tuple> keys, Updater<SpaceType> updater);

    Flux<SpaceType> update(ImmutableCollection<? extends Tuple> keys, Updater<SpaceType> updater);

    default Flux<SpaceType> update(Collection<? extends Tuple> keys, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default Flux<SpaceType> update(ImmutableCollection<? extends Tuple> keys, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default Flux<SpaceType> find(Tuple... keys) {
        return find(asList(keys));
    }

    Flux<SpaceType> find(Collection<? extends Tuple> keys);

    Flux<SpaceType> find(ImmutableCollection<? extends Tuple> keys);

    Mono<SpaceType> delete(Tuple key);

    default Flux<SpaceType> delete(Tuple... keys) {
        return delete(asList(keys));
    }

    Flux<SpaceType> delete(Collection<? extends Tuple> keys);

    Flux<SpaceType> delete(ImmutableCollection<? extends Tuple> keys);

    Mono<Long> count(Tuple tuple);

    ReactiveSpaceStream<SpaceType> stream();

    ReactiveSpaceStream<SpaceType> stream(Tuple baseKey);
}
