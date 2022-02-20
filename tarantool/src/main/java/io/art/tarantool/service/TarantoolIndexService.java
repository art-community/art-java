package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.tarantool.storage.*;
import lombok.Builder;
import lombok.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolIndexService<KeyType, ModelType> implements IndexService<KeyType, ModelType> {
    private ReactiveIndexService<KeyType, ModelType> reactive;

    @Builder
    public TarantoolIndexService(MetaType<KeyType> keyMeta,
                                 MetaType<ModelType> spaceMeta,
                                 String spaceName,
                                 String indexName,
                                 TarantoolStorage storage) {
        reactive = TarantoolReactiveIndexService.<KeyType, ModelType>builder()
                .spaceName(spaceName)
                .spaceMeta(spaceMeta)
                .keyMeta(keyMeta)
                .indexName(indexName)
                .storage(storage)
                .build();
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
    public long count() {
        return block(reactive.count());
    }

    @Override
    public ReactiveIndexService<KeyType, ModelType> reactive() {
        return reactive;
    }
}
