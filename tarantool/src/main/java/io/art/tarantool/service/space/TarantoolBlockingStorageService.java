package io.art.tarantool.service.space;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.storage.updater.*;
import io.art.tarantool.connector.*;
import io.art.tarantool.service.index.*;
import io.art.tarantool.stream.*;
import lombok.*;
import org.msgpack.value.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolBlockingStorageService<KeyType, SpaceType> implements BlockingSpaceService<KeyType, SpaceType> {
    private final MetaType<SpaceType> spaceMetaType;
    private final TarantoolStorageConnector connector;
    private final TarantoolReactiveStorageService<KeyType, SpaceType> reactive;
    private final TarantoolBlockingRouterService<KeyType, SpaceType> sharded;
    private final TarantoolBlockingStorageIndexService<SpaceType> index;

    public TarantoolBlockingStorageService(MetaType<KeyType> keyMeta, MetaClass<SpaceType> spaceMeta, TarantoolStorageConnector connector) {
        this.connector = connector;
        this.spaceMetaType = spaceMeta.definition();
        ImmutableStringValue spaceName = newString(idByDash(spaceMeta.definition().type()));
        reactive = new TarantoolReactiveStorageService<>(keyMeta, spaceMeta, connector);
        sharded = new TarantoolBlockingRouterService<>(keyMeta, spaceMeta, connector);
        index = new TarantoolBlockingStorageIndexService<>(spaceMetaType, spaceName, connector);
    }


    @Override
    public final TarantoolBlockingStorageIndexService<SpaceType> index(Index index) {
        return this.index.indexed(index);
    }

    @Override
    public TarantoolBlockingRouterService<KeyType, SpaceType> shard(ShardRequest request) {
        return sharded.shard(request);
    }

    @Override
    public TarantoolBlockingStorageSpaceStream<SpaceType> stream() {
        return new TarantoolBlockingStorageSpaceStream<>(spaceMetaType, reactive.stream());
    }

    @Override
    public TarantoolBlockingStorageSpaceStream<SpaceType> stream(KeyType baseKey) {
        return new TarantoolBlockingStorageSpaceStream<>(spaceMetaType, reactive.stream(baseKey));
    }

    @Override
    public TarantoolReactiveStorageService<KeyType, SpaceType> reactive() {
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
