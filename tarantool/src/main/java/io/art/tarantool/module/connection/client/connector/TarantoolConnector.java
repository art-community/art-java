package io.art.tarantool.module.connection.client.connector;

import io.art.logging.logger.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import io.tarantool.driver.*;
import io.tarantool.driver.api.*;
import io.tarantool.driver.auth.*;
import io.tarantool.driver.exceptions.*;
import lombok.*;
import static io.art.logging.Logging.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;


public class TarantoolConnector {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolConnector.class);

    public static TarantoolClient connect(String instanceId, TarantoolClientConfiguration config) {
        TarantoolClientConfig clientConfig = new TarantoolClientConfig();
        clientConfig.setConnections(config.getConnections());
        clientConfig.setConnectTimeout(config.getConnectionTimeout());
        clientConfig.setCredentials(new SimpleTarantoolCredentials(config.getUsername(), config.getPassword()));
        clientConfig.setReadTimeout(config.getReadTimeout());
        clientConfig.setRequestTimeout(config.getRequestTimeout());

        TarantoolServerAddress address = new TarantoolServerAddress(config.getHost(), config.getPort());

        int retries = 0;
        while (retries < config.getMaxConnectionRetries()) {
            try {
                TarantoolClient client = new StandaloneTarantoolClient(clientConfig, address);
                getLogger().info(format(TARANTOOL_CLIENT_CREATED, instanceId, address.toString()));
                return client;
            } catch (NoAvailableConnectionsException | TarantoolClientNotConnectedException | TarantoolSocketException exception) {
                getLogger().warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_RETRY, instanceId, address.toString()));
            } catch (Throwable throwable) {
                getLogger().error(format(UNABLE_TO_CONNECT_TO_TARANTOOL_RETRY, instanceId, address.toString()));
                throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL_RETRY, instanceId, address.toString()), throwable);
            }
            retries++;
        }
        getLogger().error(format(UNABLE_TO_CONNECT_TO_TARANTOOL_RETRY, instanceId, address.toString()));
        throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address.toString()));
    }

}
