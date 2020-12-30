package io.art.tarantool.configuration;

import io.art.core.source.ConfigurationSource;
import lombok.Getter;
import static io.art.core.checker.NullityChecker.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;

@Getter
public class TarantoolInstanceConfiguration {
    private String host;
    private int port;
    private String username;
    private String password;
    private int connections;
    private int connectionTimeout;
    private int readTimeout;
    private int requestTimeout;
    private int maxConnectionRetries;


    public static TarantoolInstanceConfiguration from(ConfigurationSource source){
        TarantoolInstanceConfiguration configuration = new TarantoolInstanceConfiguration();
        configuration.host = orElse(source.getString(TARANTOOL_INSTANCE_HOST_KEY), DEFAULT_TARANTOOL_HOST);
        configuration.port = orElse(source.getInt(TARANTOOL_INSTANCE_PORT_KEY), DEFAULT_TARANTOOL_PORT);
        configuration.username = orElse(source.getString(TARANTOOL_INSTANCE_USERNAME_KEY), DEFAULT_TARANTOOL_USERNAME);
        configuration.password = orElse(source.getString(TARANTOOL_INSTANCE_PASSWORD_KEY), DEFAULT_TARANTOOL_PASSWORD);
        configuration.connections = orElse(source.getInt(TARANTOOL_INSTANCE_CONNECTIONS_KEY), DEFAULT_TARANTOOL_CONNECTIONS_NUMBER);
        configuration.connectionTimeout = orElse(source.getInt(TARANTOOL_INSTANCE_CONNECTION_TIMEOUT_KEY), DEFAULT_TARANTOOL_CONNECTION_TIMEOUT);
        configuration.readTimeout = orElse(source.getInt(TARANTOOL_INSTANCE_READ_TIMEOUT_KEY), DEFAULT_TARANTOOL_READ_TIMEOUT);
        configuration.requestTimeout = orElse(source.getInt(TARANTOOL_INSTANCE_REQUEST_TIMEOUT_KEY), DEFAULT_TARANTOOL_REQUEST_TIMEOUT);
        configuration.maxConnectionRetries = orElse(source.getInt(TARANTOOL_INSTANCE_MAX_CONNECTIONS_RETRY_KEY), DEFAULT_TARANTOOL_RETRIES);
        return configuration;
    }
}
