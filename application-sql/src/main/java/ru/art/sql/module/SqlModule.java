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

package ru.art.sql.module;

import com.zaxxer.hikari.*;
import io.dropwizard.db.*;
import lombok.*;
import org.apache.logging.log4j.*;
import ru.art.core.module.Module;
import ru.art.sql.configuration.*;
import ru.art.sql.exception.*;
import ru.art.sql.state.*;
import static java.lang.Class.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.wrapper.ExceptionWrapper.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.metrics.module.MetricsModule.*;
import static ru.art.sql.configuration.SqlModuleConfiguration.*;
import static ru.art.sql.constants.ConnectionPoolInitializationMode.*;
import static ru.art.sql.constants.SqlModuleConstants.LoggingMessages.*;
import static ru.art.sql.constants.SqlModuleConstants.*;
import javax.sql.*;
import java.util.function.*;

@Getter
public class SqlModule implements Module<SqlModuleConfiguration, SqlModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final SqlModuleConfiguration sqlModule = context().getModule(SQL_MODULE_ID, SqlModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private static final SqlModuleState sqlModuleState = context().getModuleState(SQL_MODULE_ID, SqlModule::new);
    private final String id = SQL_MODULE_ID;
    private final SqlModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;
    private final SqlModuleState state = new SqlModuleState();
    @Getter(lazy = true, value = PRIVATE)
    private final Logger logger = loggingModule().getLogger(SqlModule.class);

    public static SqlModuleConfiguration sqlModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getSqlModule();
    }

    public static SqlModuleState sqlModuleState() {
        return getSqlModuleState();
    }

    @Override
    public void onLoad() {
        DataSource dataSource = null;
        try {
            switch (sqlModule().getConnectionPoolType()) {
                case HIKARI:
                    HikariDataSource hikariDataSource = new HikariDataSource(sqlModule().getHikariPoolConfig());
                    dataSource = hikariDataSource;
                    sqlModuleState().hikariDataSource(hikariDataSource);
                    if (sqlModule().getConnectionPoolInitializationMode() == ON_MODULE_LOAD) {
                        hikariDataSource.getConnection();
                        getLogger().info(format(STARING_POOL, hikariDataSource));
                    }
                    break;
                case TOMCAT:
                    ManagedPooledDataSource tomcatDataSource = new ManagedPooledDataSource(sqlModule().getTomcatPoolConfig(), metricsModule().getDropwizardMetricRegistry());
                    sqlModuleState().tomcatDataSource(tomcatDataSource);
                    dataSource = tomcatDataSource;
                    if (sqlModule().getConnectionPoolInitializationMode() == ON_MODULE_LOAD) {
                        tomcatDataSource.start();
                        getLogger().info(format(STARING_POOL, tomcatDataSource));
                    }
                    break;
            }
            forName(sqlModule().getDbProvider().getDriverClassName());
            sqlModule().getJooqConfiguration().set(dataSource).set(sqlModule().getJooqSettings());
        } catch (Exception throwable) {
            throw new SqlModuleException(throwable);
        }
    }

    @Override
    public void onUnload() {
        try {
            switch (sqlModule().getConnectionPoolType()) {
                case HIKARI:
                    doIfNotNull(sqlModuleState().hikariDataSource(), (Consumer<HikariDataSource>) pool -> ignoreException(() -> {
                        pool.close();
                        getLogger().info(format(CLOSING_POOL, pool));
                    }, getLogger()::error));
                    break;
                case TOMCAT:
                    doIfNotNull(sqlModuleState().tomcatDataSource(), (Consumer<ManagedPooledDataSource>) pool -> ignoreException(() -> {
                        pool.close();
                        getLogger().info(format(CLOSING_POOL, pool));
                    }, getLogger()::error));
                    break;
            }
        } catch (Throwable throwable) {
            getLogger().error(throwable.getMessage(), throwable);
        }
    }
}
