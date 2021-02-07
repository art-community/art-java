package io.art.tarantool.space;

import io.art.core.collection.*;
import io.art.storage.space.*;
import io.art.tarantool.model.operation.*;
import io.art.tarantool.model.record.*;
import io.art.tarantool.model.transaction.dependency.*;
import io.art.value.immutable.*;
import java.util.*;
import io.art.tarantool.space.TarantoolSpaceImplementation.SelectRequest;

public interface TarantoolSpace<T, K> extends Space<T, K> {

    TarantoolRecord<T> get(K key);
    TarantoolRecord<T> get(TarantoolTransactionDependency keyDependency);

    TarantoolRecord<T> get(String index, Value key);
    TarantoolRecord<T> get(String index, TarantoolTransactionDependency keyDependency);

    TarantoolRecord<ImmutableArray<T>> getAll();

    SelectRequest select(Value request);
    SelectRequest select(TarantoolTransactionDependency requestDependency);

    TarantoolRecord<T> delete(K key);
    TarantoolRecord<T> delete(TarantoolTransactionDependency keyDependency);

    TarantoolRecord<T> insert(T data);
    TarantoolRecord<T> insert(TarantoolTransactionDependency dataDependency);

    TarantoolRecord<T> put(T data);
    TarantoolRecord<T> put(TarantoolTransactionDependency dataDependency);

    TarantoolRecord<T> replace(T data);
    TarantoolRecord<T> replace(TarantoolTransactionDependency dataDependency);

    TarantoolRecord<T> update(K key, TarantoolUpdateFieldOperation... operations);
    TarantoolRecord<T> update(TarantoolTransactionDependency keyDependency, TarantoolUpdateFieldOperation... operations);

    TarantoolRecord<T> upsert(T defaultData, TarantoolUpdateFieldOperation... operations);
    TarantoolRecord<T> upsert(TarantoolTransactionDependency defaultDataDependency, TarantoolUpdateFieldOperation... operations);

    TarantoolRecord<Long> count();
    TarantoolRecord<Long> len();

    void truncate();

    TarantoolRecord<Set<String>> listIndices();

    Long bucketOf(T data);

    void beginTransaction();

    void beginTransaction(Long bucketId);

    void commitTransaction();

    void cancelTransaction();

}
