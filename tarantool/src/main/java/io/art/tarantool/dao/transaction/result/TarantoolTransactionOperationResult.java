package io.art.tarantool.dao.transaction.result;

import io.art.tarantool.exception.TarantoolTransactionException;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static io.art.core.caster.Caster.cast;
import static io.art.core.factory.ListFactory.linkedListOf;
import static io.art.core.factory.MapFactory.mapOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.GET_RESULT_OF_NOT_COMMITTED_TRANSACTION;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.TRANSACTION_FAILED;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;


public class TarantoolTransactionOperationResult<T> implements TarantoolOperationResult<T>{
    @Getter
    private final int transactionEntryNumber;
    public CompletableFuture<List<?>> transactionFutureResponse;
    private final Function<List<?>, T> responseMapper;

    public TarantoolTransactionOperationResult(int transactionEntryNumber, Function<List<?>, T> responseMapper){
        this.transactionEntryNumber = transactionEntryNumber;
        this.responseMapper = responseMapper;
    }


    @Override
    public T get() throws ExecutionException, InterruptedException {
        if (isNull(transactionFutureResponse)) throw new TarantoolTransactionException(GET_RESULT_OF_NOT_COMMITTED_TRANSACTION);
        List<?> result = transactionFutureResponse.get();
        if (!(Boolean) result.get(0)) throw new TarantoolTransactionException(format(TRANSACTION_FAILED, (String) result.get(1)));
        result = cast(result.get(1));
        result = cast(result.get(transactionEntryNumber));
        return responseMapper.apply(result);
    }

    @Override
    public Map<String, List<?>> useResult(){
        return mapOf("dependency", linkedListOf(transactionEntryNumber + 1));
    }

    @Override
    public Map<String, List<?>> useResultField(String fieldName){
        return mapOf("dependency", linkedListOf(transactionEntryNumber + 1, fieldName));
    }

    @Override
    public boolean isDone() {
        return transactionFutureResponse.isDone();
    }
}
