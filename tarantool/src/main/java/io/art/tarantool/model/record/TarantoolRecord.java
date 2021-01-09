package io.art.tarantool.model.record;

import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TarantoolRecord<T>{
    //Non-blocking methods:
    TarantoolTransactionDependency useResult();

    TarantoolTransactionDependency useResultField(String fieldName);

    boolean isDone();

    CompletableFuture<Optional<T>> getFuture();


    //Blocking methods:
    Optional<T> getOptional();

    T get();

    T orElse(T other);

    T orElseThrow();

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    <U> U map(Function<Optional<T>, U> mapper);

    TarantoolRecord<T> synchronize();

    boolean isPresent();

    boolean isEmpty();

}
