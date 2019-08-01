package ru.adk.tarantool.configuration;

import lombok.Builder;
import lombok.Getter;
import static java.nio.file.Paths.get;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.DEFAULT_STARTUP_TIMEOUT_SECONDS;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.DEFAULT_TARANTOOL_EXECUTABLE;

@Getter
@Builder
public class TarantoolLocalConfiguration {
    @Builder.Default
    private final String executableApplicationName = DEFAULT_TARANTOOL_EXECUTABLE;
    @Builder.Default
    private final long startupTimeoutSeconds = DEFAULT_STARTUP_TIMEOUT_SECONDS;
    @Builder.Default
    private final String workingDirectory = get(EMPTY_STRING).toAbsolutePath().toString();
}
