package refactored.storage.dao;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import refactored.storage.dao.caller.TarantoolFunctionCaller;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.tuple.PlainTupleReader;
import ru.art.entity.tuple.PlainTupleWriter;
import ru.art.entity.tuple.schema.ValueSchema;

import static ru.art.core.caster.Caster.*;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.logging.LoggingModule.loggingModule;

public class TarantoolDao {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(TarantoolDao.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolDao.class);

    public TarantoolClient client;

    public Value get(String space, Value request){
        return execFunction("art.get", buildArgs(space, unpackValue(request)));
    }

    public Value select(String space, Value request){
        return execFunction("art.select", buildArgs(space, unpackValue(request)));
    }

    public Value delete(String space, int id){
        Object[] args = {space, id};
        return execFunction("art.delete", args);
    }

    public Value insert(String space, Value data){
        return execFunction("art.insert", buildArgs(space, addSchema(data)));
    }

    public Value autoIncrement(String space, Value data){
        return execFunction("art.auto_increment", buildArgs(space, addSchema(data)));
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

    private Object[] buildArgs(String space, List<?> request){
        Object[] result = {space, request};
        return result;
    }

    private Value execFunction(String function, Object[] args){
        List<?> response = TarantoolFunctionCaller.call(client, function, args);
        List<?> data = cast(response.get(0));
        ValueSchema schema = ValueSchema.fromTuple(cast(response.get(1)));
        Value result = PlainTupleReader.readTuple(data, schema);
        getLogger().info("Got Value: " + result.toString() + " Type:" + result.getType());
        return result;
    }
}
