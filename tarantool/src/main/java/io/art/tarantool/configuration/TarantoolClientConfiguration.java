package io.art.tarantool.configuration;

import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ConfigurationKeys.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import java.time.*;

@Getter
@Builder
public class TarantoolClientConfiguration {
    private String host;
    private int port;
    private String username;
    private String password;
    private boolean immutable;
    private Duration connectionTimeout;
    private boolean logging;

    public static TarantoolClientConfiguration tarantoolClientConfiguration(ConfigurationSource source) {
        TarantoolClientConfiguration configuration = TarantoolClientConfiguration.builder().build();
        configuration.host = orElse(source.getString(TARANTOOL_INSTANCE_HOST_KEY), DEFAULT_TARANTOOL_HOST);
        configuration.port = orElse(source.getInteger(TARANTOOL_INSTANCE_PORT_KEY), DEFAULT_TARANTOOL_PORT);
        configuration.username = orElse(source.getString(TARANTOOL_INSTANCE_USERNAME_KEY), DEFAULT_TARANTOOL_USERNAME);
        configuration.password = orElse(source.getString(TARANTOOL_INSTANCE_PASSWORD_KEY), DEFAULT_TARANTOOL_PASSWORD);
        configuration.immutable = orElse(source.getBoolean(TARANTOOL_INSTANCE_IMMUTABLE_KEY), true);
        configuration.connectionTimeout = orElse(source.getDuration(TARANTOOL_INSTANCE_CONNECTION_TIMEOUT_KEY), DEFAULT_TARANTOOL_CONNECTION_TIMEOUT);
        configuration.logging = orElse(source.getBoolean(TARANTOOL_LOGGING_KEY), false);
        return configuration;
    }
}
