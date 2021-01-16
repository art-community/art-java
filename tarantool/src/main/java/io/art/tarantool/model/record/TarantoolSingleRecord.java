package io.art.tarantool.model.record;

import io.art.core.checker.EmptinessChecker;
import io.art.storage.record.StorageRecord;
import io.art.tarantool.exception.TarantoolDaoException;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;
import io.art.tarantool.exception.TarantoolTransactionException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

import static io.art.core.caster.Caster.cast;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;


public class TarantoolSingleRecord<T> implements TarantoolRecord<T> {
    private final CompletableFuture<Optional<T>> futureResult;

    public TarantoolSingleRecord(CompletableFuture<List<?>> futureResult, Function<List<?>, Optional<?>> responseMapper){
        this.futureResult = cast(futureResult.thenApply(responseMapper));
    }

    private TarantoolSingleRecord(CompletableFuture<Optional<T>> futureResult){
        this.futureResult = futureResult;
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
    public <U> TarantoolSingleRecord<U> thenApply(Function<Optional<T>, Optional<U>> mapper){
        return new TarantoolSingleRecord<U>(futureResult.thenApply(mapper));
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
        return getOptional().orElseThrow(() -> new NoSuchElementException("No value present"));
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
        return EmptinessChecker.isNotEmpty(getOptional());
    }

    @Override
    public boolean isEmpty() {
        return EmptinessChecker.isEmpty(getOptional());
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
