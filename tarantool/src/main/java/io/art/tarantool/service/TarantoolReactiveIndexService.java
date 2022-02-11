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
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolReactiveIndexService<KeyType, ValueType> implements ReactiveIndexService<KeyType, ValueType> {
    private final Class<ValueType> spaceType;
    private final StringValue spaceName;
    private final StringValue indexName;
    private final TarantoolStorage storage;
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;

    public TarantoolReactiveIndexService(MetaType<ValueType> spaceMeta, String indexName, TarantoolStorage storage) {
        this.spaceType = spaceMeta.type();
        this.storage = storage;
        this.spaceName = newString(idByDash(spaceType));
        this.indexName = newString(indexName);
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
    }

    @Override
    public Mono<ValueType> findFirst(KeyType key) {
        ArrayValue input = wrapRequest(writer.write(definition(key.getClass()), key));
        Mono<Value> output = storage.immutable().call(SPACE_FIND_FIRST, input);
        return parseMono(output, spaceType);
    }

    @Override
    public Flux<ValueType> findAll(Collection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_FIND_ALL, input);
        return parseFlux(output, spaceType);
    }

    @Override
    public Flux<ValueType> findAll(ImmutableCollection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_FIND_ALL, input);
        return parseFlux(output, spaceType);
    }

    @Override
    public Mono<ValueType> delete(KeyType key) {
        ArrayValue input = wrapRequest(writer.write(definition(key.getClass()), key));
        Mono<Value> output = storage.immutable().call(SPACE_SINGLE_DELETE, input);
        return parseMono(output, spaceType);
    }

    @Override
    public Flux<ValueType> delete(Collection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseFlux(output, spaceType);
    }

    @Override
    public Flux<ValueType> delete(ImmutableCollection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseFlux(output, spaceType);
    }

    @Override
    public Mono<Long> count() {
        Mono<Value> output = storage.mutable().call(SPACE_COUNT, newArray(spaceName));
        return parseMono(output, Long.class);
    }


    private ArrayValue wrapRequest(Value data) {
        return newArray(spaceName, indexName, data);
    }

    private <T> Mono<T> parseMono(Mono<Value> value, Class<?> type) {
        return value.map(element -> reader.read(definition(type), element));
    }

    private <T> Flux<T> parseFlux(Mono<Value> value, Class<?> type) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(definition(type), element))));
    }
}
