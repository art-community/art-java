package io.art.tarantool.service;

import io.art.meta.model.*;
import io.art.meta.registry.*;
import io.art.storage.*;
import io.art.storage.SpaceStream.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.meta.registry.BuiltinMetaTypes.longPrimitiveType;
import static io.art.tarantool.constants.TarantoolModuleConstants.FilterOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SelectOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SortOptions.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;


@RequiredArgsConstructor
public class TarantoolReactiveStream<ModelType> extends ReactiveSpaceStream<ModelType> {
    private final TarantoolReactiveSpaceService<?, ModelType> service;

    @Override
    public Flux<ModelType> collect() {
        Mono<Value> result = service
                .storage
                .immutable()
                .call(SPACE_FIND, newArray(service.spaceName, newArray(serializeStream())));
        return service.parseSpaceFlux(result);
    }

    private List<Value> serializeStream() {
        List<Value> serialized = linkedList();
        for (StreamOperator operator : operators) {
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
                    Sorter<ModelType, ?> sorter = cast(operator.getValue());
                    SortComparator comparator = sorter.getComparator();
                    MetaField<?, ?> field = sorter.getField();
                    switch (comparator) {
                        case MORE:
                            serialized.add(newArray(SORT, newArray(COMPARATOR_MORE, newInteger(field.index() + 1))));
                            break;
                        case LESS:
                            serialized.add(newArray(SORT, newArray(COMPARATOR_LESS, newInteger(field.index() + 1))));
                            break;
                    }
                    break;
                case FILTER:
                    Filter<ModelType> filter = cast(operator.getValue());
                    FilterOperator filterOperator = filter.getOperator();
                    field = filter.getField();
                    List<Object> values = filter.getValues();
                    switch (filterOperator) {
                        case EQUALS:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_EQUALS, field, values)));
                            break;
                        case NOT_EQUALS:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_NOT_EQUALS, field, values)));
                            break;
                        case MORE:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_MORE, field, values)));
                            break;
                        case LESS:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_LESS, field, values)));
                            break;
                        case IN:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_IN, field, values)));
                            break;
                        case NOT_IN:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_NOT_IN, field, values)));
                            break;
                        case LIKE:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_LIKE, field, values)));
                            break;
                        case STARTS_WITH:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_STARTS_WITH, field, values)));
                            break;
                        case ENDS_WITH:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_ENDS_WITH, field, values)));
                            break;
                        case CONTAINS:
                            serialized.add(newArray(FILTER, serializeFilterOperator(OPERATOR_CONTAINS, field, values)));
                            break;
                    }
                    break;
            }
        }
        return serialized;
    }

    private ImmutableArrayValue serializeFilterOperator(ImmutableStringValue operator, MetaField<?, ?> field, List<Object> values) {
        return newArray(operator, newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
    }

    private ImmutableArrayValue serializeFilterValues(MetaType<?> type, List<Object> values) {
        return newArray(values.stream().map(value -> serializeValue(type, value)).collect(listCollector()));
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return service.writer.write(type, value);
    }
}
