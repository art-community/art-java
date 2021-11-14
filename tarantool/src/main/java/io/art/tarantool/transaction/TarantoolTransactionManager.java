package io.art.tarantool.transaction;

import io.art.core.local.ThreadLocalValue;
import io.art.tarantool.exception.*;
import io.art.tarantool.model.record.TarantoolRecord;
import io.art.tarantool.model.record.TarantoolSingleRecord;
import io.art.tarantool.model.record.TarantoolTransactionRecord;
import io.art.tarantool.module.connection.client.TarantoolCluster;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;

import static io.art.core.factory.ListFactory.linkedList;
import static io.art.core.local.ThreadLocalValue.threadLocal;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.ATTEMPT_OF_NESTED_TRANSACTION;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.TRANSACTION;
import static io.art.tarantool.model.transaction.operation.TarantoolTransactionOperation.tarantoolTransactionOperation;

public class TarantoolTransactionManager {
    private final Supplier<TarantoolCluster> client;
    private final ThreadLocalValue<State> state = threadLocal(State::new);

    public TarantoolTransactionManager(Supplier<TarantoolCluster> client){
        this.client = client;
    }

    public void begin(){
        if (state().activeTransaction) throw new TarantoolTransactionException(ATTEMPT_OF_NESTED_TRANSACTION);
        state().activeTransaction = true;
    }

    public void begin(Long bucketId){
        begin();
        state().bucketId = bucketId;
    }

    public void commit(){
        CompletableFuture<List<?>> response = call(TRANSACTION, state().operations, state().bucketId);
        for (TarantoolTransactionRecord<?> result : state().results){
            result.transactionCommitted(response);
        }
        state().clear();
    }

    public void cancel(){
        state().clear();
    }

    public TarantoolRecord<?> callRW(String function, Function<List<?>, Optional<?>> responseMapper, Object ... args){
        state().isRWTransaction = true;
        return callRO(function, responseMapper, args);
    }

    public TarantoolRecord<?> callRO(String function, Function<List<?>, Optional<?>> responseMapper, Object ... args){
        return state().activeTransaction ? addOperation(function, responseMapper, args) : callSingleOperation(function, responseMapper, args);
    }



    private State state(){
        return state.get();
    }

    private TarantoolRecord<?> addOperation(String function, Function<List<?>, Optional<?>> responseMapper, Object ... args){
        TarantoolTransactionRecord<?> result = new TarantoolTransactionRecord<>(state().operations.size(), responseMapper);
        state().operations.add(tarantoolTransactionOperation(function, args));
        state().results.add(result);
        return result;
    }

    private TarantoolRecord<?> callSingleOperation(String function, Function<List<?>, Optional<?>> responseMapper, Object ... args){
        TarantoolSingleRecord<?> result = new TarantoolSingleRecord<>(call(function, args), responseMapper);
        state().isRWTransaction = false;
        return result;
    }

    private CompletableFuture<List<?>> call(String function, Object... args){
        return state().isRWTransaction ? client.get().callRW(function, args) : client.get().callRO(function, args);
    }

    private static class State {
        public boolean activeTransaction = false;
        public boolean isRWTransaction = false;
        public Long bucketId = null;
        public final List<List<?>> operations = linkedList();
        public final List<TarantoolTransactionRecord<?>> results = linkedList();

        private void clear(){
            operations.clear();
            results.clear();
            isRWTransaction = false;
            activeTransaction = false;
            bucketId = null;
        }
    }
}
