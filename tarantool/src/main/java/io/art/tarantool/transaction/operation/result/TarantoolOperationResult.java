package io.art.tarantool.transaction.operation.result;

import io.art.tarantool.transaction.operation.dependency.TarantoolTransactionDependency;

import java.util.Optional;
import java.util.function.Supplier;

public interface TarantoolOperationResult <T>{

    TarantoolTransactionDependency useResult();

    TarantoolTransactionDependency useResultField(String fieldName);

    boolean isDone();

    TarantoolOperationResult<T> synchronize();

    Optional<T> getOptional();


    /*
    //Optional delegates:
    */
    T get();

    boolean isPresent();

    boolean isEmpty();

    T orElse(T other);

    T orElseThrow();

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;
}
