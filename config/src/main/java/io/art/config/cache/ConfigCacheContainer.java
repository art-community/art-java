/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.config.cache;

import io.art.config.*;
import io.art.config.constants.*;
import io.art.core.model.cached.*;
import static java.lang.System.*;
import static java.util.Calendar.*;
import static java.util.Objects.*;
import static io.art.config.module.ConfigModule.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.CollectionsFactory.*;
import java.util.*;

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
