package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.SpaceStream.*;
import io.art.tarantool.constants.TarantoolModuleConstants.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.meta.Meta.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.FilterOptions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.SortOptions.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;


@RequiredArgsConstructor
public class TarantoolReactiveStream<ModelType> extends ReactiveSpaceStream<ModelType> {
    private final MetaType<Long> LONG_TYPE = definition(long.class);
    private final TarantoolReactiveSpaceService<?, ModelType> service;

    @Override
    public Flux<ModelType> collect() {
        List<Value> serialized = linkedList();
        for (Pair<StreamOperation, Object> operator : operators) {
            switch (operator.getFirst()) {
                case LIMIT:
                    serialized.add(newArray(newString(SelectOptions.LIMIT), serializeValue(LONG_TYPE, operator.getSecond())));
                    break;
                case OFFSET:
                    serialized.add(newArray(newString(SelectOptions.OFFSET), serializeValue(LONG_TYPE, operator.getSecond())));
                    break;
                case DISTINCT:
                    serialized.add(newArray(newString(SelectOptions.DISTINCT)));
                    break;
                case SORT:
                    Sorter<ModelType, ?> sorter = cast(operator.getSecond());
                    Sorter.SortComparator comparator = sorter.getComparator();
                    MetaField<?, ?> field = sorter.getField();
                    switch (comparator) {
                        case MORE:
                            serialized.add(newArray(newString(SelectOptions.SORT), newArray(newString(COMPARATOR_MORE), newInteger(field.index() + 1))));
                            break;
                        case LESS:
                            serialized.add(newArray(newString(SelectOptions.SORT), newArray(newString(COMPARATOR_LESS), newInteger(field.index() + 1))));
                            break;
                    }
                    break;
                case FILTER:
                    Filter<ModelType> filter = cast(operator.getSecond());
                    Filter.FilterOperator filterOperator = filter.getOperator();
                    field = filter.getField();
                    List<Object> values = filter.getValues();
                    switch (filterOperator) {
                        case EQUALS:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_EQUALS, field, values)));
                            break;
                        case NOT_EQUALS:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_NOT_EQUALS, field, values)));
                            break;
                        case MORE:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_MORE, field, values)));
                            break;
                        case LESS:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_LESS, field, values)));
                            break;
                        case IN:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_IN, field, values)));
                            break;
                        case NOT_IN:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_NOT_IN, field, values)));
                            break;
                        case LIKE:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_LIKE, field, values)));
                            break;
                        case STARTS_WITH:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_STARTS_WITH, field, values)));
                            break;
                        case ENDS_WITH:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_ENDS_WITH, field, values)));
                            break;
                        case CONTAINS:
                            serialized.add(newArray(newString(SelectOptions.FILTER), serializeFilterOperator(OPERATOR_CONTAINS, field, values)));
                            break;
                    }
                    break;
            }
        }

        return service.parseSpaceFlux(service.storage.immutable().call(SPACE_FIND, newArray(service.spaceName, newArray(serialized))));
    }

    private ImmutableArrayValue serializeFilterOperator(String operator, MetaField<?, ?> field, List<Object> values) {
        return newArray(newString(operator), newInteger(field.index() + 1), serializeFilterValues(field.type(), values));
    }

    private ImmutableArrayValue serializeFilterValues(MetaType<?> type, List<Object> values) {
        return newArray(values.stream().map(value -> serializeValue(type, value)).collect(listCollector()));
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return service.writer.write(type, value);
    }
}
