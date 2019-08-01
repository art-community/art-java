package ru.adk.config.extensions.sql;

import ru.adk.config.Config;
import static ru.adk.config.extensions.ConfigExtensions.configInner;
import static ru.adk.config.extensions.sql.SqlConfigKeys.SQL_DB_SECTION_ID;

public interface SqlConfigProvider {
    static Config sqlDbConfig() {
        return configInner(SQL_DB_SECTION_ID);
    }
}
