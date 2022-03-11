package io.art.tarantool.service;

import io.art.storage.filter.implementation.*;
import io.art.storage.filter.model.*;
import io.art.storage.stream.*;
import io.art.tarantool.constants.TarantoolModuleConstants.StreamProtocol.*;
import io.art.tarantool.serializer.*;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.constants.StorageConstants.FilterCondition.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.function.*;


public class TarantoolReactiveStream<ModelType> extends ReactiveSpaceStream<ModelType> {
    private final TarantoolReactiveSpaceService<?, ModelType> service;
    private final TarantoolStreamSerializer serializer;
    private final static TerminatingFunctions terminatingFunctions = STREAM_PROTOCOL.terminatingFunctions;

    public TarantoolReactiveStream(TarantoolReactiveSpaceService<?, ModelType> service) {
        super(service.spaceMetaType);
        this.service = service;
        serializer = new TarantoolStreamSerializer(service.writer);
    }

    @Override
    public Flux<ModelType> collect() {
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingCollect;
        ImmutableArrayValue stream = newArray(service.spaceName,
                newArray(serializer.serializeStream(operators)),
                newArray(operator)
        );
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseSpaceFlux(returningType, result);
    }

    @Override
    public Mono<Long> count() {
        ImmutableArrayValue stream = newArray(service.spaceName,
                newArray(serializer.serializeStream(operators)),
                newArray(terminatingFunctions.terminatingCount)
        );
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseLongMono(result);
    }

    @Override
    public Mono<Boolean> all(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableArrayValue stream = newArray(service.spaceName,
                newArray(serializer.serializeStream(operators)),
                newArray(terminatingFunctions.terminatingAll, serializer.serializeFilter(newFilter.getParts()))
        );
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> any(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableArrayValue stream = newArray(service.spaceName, newArray(
                serializer.serializeStream(operators)),
                newArray(terminatingFunctions.terminatingAny, serializer.serializeFilter(newFilter.getParts()))
        );
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> none(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        ImmutableArrayValue stream = newArray(service.spaceName,
                newArray(serializer.serializeStream(operators)),
                newArray(terminatingFunctions.terminatingNone, serializer.serializeFilter(newFilter.getParts()))
        );
        Mono<Value> result = service.clients.immutable().call(SPACE_STREAM, stream);
        return service.parseBooleanMono(result);
    }
}
