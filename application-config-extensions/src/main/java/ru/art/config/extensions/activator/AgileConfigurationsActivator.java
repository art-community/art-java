package ru.art.config.extensions.activator;

import ru.art.config.extensions.provider.AgileConfigurationProvider;
import ru.art.core.configuration.ContextInitialConfiguration.ApplicationContextConfiguration;
import ru.art.core.context.Context;
import static ru.art.core.constants.ContextConstants.DEFAULT_MAIN_MODULE_ID;
import static ru.art.core.context.Context.initContext;

public interface AgileConfigurationsActivator {
    static Context useAgileConfigurations(String mainModuleId) {
        return initContext(new ApplicationContextConfiguration(mainModuleId, new AgileConfigurationProvider()));
    }

    static Context useAgileConfigurations() {
        return initContext(new ApplicationContextConfiguration(DEFAULT_MAIN_MODULE_ID, new AgileConfigurationProvider()));
    }
}
