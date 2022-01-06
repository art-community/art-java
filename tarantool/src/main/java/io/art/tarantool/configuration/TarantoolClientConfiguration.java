package io.art.tarantool.configuration;

import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;

@Getter
@Builder
public class TarantoolClientConfiguration {
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean readable;
    private boolean writeable;
    private int connections;
    private int connectionTimeout;
    private int readTimeout;
    private int requestTimeout;
    private int maxConnectionRetries;


    public static TarantoolClientConfiguration from(ConfigurationSource source) {
        TarantoolClientConfiguration configuration = TarantoolClientConfiguration.builder().build();
        configuration.host = orElse(source.getString(TARANTOOL_INSTANCE_HOST_KEY), DEFAULT_TARANTOOL_HOST);
        configuration.port = orElse(source.getInteger(TARANTOOL_INSTANCE_PORT_KEY), DEFAULT_TARANTOOL_PORT);
        configuration.username = orElse(source.getString(TARANTOOL_INSTANCE_USERNAME_KEY), DEFAULT_TARANTOOL_USERNAME);
        configuration.password = orElse(source.getString(TARANTOOL_INSTANCE_PASSWORD_KEY), DEFAULT_TARANTOOL_PASSWORD);
        configuration.readable = orElse(source.getBoolean(TARANTOOL_INSTANCE_READABLE_KEY), true);
        configuration.writeable = orElse(source.getBoolean(TARANTOOL_INSTANCE_WRITEABLE_KEY), true);
        configuration.connections = orElse(source.getInteger(TARANTOOL_INSTANCE_CONNECTIONS_KEY), DEFAULT_TARANTOOL_CONNECTIONS_NUMBER);
        configuration.connectionTimeout = orElse(source.getInteger(TARANTOOL_INSTANCE_CONNECTION_TIMEOUT_KEY), DEFAULT_TARANTOOL_CONNECTION_TIMEOUT);
        configuration.readTimeout = orElse(source.getInteger(TARANTOOL_INSTANCE_READ_TIMEOUT_KEY), DEFAULT_TARANTOOL_READ_TIMEOUT);
        configuration.requestTimeout = orElse(source.getInteger(TARANTOOL_INSTANCE_REQUEST_TIMEOUT_KEY), DEFAULT_TARANTOOL_REQUEST_TIMEOUT);
        configuration.maxConnectionRetries = orElse(source.getInteger(TARANTOOL_INSTANCE_MAX_CONNECTIONS_RETRY_KEY), DEFAULT_TARANTOOL_RETRIES);
        return configuration;
    }
}
