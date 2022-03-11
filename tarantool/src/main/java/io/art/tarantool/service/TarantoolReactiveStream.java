package io.art.tarantool.service;

import io.art.core.exception.*;
import io.art.meta.model.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.filter.model.*;
import io.art.storage.mapper.*;
import io.art.storage.sorter.implementation.*;
import io.art.storage.stream.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.storage.constants.StorageConstants.FilterCondition.*;
import static io.art.storage.filter.implementation.FilterImplementation.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProcessingOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SortOptions.*;
import static io.art.tarantool.serializer.TarantoolStreamSerializer.serializeFilter;
import static io.art.tarantool.serializer.TarantoolStreamSerializer.serializeStream;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;
import java.util.function.*;


@RequiredArgsConstructor
public class TarantoolReactiveStream<ModelType> extends ReactiveSpaceStream<ModelType> {
    private final TarantoolReactiveSpaceService<?, ModelType> service;

    @Override
    public Flux<ModelType> collect() {
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingCollect;
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator)));
        return service.parseSpaceFlux(result);
    }

    @Override
    public Mono<Long> count() {
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingCount;
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator)));
        return service.parseLongMono(result);
    }

    @Override
    public Mono<Boolean> all(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingAll;
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator, serializeFilter(newFilter))));
        return service.parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> any(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingNone;
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator, serializeFilter(newFilter))));
        return service.parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> none(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingNone;
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator, serializeFilter(newFilter))));
        return service.parseBooleanMono(result);
    }
}
