
package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static java.util.Arrays.*;
import static lombok.AccessLevel.*;
import java.util.*;

@RequiredArgsConstructor(access = PACKAGE)
public class FilterBySpace<Current, Other> {
    private final FilterRule<Current> rule;
    @Getter
    private final MetaClass<Other> mappingSpace;
    @Getter
    private MetaField<? extends MetaClass<Current>, ?> mappingKeyField;
    @Getter
    private List<MetaField<? extends MetaClass<Current>, ?>> mappingIndexedFields = linkedList();

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
        return new FilterBySpaceUseFields<>(rule, currentField);
    }

    public FilterBySpaceUseStringFields<Current, Other> currentString(MetaField<? extends MetaClass<Current>, String> currentField) {
        return new FilterBySpaceUseStringFields<>(rule, currentField);
    }

    public FilterBySpaceUseNumberFields<Current, Other> currentNumber(MetaField<? extends MetaClass<Current>, ? extends Number> currentField) {
        return new FilterBySpaceUseNumberFields<>(rule, currentField);
    }

    public FilterBySpaceUseNumbers<Current, Other> otherNumber(MetaField<? extends MetaClass<Other>, ? extends Number> otherField) {
        return new FilterBySpaceUseNumbers<>(rule, otherField);
    }

    public FilterBySpaceUseStrings<Current, Other> otherString(MetaField<? extends MetaClass<Other>, String> otherField) {
        return new FilterBySpaceUseStrings<>(rule, otherField);
    }

    public <FieldType> FilterBySpaceUseValues<Current, Other, FieldType> otherField(MetaField<? extends MetaClass<Other>, FieldType> otherField) {
        return new FilterBySpaceUseValues<>(rule, otherField);
    }
}
