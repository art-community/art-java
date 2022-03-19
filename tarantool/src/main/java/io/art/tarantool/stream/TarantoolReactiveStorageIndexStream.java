package io.art.tarantool.stream;

import io.art.core.model.*;
import io.art.meta.*;
import io.art.meta.model.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.filter.model.*;
import io.art.storage.stream.*;
import io.art.tarantool.constants.TarantoolModuleConstants.StreamProtocol.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.registry.*;
import io.art.tarantool.serializer.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.storage.constants.StorageConstants.FilterCondition.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import static reactor.core.publisher.Flux.*;
import java.util.*;
import java.util.function.*;


public class TarantoolReactiveStorageIndexStream<ModelType> extends ReactiveSpaceStream<ModelType> {
    private final TarantoolStreamSerializer serializer;
    private final static TerminatingFunctions terminatingFunctions = STREAM_PROTOCOL.terminatingFunctions;
    private final TarantoolModelReader reader;
    private final ImmutableStringValue spaceName;
    private final ImmutableStringValue indexName;
    private final TarantoolClientRegistry clients;
    private final TarantoolModelWriter writer;

    @Builder
    public TarantoolReactiveStorageIndexStream(MetaType<ModelType> spaceType,
                                               ImmutableStringValue spaceName,
                                               ImmutableStringValue indexName,
                                               TarantoolClientRegistry clients,
                                               Tuple baseKey) {
        super(spaceType, baseKey);
        this.spaceName = spaceName;
        this.indexName = indexName;
        this.clients = clients;
        reader = tarantoolModule().configuration().getReader();
        writer = tarantoolModule().configuration().getWriter();
        serializer = new TarantoolStreamSerializer(writer);
    }

    @Override
    public Flux<ModelType> collect() {
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingCollect;
        ImmutableArrayValue stream = newArray(
                spaceName,
                indexName,
                newArray(newArray(serializer.serializeStream(operators)), newArray(operator)),
                writeOptions()
        );
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return parseSpaceFlux(returningType, result);
    }

    @Override
    public Mono<Long> count() {
        ImmutableArrayValue stream = newArray(
                spaceName,
                indexName,
                newArray(newArray(serializer.serializeStream(operators)), newArray(terminatingFunctions.terminatingCount)),
                writeOptions()
        );
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return parseLongMono(result);
    }

    @Override
    public Mono<Boolean> all(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableArrayValue stream = newArray(
                spaceName,
                indexName,
                newArray(serializer.serializeStream(operators)),
                newArray(terminatingFunctions.terminatingAll, serializer.serializeFilter(newFilter.getParts())),
                writeOptions()
        );
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> any(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableArrayValue stream = newArray(
                spaceName,
                indexName,
                newArray(serializer.serializeStream(operators)),
                newArray(terminatingFunctions.terminatingAny, serializer.serializeFilter(newFilter.getParts())),
                writeOptions()
        );
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> none(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableArrayValue stream = newArray(
                spaceName,
                indexName,
                newArray(serializer.serializeStream(operators)),
                newArray(terminatingFunctions.terminatingNone, serializer.serializeFilter(newFilter.getParts())),
                writeOptions()
        );
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return parseBooleanMono(result);
    }

    private Mono<Long> parseLongMono(Mono<Value> value) {
        return value.map(element -> reader.read(longType(), element));
    }

    private Mono<Boolean> parseBooleanMono(Mono<Value> value) {
        return value.map(element -> reader.read(booleanType(), element));
    }

    private Flux<ModelType> parseSpaceFlux(MetaType<ModelType> type, Mono<Value> value) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(type, element))));
    }

    private ImmutableValue writeOptions() {
        if (isNull(baseKey)) return newNil();
        List<Value> serializedKey = baseKey.values()
                .stream()
                .map(key -> writer.write(Meta.definition(key.getClass()), key))
                .collect(listCollector());
        return newArray(newArray(serializedKey));
    }
}
