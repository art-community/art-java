package io.art.tarantool.caller;

import io.art.tarantool.exception.TarantoolDaoException;
import io.tarantool.driver.api.TarantoolClient;
import lombok.Getter;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static io.art.logging.LoggingModule.logger;
import static io.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static io.art.tarantool.module.TarantoolModule.tarantoolModule;
import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;

public class TarantoolFunctionCaller {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolFunctionCaller.class);


    public static List<?> call(TarantoolClient client, String function, Object... args){
        try{
            List<?> result = callAsync(client, function, args).get();
            logInfo(format(CALLED_FUNCTION, function, result.toString()));
            return result;
        } catch (Exception e){
            getLogger().error(format(FAILED_FUNCTION, function));
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    public static CompletableFuture<List<?>> callAsync(TarantoolClient client, String function, Object... args){
        try {
            logInfo(format(CALLING_FUNCTION, function));
            return client.call(function, args);
        } catch (Exception e){
            getLogger().error(format(FAILED_FUNCTION, function));
            throw new TarantoolDaoException(e.getMessage());
        }
    }

    private static void logInfo(String message){
        if (tarantoolModule().configuration().enableTracing) {
            getLogger().info(message);
        }
    }
}
