package io.art.tarantool.service;

import io.art.core.exception.*;
import io.art.meta.model.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.filter.model.*;
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
import static io.art.tarantool.constants.TarantoolModuleConstants.SortOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TerminatingOptions.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;
import java.util.function.*;


@RequiredArgsConstructor
public class TarantoolReactiveStream<ModelType> extends ReactiveSpaceStream<ModelType> {
    private final TarantoolReactiveSpaceService<?, ModelType> service;

    @Override
    public Flux<ModelType> collect() {
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(COLLECT)));
        return service.parseSpaceFlux(result);
    }

    @Override
    public Mono<Long> count() {
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(COUNT)));
        return service.parseLongMono(result);
    }

    @Override
    public Mono<Boolean> all(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(ALL, serializeFilter(newFilter))));
        return service.parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> any(Consumer<Filter<ModelType>> filter) {
        FilterImplementation<ModelType> newFilter = new FilterImplementation<>(AND, linkedList());
        filter.accept(newFilter);
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(ANY, serializeFilter(newFilter))));
        return service.parseBooleanMono(result);
    }

    private List<Value> serializeStream() {
        List<Value> serialized = linkedList();
        for (ProcessingOperator operator : operators) {
            switch (operator.getOperation()) {
                case LIMIT:
                    serialized.add(newArray(LIMIT, serializeValue(longPrimitiveType(), operator.getValue())));
                    break;
                case OFFSET:
                    serialized.add(newArray(OFFSET, serializeValue(longPrimitiveType(), operator.getValue())));
                    break;
                case DISTINCT:
                    serialized.add(newArray(DISTINCT));
                    break;
                case SORT:
                    serialized.add(newArray(SORT, serializeSort(cast(operator.getValue()))));
                    break;
                case FILTER:
                    serialized.add(newArray(FILTER, serializeFilter(cast(operator.getValue()))));
                    break;
                case MAP:
                    serialized.add(newArray(FILTER, serializeFilter(cast(operator.getValue()))));
                    break;
            }
        }
        return serialized;
    }

    private ImmutableArrayValue serializeSort(SorterImplementation<?, ?> sorter) {
        SortComparator comparator = sorter.getComparator();
        MetaField<?, ?> field = sorter.getField();
        switch (comparator) {
            case MORE:
                return newArray(COMPARATOR_MORE, newInteger(field.index() + 1));
            case LESS:
                return newArray(COMPARATOR_LESS, newInteger(field.index() + 1));
        }
        throw new ImpossibleSituationException();
    }

    private ImmutableArrayValue serializeFilter(FilterImplementation<?> filter) {
        List<FilterPart> parts = filter.getParts();
        for (FilterPart part : parts) {
            FilterCondition condition = part.getCondition();
            switch (part.getMode()) {
                case FIELD:
                    FilterByFieldImplementation<?, ?> byField = part.getByField();
                    switch (byField.getOperator()) {
                        case EQUALS:
                            break;
                        case NOT_EQUALS:
                            break;
                        case MORE:
                            break;
                        case LESS:
                            break;
                        case BETWEEN:
                            break;
                        case NOT_BETWEEN:
                            break;
                        case IN:
                            break;
                        case NOT_IN:
                            break;
                        case STARTS_WITH:
                            break;
                        case ENDS_WITH:
                            break;
                        case CONTAINS:
                            break;
                    }
                    break;
                case FUNCTION:
                    FilterByFunctionImplementation<?> byFunction = part.getByFunction();
                    break;
                case SPACE:
                    FilterBySpaceImplementation<?, ?> bySpace = part.getBySpace();
                    switch (bySpace.getExpressionType()) {
                        case FIELD:
                            switch (bySpace.getBySpaceUseFields().getOperator()) {

                            }
                            break;
                        case STRING_FIELD:
                            break;
                        case NUMBER_FIELD:
                            break;
                        case VALUE:
                            break;
                        case STRING_VALUE:
                            break;
                        case NUMBER_VALUE:
                            break;
                    }

                    break;
                case INDEX:
                    FilterBySpaceImplementation<?, ?> byIndex = part.getByIndex();
                    break;
                case NESTED:
                    NestedFilter nested = part.getNested();
                    break;
            }
        }
        throw new ImpossibleSituationException();
    }


    private ImmutableArrayValue serializeFilterValues(MetaType<?> type, List<Object> values) {
        return newArray(values.stream().map(value -> serializeValue(type, value)).collect(listCollector()));
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return service.writer.write(type, value);
    }
}
