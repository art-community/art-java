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

@Public
@RequiredArgsConstructor
public class TarantoolIndexService<KeyType, ValueType> implements IndexService<KeyType, ValueType> {
    private ReactiveIndexService<KeyType, ValueType> reactive;

    public TarantoolIndexService(MetaType<ValueType> spaceMeta, String indexName, TarantoolStorage storage) {
        reactive = new TarantoolReactiveIndexService<>(spaceMeta, indexName, storage);
    }

    @Override
    public ValueType findFirst(KeyType key) {
        return block(reactive.findFirst(key));
    }

    @Override
    @SafeVarargs
    public final ImmutableArray<ValueType> findAll(KeyType... keys) {
        return findAll(asList(keys));
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
    public long count() {
        return block(reactive.count());
    }

    @Override
    public ReactiveIndexService<KeyType, ValueType> reactive() {
        return reactive;
    }
}
