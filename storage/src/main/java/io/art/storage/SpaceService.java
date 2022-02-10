package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import java.util.*;

@Public
public interface SpaceService<KeyType, ValueType> {
    ValueType findFirst(KeyType key);

    ImmutableArray<ValueType> findAll(KeyType... keys);

    ImmutableArray<ValueType> findAll(Collection<KeyType> keys);

    ImmutableArray<ValueType> findAll(ImmutableCollection<KeyType> keys);

    ValueType delete(KeyType key);

    ImmutableArray<ValueType> delete(KeyType... keys);

    ImmutableArray<ValueType> delete(Collection<KeyType> keys);

    ImmutableArray<ValueType> delete(ImmutableCollection<KeyType> keys);

    long count();

    void truncate();

    ValueType insert(ValueType value);

    ImmutableArray<ValueType> insert(ValueType... value);

    ImmutableArray<ValueType> insert(Collection<ValueType> value);

    ImmutableArray<ValueType> insert(ImmutableCollection<ValueType> value);

    ValueType put(ValueType value);

    ImmutableArray<ValueType> put(ValueType... value);

    ImmutableArray<ValueType> put(Collection<ValueType> value);

    ImmutableArray<ValueType> put(ImmutableCollection<ValueType> value);

    ValueType replace(ValueType value);

    ImmutableArray<ValueType> replace(ValueType... value);

    ImmutableArray<ValueType> replace(Collection<ValueType> value);

    ImmutableArray<ValueType> replace(ImmutableCollection<ValueType> value);

    ReactiveSpaceService<KeyType, ValueType> reactive();
}
