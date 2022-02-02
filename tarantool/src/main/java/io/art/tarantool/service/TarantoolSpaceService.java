package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.tarantool.storage.*;
import lombok.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class TarantoolSpaceService<KeyType, ValueType extends Space> {
    private TarantoolReactiveSpaceService<KeyType, ValueType> reactive;

    public TarantoolSpaceService(MetaType<ValueType> spaceMeta, Supplier<TarantoolStorage> storage) {
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

    public long count() {
        return block(reactive.count());
    }

    public void truncate() {
        reactive.truncate();
    }

    public ValueType insert(ValueType value) {
        return block(reactive.insert(value));
    }

    public ValueType put(ValueType value) {
        return block(reactive.put(value));
    }

    public ImmutableArray<ValueType> put(Collection<ValueType> value) {
        return reactive.put(value).toStream().collect(immutableArrayCollector());
    }

    public ValueType replace(ValueType value) {
        return put(value);
    }

    public TarantoolReactiveSpaceService<KeyType, ValueType> reactive() {
        return reactive;
    }
}
