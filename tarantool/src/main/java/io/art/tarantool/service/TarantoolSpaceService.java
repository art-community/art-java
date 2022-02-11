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
public class TarantoolSpaceService<KeyType, ValueType> implements SpaceService<KeyType, ValueType> {
    private TarantoolReactiveSpaceService<KeyType, ValueType> reactive;

    public TarantoolSpaceService(MetaType<ValueType> spaceMeta, TarantoolStorage storage) {
        reactive = new TarantoolReactiveSpaceService<>(spaceMeta, storage);
    }

    @Override
    public ValueType findFirst(KeyType key) {
        return block(reactive.findFirst(key));
    }

    @Override
    public ImmutableArray<ValueType> findAll(Collection<KeyType> keys) {
        return reactive.findAll(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ValueType> findAll(ImmutableCollection<KeyType> keys) {
        return reactive.findAll(keys).toStream().collect(immutableArrayCollector());
    }


    @Override
    public ValueType delete(KeyType key) {
        return block(reactive.delete(key));
    }

    @Override
    public ImmutableArray<ValueType> delete(Collection<KeyType> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ValueType> delete(ImmutableCollection<KeyType> keys) {
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
    public ValueType insert(ValueType value) {
        return block(reactive.insert(value));
    }

    @Override
    public ImmutableArray<ValueType> insert(Collection<ValueType> value) {
        return reactive.insert(value).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ValueType> insert(ImmutableCollection<ValueType> value) {
        return reactive.insert(value).toStream().collect(immutableArrayCollector());
    }


    @Override
    public ValueType put(ValueType value) {
        return block(reactive.put(value));
    }

    @Override
    @SafeVarargs
    public final ImmutableArray<ValueType> put(ValueType... value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ValueType> put(Collection<ValueType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ValueType> put(ImmutableCollection<ValueType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }


    @Override
    public ReactiveSpaceService<KeyType, ValueType> reactive() {
        return reactive;
    }
}
