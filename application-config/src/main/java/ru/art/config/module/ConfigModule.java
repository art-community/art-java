package ru.art.config.module;

import lombok.Getter;
import ru.art.config.configuration.ConfigModuleConfiguration;
import ru.art.config.configuration.ConfigModuleConfiguration.ConfigModuleDefaultConfiguration;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import static ru.art.config.constants.ConfigModuleConstants.CONFIG_MODULE_ID;
import static ru.art.core.context.Context.context;

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
