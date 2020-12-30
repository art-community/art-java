package io.art.tarantool.model.record;

import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;

import java.util.Optional;
import java.util.function.Supplier;

public interface TarantoolRecord<T>{

    TarantoolTransactionDependency useResult();

    TarantoolTransactionDependency useResultField(String fieldName);

    boolean isDone();

    TarantoolRecord<T> synchronize();

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
