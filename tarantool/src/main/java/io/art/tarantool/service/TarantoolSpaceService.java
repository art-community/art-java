package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.tarantool.storage.*;
import lombok.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static java.util.Arrays.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolSpaceService<KeyType, ValueType> {
    private TarantoolReactiveSpaceService<KeyType, ValueType> reactive;

    public TarantoolSpaceService(MetaType<ValueType> spaceMeta, TarantoolStorage storage) {
        reactive = new TarantoolReactiveSpaceService<>(spaceMeta, storage);
    }

    public ValueType findFirst(KeyType key) {
        return block(reactive.findFirst(key));
    }

    @SafeVarargs
    public final ImmutableArray<ValueType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    public ImmutableArray<ValueType> findAll(Collection<KeyType> keys) {
        return reactive.findAll(keys).toStream().collect(immutableArrayCollector());
    }

    public ImmutableArray<ValueType> findAll(ImmutableCollection<KeyType> keys) {
        return reactive.findAll(keys).toStream().collect(immutableArrayCollector());
    }


    public ValueType delete(KeyType key) {
        return block(reactive.delete(key));
    }

    @SafeVarargs
    public final ImmutableArray<ValueType> delete(KeyType... keys) {
        return delete(asList(keys));
    }

    public ImmutableArray<ValueType> delete(Collection<KeyType> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    public ImmutableArray<ValueType> delete(ImmutableCollection<KeyType> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }


    public long count() {
        return block(reactive.count());
    }

    public void truncate() {
        reactive.truncate();
    }


    public ValueType insert(ValueType value) {
        return block(reactive.insert(value));
    }

    @SafeVarargs
    public final ImmutableArray<ValueType> insert(ValueType... value) {
        return reactive.insert(value).toStream().collect(immutableArrayCollector());
    }

    public ImmutableArray<ValueType> insert(Collection<ValueType> value) {
        return reactive.insert(value).toStream().collect(immutableArrayCollector());
    }

    public ImmutableArray<ValueType> insert(ImmutableCollection<ValueType> value) {
        return reactive.insert(value).toStream().collect(immutableArrayCollector());
    }


    public ValueType put(ValueType value) {
        return block(reactive.put(value));
    }

    @SafeVarargs
    public final ImmutableArray<ValueType> put(ValueType... value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    public ImmutableArray<ValueType> put(Collection<ValueType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    public ImmutableArray<ValueType> put(ImmutableCollection<ValueType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }


    public ValueType replace(ValueType value) {
        return block(reactive.put(value));
    }

    @SafeVarargs
    public final ImmutableArray<ValueType> replace(ValueType... value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    public ImmutableArray<ValueType> replace(Collection<ValueType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    public ImmutableArray<ValueType> replace(ImmutableCollection<ValueType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }


    public TarantoolReactiveSpaceService<KeyType, ValueType> reactive() {
        return reactive;
    }
}
