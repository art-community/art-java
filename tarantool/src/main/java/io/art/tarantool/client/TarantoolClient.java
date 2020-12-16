package io.art.tarantool.client;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static io.art.core.factory.ListFactory.linkedList;
import static io.art.tarantool.client.caller.TarantoolFunctionCaller.*;

@RequiredArgsConstructor
public class TarantoolClient {
    @NonNull
    private final List<io.tarantool.driver.api.TarantoolClient> readWriteClients = linkedList();
    @NonNull
    private final List<io.tarantool.driver.api.TarantoolClient> readOnlyClients = linkedList();
    private Iterator<io.tarantool.driver.api.TarantoolClient> readWriteIterator = readWriteClients.iterator();
    private Iterator<io.tarantool.driver.api.TarantoolClient> readOnlyIterator = readOnlyClients.iterator();

    public CompletableFuture<List<?>> callReadWrite(String function, Object... args){
        if (!readWriteIterator.hasNext()) readWriteIterator = readWriteClients.iterator();
        return asynchronousCall(readWriteIterator.next(), function, args);
    }

    public CompletableFuture<List<?>> callReadOnly(String function, Object... args){
        if (!readOnlyIterator.hasNext()) readOnlyIterator = readOnlyClients.iterator();
        return asynchronousCall(readOnlyIterator.next(), function, args);
    }

}
