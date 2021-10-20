package io.art.tarantool.model.record;

import io.art.core.checker.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.transaction.dependency.*;
import static io.art.core.caster.Caster.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;


public class TarantoolSingleRecord<T> implements TarantoolRecord<T> {
    private final CompletableFuture<Optional<T>> futureResult;

    public TarantoolSingleRecord(CompletableFuture<List<?>> futureResult, Function<List<?>, Optional<?>> responseMapper) {
        this.futureResult = cast(futureResult.thenApply(responseMapper));
    }

    private TarantoolSingleRecord(CompletableFuture<Optional<T>> futureResult) {
        this.futureResult = futureResult;
    }

    @Override
    public TarantoolTransactionDependency useResult() {
        throw new TarantoolTransactionException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
    }

    @Override
    public TarantoolTransactionDependency useResultField(String fieldName) {
        throw new TarantoolTransactionException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
    }


    public CompletableFuture<Optional<T>> getFuture() {
        return futureResult;
    }

    @Override
    public <U> TarantoolSingleRecord<U> thenApply(Function<Optional<T>, Optional<U>> mapper) {
        return new TarantoolSingleRecord<U>(futureResult.thenApply(mapper));
    }

    public Optional<T> getOptional() {
        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TarantoolDaoException(UNABLE_TO_GET_RESPONSE, e);
        }
    }

    public T get() {
        return getOptional().get();
    }

    public T orElse(T other) {
        return getOptional().orElse(other);
    }

    public T orElseThrow() {
        return getOptional().orElseThrow(() -> new NoSuchElementException("No emit present"));
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return getOptional().orElseThrow(exceptionSupplier);
    }

    public <U> U map(Function<Optional<T>, U> mapper) {
        return mapper.apply(getOptional());
    }


    public boolean isPresent() {
        return EmptinessChecker.isNotEmpty(getOptional());
    }

    public boolean isEmpty() {
        return EmptinessChecker.isEmpty(getOptional());
    }

    public boolean isDone() {
        return futureResult.isDone();
    }

    @Override
    public TarantoolSingleRecord<T> synchronize() {
        getOptional();
        return this;
    }

}
