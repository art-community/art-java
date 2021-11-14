package io.art.tarantool.module.connection.client.caller;

import io.art.logging.logger.*;
import io.art.tarantool.exception.*;
import io.tarantool.driver.api.*;
import lombok.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static io.art.tarantool.module.TarantoolModule.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.concurrent.*;

public class TarantoolFunctionCaller {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolFunctionCaller.class);

    public static CompletableFuture<List<?>> call(TarantoolClient client, String function, Object... args) {
        try {
            logInfo(format(CALLING_FUNCTION, function));
            return client.call(function, args);
        } catch (Throwable throwable) {
            getLogger().error(format(FAILED_FUNCTION, function));
            throw new TarantoolDaoException(format(FAILED_FUNCTION, function), throwable);
        }
    }

    private static void logInfo(String message) {
        if (tarantoolModule().configuration().logging) getLogger().info(message);
    }
}
