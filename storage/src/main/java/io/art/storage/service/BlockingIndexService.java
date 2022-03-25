package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.stream.*;
import io.art.storage.updater.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
@SuppressWarnings({VARARGS})
public interface BlockingIndexService<SpaceType> {
    SpaceType first(Tuple tuple);

    ImmutableArray<SpaceType> select(Tuple tuple);

    ImmutableArray<SpaceType> select(Tuple tuple, int offset, int limit);

    default ImmutableArray<SpaceType> find(Tuple... keys) {
        return find(asList(keys));
    }

    ImmutableArray<SpaceType> find(Collection<? extends Tuple> keys);

    ImmutableArray<SpaceType> find(ImmutableCollection<? extends Tuple> keys);

    SpaceType delete(Tuple key);

    default SpaceType update(Tuple key, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(key, spaceUpdater);
    }

    SpaceType update(Tuple key, Updater<SpaceType> updater);

    ImmutableArray<SpaceType> update(Collection<? extends Tuple> keys, Updater<SpaceType> updater);

    ImmutableArray<SpaceType> update(ImmutableCollection<? extends Tuple> keys, Updater<SpaceType> updater);

    default ImmutableArray<SpaceType> update(Collection<? extends Tuple> keys, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default ImmutableArray<SpaceType> update(ImmutableCollection<? extends Tuple> keys, UnaryOperator<Updater<SpaceType>> updater) {
        Updater<SpaceType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default ImmutableArray<SpaceType> delete(Tuple... keys) {
        return delete(asList(keys));
    }

    ImmutableArray<SpaceType> delete(Collection<? extends Tuple> keys);

    ImmutableArray<SpaceType> delete(ImmutableCollection<? extends Tuple> keys);

    long count(Tuple tuple);

    ReactiveIndexService<SpaceType> reactive();

    BlockingSpaceStream<SpaceType> stream();

    BlockingSpaceStream<SpaceType> stream(Tuple baseKey);
}
