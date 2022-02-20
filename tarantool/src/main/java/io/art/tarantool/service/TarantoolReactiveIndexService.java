package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.storage.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;

@Public
public class TarantoolReactiveIndexService<KeyType, ModelType> implements ReactiveIndexService<KeyType, ModelType> {
    private final StringValue spaceName;
    private final StringValue indexName;
    private final TarantoolStorage storage;
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;
    private final static MetaType<Long> LONG_TYPE = definition(Long.class);
    private final MetaType<ModelType> spaceMeta;
    private final MetaType<KeyType> keyMeta;

    @Builder
    public TarantoolReactiveIndexService(MetaType<KeyType> keyMeta,
                                         MetaType<ModelType> spaceMeta,
                                         String spaceName,
                                         String indexName,
                                         TarantoolStorage storage) {
        this.spaceMeta = spaceMeta;
        this.keyMeta = keyMeta;
        this.storage = storage;
        this.spaceName = newString(spaceName);
        this.indexName = newString(indexName);
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
    }

    @Override
    public Mono<ModelType> findFirst(KeyType key) {
        ArrayValue input = wrapRequest(writer.write(keyMeta, key));
        Mono<Value> output = storage.immutable().call(SPACE_FIND_FIRST, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> findAll(Collection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_FIND_ALL, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> findAll(ImmutableCollection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_FIND_ALL, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<ModelType> delete(KeyType key) {
        ArrayValue input = wrapRequest(writer.write(keyMeta, key));
        Mono<Value> output = storage.immutable().call(SPACE_SINGLE_DELETE, input);
        return parseSpaceMono(output);
    }

    @Override
    public Flux<ModelType> delete(Collection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Flux<ModelType> delete(ImmutableCollection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(keyMeta, key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseSpaceFlux(output);
    }

    @Override
    public Mono<Long> count() {
        return parseCountMono(storage.mutable().call(SPACE_COUNT, newArray(spaceName)));
    }


    private ArrayValue wrapRequest(Value data) {
        return newArray(spaceName, indexName, data);
    }

    private Mono<Long> parseCountMono(Mono<Value> value) {
        return value.map(element -> reader.read(LONG_TYPE, element));
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
