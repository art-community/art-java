package ru.adk.config.module;

import lombok.Getter;
import ru.adk.config.configuration.ConfigModuleConfiguration;
import ru.adk.config.configuration.ConfigModuleConfiguration.ConfigModuleDefaultConfiguration;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import static ru.adk.config.constants.ConfigModuleConstants.CONFIG_MODULE_ID;
import static ru.adk.core.context.Context.context;

@Getter
public class ConfigModule implements Module<ConfigModuleConfiguration, ModuleState> {
    @Getter(lazy = true)
    private static final ConfigModuleConfiguration configModule = context().getModule(CONFIG_MODULE_ID, new ConfigModule());
    private final ConfigModuleConfiguration defaultConfiguration = new ConfigModuleDefaultConfiguration();
    private final String id = CONFIG_MODULE_ID;

    public static ConfigModuleConfiguration configModule() {
        return getConfigModule();
    }
}
