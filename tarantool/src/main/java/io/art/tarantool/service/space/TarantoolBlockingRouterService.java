package io.art.tarantool.service.space;

import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.sharder.*;
import io.art.storage.updater.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.service.index.*;
import io.art.tarantool.stream.*;
import org.msgpack.value.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

public class TarantoolBlockingRouterService<KeyType, ModelType> implements BlockingShardService<KeyType, ModelType> {
    private final MetaType<ModelType> spaceMetaType;
    private final TarantoolReactiveRouterService<KeyType, ModelType> reactive;
    private final TarantoolBlockingRouterIndexService<ModelType> index;

    public TarantoolBlockingRouterService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
        this.spaceMetaType = spaceMeta.definition();
        ImmutableStringValue spaceName = newString(idByDash(spaceMeta.definition().type()));
        reactive = new TarantoolReactiveRouterService<>(keyMeta, spaceMeta, clients);
        index = new TarantoolBlockingRouterIndexService<>(spaceMetaType, spaceName, clients);
    }

    TarantoolBlockingRouterService<KeyType, ModelType> shard(ShardRequest request) {
        reactive.sharded(request);
        index.sharded(request);
        return this;
    }

    @Override
    public ModelType first(KeyType key) {
        return block(reactive.first(key));
    }

    @Override
    public ImmutableArray<ModelType> select(KeyType key) {
        return reactive.select(key).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> select(KeyType key, long offset, long limit) {
        return reactive.select(key, limit, offset).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> find(Collection<KeyType> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> find(ImmutableCollection<KeyType> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }


    @Override
    public ModelType delete(KeyType key) {
        return block(reactive.delete(key));
    }

    @Override
    public ImmutableArray<ModelType> delete(Collection<KeyType> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> delete(ImmutableCollection<KeyType> keys) {
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
    public ModelType insert(ModelType value) {
        return block(reactive.insert(value));
    }

    @Override
    public ImmutableArray<ModelType> insert(Collection<ModelType> value) {
        return reactive.insert(value).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> insert(ImmutableCollection<ModelType> value) {
        return reactive.insert(value).toStream().collect(immutableArrayCollector());
    }


    @Override
    public ModelType put(ModelType value) {
        return block(reactive.put(value));
    }


    @Override
    public ImmutableArray<ModelType> put(Collection<ModelType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> put(ImmutableCollection<ModelType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ModelType update(KeyType key, Updater<ModelType> updater) {
        return block(reactive.update(key, updater));
    }

    @Override
    public void upsert(ModelType model, Updater<ModelType> updater) {
        block(reactive.upsert(model, updater));
    }

    @Override
    public ImmutableArray<ModelType> update(Collection<KeyType> keys, Updater<ModelType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> update(ImmutableCollection<KeyType> keys, Updater<ModelType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }

    @Override
    public TarantoolBlockingRouterSpaceStream<ModelType> stream() {
        return new TarantoolBlockingRouterSpaceStream<>(spaceMetaType, reactive.stream());
    }

    @Override
    public TarantoolBlockingRouterSpaceStream<ModelType> stream(KeyType baseKey) {
        return new TarantoolBlockingRouterSpaceStream<>(spaceMetaType, reactive.stream(baseKey));
    }

    @Override
    public TarantoolReactiveRouterService<KeyType, ModelType> reactive() {
        return reactive;
    }

    @Override
    public TarantoolBlockingRouterIndexService<ModelType> index(Index index) {
        return this.index.indexed(index);
    }
}
