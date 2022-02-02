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
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.util.Arrays.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class TarantoolReactiveSpaceService<KeyType, ValueType extends Space> {
    private final Class<ValueType> spaceType;
    private final StringValue spaceName;
    private final MetaType<ValueType> spaceMeta;
    private final Supplier<TarantoolStorage> storage;
    private final TarantoolModelWriter writer;
    private final TarantoolModelReader reader;

    public TarantoolReactiveSpaceService(MetaType<ValueType> spaceMeta, Supplier<TarantoolStorage> storage) {
        this.spaceType = spaceMeta.type();
        this.storage = storage;
        this.spaceMeta = spaceMeta;
        this.spaceName = newString(idByDot(spaceType));
        writer = tarantoolModule().configuration().getWriter();
        reader = tarantoolModule().configuration().getReader();
    }

    public Mono<ValueType> findFirst(KeyType key) {
        ArrayValue input = wrapRequest(writer.write(definition(key.getClass()), key));
        Mono<Value> output = storage.get().immutable().call(SPACE_FIND_FIRST, input);
        return parse(output, spaceType);
    }

    @SafeVarargs
    public final Mono<ValueType> findAll(KeyType... keys) {
        return findAll(asList(keys));
    }

    public Mono<ValueType> findAll(Collection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.get().immutable().call(SPACE_FIND_ALL, input);
        return parse(output, spaceType);
    }

    public Mono<ValueType> findAll(ImmutableCollection<KeyType> keys) {
        ArrayValue input = wrapRequest(newArray(keys.stream().map(key -> writer.write(definition(key.getClass()), key)).collect(listCollector())));
        Mono<Value> output = storage.get().immutable().call(SPACE_FIND_ALL, input);
        return parse(output, spaceType);
    }

    public Mono<Long> count() {
        Mono<Value> output = storage.get().mutable().call(SPACE_COUNT);
        return parse(output, Long.class);
    }

    public void truncate() {
        storage.get().mutable().call(SPACE_TRUNCATE).subscribe();
    }

    public Mono<ValueType> insert(ValueType value) {
        Value input = wrapRequest(writer.write(spaceMeta, value));
        Mono<Value> output = storage.get().immutable().call(SPACE_SINGLE_INSERT, input);
        return parse(output, spaceType);
    }

    public Mono<ValueType> put(ValueType value) {
        Value input = wrapRequest(writer.write(spaceMeta, value));
        Mono<Value> output = storage.get().immutable().call(SPACE_SINGLE_PUT, input);
        return parse(output, spaceType);
    }

    public Mono<ValueType> replace(ValueType value) {
        return put(value);
    }

    private ArrayValue wrapRequest(Value data) {
        return newArray(spaceName, data);
    }

    private <T> Mono<T> parse(Mono<Value> value, Class<?> type) {
        TarantoolModelReader reader = tarantoolModule().configuration().getReader();
        return value.map(element -> cast(reader.read(definition(type), element)));
    }
}
