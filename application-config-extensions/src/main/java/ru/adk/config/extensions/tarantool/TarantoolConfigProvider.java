package ru.adk.config.extensions.tarantool;

import ru.adk.config.Config;
import static ru.adk.config.extensions.ConfigExtensions.configInner;
import static ru.adk.config.extensions.sql.SqlConfigKeys.SQL_DB_SECTION_ID;

public interface TarantoolConfigProvider {
    static Config sqlDbConfig() {
        return configInner(SQL_DB_SECTION_ID);
    }
}
