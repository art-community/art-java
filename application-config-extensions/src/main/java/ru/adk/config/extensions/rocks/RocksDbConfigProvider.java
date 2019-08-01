package ru.adk.config.extensions.rocks;

import ru.adk.config.Config;
import static ru.adk.config.extensions.ConfigExtensions.configInner;
import static ru.adk.config.extensions.rocks.RocksDbConfigKeys.ROCKS_DB_SECTION_ID;

public interface RocksDbConfigProvider {
    static Config rocksDbConfig() {
        return configInner(ROCKS_DB_SECTION_ID);
    }
}
