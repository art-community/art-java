package io.art.tarantool.service.space;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.storage.updater.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.service.index.*;
import io.art.tarantool.stream.*;
import org.msgpack.value.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

public class TarantoolBlockingRouterService<KeyType, SpaceType> implements BlockingShardService<KeyType, SpaceType> {
    private final MetaType<SpaceType> spaceMetaType;
    private final TarantoolReactiveRouterService<KeyType, SpaceType> reactive;
    private final TarantoolBlockingRouterIndexService<SpaceType> index;

    public TarantoolBlockingRouterService(MetaType<KeyType> keyMeta, MetaClass<SpaceType> spaceMeta, TarantoolStorageConnector connector) {
        this.spaceMetaType = spaceMeta.definition();
        ImmutableStringValue spaceName = newString(idByDash(spaceMeta.definition().type()));
        reactive = new TarantoolReactiveRouterService<>(keyMeta, spaceMeta, connector);
        index = new TarantoolBlockingRouterIndexService<>(spaceMetaType, spaceName, connector);
    }

    public TarantoolBlockingRouterService<KeyType, SpaceType> shard(ShardRequest request) {
        reactive.sharded(request);
        index.sharded(request);
        return this;
    }

    @Override
    public TarantoolBlockingRouterIndexService<SpaceType> index(Index index) {
        return this.index.indexed(index);
    }


    @Override
    public TarantoolBlockingRouterSpaceStream<SpaceType> stream() {
        return new TarantoolBlockingRouterSpaceStream<>(spaceMetaType, reactive.stream());
    }

    @Override
    public TarantoolBlockingRouterSpaceStream<SpaceType> stream(KeyType baseKey) {
        return new TarantoolBlockingRouterSpaceStream<>(spaceMetaType, reactive.stream(baseKey));
    }

    @Override
    public TarantoolReactiveRouterService<KeyType, SpaceType> reactive() {
        return reactive;
    }

    @Override
    public SpaceType first(KeyType key) {
        return block(reactive.first(key));
    }

    @Override
    public ImmutableArray<SpaceType> select(KeyType key) {
        return reactive.select(key).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> select(KeyType key, long offset, long limit) {
        return reactive.select(key, limit, offset).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> find(Collection<KeyType> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> find(ImmutableCollection<KeyType> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }


    @Override
    public SpaceType delete(KeyType key) {
        return block(reactive.delete(key));
    }

    @Override
    public ImmutableArray<SpaceType> delete(Collection<KeyType> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> delete(ImmutableCollection<KeyType> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }


    @Override
    public long count(KeyType key) {
        return block(reactive.count(key));
    }

    @Override
    public long size() {
        return block(reactive.size());
    }

    @Override
    public void truncate() {
        block(reactive.truncate());
    }


    @Override
    public SpaceType insert(SpaceType value) {
        return block(reactive.insert(value));
    }

    @Override
    public ImmutableArray<SpaceType> insert(Collection<SpaceType> value) {
        return reactive.insert(value).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> insert(ImmutableCollection<SpaceType> value) {
        return reactive.insert(value).toStream().collect(immutableArrayCollector());
    }


    @Override
    public SpaceType put(SpaceType value) {
        return block(reactive.put(value));
    }


    @Override
    public ImmutableArray<SpaceType> put(Collection<SpaceType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> put(ImmutableCollection<SpaceType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    @Override
    public SpaceType update(KeyType key, Updater<SpaceType> updater) {
        return block(reactive.update(key, updater));
    }

    @Override
    public void upsert(SpaceType model, Updater<SpaceType> updater) {
        block(reactive.upsert(model, updater));
    }

    @Override
    public ImmutableArray<SpaceType> update(Collection<KeyType> keys, Updater<SpaceType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<SpaceType> update(ImmutableCollection<KeyType> keys, Updater<SpaceType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }
}
