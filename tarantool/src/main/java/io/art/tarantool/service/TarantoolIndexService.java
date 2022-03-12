package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.tarantool.registry.*;
import lombok.Builder;
import lombok.*;
import org.msgpack.value.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolIndexService<ModelType> implements IndexService<ModelType> {
    private ReactiveIndexService<ModelType> reactive;

    @Builder
    public TarantoolIndexService(List<MetaField<? extends MetaClass<ModelType>, ?>> fields,
                                 MetaType<ModelType> spaceMeta,
                                 ImmutableStringValue spaceName,
                                 ImmutableStringValue indexName,
                                 TarantoolClientRegistry storage) {
        reactive = TarantoolReactiveIndexService.<ModelType>builder()
                .spaceName(spaceName)
                .spaceMeta(spaceMeta)
                .fields(fields)
                .indexName(indexName)
                .storage(storage)
                .build();
    }

    @Override
    public ModelType findFirst(Collection<Object> keys) {
        return block(reactive.findFirst(keys));
    }

    @Override
    public ModelType findFirst(ImmutableCollection<Object> keys) {
        return block(reactive.findFirst(keys));
    }

    @Override
    public ImmutableArray<ModelType> findAll(Collection<Object> keys) {
        return reactive.findAll(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> findAll(ImmutableCollection<Object> keys) {
        return reactive.findAll(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> delete(Collection<Object> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> delete(ImmutableCollection<Object> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public long count() {
        return block(reactive.count());
    }

    @Override
    public ReactiveIndexService<ModelType> reactive() {
        return reactive;
    }
}
