package ru.adk.configurator.dao;


import ru.adk.configurator.api.entity.Configuration;
import ru.adk.configurator.api.entity.ModuleKey;
import ru.adk.entity.Entity;
import ru.adk.entity.Value;
import ru.adk.rocks.db.dao.RocksDbPrimitiveDao;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static ru.adk.configurator.constants.ConfiguratorDbConstants.*;
import static ru.adk.rocks.db.dao.RocksDbCollectionDao.getStringList;
import static ru.adk.rocks.db.dao.RocksDbPrimitiveDao.delete;
import static ru.adk.rocks.db.dao.RocksDbValueDao.getAsProtobuf;
import static ru.adk.rocks.db.dao.RocksDbValueDao.putAsProtobuf;
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
        return ofNullable(getAsProtobuf(moduleKey));
    }

    static Set<ModuleKey> getModuleKeys() {
        return getStringList((MODULE_KEYS)).stream().map(ModuleKey::parseKey).collect(toSet());
    }

    static Set<ModuleKey> getProfileKeys() {
        return getStringList((PROFILE_KEYS)).stream().map(ModuleKey::parseKey).collect(toSet());
    }
}
