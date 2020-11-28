package io.art.refactored.dao.caller;

import io.tarantool.driver.api.TarantoolClient;
import lombok.Getter;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import static io.art.logging.LoggingModule.logger;
import static io.art.refactored.constants.TarantoolModuleConstants.LoggingMessages.FAILED_FUNCTION;

import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;

public class TarantoolFunctionCaller {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolFunctionCaller.class);


    public static List<?> call(TarantoolClient client, String function, Object... args){
        try {
            getLogger().info("Calling " + function + "()");
            List<?> result = client.call(function, args).get();
            //getLogger().info("response:" + result.toString());
            return result;
        } catch (Exception e){
            getLogger().warn(format(FAILED_FUNCTION, function));
        }

        return new LinkedList<>();
    }


}
