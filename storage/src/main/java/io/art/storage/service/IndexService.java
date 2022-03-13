package io.art.storage.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.storage.updater.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
@SuppressWarnings({VARARGS})
public interface IndexService<ModelType> {
    ModelType first(Tuple tuple);

    ImmutableArray<ModelType> select(Tuple tuple);

    ImmutableArray<ModelType> select(Tuple tuple, int offset, int limit);

    default ImmutableArray<ModelType> find(Tuple... keys) {
        return find(asList(keys));
    }

    ImmutableArray<ModelType> find(Collection<? extends Tuple> keys);

    ImmutableArray<ModelType> find(ImmutableCollection<? extends Tuple> keys);

    ModelType delete(Tuple key);

    default ModelType update(Tuple key, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(key, spaceUpdater);
    }

    ModelType update(Tuple key, Updater<ModelType> updater);

    ImmutableArray<ModelType> update(Collection<? extends Tuple> keys, Updater<ModelType> updater);

    ImmutableArray<ModelType> update(ImmutableCollection<? extends Tuple> keys, Updater<ModelType> updater);

    default ImmutableArray<ModelType> update(Collection<? extends Tuple> keys, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default ImmutableArray<ModelType> update(ImmutableCollection<? extends Tuple> keys, UnaryOperator<Updater<ModelType>> updater) {
        Updater<ModelType> spaceUpdater = new UpdaterImplementation<>();
        updater.apply(spaceUpdater);
        return update(keys, spaceUpdater);
    }

    default ImmutableArray<ModelType> delete(Tuple... keys) {
        return delete(asList(keys));
    }

    ImmutableArray<ModelType> delete(Collection<? extends Tuple> keys);

    ImmutableArray<ModelType> delete(ImmutableCollection<? extends Tuple> keys);

    long count(Tuple tuple);

    ReactiveIndexService<ModelType> reactive();
}
