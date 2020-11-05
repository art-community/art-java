package ru.art.refactored.storage.dao;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import ru.art.entity.Value;
import ru.art.entity.tuple.PlainTupleReader;
import ru.art.entity.tuple.PlainTupleWriter;
import ru.art.entity.tuple.schema.ValueSchema;
import ru.art.refactored.model.TarantoolUpdateFieldOperation;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.art.refactored.storage.dao.caller.TarantoolFunctionCaller.call;
import static ru.art.core.caster.Caster.*;
import java.io.OutputStream;
import java.util.*;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.logging.LoggingModule.loggingModule;

public class TarantoolDao {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(TarantoolDao.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolDao.class);
    private TarantoolClient client;

    public TarantoolDao(TarantoolClient client){
        this.client = client;
        this.space = new TarantoolSpaceDao(client);
    }

    public TarantoolSpaceDao space;

    public Optional<Value> get(String space, Value request){
        List<?> response = call(client, "art.get", space, unpackValue(request));
        return convertResponse(response);
    }

    public Optional<List<Value>> select(String space, Value request){
        List<?> response = call(client, "art.select", space, unpackValue(request));
        response = cast(response.get(0));
        if (response.isEmpty()) return empty();

        List<Value> result = response.stream()
                .map(entry -> convertResponse(cast(entry)))
                .map(entry -> entry.get())
                .collect(toList());
        return ofNullable(result);
    }

    public Optional<Value> delete(String space, Value key){
        return convertResponse(call(client, "art.delete", space, unpackValue(key)));
    }

    public Optional<Value> insert(String space, Value data){
        return convertResponse(call(client, "art.insert", space, addSchema(data)));
    }

    public Optional<Value> autoIncrement(String space, Value data){
        return convertResponse(call(client, "art.auto_increment", space, addSchema(data)));
    }

    public Optional<Value> put(String space, Value data){
        return convertResponse(call(client, "art.put", space, addSchema(data)));
    }

    public Optional<Value> replace(String space, Value data){
        return convertResponse(call(client, "art.replace", space, addSchema(data)));
    }

    public Optional<Value> update(String space, Value key, TarantoolUpdateFieldOperation... operations){
        List<?> response = call(client, "art.update", space, unpackValue(key), unpackUpdateOperations(operations));
        return convertResponse(response);
    }

    public Optional<Value> upsert(String space, Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return convertResponse(call(client, "art.upsert", space, addSchema(defaultValue), unpackUpdateOperations(operations)));
    }


    private Optional<Value> convertResponse(List<?> response){
        response = cast(response.get(0));
        if ((isEmpty(response.get(0))) || response.size() < 2) {
            getLogger().info("Got empty response");
            return empty();
        }
        List<?> data = cast(response.get(0));
        ValueSchema schema = ValueSchema.fromTuple(cast(response.get(1)));
        Value result = PlainTupleReader.readTuple(data, schema);
        getLogger().info("Got Value: " + result.toString() + " Type:" + result.getType());
        return ofNullable(result);
    }

    private List<?> unpackValue(Value request){
        return PlainTupleWriter.writeTuple(request).getTuple();
    }

    private List<?> addSchema(Value data){
        PlainTupleWriter.PlainTupleWriterResult writerResult = PlainTupleWriter.writeTuple(data);
        List<?> result = new ArrayList<>();
        result.add(cast(writerResult.getTuple()));
        result.add(cast(writerResult.getSchema().toTuple()));
        return result;
    }

    private List<?> unpackUpdateOperations(TarantoolUpdateFieldOperation... operations) {
        List<?> valueOperations = stream(operations)
                .map(TarantoolUpdateFieldOperation::getValueOperation)
                .collect(toList());
        List<?> schemaOperations = stream(operations)
                .filter(operation -> !isEmpty(operation.getSchemaOperation()))
                .map(TarantoolUpdateFieldOperation::getSchemaOperation)
                .collect(toList());
        List<?> results = new ArrayList<>();
        results.add(cast(valueOperations));
        results.add(cast(schemaOperations));
        return results;
    }

}
