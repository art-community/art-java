package io.art.tarantool.storage;

import io.art.value.immutable.*;
import java.util.*;

public interface StorageSpace {

    Optional<Value> get(Value key);

    Optional<List<Value>> find(Value request);

    Optional<Value> insert(Value data);

    Optional<Value> autoIncrement(Value data);

    Optional<Value> put(Value data);

    Optional<Value> delete(Value key);

    void truncate();

    Optional<Long> count();

}
