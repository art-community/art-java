package io.art.tarantool.model.record;

import io.art.storage.record.StorageRecord;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;

public interface TarantoolRecord<T> extends StorageRecord<T> {

    TarantoolTransactionDependency useResult();

    TarantoolTransactionDependency useResultField(String fieldName);

    @Override
    TarantoolRecord<T> synchronize();
}
