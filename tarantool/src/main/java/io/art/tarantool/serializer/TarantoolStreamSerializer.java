package io.art.tarantool.serializer;

import io.art.core.exception.*;
import io.art.core.factory.*;
import io.art.meta.model.*;
import io.art.storage.constants.*;
import io.art.storage.constants.StorageConstants.*;
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
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.registry.BuiltinMetaTypes.*;
import static io.art.storage.constants.StorageConstants.FilterCondition.*;
import static io.art.storage.constants.StorageConstants.FilterMode.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.factory.TarantoolNameFactory.*;
import static java.util.Collections.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

@RequiredArgsConstructor
public class TarantoolStreamSerializer {
    private final TarantoolModelWriter writer;
    private final static ProcessingFunctions processingFunctions = STREAM_PROTOCOL.processingFunctions;
    private final static Comparators comparators = STREAM_PROTOCOL.comparators;
    private final static Conditions conditions = STREAM_PROTOCOL.conditions;
    private final static FilterModes filterModes = STREAM_PROTOCOL.filterModes;
    private final static MappingModes mappingModes = STREAM_PROTOCOL.mappingModes;
    private final static Filters filters = STREAM_PROTOCOL.filters;
    private final static FilterExpressions filterExpressions = STREAM_PROTOCOL.filterExpressions;

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

    public ImmutableArrayValue serializeFilter(List<FilterImplementation.FilterPart> parts) {
        List<ImmutableValue> serialized = linkedList();
        Map<FilterExpressionCacheKey, List<ImmutableValue>> expressionsCache = map();
        for (FilterImplementation.FilterPart part : parts) {
            FilterMode mode = part.getMode();
            if (mode != SPACE && mode != INDEX) continue;
            serializeFilterSpaceExpressions(expressionsCache, part);
        }
        for (FilterImplementation.FilterPart part : parts) {
            serialized.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
            switch (part.getMode()) {
                case NESTED:
                    serialized.add(filterModes.nestedFilter);
                    serialized.add(serializeFilter(part.getNested().getParts()));
                    break;
                case FIELD:
                    serialized.add(filterModes.filterByField);
                    FilterByFieldImplementation<?, ?> byField = part.getByField();
                    MetaField<? extends MetaClass<?>, ?> field = byField.getField();
                    ImmutableIntegerValue index = newInteger(field.index() + 1);
                    ImmutableIntegerValue operator = filters.filtersMapping.get(byField.getOperator());
                    List<Object> values = byField.getValues();
                    serialized.add(newArray(index, operator, serializeValues(field.type(), values)));
                    break;
                case FUNCTION:
                    serialized.add(filterModes.filterByFunction);
                    serialized.add(newString(part.getByFunction().getFunction().name()));
                    break;
                case SPACE:
                    serialized.add(filterModes.filterBySpace);
                    FilterBySpaceImplementation<?, ?> bySpace = part.getBySpace();
                    FilterExpressionCacheKey expressionKey = new FilterExpressionCacheKey(bySpace.getMappingSpace(), bySpace.getMappingKeyField(), emptyList());
                    serialized.add(newArray(spaceName(bySpace.getMappingSpace()), newInteger(bySpace.getMappingKeyField().index() + 1)));
                    serialized.add(newArray(expressionsCache.get(expressionKey)));
                    break;
                case INDEX:
                    serialized.add(filterModes.filterByIndex);
                    FilterBySpaceImplementation<?, ?> byIndex = part.getByIndex();
                    expressionKey = new FilterExpressionCacheKey(byIndex.getMappingSpace(), null, cast(byIndex.getMappingIndexedFields()));
                    ImmutableArrayValue indexedFields = newArray(byIndex.getMappingIndexedFields()
                            .stream()
                            .map(indexedField -> serializeValue(integerType(), indexedField.index() + 1))
                            .collect(listCollector())
                    );
                    serialized.add(newArray(spaceName(byIndex.getMappingSpace()), indexedFields));
                    serialized.add(newArray(expressionsCache.get(expressionKey)));
                    break;
            }
        }
        return newArray(serialized);
    }

    private void serializeFilterSpaceExpressions(Map<FilterExpressionCacheKey, List<ImmutableValue>> expressionsCache, FilterImplementation.FilterPart part) {
        FilterBySpaceImplementation<?, ?> bySpace = part.getBySpace();
        FilterMode mode = part.getMode();
        FilterExpressionCacheKey key = mode == SPACE
                ? new FilterExpressionCacheKey(bySpace.getMappingSpace(), bySpace.getMappingKeyField(), emptyList())
                : new FilterExpressionCacheKey(bySpace.getMappingSpace(), null, cast(bySpace.getMappingIndexedFields()));
        List<ImmutableValue> expressions = computeIfAbsent(expressionsCache, key, ListFactory::linkedList);
        switch (bySpace.getExpressionType()) {
            case FIELD:
                FilterBySpaceUseFieldsImplementation<?, ?, ?> bySpaceUseFields = bySpace.getBySpaceUseFields();
                ImmutableArrayValue expressionFields = newArray(bySpaceUseFields.getFields()
                        .stream()
                        .map(field -> serializeValue(integerType(), field.index() + 1))
                        .collect(listCollector())
                );
                expressions.add(filterExpressions.filterExpressionField);
                expressions.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expressions.add(filters.filtersMapping.get(bySpaceUseFields.getOperator()));
                expressions.add(newInteger(bySpace.getCurrentField().index() + 1));
                expressions.add(expressionFields);
                break;
            case STRING_FIELD:
                FilterBySpaceUseStringFieldsImplementation<?, ?> bySpaceUseStringFields = bySpace.getBySpaceUseStringFields();
                expressionFields = newArray(bySpaceUseStringFields.getFields()
                        .stream()
                        .map(field -> serializeValue(integerType(), field.index() + 1))
                        .collect(listCollector())
                );
                expressions.add(filterExpressions.filterExpressionField);
                expressions.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expressions.add(filters.filtersMapping.get(bySpaceUseStringFields.getOperator()));
                expressions.add(newInteger(bySpace.getCurrentField().index() + 1));
                expressions.add(expressionFields);
                break;
            case NUMBER_FIELD:
                FilterBySpaceUseNumberFieldsImplementation<?, ?> bySpaceUseNumberFields = bySpace.getBySpaceUseNumberFields();
                expressionFields = newArray(bySpaceUseNumberFields.getFields()
                        .stream()
                        .map(field -> serializeValue(integerType(), field.index() + 1))
                        .collect(listCollector())
                );
                expressions.add(filterExpressions.filterExpressionField);
                expressions.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expressions.add(filters.filtersMapping.get(bySpaceUseNumberFields.getOperator()));
                expressions.add(newInteger(bySpace.getCurrentField().index() + 1));
                expressions.add(expressionFields);
                break;
            case VALUE:
                FilterBySpaceUseValuesImplementation<?, ?> bySpaceUseValues = bySpace.getBySpaceUseValues();
                ImmutableArrayValue expressionValues = serializeValues(bySpace.getOtherField().type(), cast(bySpaceUseValues.getValues()));
                expressions.add(filterExpressions.filterExpressionValue);
                expressions.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expressions.add(filters.filtersMapping.get(bySpaceUseValues.getOperator()));
                expressions.add(newInteger(bySpace.getOtherField().index() + 1));
                expressions.add(expressionValues);
                break;
            case STRING_VALUE:
                FilterBySpaceUseStringsImplementation<?> bySpaceUseStrings = bySpace.getBySpaceUseStrings();
                expressionValues = serializeValues(bySpace.getOtherField().type(), cast(bySpaceUseStrings.getValues()));
                expressions.add(filterExpressions.filterExpressionValue);
                expressions.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expressions.add(filters.filtersMapping.get(bySpaceUseStrings.getOperator()));
                expressions.add(newInteger(bySpace.getOtherField().index() + 1));
                expressions.add(expressionValues);
                break;
            case NUMBER_VALUE:
                FilterBySpaceUseNumbersImplementation<?> bySpaceUseNumbers = bySpace.getBySpaceUseNumbers();
                expressionValues = serializeValues(bySpace.getOtherField().type(), cast(bySpaceUseNumbers.getValues()));
                expressions.add(filterExpressions.filterExpressionValue);
                expressions.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expressions.add(filters.filtersMapping.get(bySpaceUseNumbers.getOperator()));
                expressions.add(newInteger(bySpace.getOtherField().index() + 1));
                expressions.add(expressionValues);
                break;
        }
    }

    private ImmutableArrayValue serializeMap(Mapper<?, ?> mapper) {
        List<ImmutableValue> serialized = linkedList();
        switch (mapper.getMode()) {
            case FUNCTION:
                serialized.add(mappingModes.mapByFunction);
                serialized.add(newString(mapper.getByFunction().getFunction().name()));
                break;
            case FIELD:
                serialized.add(mappingModes.mapByField);
                serialized.add(newInteger(mapper.getByField().getField().index() + 1));
                break;
            case SPACE:
                serialized.add(mappingModes.mapBySpace);
                MapperBySpace<?, ?> bySpace = mapper.getBySpace();
                serialized.add(spaceName(bySpace.getMappingSpace()));
                serialized.add(newInteger(bySpace.getMappingKeyField().index() + 1));
                break;
            case INDEX:
                serialized.add(mappingModes.mapByIndex);
                MapperBySpace<?, ?> byIndex = mapper.getByIndex();
                serialized.add(spaceName(byIndex.getMappingSpace()));
                ImmutableArrayValue indexedFields = newArray(byIndex.getMappingIndexedFields()
                        .stream()
                        .map(indexedField -> serializeValue(integerType(), indexedField.index() + 1))
                        .collect(listCollector())
                );
                serialized.add(indexedFields);
                break;
        }
        return newArray(serialized);
    }


    private ImmutableArrayValue serializeValues(MetaType<?> type, List<Object> values) {
        return newArray(values.stream().map(value -> serializeValue(type, value)).collect(listCollector()));
    }

    private Value serializeValue(MetaType<?> type, Object value) {
        return writer.write(type, value);
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    private static class FilterExpressionCacheKey {
        MetaClass<?> spaceClass;
        MetaField<?, ?> mappingKeyField;
        List<MetaField<?, ?>> mappingIndexedFields;
    }
}
