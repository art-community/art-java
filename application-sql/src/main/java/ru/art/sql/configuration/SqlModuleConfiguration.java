/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.sql.configuration;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.jooq.Configuration;
import org.jooq.impl.DefaultConfiguration;
import ru.art.core.module.ModuleConfiguration;
import ru.art.sql.constants.ConnectionPoolType;
import ru.art.sql.constants.DbProvider;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.sql.constants.ConnectionPoolType.TOMCAT;
import static ru.art.sql.constants.DbProvider.POSTGRES;
import static ru.art.sql.factory.SqlConnectionPoolsFactory.createHikariPoolConfig;
import static ru.art.sql.factory.SqlConnectionPoolsFactory.createTomcatPoolConfig;

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
