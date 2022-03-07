package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.storage.StorageConstants.FilterCondition.*;

@Getter
@RequiredArgsConstructor
public class Filter<Type> {
    public <FieldType> FilterByValue<Type> byField(MetaField<? extends MetaClass<Type>, FieldType> field) {
        return new FilterByValue<>(field, AND);
    }

    public <FieldType> FilterByValue<Type> bySpace(MetaField<? extends MetaClass<Type>, FieldType> field) {
        return new FilterByValue<>(field, AND);
    }
}

