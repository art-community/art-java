package io.art.storage.sorter.model;

import io.art.core.annotation.*;
import io.art.storage.sorter.implementation.*;

@Public
public interface Sorter<Type, FieldType> {
    SorterImplementation<Type, FieldType> ascendant();

    SorterImplementation<Type, FieldType> descendant();
}
