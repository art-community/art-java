
package io.art.storage;

import io.art.meta.model.*;
import io.art.storage.StorageConstants.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.FilterExpressionType.*;
import static java.util.Arrays.*;
import static lombok.AccessLevel.*;
import java.util.*;

@RequiredArgsConstructor(access = PACKAGE)
public class FilterBySpace<Current, Other> {
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
    private final FilterBySpaceUseFields<Current, Other, ?> bySpaceUseFields = new FilterBySpaceUseFields<>(rule);

    @Getter
    private final FilterBySpaceUseStringFields<Current, Other> bySpaceUseStringFields = new FilterBySpaceUseStringFields<>(rule);

    @Getter
    private final FilterBySpaceUseNumberFields<Current, Other> bySpaceUseNumberFields = new FilterBySpaceUseNumberFields<>(rule);

    @Getter
    private final FilterBySpaceUseNumbers<Current> bySpaceUseNumbers = new FilterBySpaceUseNumbers<>(rule);

    @Getter
    private final FilterBySpaceUseStrings<Current> bySpaceUseStrings = new FilterBySpaceUseStrings<>(rule);

    @Getter
    private final FilterBySpaceUseValues<Current, ?> bySpaceUseValues = new FilterBySpaceUseValues<>(rule);

    FilterBySpace<Current, Other> bySpace(MetaField<? extends MetaClass<Current>, ?> mappingField) {
        mappingKeyField = mappingField;
        return this;
    }

    @SafeVarargs
    final FilterBySpace<Current, Other> byIndex(MetaField<? extends MetaClass<Current>, ?>... indexedFields) {
        mappingIndexedFields = asList(indexedFields);
        return this;
    }

    public <FieldType> FilterBySpaceUseFields<Current, Other, FieldType> currentField(MetaField<? extends MetaClass<Current>, FieldType> currentField) {
        expressionType = FIELD;
        this.currentField = currentField;
        return cast(bySpaceUseFields);
    }

    public FilterBySpaceUseStringFields<Current, Other> currentString(MetaField<? extends MetaClass<Current>, String> currentField) {
        expressionType = FIELD;
        this.currentField = currentField;
        return bySpaceUseStringFields;
    }

    public FilterBySpaceUseNumberFields<Current, Other> currentNumber(MetaField<? extends MetaClass<Current>, ? extends Number> currentField) {
        expressionType = FIELD;
        this.currentField = currentField;
        return bySpaceUseNumberFields;
    }

    public FilterBySpaceUseNumbers<Current> otherNumber(MetaField<? extends MetaClass<Other>, ? extends Number> otherField) {
        expressionType = VALUE;
        this.otherField = otherField;
        return bySpaceUseNumbers;
    }

    public FilterBySpaceUseStrings<Current> otherString(MetaField<? extends MetaClass<Other>, String> otherField) {
        expressionType = VALUE;
        this.otherField = otherField;
        return bySpaceUseStrings;
    }

    public <FieldType> FilterBySpaceUseValues<Current, FieldType> otherField(MetaField<? extends MetaClass<Other>, FieldType> otherField) {
        expressionType = VALUE;
        this.otherField = otherField;
        return cast(bySpaceUseValues);
    }
}
