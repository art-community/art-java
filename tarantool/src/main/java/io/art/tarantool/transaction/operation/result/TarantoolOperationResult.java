package io.art.tarantool.transaction.operation.result;

import io.art.tarantool.transaction.operation.dependency.TarantoolTransactionDependency;

import java.util.concurrent.ExecutionException;

public interface TarantoolOperationResult <T>{
    T get() throws ExecutionException, InterruptedException;

    TarantoolTransactionDependency useResult();

    TarantoolTransactionDependency useResultField(String fieldName);

    boolean isDone();

}
