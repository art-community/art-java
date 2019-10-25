/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.sql.factory;

import com.zaxxer.hikari.*;
import org.apache.tomcat.jdbc.pool.*;
import org.jooq.*;
import org.jooq.impl.*;
import ru.art.sql.exception.*;
import ru.art.sql.model.*;
import static java.lang.Class.*;
import static ru.art.metrics.module.MetricsModule.*;
import static ru.art.sql.constants.SqlModuleConstants.ConfigurationDefaults.*;
import static ru.art.sql.module.SqlModule.*;

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
        } catch (Throwable throwable) {
            throw new SqlModuleException(throwable);
        }
    }

    static Configuration createJooqConfiguration(String driveClassName, PoolProperties config) {
        try {
            forName(driveClassName);
            return new DefaultConfiguration().set(new DataSource(config));
        } catch (Throwable throwable) {
            throw new SqlModuleException(throwable);
        }
    }
}
