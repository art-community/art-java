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
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(operator)));
        return service.parseSpaceFlux(result);
    }

    @Override
    public Mono<Long> count() {
        ImmutableIntegerValue operator = STREAM_PROTOCOL.terminatingFunctions.terminatingCount;
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(operator)));
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
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(operator, serializeFilter(newFilter))));
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
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(operator, serializeFilter(newFilter))));
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
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(operator, serializeFilter(newFilter))));
        return service.parseBooleanMono(result);
    }

    private List<Value> serializeStream() {
        List<Value> serialized = linkedList();
        for (ProcessingOperator operator : operators) {
            switch (operator.getOperation()) {
                case LIMIT:
                    serialized.add(newArray(STREAM_PROTOCOL.processingFunctions.processingLimit, serializeValue(longPrimitiveType(), operator.getValue())));
                    break;
                case OFFSET:
                    serialized.add(newArray(STREAM_PROTOCOL.processingFunctions.processingOffset, serializeValue(longPrimitiveType(), operator.getValue())));
                    break;
                case DISTINCT:
                    serialized.add(newArray(STREAM_PROTOCOL.processingFunctions.processingDistinct));
                    break;
                case SORT:
                    serialized.add(newArray(STREAM_PROTOCOL.processingFunctions.processingSort, serializeSort(cast(operator.getValue()))));
                    break;
                case FILTER:
                    serialized.add(newArray(STREAM_PROTOCOL.processingFunctions.processingFilter, serializeFilter(cast(operator.getValue()))));
                    break;
                case MAP:
                    serialized.add(newArray(STREAM_PROTOCOL.processingFunctions.processingMap, serializeMap(cast(operator.getValue()))));
                    break;
            }
        }
        return serialized;
    }

    private ImmutableArrayValue serializeSort(SorterImplementation<?, ?> sorter) {
        SortOrder order = sorter.getOrder();
        MetaField<?, ?> field = sorter.getField();
        switch (order) {
            case ASCENDANT:
                return newArray(STREAM_PROTOCOL.comparators.comparatorLess, newInteger(field.index() + 1));
            case DESCENDANT:
                return newArray(STREAM_PROTOCOL.comparators.comparatorMore, newInteger(field.index() + 1));
        }
        throw new ImpossibleSituationException();
    }

    private ImmutableArrayValue serializeFilter(FilterImplementation<?> filter) {
        List<ImmutableArrayValue> serialized = linkedList();
        List<FilterPart> parts = filter.getParts();
        for (FilterPart part : parts) {
            FilterCondition condition = part.getCondition();
            switch (part.getMode()) {
                case FIELD:
                    part.getByField();
                    break;
                case FUNCTION:
                    FilterByFunctionImplementation<?> byFunction = part.getByFunction();
                    break;
                case SPACE:
                    FilterBySpaceImplementation<?, ?> bySpace = part.getBySpace();

                    break;
                case INDEX:
                    FilterBySpaceImplementation<?, ?> byIndex = part.getByIndex();
                    break;
                case NESTED:
                    NestedFilter nested = part.getNested();
                    break;
            }
        }
        return newArray(serialized);
    }

    private ImmutableArrayValue serializeMap(Mapper<?, ?> filter) {

    }


    private ImmutableArrayValue serializeFilterValues(MetaType<?> type, List<Object> values) {
        return newArray(values.stream().map(value -> serializeValue(type, value)).collect(listCollector()));
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return service.writer.write(type, value);
    }
}
