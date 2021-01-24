package io.art.tarantool.model.record;

import io.art.storage.record.Record;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;

import java.util.Optional;
import java.util.function.Function;

public interface TarantoolRecord<T> extends Record<T> {

    TarantoolTransactionDependency useResult();

    TarantoolTransactionDependency useResultField(String fieldName);

    @Override
    <U> TarantoolRecord<U> thenApply(Function<Optional<T>, Optional<U>> mapper);

    @Override
    TarantoolRecord<T> synchronize();
}
