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
public class TarantoolIndexService<KeyType, ValueType> {
    private TarantoolReactiveIndexService<KeyType, ValueType> reactive;

    public TarantoolIndexService(MetaType<ValueType> spaceMeta, String indexName, TarantoolStorage storage) {
        reactive = new TarantoolReactiveIndexService<>(spaceMeta, indexName, storage);
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

    public TarantoolReactiveSpaceService<KeyType, ValueType> reactive() {
        return reactive;
    }
}
