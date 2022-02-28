package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.local.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.tarantool.registry.*;
import lombok.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.local.ThreadLocalValue.*;
import static java.util.stream.Collectors.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolSpaceService<KeyType, ModelType> implements SpaceService<KeyType, ModelType> {
    private TarantoolReactiveSpaceService<KeyType, ModelType> reactive;
    private ThreadLocalValue<TarantoolStream<ModelType>> stream;

    public TarantoolSpaceService(MetaType<KeyType> keyMeta, MetaClass<ModelType> spaceMeta, TarantoolClientRegistry clients) {
        reactive = new TarantoolReactiveSpaceService<>(keyMeta, spaceMeta, clients);
        stream = threadLocal(() -> new TarantoolStream<>(reactive.stream()));
    }

    @Override
    public ModelType findFirst(KeyType key) {
        return block(reactive.findFirst(key));
    }

    @Override
    public ImmutableArray<ModelType> findAll(Collection<KeyType> keys) {
        return reactive.findAll(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> findAll(ImmutableCollection<KeyType> keys) {
        return reactive.findAll(keys).toStream().collect(immutableArrayCollector());
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
    public long count() {
        return block(reactive.count());
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
    public TarantoolStream<ModelType> stream() {
        TarantoolStream<ModelType> stream = this.stream.get();
        stream.refresh();
        return stream;
    }

    @Override
    public TarantoolReactiveSpaceService<KeyType, ModelType> reactive() {
        return reactive;
    }

    @Override
    @SafeVarargs
    public final IndexService<KeyType, ModelType> index(MetaField<MetaClass<ModelType>, ?>... fields) {
        return TarantoolIndexService.<KeyType, ModelType>builder()
                .indexName(newString(Arrays.stream(fields).map(MetaField::name).collect(joining())))
                .spaceMeta(reactive.spaceMetaType)
                .keyMeta(reactive.keyMeta)
                .storage(reactive.clients)
                .spaceName(reactive.spaceName)
                .build();
    }
}
