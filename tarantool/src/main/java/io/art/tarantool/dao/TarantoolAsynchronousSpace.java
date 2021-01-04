package io.art.tarantool.dao;

import io.art.tarantool.model.*;
import io.art.value.immutable.Value;
import io.tarantool.driver.api.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.tarantool.client.caller.TarantoolFunctionCaller.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.tarantool.model.TarantoolRequest.*;
import java.util.*;
import java.util.concurrent.*;

@RequiredArgsConstructor
public class TarantoolAsynchronousSpace {
    @NonNull
    private final TarantoolClient client;
    @NonNull
    private final String space;

    public CompletableFuture<Optional<Value>> get(Value key) {
        return asynchronousCall(client, GET, space, requestTuple(key)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> get(String index, Value key) {
        return asynchronousCall(client, GET, space, index, requestTuple(key)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<List<Value>>> select(Value request) {
        return asynchronousCall(client, SELECT, space, requestTuple(request)).thenApply(TarantoolResponse::readBatch);
    }

    public CompletableFuture<Optional<List<Value>>> select(String index, Value request) {
        return asynchronousCall(client, SELECT, space, requestTuple(request), index).thenApply(TarantoolResponse::readBatch);
    }

    public CompletableFuture<Optional<Value>> delete(Value key) {
        return asynchronousCall(client, DELETE, space, requestTuple(key)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> insert(Value data) {
        return asynchronousCall(client, INSERT, space, dataTuple(data)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> autoIncrement(Value data) {
        return asynchronousCall(client, AUTO_INCREMENT, space, dataTuple(data)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> put(Value data) {
        return asynchronousCall(client, PUT, space, dataTuple(data)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> replace(Value data) {
        return asynchronousCall(client, REPLACE, space, dataTuple(data)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> update(Value key, TarantoolUpdateFieldOperation... operations) {
        return asynchronousCall(client, UPDATE, space, requestTuple(key), updateOperationsTuple(operations)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Optional<Value>> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations) {
        return asynchronousCall(client, UPSERT, space, dataTuple(defaultValue), updateOperationsTuple(operations)).thenApply(TarantoolResponse::read);
    }

    public CompletableFuture<Long> count() {
        return asynchronousCall(client, COUNT, space).thenApply(response -> ((Number) response.get(0)).longValue());
    }

    public CompletableFuture<Long> len() {
        return asynchronousCall(client, LEN, space).thenApply(response -> ((Number) response.get(0)).longValue());
    }

    public CompletableFuture<Long> schemaCount() {
        return asynchronousCall(client, SCHEMA_COUNT, space).thenApply(response -> ((Number) response.get(0)).longValue());
    }

    public CompletableFuture<Long> schemaLen() {
        return asynchronousCall(client, SCHEMA_LEN, space).thenApply(response -> ((Number) response.get(0)).longValue());
    }

    public void truncate() {
        asynchronousCall(client, TRUNCATE, space);
    }

    public CompletableFuture<Set<String>> listIndices() {
        return asynchronousCall(client, LIST_INDICES, space).thenApply(response -> {
            List<String> indices = cast(response.get(0));
            return setOf(indices);
        });
    }

}
