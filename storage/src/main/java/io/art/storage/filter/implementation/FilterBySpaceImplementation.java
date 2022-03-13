
package io.art.storage.filter.implementation;

import io.art.core.model.*;
import io.art.meta.model.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.filter.model.*;
import io.art.storage.index.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.storage.constants.StorageConstants.FilterExpressionType.*;

public class FilterBySpaceImplementation<Current, Other> implements FilterBySpace<Current, Other> {
    @Getter
    private FilterExpressionType expressionType;

    @Getter
    private MetaClass<Other> mappingSpace;

    @Getter
    private MetaField<? extends MetaClass<Current>, ?> mappingKeyField;

    @Getter
    private Index mappingIndex;

    @Getter
    private Tuple mappingIndexTuple;

    @Getter
    private MetaField<? extends MetaClass<Current>, ?> currentField;

    @Getter
    private MetaField<? extends MetaClass<Other>, ?> otherField;

    @Getter
    private final FilterBySpaceUseFieldsImplementation<Current, Other, ?> bySpaceUseFields;

    @Getter
    private final FilterBySpaceUseStringFieldsImplementation<Current, Other> bySpaceUseStringFields;

    @Getter
    private final FilterBySpaceUseNumberFieldsImplementation<Current, Other> bySpaceUseNumberFields;

    @Getter
    private final FilterBySpaceUseNumbersImplementation<Current> bySpaceUseNumbers;

    @Getter
    private final FilterBySpaceUseStringsImplementation<Current> bySpaceUseStrings;

    @Getter
    private final FilterBySpaceUseValuesImplementation<Current, ?> bySpaceUseValues;

    public FilterBySpaceImplementation(FilterRule<Current> rule) {
        bySpaceUseFields = new FilterBySpaceUseFieldsImplementation<>(rule);
        bySpaceUseStringFields = new FilterBySpaceUseStringFieldsImplementation<>(rule);
        bySpaceUseNumberFields = new FilterBySpaceUseNumberFieldsImplementation<>(rule);
        bySpaceUseNumbers = new FilterBySpaceUseNumbersImplementation<>(rule);
        bySpaceUseStrings = new FilterBySpaceUseStringsImplementation<>(rule);
        bySpaceUseValues = new FilterBySpaceUseValuesImplementation<>(rule);
    }

    FilterBySpaceImplementation<Current, Other> bySpace(MetaClass<Other> mappingSpace, MetaField<? extends MetaClass<Current>, ?> mappingField) {
        this.mappingSpace = mappingSpace;
        mappingKeyField = mappingField;
        return this;
    }

    final FilterBySpaceImplementation<Current, Other> byIndex(Index index, Tuple tuple) {
        mappingIndex = index;
        mappingIndexTuple = tuple;
        return this;
    }

    @Override
    public <FieldType> FilterBySpaceUseFields<Current, Other, FieldType> currentField(MetaField<? extends MetaClass<Current>, FieldType> currentField) {
        expressionType = FIELD;
        this.currentField = currentField;
        return cast(bySpaceUseFields);
    }

    @Override
    public FilterBySpaceUseStringFields<Current, Other> currentString(MetaField<? extends MetaClass<Current>, String> currentField) {
        expressionType = STRING_FIELD;
        this.currentField = currentField;
        return bySpaceUseStringFields;
    }

    @Override
    public FilterBySpaceUseNumberFields<Current, Other> currentNumber(MetaField<? extends MetaClass<Current>, ? extends Number> currentField) {
        expressionType = NUMBER_FIELD;
        this.currentField = currentField;
        return bySpaceUseNumberFields;
    }

    @Override
    public FilterBySpaceUseNumbers<Current> otherNumber(MetaField<? extends MetaClass<Other>, ? extends Number> otherField) {
        expressionType = NUMBER_VALUE;
        this.otherField = otherField;
        return bySpaceUseNumbers;
    }

    @Override
    public FilterBySpaceUseStrings<Current> otherString(MetaField<? extends MetaClass<Other>, String> otherField) {
        expressionType = STRING_VALUE;
        this.otherField = otherField;
        return bySpaceUseStrings;
    }

    @Override
    public <FieldType> FilterBySpaceUseValues<Current, FieldType> otherField(MetaField<? extends MetaClass<Other>, FieldType> otherField) {
        expressionType = VALUE;
        this.otherField = otherField;
        return cast(bySpaceUseValues);
    }
}
