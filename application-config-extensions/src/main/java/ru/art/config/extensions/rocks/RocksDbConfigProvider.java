package ru.art.config.extensions.rocks;

import ru.art.config.Config;
import static ru.art.config.extensions.ConfigExtensions.configInner;
import static ru.art.config.extensions.rocks.RocksDbConfigKeys.ROCKS_DB_SECTION_ID;

public interface RocksDbConfigProvider {
    static Config rocksDbConfig() {
        return configInner(ROCKS_DB_SECTION_ID);
    }
}
