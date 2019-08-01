package ru.adk.sql.factory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.jooq.Configuration;
import org.jooq.impl.DefaultConfiguration;
import ru.adk.sql.exception.SqlModuleException;
import ru.adk.sql.model.DbConnectionProperties;
import static java.lang.Class.forName;
import static ru.adk.metrics.module.MetricsModule.metricsModule;
import static ru.adk.sql.constants.SqlModuleConstants.ConfigurationDefaults.DEFAULT_CONNECTION_TEST_QUERY;
import static ru.adk.sql.module.SqlModule.sqlModule;

public interface SqlConnectionPoolsFactory {
    static HikariConfig createHikariPoolConfig() {
        return createHikariPoolConfig(DbConnectionProperties.builder()
                .login(sqlModule().getJdbcLogin())
                .password(sqlModule().getJdbcPassword())
                .url(sqlModule().getJdbcUrl())
                .driver(sqlModule().getDbProvider())
                .build());
    }

    static PoolProperties createTomcatPoolConfig() {
        return createTomcatPoolConfig(DbConnectionProperties.builder()
                .login(sqlModule().getJdbcLogin())
                .password(sqlModule().getJdbcPassword())
                .url(sqlModule().getJdbcUrl())
                .driver(sqlModule().getDbProvider())
                .build());
    }

    static HikariConfig createHikariPoolConfig(DbConnectionProperties properties) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setRegisterMbeans(false);
        hikariConfig.setJdbcUrl(properties.getUrl());
        hikariConfig.setUsername(properties.getLogin());
        hikariConfig.setPassword(properties.getPassword());
        hikariConfig.setConnectionTimeout((long) (60 * 1000));
        hikariConfig.setIdleTimeout((long) (60 * 1000));
        hikariConfig.setMaxLifetime((long) (60 * 1000));
        hikariConfig.setMinimumIdle(10);
        hikariConfig.setMaximumPoolSize(50);
        hikariConfig.setAllowPoolSuspension(false);
        hikariConfig.setInitializationFailTimeout(0);
        hikariConfig.setReadOnly(false);
        hikariConfig.setValidationTimeout((long) (5 * 1000));
        hikariConfig.setLeakDetectionThreshold(0);
        hikariConfig.setConnectionTestQuery(DEFAULT_CONNECTION_TEST_QUERY);

        if (sqlModule().isEnableMetrics()) {
            hikariConfig.setMetricRegistry(metricsModule().getPrometheusMeterRegistry());
        }
        return hikariConfig;
    }

    static PoolProperties createTomcatPoolConfig(DbConnectionProperties properties) {
        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setUrl(properties.getUrl());
        poolProperties.setDriverClassName(properties.getDriver().getDriverClassName());
        poolProperties.setUsername(properties.getLogin());
        poolProperties.setPassword(properties.getPassword());
        poolProperties.setJmxEnabled(true);
        poolProperties.setTestWhileIdle(false);
        poolProperties.setTestOnBorrow(true);
        poolProperties.setValidationQuery(DEFAULT_CONNECTION_TEST_QUERY);
        poolProperties.setTestOnReturn(false);
        poolProperties.setValidationInterval(30000);
        poolProperties.setTimeBetweenEvictionRunsMillis(30000);
        poolProperties.setMaxActive(100);
        poolProperties.setInitialSize(10);
        poolProperties.setMaxWait(10000);
        poolProperties.setMinEvictableIdleTimeMillis(30000);
        poolProperties.setMinIdle(10);
        poolProperties.setLogAbandoned(true);
        poolProperties.setRemoveAbandoned(true);
        poolProperties.setRemoveAbandonedTimeout(60);
        return poolProperties;
    }

    static Configuration createJooqConfiguration(String driveClassName, HikariConfig config) {
        try {
            forName(driveClassName);
            return new DefaultConfiguration().set(new HikariDataSource(config));
        } catch (Exception e) {
            throw new SqlModuleException(e);
        }
    }

    static Configuration createJooqConfiguration(String driveClassName, PoolProperties config) {
        try {
            forName(driveClassName);
            return new DefaultConfiguration().set(new DataSource(config));
        } catch (Exception e) {
            throw new SqlModuleException(e);
        }
    }
}
