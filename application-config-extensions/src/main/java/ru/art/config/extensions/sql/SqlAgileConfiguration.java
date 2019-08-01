package ru.art.config.extensions.sql;

import lombok.Getter;
import ru.art.sql.configuration.SqlModuleConfiguration.SqlModuleDefaultConfiguration;
import ru.art.sql.constants.DbProvider;
import static ru.art.config.extensions.ConfigExtensions.configString;
import static ru.art.config.extensions.common.CommonConfigKeys.URL;
import static ru.art.config.extensions.sql.SqlConfigKeys.*;
import static ru.art.core.extension.ExceptionExtensions.ifException;

@Getter
public class SqlAgileConfiguration extends SqlModuleDefaultConfiguration {
    private String jdbcUrl;
    private String jdbcLogin;
    private String jdbcPassword;
    private DbProvider dbProvider;

    public SqlAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        jdbcUrl = configString(SQL_DB_SECTION_ID, URL, super.getJdbcUrl());
        jdbcLogin = configString(SQL_DB_SECTION_ID, LOGIN, super.getJdbcLogin());
        jdbcPassword = configString(SQL_DB_SECTION_ID, PASSWORD, super.getJdbcLogin());
        dbProvider = ifException(() -> DbProvider.valueOf(configString(SQL_DB_SECTION_ID, PROVIDER).toUpperCase()), super.getDbProvider());
    }
}
