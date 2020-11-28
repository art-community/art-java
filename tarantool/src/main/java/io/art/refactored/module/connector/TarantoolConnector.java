package io.art.refactored.module.connector;

import io.tarantool.driver.StandaloneTarantoolClient;
import io.tarantool.driver.TarantoolClientConfig;
import io.tarantool.driver.TarantoolServerAddress;
import io.tarantool.driver.auth.SimpleTarantoolCredentials;
import io.tarantool.driver.exceptions.*;
import lombok.*;
import org.apache.logging.log4j.*;
import io.tarantool.driver.api.TarantoolClient;
import io.art.refactored.configuration.*;
import io.art.refactored.exception.*;
import static io.art.logging.LoggingModule.*;
import static io.art.refactored.constants.TarantoolModuleConstants.LoggingMessages.TARANTOOL_SUCCESSFULLY_CONNECTED;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import static io.art.refactored.constants.TarantoolModuleConstants.ExceptionMessages.*;


public class TarantoolConnector {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolConnector.class);

    public static TarantoolClient connect(String instanceId, TarantoolInstanceConfiguration config) {
        TarantoolClientConfig clientConfig = TarantoolClientConfig.builder()
                .withConnections(config.getConnections())
                .withConnectTimeout(config.getConnectionTimeout())
                .withCredentials(new SimpleTarantoolCredentials(config.getUsername(), config.getPassword()))
                .withReadTimeout(config.getReadTimeout())
                .withRequestTimeout(config.getRequestTimeout())
                .build();
        TarantoolServerAddress address = new TarantoolServerAddress(config.getHost(), config.getPort());

        int retries = 0;
        while (retries < config.getMaxConnectionRetries()){
            try {
                TarantoolClient client = new StandaloneTarantoolClient(clientConfig, address);
                getLogger().info(format(TARANTOOL_SUCCESSFULLY_CONNECTED, instanceId, address.toString()));
                return client;
            } catch (NoAvailableConnectionsException | TarantoolClientNotConnectedException | TarantoolSocketException exception) {
                getLogger().warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_RETRY, instanceId, address.toString(), exception));
            } catch (Throwable throwable) {
                throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address.toString(), throwable));
            }
            retries++;
        }
        throw new TarantoolConnectionException(format(UNABLE_TO_CONNECT_TO_TARANTOOL, instanceId, address.toString()));
    }

}
