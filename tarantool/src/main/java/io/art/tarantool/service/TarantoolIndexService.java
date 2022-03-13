package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
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
    public ModelType first(Object... keys) {
        return block(reactive.first(keys));
    }

    @Override
    public ImmutableArray<ModelType> select(Object... keys) {
        return reactive.select(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> find(Collection<Tuple> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> find(ImmutableCollection<Tuple> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> delete(Collection<Tuple> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> delete(ImmutableCollection<Tuple> keys) {
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
