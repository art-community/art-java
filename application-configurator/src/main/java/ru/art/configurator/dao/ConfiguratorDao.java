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

package ru.art.configurator.dao;


import ru.art.configurator.api.model.*;
import ru.art.entity.*;
import ru.art.rocks.db.dao.*;
import static java.util.stream.Collectors.*;
import static ru.art.configurator.constants.ConfiguratorDbConstants.*;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.rocks.db.dao.RocksDbCollectionDao.*;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.*;
import static ru.art.rocks.db.dao.RocksDbValueDao.*;
import java.util.*;

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
        putBinary(APPLICATION, applicationConfig);
    }

    static void saveProfileConfiguration(String profileId, Entity profileConfig) {
        if (profileConfig.isEmpty()) {
            delete(profileId);
            return;
        }
        if (getProfileKeys().stream().noneMatch(key -> key.getProfileId().equalsIgnoreCase(profileId))) {
            RocksDbPrimitiveDao.add(PROFILE_KEYS, profileId);
        }
        putBinary(profileId, profileConfig);
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
        putBinary(moduleKey.formatKey(), moduleConfig);
    }

    static Optional<Value> getConfig(String moduleKey) {
        return getBinary(moduleKey);
    }

    static Set<ModuleKey> getModuleKeys() {
        return getStringList((MODULE_KEYS)).stream().map(ModuleKey::parseKey).collect(toSet());
    }

    static Set<ModuleKey> getProfileKeys() {
        return getStringList((PROFILE_KEYS)).stream().map(ModuleKey::parseKey).collect(toSet());
    }

    static void deleteModule(String moduleKey) {
        if (isEmpty(moduleKey)) return;

        delete(moduleKey);
        removeStringElement(MODULE_KEYS, moduleKey);
    }

    static void deleteProfile(String profile) {
        if (isEmpty(profile)) return;

        delete(profile);
        removeStringElement(PROFILE_KEYS, profile);

        getStringList((MODULE_KEYS))
                .stream()
                .map(ModuleKey::parseKey)
                .filter(moduleKey -> profile.equals(moduleKey.getProfileId()))
                .forEach(moduleKey -> deleteModule(moduleKey.formatKey()));
    }
}
