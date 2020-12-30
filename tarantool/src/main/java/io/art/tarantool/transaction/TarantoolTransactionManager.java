package io.art.tarantool.transaction;

import io.art.core.threadlocal.ThreadLocalValue;
import io.art.tarantool.transaction.operation.result.TarantoolOperationResult;
import io.art.tarantool.transaction.operation.result.TarantoolSingleOperationResult;
import io.art.tarantool.transaction.operation.result.TarantoolTransactionOperationResult;
import io.art.tarantool.module.client.TarantoolClusterClient;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static io.art.core.factory.ListFactory.linkedList;
import static io.art.core.threadlocal.ThreadLocalValue.threadLocal;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.TRANSACTION;
import static io.art.tarantool.transaction.operation.TarantoolTransactionOperation.tarantoolTransactionOperation;

public class TarantoolTransactionManager {
    private final TarantoolClusterClient client;
    private final ThreadLocalValue<State> state = threadLocal(State::new);

    public TarantoolTransactionManager(TarantoolClusterClient client){
        this.client = client;
    }

    public void begin(){
        state().activeTransaction = true;
    }

    public void commit(){
        CompletableFuture<List<?>> response = call(TRANSACTION, state().operations);
        for (TarantoolTransactionOperationResult<?> result : state().results){
            result.transactionCommitted(response);
        }
        clearTransaction();
    }

    public void cancel(){
        clearTransaction();
    }

    public TarantoolOperationResult<?> callRW(String function, Function<List<?>, Optional<?>> responseMapper, Object ... args){
        state().isRWTransaction = true;
        return callRO(function, responseMapper, args);
    }

    public TarantoolOperationResult<?> callRO(String function, Function<List<?>, Optional<?>> responseMapper, Object ... args){
        return state().activeTransaction ? addOperation(function, responseMapper, args) : callSingleOperation(function, responseMapper, args);
    }



    private State state(){
        return state.get();
    }

    private TarantoolOperationResult<?> addOperation(String function, Function<List<?>, Optional<?>> responseMapper, Object ... args){
        TarantoolTransactionOperationResult<?> result = new TarantoolTransactionOperationResult<>(state().operations.size(), responseMapper);
        state().operations.add(tarantoolTransactionOperation(function, args));
        state().results.add(result);
        return result;
    }

    private TarantoolOperationResult<?> callSingleOperation(String function, Function<List<?>, Optional<?>> responseMapper, Object ... args){
        TarantoolSingleOperationResult<?> result = new TarantoolSingleOperationResult<>(call(function, args), responseMapper);
        state().isRWTransaction = false;
        return result;
    }

    private CompletableFuture<List<?>> call(String function, Object... args){
        return state().isRWTransaction ? client.callRW(function, args) : client.callRO(function, args);
    }

    private void clearTransaction(){
        state().operations.clear();
        state().results.clear();
        state().isRWTransaction = false;
        state().activeTransaction = false;
    }

    private static class State {
        public boolean activeTransaction = false;
        public boolean isRWTransaction = false;
        public final List<List<?>> operations = linkedList();
        public final List<TarantoolTransactionOperationResult<?>> results = linkedList();
    }
}
