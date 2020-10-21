package refactored.storage.dao.caller;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import refactored.storage.dao.TarantoolDao;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.tuple.PlainTupleWriter;

import java.io.OutputStream;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.core.caster.Caster.cast;
import static ru.art.logging.LoggingModule.loggingModule;

public class TarantoolFunctionCaller {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(TarantoolFunctionCaller.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolFunctionCaller.class);


    public static List<?> call(TarantoolClient client, String function, Object[] args){
        getLogger().info("Calling " + function + "() with args: " + args.toString());
        List<?> response = cast(client.syncOps().call(function, args));
        getLogger().info("Response:" + response);
        return response;
    }

}
