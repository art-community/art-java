package ru.adk.sql.configuration;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.jooq.Configuration;
import org.jooq.impl.DefaultConfiguration;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.sql.constants.ConnectionPoolType;
import ru.adk.sql.constants.DbProvider;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.sql.constants.ConnectionPoolType.TOMCAT;
import static ru.adk.sql.constants.DbProvider.POSTGRES;
import static ru.adk.sql.factory.SqlConnectionPoolsFactory.createHikariPoolConfig;
import static ru.adk.sql.factory.SqlConnectionPoolsFactory.createTomcatPoolConfig;

public interface SqlModuleConfiguration extends ModuleConfiguration {
    Configuration getJooqConfiguration();

    HikariConfig getHikariPoolConfig();

    PoolConfiguration getTomcatPoolConfig();

    ConnectionPoolType getConnectionPoolType();

    String getJdbcUrl();

    String getJdbcLogin();

    String getJdbcPassword();

    DbProvider getDbProvider();

    boolean isEnableMetrics();

    @Getter
    class SqlModuleDefaultConfiguration implements SqlModuleConfiguration {
        private final Configuration jooqConfiguration = new DefaultConfiguration();
        @Getter(lazy = true)
        private final HikariConfig hikariPoolConfig = createHikariPoolConfig();
        @Getter(lazy = true)
        private final PoolProperties tomcatPoolConfig = createTomcatPoolConfig();
        private final ConnectionPoolType connectionPoolType = TOMCAT;
        private final String jdbcUrl = EMPTY_STRING;
        private final String jdbcLogin = EMPTY_STRING;
        private final String jdbcPassword = EMPTY_STRING;
        private final DbProvider dbProvider = POSTGRES;
        private final boolean enableMetrics = true;
    }
}
