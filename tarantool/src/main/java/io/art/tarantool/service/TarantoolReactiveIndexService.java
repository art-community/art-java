package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.service.*;
import io.art.storage.updater.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.serializer.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
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
    private final TarantoolUpdateSerializer updateSerializer;

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
        updateSerializer = new TarantoolUpdateSerializer(writer);
    }

    @Override
    public Mono<ModelType> first(Tuple tuple) {
        ArrayValue input = wrapRequest(serializeTuple(tuple));
        Mono<Value> output = storage.immutable().call(INDEX_FIRST, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> select(Tuple tuple) {
        ArrayValue input = wrapRequest(serializeTuple(tuple));
        Mono<Value> output = storage.immutable().call(INDEX_SELECT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> select(Tuple tuple, int offset, int limit) {
        ArrayValue input = newArray(spaceName, indexName, serializeTuple(tuple), newArray(newInteger(offset), newInteger(limit)));
        Mono<Value> output = storage.immutable().call(INDEX_SELECT, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<ModelType> update(Tuple key, Updater<ModelType> updater) {
        ArrayValue input = newArray(spaceName, indexName, serializeTuple(key), updateSerializer.serializeUpdate(cast(updater)));
        Mono<Value> output = storage.immutable().call(INDEX_SINGLE_UPDATE, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> find(Collection<? extends Tuple> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(this::serializeTuple).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(INDEX_FIND, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> find(ImmutableCollection<? extends Tuple> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(this::serializeTuple).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(INDEX_FIND, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<ModelType> delete(Tuple key) {
        ArrayValue input = wrapRequest(serializeTuple(key));
        Mono<Value> output = storage.mutable().call(INDEX_SINGLE_DELETE, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> delete(Collection<? extends Tuple> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(this::serializeTuple).collect(listCollector())));
        Mono<Value> output = storage.mutable().call(INDEX_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> delete(ImmutableCollection<? extends Tuple> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(this::serializeTuple).collect(listCollector())));
        Mono<Value> output = storage.mutable().call(INDEX_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<Long> count(Tuple tuple) {
        return parseCountMono(storage.immutable().call(INDEX_COUNT, newArray(spaceName, indexName, serializeTuple(tuple))));
    }

    private ImmutableValue serializeTuple(Tuple tuple) {
        List<Value> serialized = linkedList();
        int index = 0;
        for (Object key : tuple.values()) {
            serialized.add(writer.write(fields.get(index++).type(), key));
        }
        return newArray(serialized);
    }

    private ImmutableArrayValue wrapRequest(ImmutableValue serialized) {
        return newArray(spaceName, indexName, serialized);
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
