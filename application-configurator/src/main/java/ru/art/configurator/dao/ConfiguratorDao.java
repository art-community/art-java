/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.configurator.dao;


import ru.art.configurator.api.entity.Configuration;
import ru.art.configurator.api.entity.ModuleKey;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.rocks.db.dao.RocksDbPrimitiveDao;
import static java.util.stream.Collectors.toSet;
import static ru.art.configurator.constants.ConfiguratorDbConstants.*;
import static ru.art.rocks.db.dao.RocksDbCollectionDao.getStringList;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.delete;
import static ru.art.rocks.db.dao.RocksDbValueDao.getAsProtobuf;
import static ru.art.rocks.db.dao.RocksDbValueDao.putAsProtobuf;
import java.util.Optional;
import java.util.Set;

public interface ConfiguratorDao {
    static void saveConfig(Configuration inputConfiguration) {
        Entity configuration = inputConfiguration.getConfiguration();
        Set<String> profiles = configuration.getFieldNames();
        profiles.forEach(profileId -> ConfigurationSaver.saveProfileModulesConfiguration(profileId, configuration.getEntity(profileId)));
    }

    static void saveApplicationConfiguration(Entity applicationConfig) {
        if (applicationConfig.isEmpty()) {
            delete(APPLICATION);
            return;
        }
        putAsProtobuf(APPLICATION, applicationConfig);
    }

    static void saveProfileConfiguration(String profileId, Entity profileConfig) {
        if (profileConfig.isEmpty()) {
            delete(profileId);
            return;
        }
        if (getProfileKeys().stream().noneMatch(key -> key.getProfileId().equalsIgnoreCase(profileId))) {
            RocksDbPrimitiveDao.add(PROFILE_KEYS, profileId);
        }
        putAsProtobuf(profileId, profileConfig);
    }

    static void saveModuleConfiguration(ModuleKey moduleKey, Entity moduleConfig) {
        if (moduleConfig.isEmpty()) {
            delete(moduleKey.formatKey());
            return;
        }
        if (getProfileKeys().stream().noneMatch(key -> key.getProfileId().equalsIgnoreCase(moduleKey.getProfileId()))) {
            RocksDbPrimitiveDao.add(PROFILE_KEYS, moduleKey.getProfileId());
        }
        if (getModuleKeys().stream().noneMatch(key -> key.getProfileId().equalsIgnoreCase(moduleKey.getModuleId()))) {
            RocksDbPrimitiveDao.add(MODULE_KEYS, moduleKey.formatKey());
        }
        putAsProtobuf(moduleKey.formatKey(), moduleConfig);
    }

    static Optional<Value> getConfig(String moduleKey) {
        return getAsProtobuf(moduleKey);
    }

    static Set<ModuleKey> getModuleKeys() {
        return getStringList((MODULE_KEYS)).stream().map(ModuleKey::parseKey).collect(toSet());
    }

    static Set<ModuleKey> getProfileKeys() {
        return getStringList((PROFILE_KEYS)).stream().map(ModuleKey::parseKey).collect(toSet());
    }
}
