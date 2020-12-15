package io.art.tarantool.dao;

import io.art.tarantool.exception.TarantoolDaoException;
import io.art.tarantool.model.TarantoolUpdateFieldOperation;
import io.art.value.immutable.Value;
import io.art.value.tuple.PlainTupleReader;
import io.art.value.tuple.PlainTupleWriter;
import io.art.value.tuple.schema.ValueSchema;
import io.tarantool.driver.api.TarantoolClient;
import lombok.Getter;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static io.art.core.caster.Caster.cast;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.checker.EmptinessChecker.isNotEmpty;
import static io.art.core.factory.SetFactory.setOf;
import static io.art.logging.LoggingModule.logger;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.RESULT_IS_INVALID;
import static io.art.tarantool.constants.TarantoolModuleConstants.Functions.*;
import static io.art.value.tuple.PlainTupleWriter.writeTuple;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static io.art.tarantool.caller.TarantoolFunctionCaller.callAsync;

public class TarantoolAsyncSpace {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolSpace.class);
    private final TarantoolClient client;
    private String space;

    public TarantoolAsyncSpace(TarantoolClient client, String space) {
        this.client = client;
        this.space = space;
    }

    public CompletableFuture<Optional<Value>> get(Value key){
        return callAsync(client, GET, space, unpackValue(key)).thenApply(TarantoolAsyncSpace::convertResponse);
    }

    public CompletableFuture<Optional<Value>> get(String index, Value key){
        return callAsync(client, GET, space, index, unpackValue(key)).thenApply(TarantoolAsyncSpace::convertResponse);
    }

    public CompletableFuture<Optional<List<Value>>> select(Value request){
        return callAsync(client, SELECT, space, unpackValue(request)).thenApply(TarantoolAsyncSpace::convertSelectResponse);
    }

    public CompletableFuture<Optional<List<Value>>> select(String index, Value request){
        return callAsync(client, SELECT, space, unpackValue(request), index).thenApply(TarantoolAsyncSpace::convertSelectResponse);
    }

    public CompletableFuture<Optional<Value>> delete(Value key){
        return callAsync(client, DELETE, space, unpackValue(key)).thenApply(TarantoolAsyncSpace::convertResponse);
    }

    public CompletableFuture<Optional<Value>> insert(Value data){
        return callAsync(client, INSERT, space, addSchema(data)).thenApply(TarantoolAsyncSpace::convertResponse);
    }

    public CompletableFuture<Optional<Value>> autoIncrement(Value data){
        return callAsync(client, AUTO_INCREMENT, space, addSchema(data)).thenApply(TarantoolAsyncSpace::convertResponse);
    }

    public CompletableFuture<Optional<Value>> put(Value data){
        return callAsync(client, PUT, space, addSchema(data)).thenApply(TarantoolAsyncSpace::convertResponse);
    }

    public CompletableFuture<Optional<Value>> replace(Value data){
        return callAsync(client, REPLACE, space, addSchema(data)).thenApply(TarantoolAsyncSpace::convertResponse);
    }

    public CompletableFuture<Optional<Value>> update(Value key, TarantoolUpdateFieldOperation... operations){
        return callAsync(client, UPDATE, space, unpackValue(key), unpackUpdateOperations(operations)).thenApply(TarantoolAsyncSpace::convertResponse);
    }

    public CompletableFuture<Optional<Value>> upsert(Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return callAsync(client, UPSERT, space, addSchema(defaultValue), unpackUpdateOperations(operations)).thenApply(TarantoolAsyncSpace::convertResponse);
    }

    public CompletableFuture<Long> count(){
        return callAsync(client,COUNT, space).thenApply( response -> ((Number) response.get(0)).longValue() );
    }

    public CompletableFuture<Long> len(){
        return callAsync(client, LEN, space).thenApply( response -> ((Number) response.get(0)).longValue() );
    }

    public CompletableFuture<Long> schemaCount(){
        return callAsync(client,SCHEMA_COUNT, space).thenApply( response -> ((Number) response.get(0)).longValue() );
    }

    public CompletableFuture<Long> schemaLen(){
        return callAsync(client, SCHEMA_LEN, space).thenApply( response -> ((Number) response.get(0)).longValue() );
    }

    public void truncate(){
        callAsync(client, TRUNCATE, space);
    }

    public CompletableFuture<Set<String>> listIndices(){
        return callAsync(client, LIST_INDICES, space).thenApply(response -> {
            List<String> indices = cast(response.get(0));
            return setOf(indices);
        });
    }




    private static List<?> unpackValue(io.art.value.immutable.Value request){
        return writeTuple(request).getTuple();
    }

    private static List<?> addSchema(io.art.value.immutable.Value data){
        PlainTupleWriter.PlainTupleWriterResult writerResult = writeTuple(data);
        List<?> result = new ArrayList<>();
        result.add(cast(writerResult.getTuple()));
        result.add(cast(writerResult.getSchema().toTuple()));
        return result;
    }

    private static List<?> unpackUpdateOperations(TarantoolUpdateFieldOperation... operations) {
        List<?> valueOperations = stream(operations)
                .map(TarantoolUpdateFieldOperation::getValueOperation)
                .collect(toList());
        List<?> schemaOperations = stream(operations)
                .filter(operation -> isNotEmpty(operation.getSchemaOperation()))
                .map(TarantoolUpdateFieldOperation::getSchemaOperation)
                .collect(toList());
        List<?> results = new ArrayList<>();
        results.add(cast(valueOperations));
        results.add(cast(schemaOperations));
        return results;
    }

    private static Optional<io.art.value.immutable.Value> convertResponse(List<?> response){
        response = cast(response.get(0));
        if ((isEmpty(response.get(0))) || response.size() < 2) return empty();
        return ofNullable(convertTuple(response));
    }

    private static Optional<List<io.art.value.immutable.Value>> convertSelectResponse(List<?> response){
        response = cast(response.get(0));
        if (response.isEmpty()) return empty();

        List<io.art.value.immutable.Value> result = response.stream()
                .map(entry -> convertTuple(cast(entry)))
                .collect(toList());
        return ofNullable(result);
    }

    private static Value convertTuple(List<?> tuple){
        try {
            List<?> data = cast(tuple.get(0));
            ValueSchema schema = ValueSchema.fromTuple(cast(tuple.get(1)));
            io.art.value.immutable.Value result = PlainTupleReader.readTuple(data, schema);
            return result;
        } catch(Exception e){
            throw new TarantoolDaoException(format(RESULT_IS_INVALID, tuple.toString()));
        }
    }
}
