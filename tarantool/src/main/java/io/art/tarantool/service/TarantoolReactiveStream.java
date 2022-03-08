package io.art.tarantool.service;

import io.art.core.exception.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.StorageConstants.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.storage.StorageConstants.FilterCondition.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.FilterOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ProcessingOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SortOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TerminatingOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.WithOptions.*;
import static io.art.tarantool.factory.TarantoolNameFactory.*;
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
        Filter<ModelType> newFilter = new Filter<>(AND, linkedList());
        filter.accept(newFilter);
        Mono<Value> result = service
                .clients
                .immutable()
                .call(SPACE_STREAM, newArray(service.spaceName, newArray(serializeStream()), newArray(ALL, serializeFilter(newFilter))));
        return service.parseBooleanMono(result);
    }

    @Override
    public Mono<Boolean> any(Consumer<Filter<ModelType>> filter) {
        Filter<ModelType> newFilter = new Filter<>(AND, linkedList());
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

    private ImmutableArrayValue serializeSort(Sorter<?, ?> sorter) {
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

    private ImmutableArrayValue serializeFilter(Filter<?> filter) {
        FilterOperator filterOperator = filter.getOperator();
        MetaField<?, ?> field = filter.getField();
        List<Object> values = filter.getValues();
        switch (filterOperator) {
            case EQUALS:
                return newArray(OPERATOR_EQUALS, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case NOT_EQUALS:
                return newArray(OPERATOR_NOT_EQUALS, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case MORE:
                return newArray(OPERATOR_MORE, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case LESS:
                return newArray(OPERATOR_LESS, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case IN:
                return newArray(OPERATOR_IN, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case NOT_IN:
                return newArray(OPERATOR_NOT_IN, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case BETWEEN:
                return newArray(OPERATOR_BETWEEN, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case NOT_BETWEEN:
                return newArray(OPERATOR_NOT_BETWEEN, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case STARTS_WITH:
                return newArray(OPERATOR_STARTS_WITH, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case ENDS_WITH:
                return newArray(OPERATOR_ENDS_WITH, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
            case CONTAINS:
                return newArray(OPERATOR_CONTAINS, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
        }
        throw new ImpossibleSituationException();
    }

    private ImmutableArrayValue serializeFilterWith(FilterBySpaceUseFields<?, ?> filter) {
        FilterOperator filterOperator = filter.getOperator();
        MetaField<?, ?> field = filter.getCurrentField();

        MetaField<? extends MetaClass<?>, ?> mappingFieldIndex = filter.getMappingKeyField();
        List<MetaField<MetaClass<?>, ?>> mappingIndexedFields = cast(filter.getMappingIndexedFields());
        List<Integer> filterableIndexes = filter.getFilterableFields()
                .stream()
                .map(filterable -> filterable.index() + 1)
                .collect(listCollector());
        FilterMode mode = filter.getMode();
        MetaClass<?> mappingSpace = filter.getMappingSpace();

        List<ImmutableArrayValue> serialized = linkedList();

        switch (mode) {
            case KEY:
                serialized.add(newArray(WITH_BY_KEY, spaceName(mappingSpace), newInteger(mappingFieldIndex.index() + 1)));
                break;
            case INDEX:
                serialized.add(newArray(WITH_BY_INDEX, spaceName(mappingSpace), indexName(mappingIndexedFields), newInteger(mappingFieldIndex.index() + 1)));
                break;
        }

        switch (filterOperator) {
            case EQUALS:
                serialized.add(newArray(OPERATOR_EQUALS, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case NOT_EQUALS:
                serialized.add(newArray(OPERATOR_NOT_EQUALS, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case MORE:
                serialized.add(newArray(OPERATOR_MORE, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case LESS:
                serialized.add(newArray(OPERATOR_LESS, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case IN:
                serialized.add(newArray(OPERATOR_IN, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case NOT_IN:
                serialized.add(newArray(OPERATOR_NOT_IN, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case BETWEEN:
                serialized.add(newArray(OPERATOR_BETWEEN, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case NOT_BETWEEN:
                serialized.add(newArray(OPERATOR_NOT_BETWEEN, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case STARTS_WITH:
                serialized.add(newArray(OPERATOR_STARTS_WITH, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case ENDS_WITH:
                serialized.add(newArray(OPERATOR_ENDS_WITH, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
            case CONTAINS:
                serialized.add(newArray(OPERATOR_CONTAINS, newInteger(field.index() + 1), serializeFilterValues(integerType(), cast(filterableIndexes))));
        }

        return newArray(serialized);
    }

    private ImmutableArrayValue serializeFilterValues(MetaType<?> type, List<Object> values) {
        return newArray(values.stream().map(value -> serializeValue(type, value)).collect(listCollector()));
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return service.writer.write(type, value);
    }
}
