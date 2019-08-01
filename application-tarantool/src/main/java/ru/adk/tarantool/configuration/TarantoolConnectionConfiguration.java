package ru.adk.tarantool.configuration;

import lombok.Builder;
import lombok.Getter;
import static ru.adk.core.constants.NetworkConstants.LOCALHOST;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.DEFAULT_TARANTOOL_PORT;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.DEFAULT_TARANTOOL_USERNAME;

@Getter
@Builder
public class TarantoolConnectionConfiguration {
    @Builder.Default
    private final String host = LOCALHOST;
    @Builder.Default
    private final Integer port = DEFAULT_TARANTOOL_PORT;
    @Builder.Default
    private final String username = DEFAULT_TARANTOOL_USERNAME;
    private String password;
}
