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

import lombok.*;
import ru.art.core.module.*;
import ru.art.sql.configuration.*;
import ru.art.sql.exception.*;
import static java.lang.Class.*;
import static lombok.AccessLevel.*;
import static ru.art.core.context.Context.*;
import static ru.art.sql.configuration.SqlModuleConfiguration.*;
import static ru.art.sql.constants.SqlModuleConstants.*;
import javax.sql.*;

@Getter
public class SqlModule implements Module<SqlModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final SqlModuleConfiguration sqlModule = context().getModule(SQL_MODULE_ID, SqlModule::new);
    private final String id = SQL_MODULE_ID;
    private final SqlModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;

    public static SqlModuleConfiguration sqlModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getSqlModule();
    }

    @Override
    public void onLoad() {
        DataSource dataSource = null;
        switch (sqlModule().getConnectionPoolType()) {
            case HIKARI:
                dataSource = sqlModule().getHikariPoolConfig().getDataSource();
                break;
            case TOMCAT:
                dataSource = new org.apache.tomcat.jdbc.pool.DataSource(sqlModule().getTomcatPoolConfig());
                break;
        }
        try {
            forName(sqlModule().getDbProvider().getDriverClassName());
            sqlModule().getJooqConfiguration().set(dataSource);
        } catch (Exception throwable) {
            throw new SqlModuleException(throwable);
        }

    }
}
