package ru.art.config.extensions.sql;

import ru.art.config.Config;
import static ru.art.config.extensions.ConfigExtensions.configInner;
import static ru.art.config.extensions.sql.SqlConfigKeys.SQL_DB_SECTION_ID;

public interface SqlConfigProvider {
    static Config sqlDbConfig() {
        return configInner(SQL_DB_SECTION_ID);
    }
}
