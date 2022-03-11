package io.art.tarantool.serializer;

import io.art.core.exception.*;
import io.art.meta.model.*;
import io.art.storage.constants.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.mapper.*;
import io.art.storage.sorter.implementation.*;
import io.art.storage.stream.*;
import io.art.tarantool.constants.TarantoolModuleConstants.StreamProtocol.Comparators;
import io.art.tarantool.constants.TarantoolModuleConstants.StreamProtocol.*;
import io.art.tarantool.descriptor.*;
import lombok.*;
import org.msgpack.value.Value;
import org.msgpack.value.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@RequiredArgsConstructor
public class TarantoolStreamSerializer {
    private final TarantoolModelWriter writer;
    private final static ProcessingFunctions processingFunctions = STREAM_PROTOCOL.processingFunctions;
    private final static Comparators comparators = STREAM_PROTOCOL.comparators;

    public List<Value> serializeStream(List<ProcessingOperator> operators) {
        List<Value> serialized = linkedList();
        for (ProcessingOperator operator : operators) {
            switch (operator.getOperation()) {
                case LIMIT:
                    serialized.add(newArray(processingFunctions.processingLimit, serializeValue(longPrimitiveType(), operator.getValue())));
                    break;
                case OFFSET:
                    serialized.add(newArray(processingFunctions.processingOffset, serializeValue(longPrimitiveType(), operator.getValue())));
                    break;
                case DISTINCT:
                    serialized.add(newArray(processingFunctions.processingDistinct));
                    break;
                case SORT:
                    serialized.add(newArray(processingFunctions.processingSort, serializeSort(cast(operator.getValue()))));
                    break;
                case FILTER:
                    serialized.add(newArray(processingFunctions.processingFilter, serializeFilter(cast(operator.getValue()))));
                    break;
                case MAP:
                    serialized.add(newArray(processingFunctions.processingMap, serializeMap(cast(operator.getValue()))));
                    break;
            }
        }
        return serialized;
    }

    private static ImmutableArrayValue serializeSort(SorterImplementation<?, ?> sorter) {
        StorageConstants.SortOrder order = sorter.getOrder();
        MetaField<?, ?> field = sorter.getField();
        switch (order) {
            case ASCENDANT:
                return newArray(comparators.comparatorLess, newInteger(field.index() + 1));
            case DESCENDANT:
                return newArray(comparators.comparatorMore, newInteger(field.index() + 1));
        }
        throw new ImpossibleSituationException();
    }

    public static ImmutableArrayValue serializeFilter(FilterImplementation<?> filter) {
        List<ImmutableArrayValue> serialized = linkedList();
        List<FilterImplementation.FilterPart> parts = filter.getParts();
        for (FilterImplementation.FilterPart part : parts) {
            StorageConstants.FilterCondition condition = part.getCondition();
            switch (part.getMode()) {
                case FIELD:
                    part.getByField();
                    break;
                case FUNCTION:
                    FILTERBYFUNCTIONIMPLEMENTATION<?> byFunction = part.getByFunction();
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

    private static ImmutableArrayValue serializeMap(Mapper<?, ?> mapper) {
        List<ImmutableArrayValue> serialized = linkedList();
        switch (mapper.getMode()) {
            case FIELD:
                break;
            case FUNCTION:
                break;
            case SPACE:
                break;
            case INDEX:
                break;
        }
        return newArray(serialized);
    }


    private ImmutableArrayValue serializeFilterValues(MetaType<?> type, List<Object> values) {
        return newArray(values.stream().map(value -> serializeValue(type, value)).collect(listCollector()));
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return writer.write(type, value);
    }
}
