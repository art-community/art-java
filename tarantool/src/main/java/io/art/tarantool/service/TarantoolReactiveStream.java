package io.art.tarantool.service;

import io.art.storage.filter.implementation.*;
import io.art.storage.filter.model.*;
import io.art.storage.stream.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.constants.StorageConstants.FilterCondition.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.serializer.TarantoolStreamSerializer.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.function.*;


@RequiredArgsConstructor
public class TarantoolReactiveStream<ModelType> extends ReactiveSpaceStream<ModelType> {
    private final TarantoolReactiveSpaceService<?, ModelType> service;

    @Override
    public Flux<ModelType> collect() {
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingCollect;
        ImmutableArrayValue stream = newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator));
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseSpaceFlux(result);
    }

    @Override
    public Mono<Long> count() {
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingCount;
        ImmutableArrayValue stream = newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator));
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseLongMono(result);
    }

    @Override
    public Mono<Boolean> all(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingAll;
        ImmutableArrayValue stream = newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator, serializeFilter(newFilter)));
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> any(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingAny;
        ImmutableArrayValue stream = newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator, serializeFilter(newFilter)));
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> none(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingNone;
        ImmutableArrayValue stream = newArray(service.spaceName, newArray(serializeStream(service.writer, operators)), newArray(operator, serializeFilter(newFilter)));
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseBooleanMono(result);
    }
}
