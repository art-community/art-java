package io.art.tarantool.dao;

import io.art.tarantool.model.TarantoolUpdateFieldOperation;
import io.art.tarantool.module.client.TarantoolClusterClient;
import io.art.value.immutable.Value;
import io.tarantool.driver.api.TarantoolClient;
import io.art.tarantool.model.TarantoolResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static io.art.core.caster.Caster.cast;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.checker.EmptinessChecker.isNotEmpty;
import static io.art.core.factory.SetFactory.setOf;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static io.art.tarantool.module.client.caller.TarantoolFunctionCaller.asynchronousCall;
import static io.art.tarantool.model.TarantoolRequest.*;

@RequiredArgsConstructor
public class TarantoolAsynchronousSpace {
    @NonNull
    private final TarantoolClusterClient client;
    @NonNull
    private final String space;


    public CompletableFuture<Optional<Value>> get(Value key){
        return client.asynchronousCallRO(GET, space, requestTuple(key)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> get(String index, Value key){
        return client.asynchronousCallRO(GET, space, index, requestTuple(key)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<List<Value>>> select(Value request){
        return client.asynchronousCallRO(SELECT, space, requestTuple(request)).thenApply(TarantoolResponse::readBatch);
    }

    public CompletableFuture<Optional<List<Value>>> select(String index, Value request){
        return client.asynchronousCallRO(SELECT, space, requestTuple(request), index).thenApply(TarantoolResponse::readBatch);
    }

    public CompletableFuture<Optional<Value>> delete(Value key){
        return client.asynchronousCallRW(DELETE, space, requestTuple(key)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> insert(Value data){
        return client.asynchronousCallRW(INSERT, space, dataTuple(data)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> autoIncrement(Value data){
        return client.asynchronousCallRW(AUTO_INCREMENT, space, dataTuple(data)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> put(Value data){
        return client.asynchronousCallRW(PUT, space, dataTuple(data)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> replace(Value data){
        return client.asynchronousCallRW(REPLACE, space, dataTuple(data)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> update(Value key, TarantoolUpdateFieldOperation... operations){
        return client.asynchronousCallRW(UPDATE, space, requestTuple(key), updateOperationsTuple(operations)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return client.asynchronousCallRW(UPSERT, space, dataTuple(defaultValue), updateOperationsTuple(operations)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Long> count(){
        return client.asynchronousCallRO(COUNT, space).thenApply(response -> ((Number) response.get(0)).longValue() );
    }

    public CompletableFuture<Long> len(){
        return client.asynchronousCallRO(LEN, space).thenApply(response -> ((Number) response.get(0)).longValue() );
    }

    public CompletableFuture<Long> schemaCount(){
        return client.asynchronousCallRO(SCHEMA_COUNT, space).thenApply(response -> ((Number) response.get(0)).longValue() );
    }

    public CompletableFuture<Long> schemaLen(){
        return client.asynchronousCallRO(SCHEMA_LEN, space).thenApply(response -> ((Number) response.get(0)).longValue() );
    }

    public void truncate(){
        client.asynchronousCallRW(TRUNCATE, space);
    }

    public CompletableFuture<Set<String>> listIndices(){
        return client.asynchronousCallRO(LIST_INDICES, space).thenApply(response -> {
            List<String> indices = cast(response.get(0));
            return setOf(indices);
        });
    }
    
}
