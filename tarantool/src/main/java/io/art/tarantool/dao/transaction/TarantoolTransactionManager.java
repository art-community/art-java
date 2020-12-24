package io.art.tarantool.dao.transaction;

import io.art.tarantool.dao.transaction.entry.TarantoolTransactionEntry;
import io.art.tarantool.dao.transaction.result.TarantoolOperationResult;
import io.art.tarantool.dao.transaction.result.TarantoolSingleOperationResult;
import io.art.tarantool.dao.transaction.result.TarantoolTransactionOperationResult;
import io.art.tarantool.module.client.TarantoolClusterClient;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static io.art.core.factory.ListFactory.linkedList;
import static io.art.core.factory.ListFactory.linkedListOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.TRANSACTION;
import static io.art.tarantool.dao.transaction.entry.TarantoolTransactionEntry.tarantoolTransactionEntry;

public class TarantoolTransactionManager {
    private final TarantoolClusterClient client;
    private boolean activeTransaction = false;
    private boolean isRWTransaction = false;
    private final List<List<?>> operations = linkedList();
    private final List<TarantoolTransactionOperationResult<?>> results = linkedList();

    public TarantoolTransactionManager(TarantoolClusterClient client){
        this.client = client;
    }

    public void begin(){
        activeTransaction = true;
    }

    public void commit(){
        CompletableFuture<List<?>> response = call(TRANSACTION, operations);
        for (TarantoolTransactionOperationResult<?> result : results){
            result.transactionFutureResponse = response;
        }
        clearTransaction();
    }

    public void cancel(){
        clearTransaction();
    }

    public TarantoolOperationResult<?> callRW(String function, Function<List<?>, Object> responseMapper, Object ... args){
        isRWTransaction = true;
        return callRO(function, responseMapper, args);
    }

    public TarantoolOperationResult<?> callRO(String function, Function<List<?>, Object> responseMapper, Object ... args){
        return activeTransaction ? addOperation(function, responseMapper, args) : callSingleOperation(function, responseMapper, args);
    }



    private TarantoolOperationResult<?> addOperation(String function, Function<List<?>, Object> responseMapper, Object ... args){
        TarantoolTransactionOperationResult<Object> result = new TarantoolTransactionOperationResult<Object>(operations.size(), responseMapper);
        operations.add(tarantoolTransactionEntry(function, args));
        results.add(result);
        return result;
    }

    private TarantoolOperationResult<?> callSingleOperation(String function, Function<List<?>, Object> responseMapper, Object ... args){
        TarantoolSingleOperationResult<Object> result = new TarantoolSingleOperationResult<Object>(call(function, args), responseMapper);
        isRWTransaction = false;
        return result;
    }

    private CompletableFuture<List<?>> call(String function, Object... args){
        return isRWTransaction ? client.callRW(function, args) : client.callRO(function, args);
    }

    private void clearTransaction(){
        operations.clear();
        results.clear();
        isRWTransaction = false;
        activeTransaction = false;
    }
}
