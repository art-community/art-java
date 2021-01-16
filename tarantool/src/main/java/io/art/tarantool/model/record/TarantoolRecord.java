package io.art.tarantool.model.record;

import io.art.storage.record.StorageRecord;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;

import java.util.Optional;
import java.util.function.Function;

public interface TarantoolRecord<T> extends StorageRecord<T> {

    TarantoolTransactionDependency useResult();

    TarantoolTransactionDependency useResultField(String fieldName);

    @Override
    <U> TarantoolRecord<U> thenApply(Function<Optional<T>, Optional<U>> mapper);

    @Override
    TarantoolRecord<T> synchronize();
}
