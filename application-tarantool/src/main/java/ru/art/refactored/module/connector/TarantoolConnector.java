package ru.art.refactored.module.connector;

import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.tarantool.CommunicationException;
import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClusterClient;
import ru.art.refactored.configuration.TarantoolInstanceConfiguration;
import ru.art.refactored.exception.TarantoolConnectionException;
import java.io.OutputStream;

import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.refactored.constants.TarantoolModuleConstants.ExceptionMessages.*;

public class TarantoolConnector {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolConnector.class);

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
