package io.art.tarantool.service;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.meta.model.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.storage.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.extensions.ReactiveExtensions.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.util.Arrays.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;

@Public
@RequiredArgsConstructor
public class TarantoolReactiveSpaceService<KeyType, ValueType> {
    private final Class<ValueType> spaceType;
    private final StringValue spaceName;
    private final MetaType<ValueType> spaceMeta;
    private final TarantoolStorage storage;
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;

    public TarantoolReactiveSpaceService(MetaType<ValueType> spaceMeta, TarantoolStorage storage) {
        this.spaceType = spaceMeta.type();
        this.storage = storage;
        this.spaceMeta = spaceMeta;
        this.spaceName = newString(idByDash(spaceType));
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
    }

    public Mono<ValueType> findFirst(KeyType key) {
        ArrayValue input = wrapRequest(writer.write(definition(key.getClass()), key));
        Mono<Value> output = storage.immutable().call(SPACE_FIND_FIRST, input);
        return parseMono(output, spaceType);
    }

    @SafeVarargs
    public final Flux<ValueType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    public Flux<ValueType> findAll(Collection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_FIND_ALL, input);
        return parseFlux(output, spaceType);
    }

    public Flux<ValueType> findAll(ImmutableCollection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_FIND_ALL, input);
        return parseFlux(output, spaceType);
    }


    public Mono<ValueType> delete(KeyType key) {
        ArrayValue input = wrapRequest(writer.write(definition(key.getClass()), key));
        Mono<Value> output = storage.immutable().call(SPACE_SINGLE_DELETE, input);
        return parseMono(output, spaceType);
    }

    @SafeVarargs
    public final Flux<ValueType> delete(KeyType... keys) {
        return findAll(asList(keys));
    }

    public Flux<ValueType> delete(Collection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseFlux(output, spaceType);
    }

    public Flux<ValueType> delete(ImmutableCollection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_DELETE, input);
        return parseFlux(output, spaceType);
    }


    public Mono<ValueType> insert(ValueType value) {
        ArrayValue input = wrapRequest(writer.write(spaceMeta, value));
        Mono<Value> output = storage.immutable().call(SPACE_SINGLE_INSERT, input);
        return parseMono(output, spaceType);
    }

    @SafeVarargs
    public final Flux<ValueType> insert(ValueType... value) {
        return insert(Arrays.asList(value));
    }

    public Flux<ValueType> insert(Collection<ValueType> value) {
        ArrayValue input = wrapRequest(newArray(value.stream().map(element -> writer.write(spaceMeta, element)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_INSERT, input);
        return parseFlux(output, spaceType);
    }

    public Flux<ValueType> insert(ImmutableCollection<ValueType> value) {
        ArrayValue input = wrapRequest(newArray(value.stream().map(element -> writer.write(spaceMeta, element)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_INSERT, input);
        return parseFlux(output, spaceType);
    }


    public Mono<ValueType> put(ValueType value) {
        ArrayValue input = wrapRequest(writer.write(spaceMeta, value));
        Mono<Value> output = storage.immutable().call(SPACE_SINGLE_PUT, input);
        return parseMono(output, spaceType);
    }

    @SafeVarargs
    public final Flux<ValueType> put(ValueType... value) {
        return insert(Arrays.asList(value));
    }

    public Flux<ValueType> put(Collection<ValueType> value) {
        ArrayValue input = wrapRequest(newArray(value.stream().map(element -> writer.write(spaceMeta, element)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_PUT, input);
        return parseFlux(output, spaceType);
    }

    public Flux<ValueType> put(ImmutableCollection<ValueType> value) {
        ArrayValue input = wrapRequest(newArray(value.stream().map(element -> writer.write(spaceMeta, element)).collect(listCollector())));
        Mono<Value> output = storage.immutable().call(SPACE_MULTIPLE_PUT, input);
        return parseFlux(output, spaceType);
    }


    public Mono<ValueType> replace(ValueType value) {
        return put(value);
    }

    @SafeVarargs
    public final Flux<ValueType> replace(ValueType... value) {
        return put(value);
    }

    public Flux<ValueType> replace(Collection<ValueType> value) {
        return put(value);
    }

    public Flux<ValueType> replace(ImmutableCollection<ValueType> value) {
        return put(value);
    }


    public Mono<Long> count() {
        Mono<Value> output = storage.mutable().call(SPACE_COUNT, newArray(spaceName));
        return parseMono(output, Long.class);
    }

    public void truncate() {
        block(storage.mutable().call(SPACE_TRUNCATE));
    }


    private ArrayValue wrapRequest(Value data) {
        return newArray(spaceName, data);
    }

    private <T> Mono<T> parseMono(Mono<Value> value, Class<?> type) {
        TarantoolModelReader reader = tarantoolModule().configuration().getReader();
        return value.map(element -> reader.read(definition(type), element));
    }

    private <T> Flux<T> parseFlux(Mono<Value> value, Class<?> type) {
        TarantoolModelReader reader = tarantoolModule().configuration().getReader();
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(definition(type), element))));
    }
}
