package ru.adk.config.extensions.activator;

import ru.adk.config.extensions.provider.AgileConfigurationProvider;
import ru.adk.core.configuration.ContextInitialConfiguration.ApplicationContextConfiguration;
import ru.adk.core.context.Context;
import static ru.adk.core.constants.ContextConstants.DEFAULT_MAIN_MODULE_ID;
import static ru.adk.core.context.Context.initContext;

public interface AgileConfigurationsActivator {
    static Context useAgileConfigurations(String mainModuleId) {
        return initContext(new ApplicationContextConfiguration(mainModuleId, new AgileConfigurationProvider()));
    }

    static Context useAgileConfigurations() {
        return initContext(new ApplicationContextConfiguration(DEFAULT_MAIN_MODULE_ID, new AgileConfigurationProvider()));
    }
}
