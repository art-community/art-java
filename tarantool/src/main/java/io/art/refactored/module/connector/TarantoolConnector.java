package io.art.refactored.module.connector;

import lombok.*;
import org.apache.logging.log4j.*;
import org.tarantool.*;
import io.art.refactored.configuration.*;
import io.art.refactored.exception.*;
import static io.art.logging.LoggingModule.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import static io.art.refactored.constants.TarantoolModuleConstants.ExceptionMessages.*;

public class TarantoolConnector {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolConnector.class);

    public static TarantoolClient connect(String instanceId, TarantoolInstanceConfiguration config) {
        int retries = 0;
        while (retries < config.getMaxConnectionRetries()){
            try {
                getLogger().info("Connecting to tarantool at [" + config.getAddress() + "]...");
                org.tarantool.TarantoolClient client = new TarantoolClusterClient(config.getConfig(), config.getAddress());
                getLogger().info("Client connected.");
                return client;
            } catch (CommunicationException exception) {
                getLogger().warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_RETRY, instanceId, config.getAddress(), exception));
            } catch (Throwable throwable) {
                throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, config.getAddress(), throwable));
            }
            retries++;
        }
        throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, config.getAddress()));
    }

}
