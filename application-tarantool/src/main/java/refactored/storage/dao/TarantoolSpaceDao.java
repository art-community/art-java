package refactored.storage.dao;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import refactored.storage.dao.caller.TarantoolFunctionCaller;
import ru.art.entity.Value;
import ru.art.entity.tuple.PlainTupleReader;
import ru.art.entity.tuple.schema.ValueSchema;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.logging.LoggingModule.loggingModule;

public class TarantoolSpaceDao {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(TarantoolSpaceDao.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolSpaceDao.class);
    private TarantoolClient client;

    public TarantoolSpaceDao(TarantoolClient client){
        this.client = client;
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
