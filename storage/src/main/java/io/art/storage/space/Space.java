package io.art.storage.space;

import io.art.core.collection.ImmutableArray;
import io.art.storage.record.Record;
import io.art.value.immutable.*;
import java.util.*;

public interface Space<T, K> {

    Record<T> get(K key);

    Record<ImmutableArray<T>> getAll();

    Record<T> insert(T data);

    Record<T> put(T data);

    Record<T> delete(K key);

    void truncate();

    Record<Long> count();

}
