package io.art.storage;

import io.art.meta.model.*;
import io.art.storage.StorageConstants.*;
import lombok.*;
import static io.art.storage.StorageConstants.SortComparator.*;
import static io.art.storage.StorageConstants.SortOrder.*;

@Getter
@RequiredArgsConstructor
public class Sorter<Type, FieldType> {
    private final MetaField<? extends MetaClass<Type>, FieldType> field;
    private SortOrder order = ASCENDANT;
    private SortComparator comparator = MORE;

    public Sorter<Type, FieldType> ascendant() {
        order = ASCENDANT;
        return this;
    }

    public Sorter<Type, FieldType> descendant() {
        order = DESCENDANT;
        return this;
    }

    public Sorter<Type, FieldType> more() {
        comparator = MORE;
        return this;
    }

    public Sorter<Type, FieldType> less() {
        comparator = LESS;
        return this;
    }
}
