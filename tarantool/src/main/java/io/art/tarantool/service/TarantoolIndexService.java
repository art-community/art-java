package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.storage.updater.*;
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
    private final MetaType<ModelType> spaceType;
    private TarantoolReactiveIndexService<ModelType> reactive;

    @Builder
    public TarantoolIndexService(List<MetaField<? extends MetaClass<ModelType>, ?>> fields,
                                 MetaType<ModelType> spaceType,
                                 ImmutableStringValue spaceName,
                                 ImmutableStringValue indexName,
                                 TarantoolClientRegistry clients) {
        this.spaceType = spaceType;
        reactive = TarantoolReactiveIndexService.<ModelType>builder()
                .spaceName(spaceName)
                .spaceType(spaceType)
                .fields(fields)
                .indexName(indexName)
                .clients(clients)
                .build();
    }

    @Override
    public ModelType first(Tuple tuple) {
        return block(reactive.first(tuple));
    }

    @Override
    public ImmutableArray<ModelType> select(Tuple tuple) {
        return reactive.select(tuple).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> select(Tuple tuple, int offset, int limit) {
        return reactive.select(tuple, offset, limit).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> find(Collection<? extends Tuple> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> find(ImmutableCollection<? extends Tuple> keys) {
        return reactive.find(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ModelType delete(Tuple key) {
        return block(reactive.delete(key));
    }

    @Override
    public ModelType update(Tuple key, Updater<ModelType> updater) {
        return block(reactive.update(key, updater));
    }

    @Override
    public ImmutableArray<ModelType> update(Collection<? extends Tuple> keys, Updater<ModelType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> update(ImmutableCollection<? extends Tuple> keys, Updater<ModelType> updater) {
        return reactive.update(keys, updater).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> delete(Collection<? extends Tuple> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public ImmutableArray<ModelType> delete(ImmutableCollection<? extends Tuple> keys) {
        return reactive.delete(keys).toStream().collect(immutableArrayCollector());
    }

    @Override
    public long count(Tuple tuple) {
        return block(reactive.count(tuple));
    }

    @Override
    public ReactiveIndexService<ModelType> reactive() {
        return reactive;
    }

    @Override
    public TarantoolStream<ModelType> stream() {
        return new TarantoolStream<>(spaceType, reactive.stream());
    }

    @Override
    public TarantoolStream<ModelType> stream(Tuple baseKey) {
        return new TarantoolStream<>(spaceType, reactive.stream(baseKey));
    }
}
