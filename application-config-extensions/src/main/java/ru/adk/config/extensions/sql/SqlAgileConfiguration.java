package ru.adk.config.extensions.sql;

import lombok.Getter;
import ru.adk.sql.configuration.SqlModuleConfiguration.SqlModuleDefaultConfiguration;
import ru.adk.sql.constants.DbProvider;
import static ru.adk.config.extensions.ConfigExtensions.configString;
import static ru.adk.config.extensions.common.CommonConfigKeys.URL;
import static ru.adk.config.extensions.sql.SqlConfigKeys.*;
import static ru.adk.core.extension.ExceptionExtensions.ifException;

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
