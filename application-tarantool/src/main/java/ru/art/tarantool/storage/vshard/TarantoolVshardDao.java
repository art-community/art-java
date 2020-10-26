package ru.art.tarantool.storage.vshard;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import ru.art.entity.Value;

import static ru.art.entity.tuple.PlainTupleReader.readTuple;
import ru.art.entity.tuple.schema.ValueSchema;
import refactored.module.connector.TarantoolConnector;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.core.caster.Caster.*;
import static ru.art.logging.LoggingModule.loggingModule;

import java.io.OutputStream;
import java.util.List;

public class TarantoolVshardDao {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(TarantoolVshardDao.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolVshardDao.class);


    private TarantoolClient client;

    public TarantoolVshardDao(){
        client = TarantoolConnector.getClient("localhost:3300", TarantoolConnector.getDefaultConfig());
    }

    public Value call(Value params){
        List<?> response = cast(client.syncOps().call("vshard.router.call", params));
        Value result = readTuple(response, ValueSchema.fromTuple(response));
        return result;
    }

}
