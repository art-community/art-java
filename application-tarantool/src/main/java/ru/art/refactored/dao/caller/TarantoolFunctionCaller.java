package ru.art.refactored.dao.caller;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.TarantoolClient;
import java.util.List;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.caster.Caster.cast;
import static ru.art.logging.LoggingModule.loggingModule;

public class TarantoolFunctionCaller {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolFunctionCaller.class);


    public static List<?> call(TarantoolClient client, String function, Object... args){
        getLogger().info("Calling " + function + "()");
        List<?> result = client.syncOps().call(function, args);
        getLogger().info("response:" + result.toString());
        return result;
    }


}
