package io.art.tarantool.serializer;

import io.art.core.exception.*;
import io.art.core.factory.*;
import io.art.meta.model.*;
import io.art.storage.constants.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.index.*;
import io.art.storage.mapper.*;
import io.art.storage.model.*;
import io.art.storage.sorter.implementation.*;
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
                    MetaField<?, ?> distinctField = cast(operator.getValue());
                    serialized.add(newArray(processingFunctions.processingDistinct, serializeValue(integerPrimitiveType(), distinctField.index() + 1)));
                    break;
                case SORT:
                    serialized.add(newArray(processingFunctions.processingSort, serializeSort(cast(operator.getValue()))));
                    break;
                case FILTER:
                    FilterImplementation<?> filter = cast(operator.getValue());
                    serialized.add(newArray(processingFunctions.processingFilter, serializeFilter(filter.getParts())));
                    break;
                case MAP:
                    serialized.add(newArray(processingFunctions.processingMap, serializeMap(cast(operator.getValue()))));
                    break;
            }
        }
        return serialized;
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
            List<ImmutableValue> serializedPart = linkedList();
            serializedPart.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
            switch (part.getMode()) {
                case NESTED:
                    serializedPart.add(filterModes.nestedFilter);
                    serializedPart.add(serializeFilter(part.getNested().getParts()));
                    break;
                case FIELD:
                    serializedPart.add(filterModes.filterByField);
                    FilterByFieldImplementation<?, ?> byField = part.getByField();
                    MetaField<? extends MetaClass<?>, ?> field = byField.getField();
                    ImmutableIntegerValue index = newInteger(field.index() + 1);
                    ImmutableIntegerValue operator = filters.filtersMapping.get(byField.getOperator());
                    List<Object> values = byField.getValues();
                    serializedPart.add(newArray(index, operator, serializeValues(field.type(), values)));
                    break;
                case FUNCTION:
                    serializedPart.add(filterModes.filterByFunction);
                    serializedPart.add(newString(part.getByFunction().getFunction().name()));
                    break;
                case SPACE:
                    serializedPart.add(filterModes.filterBySpace);
                    FilterBySpaceImplementation<?, ?> bySpace = part.getBySpace();
                    ImmutableStringValue spaceName = spaceName(bySpace.getMappingSpace());
                    FilterExpressionCacheKey expressionKey = new FilterExpressionCacheKey(spaceName, bySpace.getMappingKeyField(), emptyList());
                    serializedPart.add(newArray(spaceName(bySpace.getMappingSpace()), newInteger(bySpace.getMappingKeyField().index() + 1)));
                    serializedPart.add(newArray(expressionsCache.get(expressionKey)));
                    break;
                case INDEX:
                    FilterBySpaceImplementation<?, ?> byIndex = part.getByIndex();
                    Index mappingIndex = byIndex.getMappingIndex();
                    List<Object> mappingFields = byIndex.getMappingIndexTuple().values();
                    serializedPart.add(filterModes.filterByIndex);
                    spaceName = spaceName(mappingIndex.owner());
                    expressionKey = new FilterExpressionCacheKey(spaceName, null, cast(mappingFields));
                    ImmutableArrayValue indexedFields = newArray(byIndex.getMappingIndexTuple()
                            .values()
                            .stream()
                            .map(indexedField -> serializeValue(integerType(), ((MetaField<?, ?>) indexedField).index() + 1))
                            .collect(listCollector())
                    );
                    serializedPart.add(newArray(spaceName, indexedFields, newString(mappingIndex.name())));
                    serializedPart.add(newArray(expressionsCache.get(expressionKey)));
                    break;
            }
            serialized.add(newArray(serializedPart));
        }
        return newArray(serialized);
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

    private void serializeFilterSpaceExpressions(Map<FilterExpressionCacheKey, List<ImmutableValue>> expressionsCache, FilterImplementation.FilterPart part) {
        FilterMode mode = part.getMode();
        FilterBySpaceImplementation<?, ?> bySpace = mode == SPACE ? part.getBySpace() : part.getByIndex();
        ImmutableStringValue spaceName = mode == SPACE
                ? spaceName(bySpace.getMappingSpace())
                : spaceName(bySpace.getMappingIndex().owner());
        FilterExpressionCacheKey key = mode == SPACE
                ? new FilterExpressionCacheKey(spaceName, bySpace.getMappingKeyField(), emptyList())
                : new FilterExpressionCacheKey(spaceName, null, cast(bySpace.getMappingIndexTuple().values()));
        List<ImmutableValue> expressions = computeIfAbsent(expressionsCache, key, ListFactory::linkedList);
        List<ImmutableValue> expresion = linkedList();
        switch (bySpace.getExpressionType()) {
            case FIELD:
                FilterBySpaceUseFieldsImplementation<?, ?, ?> bySpaceUseFields = bySpace.getBySpaceUseFields();
                ImmutableArrayValue expressionFields = newArray(bySpaceUseFields.getFields()
                        .stream()
                        .map(field -> serializeValue(integerType(), field.index() + 1))
                        .collect(listCollector())
                );
                expresion.add(filterExpressions.filterExpressionField);
                expresion.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expresion.add(filters.filtersMapping.get(bySpaceUseFields.getOperator()));
                expresion.add(newInteger(bySpace.getCurrentField().index() + 1));
                expresion.add(expressionFields);
                break;
            case STRING_FIELD:
                FilterBySpaceUseStringFieldsImplementation<?, ?> bySpaceUseStringFields = bySpace.getBySpaceUseStringFields();
                expressionFields = newArray(bySpaceUseStringFields.getFields()
                        .stream()
                        .map(field -> serializeValue(integerType(), field.index() + 1))
                        .collect(listCollector())
                );
                expresion.add(filterExpressions.filterExpressionField);
                expresion.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expresion.add(filters.filtersMapping.get(bySpaceUseStringFields.getOperator()));
                expresion.add(newInteger(bySpace.getCurrentField().index() + 1));
                expresion.add(expressionFields);
                break;
            case NUMBER_FIELD:
                FilterBySpaceUseNumberFieldsImplementation<?, ?> bySpaceUseNumberFields = bySpace.getBySpaceUseNumberFields();
                expressionFields = newArray(bySpaceUseNumberFields.getFields()
                        .stream()
                        .map(field -> serializeValue(integerType(), field.index() + 1))
                        .collect(listCollector())
                );
                expresion.add(filterExpressions.filterExpressionField);
                expresion.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expresion.add(filters.filtersMapping.get(bySpaceUseNumberFields.getOperator()));
                expresion.add(newInteger(bySpace.getCurrentField().index() + 1));
                expresion.add(expressionFields);
                break;
            case VALUE:
                FilterBySpaceUseValuesImplementation<?, ?> bySpaceUseValues = bySpace.getBySpaceUseValues();
                ImmutableArrayValue expressionValues = serializeValues(bySpace.getOtherField().type(), cast(bySpaceUseValues.getValues()));
                expresion.add(filterExpressions.filterExpressionValue);
                expresion.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expresion.add(filters.filtersMapping.get(bySpaceUseValues.getOperator()));
                expresion.add(newInteger(bySpace.getOtherField().index() + 1));
                expresion.add(expressionValues);
                break;
            case STRING_VALUE:
                FilterBySpaceUseStringsImplementation<?> bySpaceUseStrings = bySpace.getBySpaceUseStrings();
                expressionValues = serializeValues(bySpace.getOtherField().type(), cast(bySpaceUseStrings.getValues()));
                expresion.add(filterExpressions.filterExpressionValue);
                expresion.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expresion.add(filters.filtersMapping.get(bySpaceUseStrings.getOperator()));
                expresion.add(newInteger(bySpace.getOtherField().index() + 1));
                expresion.add(expressionValues);
                break;
            case NUMBER_VALUE:
                FilterBySpaceUseNumbersImplementation<?> bySpaceUseNumbers = bySpace.getBySpaceUseNumbers();
                expressionValues = serializeValues(bySpace.getOtherField().type(), cast(bySpaceUseNumbers.getValues()));
                expresion.add(filterExpressions.filterExpressionValue);
                expresion.add(part.getCondition() == AND ? conditions.conditionAnd : conditions.conditionOr);
                expresion.add(filters.filtersMapping.get(bySpaceUseNumbers.getOperator()));
                expresion.add(newInteger(bySpace.getOtherField().index() + 1));
                expresion.add(expressionValues);
                break;
        }
        expressions.add(newArray(expresion));
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
                ImmutableArrayValue fields = newArray(byIndex.getMappingIndexTuple()
                        .values()
                        .stream()
                        .map(field -> serializeValue(integerType(), ((MetaField<?, ?>) field).index() + 1))
                        .collect(listCollector())
                );
                serialized.add(spaceName(byIndex.getMappingSpace()));
                serialized.add(fields);
                serialized.add(newString(byIndex.getMappingIndex().name()));
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
        ImmutableStringValue spaceName;
        MetaField<?, ?> mappingKeyField;
        List<MetaField<?, ?>> mappingIndexedFields;
    }
}
