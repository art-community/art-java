package io.art.tarantool.model.record;

import io.art.tarantool.exception.TarantoolDaoException;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;
import io.art.tarantool.exception.TarantoolTransactionException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.art.core.caster.Caster.cast;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;


public class TarantoolSingleRecord<T> implements TarantoolRecord<T> {
    private final CompletableFuture<Optional<T>> futureResult;

    public TarantoolSingleRecord(CompletableFuture<List<?>> futureResult, Function<List<?>, ?> responseMapper){
        this.futureResult = cast(futureResult.thenApply(responseMapper));
    }

    @Override
    public TarantoolTransactionDependency useResult(){
        throw new TarantoolTransactionException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
    }

    @Override
    public TarantoolTransactionDependency useResultField(String fieldName){
        throw new TarantoolTransactionException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
    }



    @Override
    public CompletableFuture<Optional<T>> getFuture() {
        return futureResult;
    }

    @Override
    public Optional<T> getOptional(){
        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TarantoolDaoException(UNABLE_TO_GET_RESPONSE, e);
        }
    }

    @Override
    public T get() {
        return getOptional().get();
    }

    @Override
    public T orElse(T other) {
        return getOptional().orElse(other);
    }

    @Override
    public T orElseThrow() {
        return getOptional().orElseThrow();
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return getOptional().orElseThrow(exceptionSupplier);
    }

    @Override
    public <U> U map(Function<Optional<T>, U> mapper) {
        return mapper.apply(getOptional());
    }



    @Override
    public boolean isPresent() {
        return getOptional().isPresent();
    }

    @Override
    public boolean isEmpty() {
        return getOptional().isEmpty();
    }

    @Override
    public boolean isDone() {
        return futureResult.isDone();
    }

    @Override
    public TarantoolSingleRecord<T> synchronize(){
        getOptional();
        return this;
    }

}
