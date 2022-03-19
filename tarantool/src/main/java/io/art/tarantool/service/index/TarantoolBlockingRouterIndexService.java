package io.art.tarantool.service.index;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.storage.updater.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.stream.*;
import lombok.Builder;
import lombok.*;
import org.msgpack.value.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolBlockingRouterIndexService<ModelType> implements BlockingIndexService<ModelType> {
    private final MetaType<ModelType> spaceType;
    private TarantoolReactiveRouterIndexService<ModelType> reactive;

    @Builder
    public TarantoolBlockingRouterIndexService(MetaType<ModelType> spaceType, ImmutableStringValue spaceName, TarantoolClientRegistry clients) {
        this.spaceType = spaceType;
        reactive = new TarantoolReactiveRouterIndexService<>(spaceType, spaceName, clients);
    }

    public TarantoolBlockingRouterIndexService<ModelType> indexed(Index index) {
        reactive.indexed(index);
        return this;
    }

    public TarantoolBlockingRouterIndexService<ModelType> sharded(ShardRequest request) {
        reactive.sharded(request);
        return this;
    }

    @Override
    public ModelType first(Tuple tuple) {
        return block(reactive.first(tuple));
    }

    @Override
    public ImmutableArray<ModelType> select(Tuple tuple) {
        return reactive.select(tuple).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> select(Tuple tuple, int offset, int limit) {
        return reactive.select(tuple, offset, limit).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> find(Collection<? extends Tuple> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> find(ImmutableCollection<? extends Tuple> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ModelType delete(Tuple key) {
        return block(reactive.delete(key));
    }

    @Override
    public ModelType update(Tuple key, Updater<ModelType> updater) {
        return block(reactive.update(key, updater));
    }

    @Override
    public ImmutableArray<ModelType> update(Collection<? extends Tuple> keys, Updater<ModelType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> update(ImmutableCollection<? extends Tuple> keys, Updater<ModelType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> delete(Collection<? extends Tuple> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> delete(ImmutableCollection<? extends Tuple> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public long count(Tuple tuple) {
        return block(reactive.count(tuple));
    }

    @Override
    public ReactiveIndexService<ModelType> reactive() {
        return reactive;
    }

    @Override
    public TarantoolBlockingStorageSpaceStream<ModelType> stream() {
        return new TarantoolBlockingStorageSpaceStream<>(spaceType, reactive.stream());
    }

    @Override
    public TarantoolBlockingStorageSpaceStream<ModelType> stream(Tuple baseKey) {
        return new TarantoolBlockingStorageSpaceStream<>(spaceType, reactive.stream(baseKey));
    }
}
