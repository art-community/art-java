package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.tarantool.storage.*;
import lombok.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolSpaceService<KeyType, ModelType> implements SpaceService<KeyType, ModelType> {
    private TarantoolReactiveSpaceService<KeyType, ModelType> reactive;

    public TarantoolSpaceService(MetaType<KeyType> keyMeta, MetaType<ModelType> spaceMeta, TarantoolStorage storage) {
        reactive = new TarantoolReactiveSpaceService<>(keyMeta, spaceMeta, storage);
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
    public <MetaModel extends MetaClass<ModelType>> TarantoolStream<ModelType, MetaModel> stream(MetaModel model) {
        return new TarantoolStream<>(reactive.stream(model));
    }

    @Override
    public TarantoolReactiveSpaceService<KeyType, ModelType> reactive() {
        return reactive;
    }
}
