package io.art.storage;

import io.art.communicator.*;
import io.art.core.annotation.*;

@Public
public interface Space extends Communicator {
    default <T, K> T get(K key) {
        return null;
    }

    default <T, K> T insert(T data) {
        return null;
    }

    default <T, K> T put(T data) {
        return null;
    }

    default <T, K> T delete(K key) {
        return null;
    }

    default void truncate() {

    }

    default Long count() {
        return null;
    }
}
