package ru.art.tarantool.storage.vshard;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.msgpack.core.MessagePack;
import org.msgpack.value.ValueFactory;
import org.tarantool.MsgPackLite;
import org.tarantool.TarantoolClient;
import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueMapper;
import static ru.art.entity.tuple.PlainTupleReader.readTuple;
import ru.art.entity.tuple.schema.ValueSchema;
import ru.art.tarantool.storage.vshard.connector.VshardStandardConnector;

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
        client = VshardStandardConnector.getClient("localhost:3300", VshardStandardConnector.getDefaultConfig());
    }

    public Value call(Value params){
        List<?> response = cast(client.syncOps().call("vshard.router.call", params));
        Value result = readTuple(response, ValueSchema.fromTuple(response));
        return result;
    }

}
