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
import static ru.art.sql.constants.ConnectionPoolDefaultConfigurations.*;
import static ru.art.sql.module.SqlModule.*;

public interface SqlConnectionPoolsFactory {
    static HikariConfig createHikariPoolConfig() {
        return createDefaultHikariPoolConfig(DbConnectionProperties.builder()
                .login(sqlModule().getJdbcLogin())
                .password(sqlModule().getJdbcPassword())
                .url(sqlModule().getJdbcUrl())
                .driver(sqlModule().getDbProvider())
                .build());
    }

    static PoolProperties createTomcatPoolConfig() {
        return createDefaultTomcatPoolConfig(DbConnectionProperties.builder()
                .login(sqlModule().getJdbcLogin())
                .password(sqlModule().getJdbcPassword())
                .url(sqlModule().getJdbcUrl())
                .driver(sqlModule().getDbProvider())
                .build());
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
