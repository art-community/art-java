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

package ru.art.config.extensions.sql;

import com.zaxxer.hikari.*;
import lombok.*;
import org.apache.tomcat.jdbc.pool.*;
import org.jooq.*;
import org.jooq.conf.*;
import ru.art.config.*;
import ru.art.sql.configuration.*;
import ru.art.sql.configuration.SqlModuleConfiguration.*;
import ru.art.sql.constants.*;
import ru.art.sql.model.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.sql.SqlConfigKeys.*;
import static ru.art.core.extension.ExceptionExtensions.*;
import static ru.art.sql.constants.ConnectionPoolDefaultConfigurations.*;
import static ru.art.sql.factory.SqlConnectionPoolsFactory.*;
import java.util.*;
import java.util.function.*;

@Getter
public class SqlAgileConfiguration extends SqlModuleDefaultConfiguration {
    private Map<String, SqlDbConfiguration> dbConfigurations = super.getDbConfigurations();
    private ConnectionPoolInitializationMode initializationMode = super.getInitializationMode();

    public SqlAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        initializationMode = ifException(
                () -> ConnectionPoolInitializationMode.valueOf(configString(SQL_DB_INSTANCES_SECTION_ID, POOL_INITIALIZATION_MODE).toUpperCase()),
                initializationMode
        );
        Function<Config, SqlDbConfiguration> mapper = config -> {
            Settings defaultSettings = new Settings();
            DbConnectionProperties properties = DbConnectionProperties.builder()
                    .driver(DbProvider.valueOf(config.getString(PROVIDER).toUpperCase()))
                    .url(config.getString(URL))
                    .login(config.getString(LOGIN))
                    .password(config.getString(PASSWORD))
                    .build();
            HikariConfig hikariConfig = extractHikariPoolConfig(config, properties);
            PoolProperties tomcatPoolConfig = extractTomcatPoolConfig(config, properties);
            Configuration jooqConfiguration;
            ConnectionPoolType connectionPoolType = ConnectionPoolType.valueOf(config.getString(POOL_TYPE).toUpperCase());
            switch (connectionPoolType) {
                case HIKARI:
                    jooqConfiguration = createJooqConfiguration(properties.getDriver().getDriverClassName(), hikariConfig);
                    break;
                default:
                    jooqConfiguration = createJooqConfiguration(properties.getDriver().getDriverClassName(), tomcatPoolConfig);
                    break;
            }
            return SqlDbConfiguration.builder()
                    .connectionPoolType(connectionPoolType)
                    .enableMetrics(ifExceptionOrEmpty(() -> config.getBool(ENABLE_METRICS), false))
                    .jooqConfiguration(jooqConfiguration.set(defaultSettings.withQueryTimeout(config.getInt(QUERY_TIMEOUT))))
                    .hikariPoolConfig(hikariConfig)
                    .tomcatPoolConfig(tomcatPoolConfig)
                    .build();
        };
        dbConfigurations = configInnerMap(SQL_DB_INSTANCES_SECTION_ID, mapper, super.getDbConfigurations());
    }

    private HikariConfig extractHikariPoolConfig(Config config, DbConnectionProperties properties) {
        HikariConfig hikariPoolConfig = createDefaultHikariPoolConfig(properties);
        hikariPoolConfig.setPoolName(ifExceptionOrEmpty(() -> config.getString(POOL_NAME), hikariPoolConfig.getPoolName());
        hikariPoolConfig.setRegisterMbeans(configBoolean(POOL_HIKARI_SECTION_DI, HIKARI_REGISTER_MBEANS, super.getHikariPoolConfig().isRegisterMbeans()));
        hikariPoolConfig.setConnectionTimeout(configLong(POOL_HIKARI_SECTION_DI, HIKARI_CONNECTION_TIMEOUT_MILLIS, super.getHikariPoolConfig().getConnectionTimeout()));
        hikariPoolConfig.setIdleTimeout(configLong(POOL_HIKARI_SECTION_DI, HIKARI_IDLE_TIMEOUT_MILLIS, super.getHikariPoolConfig().getIdleTimeout()));
        hikariPoolConfig.setMaxLifetime(configLong(POOL_HIKARI_SECTION_DI, HIKARI_MAX_LIFETIME_MILLIS, super.getHikariPoolConfig().getMaxLifetime()));
        hikariPoolConfig.setMinimumIdle(configInt(POOL_HIKARI_SECTION_DI, HIKARI_MINIMUM_IDLE, super.getHikariPoolConfig().getMinimumIdle()));
        hikariPoolConfig.setMaximumPoolSize(configInt(POOL_HIKARI_SECTION_DI, HIKARI_MAXIMUM_POOL_SIZE, super.getHikariPoolConfig().getMaximumPoolSize()));
        hikariPoolConfig.setAllowPoolSuspension(configBoolean(POOL_HIKARI_SECTION_DI, HIKARI_ALLOW_POOL_SUSPENSION, super.getHikariPoolConfig().isAllowPoolSuspension()));
        hikariPoolConfig.setInitializationFailTimeout(configLong(POOL_HIKARI_SECTION_DI, HIKARI_INITIALIZATION_FAIL_TIMEOUT_MILLIS,
                super.getHikariPoolConfig().getInitializationFailTimeout()));
        hikariPoolConfig.setReadOnly(configBoolean(POOL_HIKARI_SECTION_DI, HIKARI_READ_ONLY, super.getHikariPoolConfig().isReadOnly()));
        hikariPoolConfig.setValidationTimeout(configLong(POOL_HIKARI_SECTION_DI, HIKARI_VALIDATION_TIMEOUT_MILLIS, super.getHikariPoolConfig().getValidationTimeout()));
        hikariPoolConfig.setLeakDetectionThreshold(configLong(POOL_HIKARI_SECTION_DI, HIKARI_LEAK_DETECTION_THRESHOLD_MILLIS, super.getHikariPoolConfig().getLeakDetectionThreshold()));
        return hikariPoolConfig;
    }

    private PoolProperties extractTomcatPoolConfig(Config config, DbConnectionProperties properties) {
        PoolProperties tomcatPoolConfig = new PoolProperties();
        tomcatPoolConfig.setName(configString(POOL_TOMCAT_SECTION_DI, POOL_NAME, super.getTomcatPoolConfig().getName()));
        tomcatPoolConfig.setJmxEnabled(configBoolean(POOL_TOMCAT_SECTION_DI, TOMCAT_JMX_ENABLED, super.getTomcatPoolConfig().isJmxEnabled()));
        tomcatPoolConfig.setTestWhileIdle(configBoolean(POOL_TOMCAT_SECTION_DI, TOMCAT_TEST_WHILE_IDLE, super.getTomcatPoolConfig().isTestWhileIdle()));
        tomcatPoolConfig.setTestOnReturn(configBoolean(POOL_TOMCAT_SECTION_DI, TOMCAT_TEST_ON_RETURN, super.getTomcatPoolConfig().isTestOnReturn()));
        tomcatPoolConfig.setTestOnBorrow(configBoolean(POOL_TOMCAT_SECTION_DI, TOMCAT_TEST_ON_BORROW, super.getTomcatPoolConfig().isTestOnBorrow()));
        tomcatPoolConfig.setTestOnConnect(configBoolean(POOL_TOMCAT_SECTION_DI, TOMCAT_TEST_ON_CONNECT, super.getTomcatPoolConfig().isTestOnConnect()));
        tomcatPoolConfig.setValidationQuery(configString(POOL_TOMCAT_SECTION_DI, TOMCAT_VALIDATION_QUERY, super.getTomcatPoolConfig().getValidationQuery()));
        tomcatPoolConfig.setValidationInterval(configLong(POOL_TOMCAT_SECTION_DI, TOMCAT_VALIDATION_INTERVAL_MILLIS, super.getTomcatPoolConfig().getValidationInterval()));
        tomcatPoolConfig.setInitialSize(configInt(POOL_TOMCAT_SECTION_DI, TOMCAT_INITIAL_SIZE, super.getTomcatPoolConfig().getInitialSize()));
        tomcatPoolConfig.setMinIdle(configInt(POOL_TOMCAT_SECTION_DI, TOMCAT_MIN_IDLE, super.getTomcatPoolConfig().getMinIdle()));
        tomcatPoolConfig.setMaxActive(configInt(POOL_TOMCAT_SECTION_DI, TOMCAT_MAX_ACTIVE, super.getTomcatPoolConfig().getMaxActive()));
        tomcatPoolConfig.setMaxIdle(configInt(POOL_TOMCAT_SECTION_DI, TOMCAT_MAX_IDLE, super.getTomcatPoolConfig().getMaxIdle()));
        tomcatPoolConfig.setMaxAge(configLong(POOL_TOMCAT_SECTION_DI, TOMCAT_MAX_LIFE_TIME_MILLIS, super.getTomcatPoolConfig().getMaxAge()));
        tomcatPoolConfig.setTimeBetweenEvictionRunsMillis(configInt(POOL_TOMCAT_SECTION_DI, TOMCAT_TIME_BETWEEN_EVICTION_RUNS_MILLIS,
                super.getTomcatPoolConfig().getTimeBetweenEvictionRunsMillis()));
        tomcatPoolConfig.setMinEvictableIdleTimeMillis(configInt(POOL_TOMCAT_SECTION_DI, TOMCAT_MIN_EVICTABLE_IDLE_TIME_MILLIS,
                super.getTomcatPoolConfig().getMinEvictableIdleTimeMillis()));
        tomcatPoolConfig.setMaxWait(configInt(POOL_TOMCAT_SECTION_DI, TOMCAT_MAX_WAIT_MILLIS, super.getTomcatPoolConfig().getMaxWait()));
        tomcatPoolConfig.setLogAbandoned(configBoolean(POOL_TOMCAT_SECTION_DI, TOMCAT_LOG_ABANDONED, super.getTomcatPoolConfig().isLogAbandoned()));
        tomcatPoolConfig.setRemoveAbandoned(configBoolean(POOL_TOMCAT_SECTION_DI, TOMCAT_REMOVE_ABANDONED, super.getTomcatPoolConfig().isRemoveAbandoned()));
        tomcatPoolConfig.setRemoveAbandonedTimeout(configInt(POOL_TOMCAT_SECTION_DI, TOMCAT_REMOVE_ABANDONED_TIMEOUT_MILLIS, super.getTomcatPoolConfig().getRemoveAbandonedTimeout()));
        return tomcatPoolConfig;
    }
}