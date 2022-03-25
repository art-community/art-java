package io.art.tarantool.service.index;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.updater.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.stream.*;
import lombok.Builder;
import lombok.*;
import org.msgpack.value.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolBlockingStorageIndexService<SpaceType> implements BlockingIndexService<SpaceType> {
    private final MetaType<SpaceType> spaceType;
    private TarantoolReactiveStorageIndexService<SpaceType> reactive;

    @Builder
    public TarantoolBlockingStorageIndexService(MetaType<SpaceType> spaceType, ImmutableStringValue spaceName, TarantoolStorageConnector connector) {
        this.spaceType = spaceType;
        reactive = new TarantoolReactiveStorageIndexService<>(spaceType, spaceName, connector);
    }

    public TarantoolBlockingStorageIndexService<SpaceType> indexed(Index index) {
        reactive.indexed(index);
        return this;
    }

    @Override
    public SpaceType first(Tuple tuple) {
        return block(reactive.first(tuple));
    }

    @Override
    public ImmutableArray<SpaceType> select(Tuple tuple) {
        return reactive.select(tuple).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> select(Tuple tuple, int offset, int limit) {
        return reactive.select(tuple, offset, limit).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> find(Collection<? extends Tuple> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> find(ImmutableCollection<? extends Tuple> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public SpaceType delete(Tuple key) {
        return block(reactive.delete(key));
    }

    @Override
    public SpaceType update(Tuple key, Updater<SpaceType> updater) {
        return block(reactive.update(key, updater));
    }

    @Override
    public ImmutableArray<SpaceType> update(Collection<? extends Tuple> keys, Updater<SpaceType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> update(ImmutableCollection<? extends Tuple> keys, Updater<SpaceType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> delete(Collection<? extends Tuple> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> delete(ImmutableCollection<? extends Tuple> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public long count(Tuple tuple) {
        return block(reactive.count(tuple));
    }

    @Override
    public ReactiveIndexService<SpaceType> reactive() {
        return reactive;
    }

    @Override
    public TarantoolBlockingStorageSpaceStream<SpaceType> stream() {
        return new TarantoolBlockingStorageSpaceStream<>(spaceType, reactive.stream());
    }

    @Override
    public TarantoolBlockingStorageSpaceStream<SpaceType> stream(Tuple baseKey) {
        return new TarantoolBlockingStorageSpaceStream<>(spaceType, reactive.stream(baseKey));
    }
}
