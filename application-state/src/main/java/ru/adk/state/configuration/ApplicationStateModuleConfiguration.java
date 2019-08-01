package ru.adk.state.configuration;

import lombok.Getter;
import ru.adk.core.module.ModuleConfiguration;
import static ru.adk.state.constants.StateModuleConstants.DEFAULT_MODULE_ENDPOINT_CHECK_RATE_SECONDS;
import static ru.adk.state.constants.StateModuleConstants.DEFAULT_MODULE_ENDPOINT_LIFE_TIME_MINUTES;

public interface ApplicationStateModuleConfiguration extends ModuleConfiguration {
    long getModuleEndpointCheckRateSeconds();

    long getModuleEndpointLifeTimeMinutes();

    @Getter
    class ApplicationStateModuleDefaultConfiguration implements ApplicationStateModuleConfiguration {
        private final long moduleEndpointCheckRateSeconds = DEFAULT_MODULE_ENDPOINT_CHECK_RATE_SECONDS;
        private final long moduleEndpointLifeTimeMinutes = DEFAULT_MODULE_ENDPOINT_LIFE_TIME_MINUTES;
    }
}
