package refactored.storage.dao;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import refactored.storage.dao.caller.TarantoolFunctionCaller;
import ru.art.entity.Value;
import ru.art.entity.tuple.PlainTupleReader;
import ru.art.entity.tuple.PlainTupleWriter;
import ru.art.entity.tuple.schema.ValueSchema;
import ru.art.tarantool.model.TarantoolUpdateFieldOperation;

import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.art.core.caster.Caster.*;

import java.io.OutputStream;
import java.util.*;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.tarantool.storage.dao.caller.TarantoolFunctionCaller.callTarantoolFunction;
import static ru.art.tarantool.storage.dao.service.TarantoolScriptService.evaluateValueScript;

public class TarantoolDao {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(TarantoolDao.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolDao.class);

    public TarantoolClient client; //temporary

    public Optional<Value> get(String space, Value request){
        return execFunction("art.get", collectToArray(space, unpackValue(request)));
    }

    public Optional<Value> select(String space, Value request){
        return execFunction("art.select", collectToArray(space, unpackValue(request)));
    }

    public Optional<Value> delete(String space, int id){
        return execFunction("art.delete", collectToArray(space, id));
    }

    public Optional<Value> insert(String space, Value data){
        return execFunction("art.insert", collectToArray(space, addSchema(data)));
    }

    public Optional<Value> autoIncrement(String space, Value data){
        return execFunction("art.auto_increment", collectToArray(space, addSchema(data)));
    }

    public Optional<Value> put(String space, Value data){
        return execFunction("art.put", collectToArray(space, addSchema(data)));
    }

    public Optional<Value> replace(String space, Value data){
        return execFunction("art.replace", collectToArray(space, addSchema(data)));
    }

    public Optional<Value> update(String space, int id, TarantoolUpdateFieldOperation... operations){
        return execFunction("test", collectToArray(space, id, unpackUpdateOperations(operations)));
    }

    public Optional<Value> upsert(String space, Value defaultValue, TarantoolUpdateFieldOperation... operations){
        return execFunction("art.upsert", collectToArray(space, addSchema(defaultValue), unpackUpdateOperations(operations)));
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

    private Object[] collectToArray(Object... args){
        Object[] result = stream(args).sequential().toArray();
        return result;
    }

    private Optional<Value> execFunction(String function, Object[] args){
        List<?> response = TarantoolFunctionCaller.call(client, function, args);
        Optional<Value> result = convertResults(response);
        if (result.isEmpty()) {
            getLogger().info("Got empty response");
        } else {
            getLogger().info("Got Value: " + result.toString() + " Type:" + result.get().getType());
        }
        return result;
    }

    private Optional<Value> convertResults(List<?> response){
        if ((isEmpty(response.get(0))) || response.size() < 2) {
            return empty();
        }
        List<?> data = cast(response.get(0));
        ValueSchema schema = ValueSchema.fromTuple(cast(response.get(1)));
        Value result = PlainTupleReader.readTuple(data, schema);
        return ofNullable(result);
    }
}
