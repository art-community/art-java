
package io.art.storage.filter.implementation;

import io.art.meta.model.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.constants.StorageConstants.FilterExpressionType.*;
import static java.util.Arrays.*;
import static lombok.AccessLevel.*;
import java.util.*;

@RequiredArgsConstructor(access = PACKAGE)
public class FilterBySpaceImplementation<Current, Other> implements FilterBySpace<Current, Other> {
    private final FilterRule<Current> rule;

    @Getter
    private FilterExpressionType expressionType;

    @Getter
    private final MetaClass<Other> mappingSpace;

    @Getter
    private MetaField<? extends MetaClass<Current>, ?> mappingKeyField;

    @Getter
    private List<MetaField<? extends MetaClass<Current>, ?>> mappingIndexedFields = linkedList();

    @Getter
    private MetaField<? extends MetaClass<Current>, ?> currentField;

    @Getter
    private MetaField<? extends MetaClass<Other>, ?> otherField;

    @Getter
    private final FilterBySpaceUseFieldsImplementation<Current, Other, ?> bySpaceUseFields = new FilterBySpaceUseFieldsImplementation<>(rule);

    @Getter
    private final FilterBySpaceUseStringFieldsImplementation<Current, Other> bySpaceUseStringFields = new FilterBySpaceUseStringFieldsImplementation<>(rule);

    @Getter
    private final FilterBySpaceUseNumberFieldsImplementation<Current, Other> bySpaceUseNumberFields = new FilterBySpaceUseNumberFieldsImplementation<>(rule);

    @Getter
    private final FilterBySpaceUseNumbersImplementation<Current> bySpaceUseNumbers = new FilterBySpaceUseNumbersImplementation<>(rule);

    @Getter
    private final FilterBySpaceUseStringsImplementation<Current> bySpaceUseStrings = new FilterBySpaceUseStringsImplementation<>(rule);

    @Getter
    private final FilterBySpaceUseValuesImplementation<Current, ?> bySpaceUseValues = new FilterBySpaceUseValuesImplementation<>(rule);

    FilterBySpaceImplementation<Current, Other> bySpace(MetaField<? extends MetaClass<Current>, ?> mappingField) {
        mappingKeyField = mappingField;
        return this;
    }

    @SafeVarargs
    final FilterBySpaceImplementation<Current, Other> byIndex(MetaField<? extends MetaClass<Current>, ?>... indexedFields) {
        mappingIndexedFields = asList(indexedFields);
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
