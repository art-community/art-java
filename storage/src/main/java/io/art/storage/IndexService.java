package io.art.storage;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import java.util.*;

@Public
public interface IndexService<KeyType, ValueType> {
    ValueType findFirst(KeyType key);

    ImmutableArray<ValueType> findAll(KeyType... keys);

    ImmutableArray<ValueType> findAll(Collection<KeyType> keys);

    ImmutableArray<ValueType> findAll(ImmutableCollection<KeyType> keys);

    long count();

    ReactiveIndexService<KeyType, ValueType> reactive();
}
