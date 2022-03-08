package io.art.storage.sorter.implementation;

import io.art.meta.model.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.sorter.model.*;
import lombok.*;
import static io.art.storage.constants.StorageConstants.SortComparator.*;
import static io.art.storage.constants.StorageConstants.SortOrder.*;

@Getter
@RequiredArgsConstructor
public class SorterImplementation<Type, FieldType> implements Sorter<Type, FieldType> {
    private final MetaField<? extends MetaClass<Type>, FieldType> field;
    private SortOrder order = ASCENDANT;
    private SortComparator comparator = MORE;

    @Override
    public SorterImplementation<Type, FieldType> ascendant() {
        order = ASCENDANT;
        return this;
    }

    @Override
    public SorterImplementation<Type, FieldType> descendant() {
        order = DESCENDANT;
        return this;
    }

    @Override
    public SorterImplementation<Type, FieldType> more() {
        comparator = MORE;
        return this;
    }

    @Override
    public SorterImplementation<Type, FieldType> less() {
        comparator = LESS;
        return this;
    }
}
