package io.art.tarantool.module.client;

import io.art.tarantool.dao.TarantoolInstance;
import lombok.NonNull;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import io.tarantool.driver.api.TarantoolClient;

import static io.art.core.factory.ListFactory.linkedListOf;
import static io.art.tarantool.module.client.caller.TarantoolFunctionCaller.*;


public class ClusterTarantoolClient {
    @NonNull
    private final List<io.tarantool.driver.api.TarantoolClient> readWriteClients;
    @NonNull
    private final List<io.tarantool.driver.api.TarantoolClient> readOnlyClients;
    private Iterator<io.tarantool.driver.api.TarantoolClient> readWriteIterator;
    private Iterator<io.tarantool.driver.api.TarantoolClient> readOnlyIterator;

    public ClusterTarantoolClient(io.tarantool.driver.api.TarantoolClient singleClient){
        readOnlyClients = linkedListOf(singleClient);
        readWriteClients = linkedListOf(singleClient);
        readOnlyIterator = readOnlyClients.iterator();
        readWriteIterator = readWriteClients.iterator();
    }

    public ClusterTarantoolClient(Collection<TarantoolClient> rwClients, Collection<TarantoolClient> roClients){
        readWriteClients = linkedListOf(rwClients);
        readOnlyClients = linkedListOf(roClients);
        readOnlyIterator = readOnlyClients.iterator();
        readWriteIterator = readWriteClients.iterator();
    }

    public CompletableFuture<List<?>> callRW(String function, Object... args){
        if (!readWriteIterator.hasNext()) readWriteIterator = readWriteClients.iterator();
        return asynchronousCall(readWriteIterator.next(), function, args);
    }

    public CompletableFuture<List<?>> callRO(String function, Object... args){
        if (!readOnlyIterator.hasNext()) readOnlyIterator = readOnlyClients.iterator();
        return asynchronousCall(readOnlyIterator.next(), function, args);
    }

}
