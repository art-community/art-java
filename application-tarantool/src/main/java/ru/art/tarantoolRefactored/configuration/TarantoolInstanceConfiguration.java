package ru.art.tarantoolRefactored.configuration;

import lombok.Builder;
import lombok.Getter;
import org.tarantool.TarantoolClusterClientConfig;

import static ru.art.tarantoolRefactored.constants.TarantoolModuleConstants.DEFAULT_TARANTOOL_RETRIES;

@Getter
@Builder(builderMethodName = "tarantoolInstanceConfigurationBuilder")
public class TarantoolInstanceConfiguration {
    private final String address;
    @Builder.Default
    private final int maxConnectionRetries = DEFAULT_TARANTOOL_RETRIES;
    private final TarantoolClusterClientConfig config;
}
