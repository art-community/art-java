package ru.adk.config;

import lombok.NoArgsConstructor;
import ru.adk.config.cache.ConfigCacheContainer;
import ru.adk.config.constants.ConfigType;
import ru.adk.config.exception.ConfigException;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.config.ConfigLoader.loadLocalConfig;
import static ru.adk.config.constants.ConfigExceptionMessages.CONFIG_ID_IS_NULL;
import static ru.adk.config.constants.ConfigExceptionMessages.CONFIG_TYPE_IS_NULL;
import static ru.adk.config.constants.ConfigModuleConstants.COMMON_CONFIG_ID;
import static ru.adk.config.module.ConfigModule.configModule;

@NoArgsConstructor(access = PRIVATE)
public class ConfigProvider {
    private static final ConfigCacheContainer CONFIG_CACHE_CONTAINER = new ConfigCacheContainer();

    public static Config commonConfig() {
        return config(COMMON_CONFIG_ID, configModule().getModuleConfigType());
    }

    public static Config commonConfig(ConfigType configType) {
        return config(COMMON_CONFIG_ID, configType);
    }

    public static Config config(String configId) {
        return config(configId, configModule().getModuleConfigType());
    }

    public static Config config(String configId, ConfigType configType) {
        if (isNull(configId)) throw new ConfigException(CONFIG_ID_IS_NULL);
        if (isNull(configType)) throw new ConfigException(CONFIG_TYPE_IS_NULL);
        if (CONFIG_CACHE_CONTAINER.containsConfig(configId, configType) && CONFIG_CACHE_CONTAINER.configCacheActualized(configId, configType)) {
            return CONFIG_CACHE_CONTAINER.getConfigFromCache(configId, configType);
        }
        return CONFIG_CACHE_CONTAINER.putConfigToCache(configId, configType, loadLocalConfig(configId, configType));
    }
}
