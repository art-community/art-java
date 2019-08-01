package ru.art.config.extensions.tarantool;

import ru.art.config.Config;
import static ru.art.config.extensions.ConfigExtensions.configInner;
import static ru.art.config.extensions.sql.SqlConfigKeys.SQL_DB_SECTION_ID;

public interface TarantoolConfigProvider {
    static Config sqlDbConfig() {
        return configInner(SQL_DB_SECTION_ID);
    }
}
