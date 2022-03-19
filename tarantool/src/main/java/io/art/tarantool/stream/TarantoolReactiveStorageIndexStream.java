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
    private final static TerminatingFunctions terminatingFunctions = STREAM_PROTOCOL.terminatingFunctions;

    private final TarantoolStreamSerializer serializer;
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
        ImmutableArrayValue terminating = newArray(terminatingFunctions.terminatingCollect);
        ImmutableArrayValue processing = newArray(serializer.serializeStream(operators));
        ImmutableArrayValue stream = newArray(spaceName, indexName, newArray(processing, terminating), writeOptions());
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return readSpaceFlux(returningType, result);
    }

    @Override
    public Mono<Long> count() {
        ImmutableArrayValue terminating = newArray(terminatingFunctions.terminatingCount);
        ImmutableArrayValue processing = newArray(serializer.serializeStream(operators));
        ImmutableArrayValue stream = newArray(spaceName, indexName, newArray(processing, terminating), writeOptions());
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return readLongMono(result);
    }

    @Override
    public Mono<Boolean> all(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableArrayValue processing = newArray(serializer.serializeStream(operators));
        ImmutableArrayValue terminating = newArray(terminatingFunctions.terminatingAll, serializer.serializeFilter(newFilter.getParts()));
        ImmutableArrayValue stream = newArray(spaceName, indexName, newArray(processing, terminating), writeOptions());
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return readBooleanMono(result);
    }

    @Override
    public Mono<Boolean> any(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableArrayValue processing = newArray(serializer.serializeStream(operators));
        ImmutableArrayValue terminating = newArray(terminatingFunctions.terminatingAny, serializer.serializeFilter(newFilter.getParts()));
        ImmutableArrayValue stream = newArray(spaceName, indexName, newArray(processing, terminating), writeOptions());
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return readBooleanMono(result);
    }

    @Override
    public Mono<Boolean> none(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableArrayValue processing = newArray(serializer.serializeStream(operators));
        ImmutableArrayValue terminating = newArray(terminatingFunctions.terminatingNone, serializer.serializeFilter(newFilter.getParts()));
        ImmutableArrayValue stream = newArray(spaceName, indexName, newArray(processing, terminating), writeOptions());
        Mono<Value> result = clients.immutable().call(INDEX_STREAM, stream);
        return readBooleanMono(result);
    }

    private Mono<Long> readLongMono(Mono<Value> value) {
        return value.map(element -> reader.read(longType(), element));
    }

    private Mono<Boolean> readBooleanMono(Mono<Value> value) {
        return value.map(element -> reader.read(booleanType(), element));
    }

    private Flux<ModelType> readSpaceFlux(MetaType<ModelType> type, Mono<Value> value) {
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
