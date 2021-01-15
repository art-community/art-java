package io.art.storage.space;

import io.art.core.collection.ImmutableArray;
import io.art.value.immutable.*;
import java.util.*;

public interface StorageSpace<T> {

    Optional<T> get(Value key);

    ImmutableArray<T> find(Value request);

    Optional<T> insert(Value data);

    Optional<T> put(Value data);

    Optional<T> delete(Value key);

    void truncate();

    Optional<Long> count();

}
