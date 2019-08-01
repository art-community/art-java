package ru.art.config.remote.provider;

import ru.art.config.Config;
import ru.art.config.cache.ConfigCacheContainer;
import ru.art.config.exception.ConfigException;
import static java.util.Objects.isNull;
import static ru.art.config.constants.ConfigType.REMOTE_ENTITY_CONFIG;
import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.MODULE_ID_IS_EMPTY;
import static ru.art.config.remote.loader.RemoteConfigLoader.loadRemoteConfig;
import static ru.art.configurator.api.constants.ConfiguratorProxyServiceConstants.CONFIGURATOR_COMMUNICATION_SERVICE_ID;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.service.ServiceModule.serviceModule;

public class RemoteConfigProvider {
    private static final ConfigCacheContainer CONFIG_CACHE_CONTAINER = new ConfigCacheContainer();

    public static Config remoteConfig(String sectionId) {
        return remoteConfig().getConfig(sectionId);
    }

    public static Config remoteConfig() {
        if (!serviceModule().getServiceRegistry().getServices().containsKey(CONFIGURATOR_COMMUNICATION_SERVICE_ID)) {
            return new Config(entityBuilder().build(), REMOTE_ENTITY_CONFIG);
        }

        String moduleId = contextConfiguration().getMainModuleId();

        if (isNull(moduleId)) throw new ConfigException(MODULE_ID_IS_EMPTY);
        if (CONFIG_CACHE_CONTAINER.containsConfig(moduleId, REMOTE_ENTITY_CONFIG) && CONFIG_CACHE_CONTAINER.configCacheActualized(moduleId, REMOTE_ENTITY_CONFIG)) {
            return CONFIG_CACHE_CONTAINER.getConfigFromCache(moduleId, REMOTE_ENTITY_CONFIG);
        }
        return CONFIG_CACHE_CONTAINER.putConfigToCache(moduleId, REMOTE_ENTITY_CONFIG, new Config(loadRemoteConfig(), REMOTE_ENTITY_CONFIG));
    }
}
