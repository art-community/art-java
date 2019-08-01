package ru.art.config.cache;

import ru.art.config.Config;
import ru.art.config.constants.ConfigType;
import ru.art.core.model.cached.CachedObject;
import static java.lang.System.currentTimeMillis;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.getInstance;
import static java.util.Objects.isNull;
import static ru.art.config.module.ConfigModule.configModule;
import static ru.art.core.constants.StringConstants.DOT;
import static ru.art.core.factory.CollectionsFactory.concurrentHashMap;
import java.util.Calendar;
import java.util.Map;

public class ConfigCacheContainer {
    private final Map<String, CachedObject<Config>> configCache = concurrentHashMap();

    public boolean configCacheActualized(String serviceId, ConfigType configType) {
        CachedObject<Config> cachedObject = configCache.get(serviceId + DOT + configType);
        if (isNull(cachedObject)) return true;
        Calendar calendar = getInstance();
        calendar.setTimeInMillis(cachedObject.getLastUpdateTime());
        calendar.add(SECOND, configModule().getCacheUpdateIntervalSeconds());
        return currentTimeMillis() < calendar.getTimeInMillis();
    }

    public Config getConfigFromCache(String configId, ConfigType configType) {
        return configCache.get(configId + DOT + configType).getObject();
    }

    public Config putConfigToCache(String configId, ConfigType configType, Config value) {
        CachedObject<Config> cache = new CachedObject<>(value, currentTimeMillis());
        configCache.put(configId + DOT + configType, cache);
        return value;
    }

    public boolean containsConfig(String serviceId, ConfigType configType) {
        return configCache.containsKey(serviceId + DOT + configType);
    }
}