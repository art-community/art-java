package ru.art.config.configuration;

import lombok.Getter;
import ru.art.config.constants.ConfigType;
import ru.art.core.module.ModuleConfiguration;
import static ru.art.config.constants.ConfigModuleConstants.DEFAULT_CACHE_UPDATE_INTERVAL_SECONDS;
import static ru.art.config.constants.ConfigType.YAML;

public interface ConfigModuleConfiguration extends ModuleConfiguration {
    ConfigType getModuleConfigType();

    int getCacheUpdateIntervalSeconds();

    @Getter
    class ConfigModuleDefaultConfiguration implements ConfigModuleConfiguration {
        private final ConfigType moduleConfigType = YAML;
        private final int cacheUpdateIntervalSeconds = DEFAULT_CACHE_UPDATE_INTERVAL_SECONDS;
    }
}
