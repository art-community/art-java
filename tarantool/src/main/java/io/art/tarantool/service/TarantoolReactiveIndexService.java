package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.registry.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;

@Public
public class TarantoolReactiveIndexService<ModelType> implements ReactiveIndexService<ModelType> {
    private final StringValue spaceName;
    private final StringValue indexName;
    private final TarantoolClientRegistry storage;
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;
    private final MetaType<ModelType> spaceMeta;
    private final List<MetaField<? extends MetaClass<ModelType>, ?>> fields;

    @Builder
    public TarantoolReactiveIndexService(List<MetaField<? extends MetaClass<ModelType>, ?>> fields,
                                         MetaType<ModelType> spaceMeta,
                                         ImmutableStringValue spaceName,
                                         ImmutableStringValue indexName,
                                         TarantoolClientRegistry storage) {
        this.fields = fields;
        this.spaceMeta = spaceMeta;
        this.storage = storage;
        this.spaceName = spaceName;
        this.indexName = indexName;
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
    }

    @Override
    public Mono<ModelType> findFirst(Collection<Object> keys) {
        ArrayValue input = serializeKeys(keys);
        Mono<Value> output = storage.immutable().call(INDEX_FIND_FIRST, input);
        return parseSpaceMono(output);
    }

    @Override
    public Mono<ModelType> findFirst(ImmutableCollection<Object> keys) {
        ArrayValue input = serializeKeys(keys);
        Mono<Value> output = storage.immutable().call(INDEX_FIND_FIRST, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> findAll(Collection<Object> keys) {
        ArrayValue input = serializeKeys(keys);
        Mono<Value> output = storage.immutable().call(INDEX_FIND_ALL, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> findAll(ImmutableCollection<Object> keys) {
        ArrayValue input = serializeKeys(keys);
        Mono<Value> output = storage.immutable().call(INDEX_FIND_ALL, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> delete(Collection<Object> keys) {
        ArrayValue input = serializeKeys(keys);
        Mono<Value> output = storage.mutable().call(INDEX_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> delete(ImmutableCollection<Object> keys) {
        ArrayValue input = serializeKeys(keys);
        Mono<Value> output = storage.mutable().call(INDEX_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<Long> count() {
        return parseCountMono(storage.immutable().call(INDEX_COUNT, newArray(spaceName)));
    }

    private ArrayValue serializeKeys(Collection<Object> keys) {
        List<ImmutableValue> serialized = linkedList();
        int index = 0;
        for (Object key : keys) {
            writer.write(fields.get(index++).type(), key);
        }
        return newArray(serialized);
    }

    private ArrayValue serializeKeys(ImmutableCollection<Object> keys) {
        List<ImmutableValue> serialized = linkedList();
        int index = 0;
        for (Object key : keys) {
            writer.write(fields.get(index++).type(), key);
        }
        return newArray(spaceName, indexName, newArray(serialized));
    }

    private Mono<Long> parseCountMono(Mono<Value> value) {
        return value.map(element -> reader.read(longType(), element));
    }

    private Mono<ModelType> parseSpaceMono(Mono<Value> value) {
        return value.map(element -> reader.read(spaceMeta, element));
    }

    private Flux<ModelType> parseSpaceFlux(Mono<Value> value) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(spaceMeta, element))));
    }
}
