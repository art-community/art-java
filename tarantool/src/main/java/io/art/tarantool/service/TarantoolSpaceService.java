package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.index.*;
import io.art.storage.service.*;
import io.art.storage.updater.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.registry.*;
import lombok.*;
import org.msgpack.value.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolSpaceService<KeyType, ModelType> implements SpaceService<KeyType, ModelType> {
    private final Class<ModelType> spaceType;
    private final ImmutableStringValue spaceName;
    private final MetaType<ModelType> spaceMetaType;
    private final TarantoolClientRegistry clients;
    private final TarantoolModelWriter writer;
    private TarantoolReactiveSpaceService<KeyType, ModelType> reactive;

    public TarantoolSpaceService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
        this.spaceType = spaceMeta.definition().type();
        this.clients = clients;
        this.spaceMetaType = spaceMeta.definition();
        this.spaceName = newString(idByDash(spaceType));
        writer = tarantoolModule().configuration().getWriter();
        reactive = new TarantoolReactiveSpaceService<>(keyMeta, spaceMeta, clients);
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
    public ModelType upsert(ModelType model, Updater<ModelType> updater) {
        return block(reactive.upsert(model, updater));
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
    public TarantoolStream<ModelType> stream() {
        return new TarantoolStream<>(spaceMetaType, reactive.stream());
    }

    @Override
    public TarantoolStream<ModelType> stream(KeyType baseKey) {
        return new TarantoolStream<>(spaceMetaType, reactive.stream(baseKey));
    }

    @Override
    public TarantoolReactiveSpaceService<KeyType, ModelType> reactive() {
        return reactive;
    }

    @Override
    public final IndexService<ModelType> index(Index index) {
        return TarantoolIndexService.<ModelType>builder()
                .indexName(newString(index.name()))
                .spaceType(spaceMetaType)
                .fields(cast(index.fields()))
                .clients(clients)
                .spaceName(spaceName)
                .build();
    }
}
