package ru.art.sql.constants;

import com.zaxxer.hikari.*;
import lombok.experimental.*;
import org.apache.tomcat.jdbc.pool.*;
import ru.art.sql.model.*;
import static ru.art.core.context.Context.*;
import static ru.art.metrics.module.MetricsModule.*;
import static ru.art.sql.constants.SqlModuleConstants.ConfigurationDefaults.*;
import static ru.art.sql.module.SqlModule.*;

@UtilityClass
public class ConnectionPoolDefaultConfigurations {
    public static HikariConfig createDefaultHikariPoolConfig(DbConnectionProperties properties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setPoolName(HIKARI_POOL_PREFIX + contextConfiguration().getMainModuleId());
        hikariConfig.setJdbcUrl(properties.getUrl());
        hikariConfig.setUsername(properties.getLogin());
        hikariConfig.setPassword(properties.getPassword());
        hikariConfig.setDriverClassName(properties.getDriver().getDriverClassName());

        hikariConfig.setRegisterMbeans(false);
        hikariConfig.setConnectionTimeout(60 * 1000);
        hikariConfig.setIdleTimeout(60 * 1000);
        hikariConfig.setMaxLifetime(60 * 1000);
        hikariConfig.setMinimumIdle(10);
        hikariConfig.setMaximumPoolSize(50);
        hikariConfig.setAllowPoolSuspension(false);
        hikariConfig.setInitializationFailTimeout(0);
        hikariConfig.setReadOnly(false);
        hikariConfig.setValidationTimeout(5 * 1000);
        hikariConfig.setLeakDetectionThreshold(0);

        switch (properties.getDriver()) {
            case POSTGRES:
                hikariConfig.setConnectionTestQuery(DEFAULT_POSTGRES_CONNECTION_TEST_QUERY);
                break;
            case ORACLE:
                hikariConfig.setConnectionTestQuery(DEFAULT_ORACLE_CONNECTION_TEST_QUERY);
                break;
            case MSSQL:
                hikariConfig.setConnectionTestQuery(DEFAULT_MSSQL_CONNECTION_TEST_QUERY);
                break;
        }

        if (sqlModule().isEnableMetrics()) {
            hikariConfig.setMetricRegistry(metricsModule().getPrometheusMeterRegistry());
        }
        return hikariConfig;
    }

    public static PoolProperties createDefaultTomcatPoolConfig(DbConnectionProperties properties) {
        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setName(TOMCAT_POOL_PREFIX + contextConfiguration().getMainModuleId());
        poolProperties.setUrl(properties.getUrl());
        poolProperties.setDriverClassName(properties.getDriver().getDriverClassName());
        poolProperties.setUsername(properties.getLogin());
        poolProperties.setPassword(properties.getPassword());

        poolProperties.setJmxEnabled(true);

        poolProperties.setTestWhileIdle(false);
        poolProperties.setTestOnReturn(false);
        poolProperties.setTestOnBorrow(true);

        switch (properties.getDriver()) {
            case POSTGRES:
                poolProperties.setValidationQuery(DEFAULT_POSTGRES_CONNECTION_TEST_QUERY);
                break;
            case ORACLE:
                poolProperties.setValidationQuery(DEFAULT_ORACLE_CONNECTION_TEST_QUERY);
                break;
            case MSSQL:
                poolProperties.setValidationQuery(DEFAULT_MSSQL_CONNECTION_TEST_QUERY);
                break;
        }

        poolProperties.setValidationInterval(30000);

        poolProperties.setInitialSize(10);
        poolProperties.setMinIdle(10);
        poolProperties.setMaxActive(100);
        poolProperties.setMaxIdle(100);

        poolProperties.setTimeBetweenEvictionRunsMillis(30000);
        poolProperties.setMinEvictableIdleTimeMillis(30000);

        poolProperties.setMaxWait(10000);

        poolProperties.setLogAbandoned(true);
        poolProperties.setRemoveAbandoned(true);
        poolProperties.setRemoveAbandonedTimeout(60);

        return poolProperties;
    }
}
