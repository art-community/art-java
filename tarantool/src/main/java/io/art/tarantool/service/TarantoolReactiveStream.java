package io.art.tarantool.service;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.SpaceStream.*;
import io.art.tarantool.constants.TarantoolModuleConstants.*;
import io.art.tarantool.descriptor.*;
import io.art.tarantool.storage.*;
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
import static reactor.core.publisher.Flux.*;
import java.util.*;


@AllArgsConstructor
public class TarantoolReactiveStream<ModelType, MetaModel extends MetaClass<ModelType>> extends ReactiveSpaceStream<ModelType, MetaModel> {
    private final TarantoolStorage storage;
    private final TarantoolModelReader reader;
    private final TarantoolModelWriter writer;
    private final StringValue spaceName;
    private final MetaType<ModelType> spaceMeta;
    private final MetaType<String> STRING = definition(String.class);
    private final MetaType<Long> LONG = definition(long.class);

    @Override
    public Flux<ModelType> collect() {
        List<Value> serialized = linkedList();
        for (Pair<StreamOperation, Object> operator : operators) {
            switch (operator.getFirst()) {
                case LIMIT:
                    serialized.add(newArray(newString(SelectOptions.LIMIT), serializeValue(LONG, operator.getSecond())));
                    break;
                case OFFSET:
                    serialized.add(newArray(newString(SelectOptions.OFFSET), serializeValue(LONG, operator.getSecond())));
                    break;
                case DISTINCT:
                    serialized.add(newArray(newString(SelectOptions.DISTINCT)));
                    break;
                case SORT:
                    Sorter<ModelType, MetaModel, ?> sorter = cast(operator.getSecond());
                    Sorter.SortComparator comparator = sorter.getComparator();
                    MetaField<MetaModel, ?> field = sorter.getField();
                    switch (comparator) {
                        case MORE:
                            serialized.add(newArray(newString(SelectOptions.SORT), newArray(newString(COMPARATOR_MORE), newInteger(field.index()))));
                            break;
                        case LESS:
                            serialized.add(newArray(newString(SelectOptions.SORT), newArray(newString(COMPARATOR_LESS), newInteger(field.index()))));
                            break;
                    }
                    break;
                case FILTER:
                    Filter<ModelType, MetaModel, ?> filter = cast(operator.getSecond());
                    Filter.FilterOperator filterOperator = filter.getOperator();
                    field = filter.getCurrent();
                    List<Object> values = filter.getValues();
                    switch (filterOperator) {
                        case EQUALS:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeModelOperator(OPERATOR_EQUALS, field, values)));
                            break;
                        case NOT_EQUALS:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeModelOperator(OPERATOR_NOT_EQUALS, field, values)));
                            break;
                        case MORE:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeNumberOperator(OPERATOR_MORE, field, values)));
                            break;
                        case LESS:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeNumberOperator(OPERATOR_LESS, field, values)));
                            break;
                        case IN:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeNumberOperator(OPERATOR_IN, field, values)));
                            break;
                        case NOT_IN:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeNumberOperator(OPERATOR_NOT_IN, field, values)));
                            break;
                        case LIKE:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeStringOperator(OPERATOR_LIKE, field, values)));
                            break;
                        case STARTS_WITH:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeStringOperator(OPERATOR_STARTS_WITH, field, values)));
                            break;
                        case ENDS_WITH:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeStringOperator(OPERATOR_ENDS_WITH, field, values)));
                            break;
                        case CONTAINS:
                            serialized.add(newArray(newString(, SelectOptions.FILTER), serializeStringOperator(OPERATOR_CONTAINS, field, values)));
                            break;
                    }
                    break;
            }
        }

        return parseSpaceFlux(storage.immutable().call(SPACE_FIND, newArray(spaceName, newArray(serialized))));
    }

    private ImmutableArrayValue serializeModelOperator(String operator, MetaField<MetaModel, ?> field, List<Object> values) {
        return newArray(newString(operator), newInteger(field.index()), serializeFilterValues(spaceMeta, values));
    }

    private ImmutableArrayValue serializeStringOperator(String operator, MetaField<MetaModel, ?> field, List<Object> values) {
        return newArray(newString(operator), newInteger(field.index()), serializeFilterValues(STRING, values));
    }

    private ImmutableArrayValue serializeNumberOperator(String operator, MetaField<MetaModel, ?> field, List<Object> values) {
        return newArray(newString(operator), newInteger(field.index()), serializeFilterValues(LONG, values));
    }

    private ImmutableArrayValue serializeFilterValues(MetaType<?> type, List<Object> values) {
        return newArray(values
                .stream()
                .map(value -> serializeValue(type, value)).collect(listCollector()));
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return writer.write(type, value);
    }

    private Flux<ModelType> parseSpaceFlux(Mono<Value> value) {
        return value.flatMapMany(elements -> fromStream(elements.asArrayValue()
                .list()
                .stream()
                .map(element -> reader.read(spaceMeta, element))));
    }
}
