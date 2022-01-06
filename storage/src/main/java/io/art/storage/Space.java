package io.art.storage;

import io.art.core.annotation.*;

@Public
public interface Space<T, K> {
    T get(K key);

    T insert(T data);

    T put(T data);

    T delete(K key);

    void truncate();

    Long count();
}
