package io.art.tarantool.model.record;

import io.art.tarantool.exception.TarantoolDaoException;
import io.art.tarantool.model.transaction.dependency.TarantoolTransactionDependency;
import io.art.tarantool.exception.TarantoolTransactionException;
import lombok.experimental.Delegate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static io.art.core.caster.Caster.cast;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;


public class TarantoolSingleRecord<T> implements TarantoolRecord<T> {
    private final CompletableFuture<Optional<T>> futureResult;

    public TarantoolSingleRecord(CompletableFuture<List<?>> futureResult, Function<List<?>, ?> responseMapper){
        this.futureResult = cast(futureResult.thenApply(responseMapper));
    }

    @Delegate
    public Optional<T> getOptional(){
        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TarantoolDaoException(UNABLE_TO_GET_RESPONSE, e);
        }
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
    public boolean isDone() {
        return futureResult.isDone();
    }

    public TarantoolSingleRecord<T> synchronize(){
        getOptional();
        return this;
    }

}
