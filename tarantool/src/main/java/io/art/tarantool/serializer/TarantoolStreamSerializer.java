package io.art.tarantool.serializer;

import io.art.core.exception.*;
import io.art.meta.model.*;
import io.art.storage.constants.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.mapper.*;
import io.art.storage.sorter.implementation.*;
import io.art.storage.stream.*;
import io.art.tarantool.descriptor.*;
import lombok.experimental.*;
import org.msgpack.value.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@UtilityClass
public class TarantoolStreamSerializer {
    public static List<Value> serializeStream(TarantoolModelWriter writer, List<ProcessingOperator> operators) {
        List<Value> serialized = linkedList();
        for (ProcessingOperator operator : operators) {
            switch (operator.getOperation()) {
                case LIMIT:
                    serialized.add(newArray(STREAM_PROTOCOL.processingFunctions.processingLimit, serializeValue(writer, longPrimitiveType(), operator.getValue())));
                    break;
                case OFFSET:
                    serialized.add(newArray(STREAM_PROTOCOL.processingFunctions.processingOffset, serializeValue(writer, longPrimitiveType(), operator.getValue())));
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

    private static ImmutableArrayValue serializeSort(SorterImplementation<?, ?> sorter) {
        StorageConstants.SortOrder order = sorter.getOrder();
        MetaField<?, ?> field = sorter.getField();
        switch (order) {
            case ASCENDANT:
                return newArray(STREAM_PROTOCOL.comparators.comparatorLess, newInteger(field.index() + 1));
            case DESCENDANT:
                return newArray(STREAM_PROTOCOL.comparators.comparatorMore, newInteger(field.index() + 1));
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


    private static ImmutableArrayValue serializeFilterValues(TarantoolModelWriter writer, MetaType<?> type, List<Object> values) {
        return newArray(values.stream().map(value -> serializeValue(writer, type, value)).collect(listCollector()));
    }

    private static Value serializeValue(TarantoolModelWriter writer, MetaType<?> type, Object value) {
        return writer.write(type, value);
    }
}
