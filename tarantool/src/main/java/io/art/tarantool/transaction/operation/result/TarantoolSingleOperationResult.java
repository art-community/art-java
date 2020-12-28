package io.art.tarantool.transaction.operation.result;

import io.art.core.exception.NotImplementedException;
import io.art.tarantool.exception.TarantoolDaoException;
import io.art.tarantool.transaction.operation.dependency.TarantoolTransactionDependency;
import lombok.experimental.Delegate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static io.art.core.caster.Caster.cast;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;


public class TarantoolSingleOperationResult<T> implements TarantoolOperationResult<T>{
    private final CompletableFuture<Optional<T>> futureResult;

    public TarantoolSingleOperationResult(CompletableFuture<List<?>> futureResult, Function<List<?>, ?> responseMapper){
        this.futureResult = cast(futureResult.thenApply(responseMapper));
    }

    @Delegate
    public Optional<T> getOptional(){
        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TarantoolDaoException(UNABLE_TO_GET_RESPONSE);
        }
    }

    @Override
    public TarantoolTransactionDependency useResult(){
        throw new NotImplementedException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
    }

    @Override
    public TarantoolTransactionDependency useResultField(String fieldName){
        throw new NotImplementedException(ILLEGAL_TRANSACTION_DEPENDENCY_USAGE);
    }

    @Override
    public boolean isDone() {
        return futureResult.isDone();
    }

    public TarantoolSingleOperationResult<T> synchronize(){
        getOptional();
        return this;
    }

}
