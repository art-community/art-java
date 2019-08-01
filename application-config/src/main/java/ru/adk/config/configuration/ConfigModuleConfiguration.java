package ru.adk.config.configuration;

import lombok.Getter;
import ru.adk.config.constants.ConfigType;
import ru.adk.core.module.ModuleConfiguration;
import static ru.adk.config.constants.ConfigModuleConstants.DEFAULT_CACHE_UPDATE_INTERVAL_SECONDS;
import static ru.adk.config.constants.ConfigType.YAML;

public interface ConfigModuleConfiguration extends ModuleConfiguration {
    ConfigType getModuleConfigType();

    int getCacheUpdateIntervalSeconds();

    @Getter
    class ConfigModuleDefaultConfiguration implements ConfigModuleConfiguration {
        private final ConfigType moduleConfigType = YAML;
        private final int cacheUpdateIntervalSeconds = DEFAULT_CACHE_UPDATE_INTERVAL_SECONDS;
    }
}
