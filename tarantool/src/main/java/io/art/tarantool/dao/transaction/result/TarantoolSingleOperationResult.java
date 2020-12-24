package io.art.tarantool.dao.transaction.result;

import io.art.core.exception.NotImplementedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static io.art.core.caster.Caster.cast;


public class TarantoolSingleOperationResult<T> implements TarantoolOperationResult<T>{
    private final CompletableFuture<T> futureResult;

    public TarantoolSingleOperationResult(CompletableFuture<List<?>> futureResult, Function<List<?>, T> responseMapper){
        this.futureResult = futureResult
                .thenApply(responseMapper);
    }

    @Override
    public T get() throws ExecutionException, InterruptedException, NoSuchElementException {
        return futureResult.get();
    }

    @Override
    public List<?> useResult(){
        throw new NotImplementedException("useResult for non-transactional method call");
    }

    @Override
    public List<?> useResultField(String fieldName){
        throw new NotImplementedException("useResultField for non-transactional method call");
    }

    @Override
    public boolean isDone() {
        return futureResult.isDone();
    }
}
