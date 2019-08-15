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

package ru.art.config.extensions.sql;

import lombok.Getter;
import ru.art.sql.configuration.SqlModuleConfiguration.SqlModuleDefaultConfiguration;
import ru.art.sql.constants.DbProvider;
import static ru.art.config.extensions.ConfigExtensions.configBoolean;
import static ru.art.config.extensions.ConfigExtensions.configString;
import static ru.art.config.extensions.common.CommonConfigKeys.ENABLE_METRICS;
import static ru.art.config.extensions.common.CommonConfigKeys.URL;
import static ru.art.config.extensions.sql.SqlConfigKeys.*;
import static ru.art.core.extension.ExceptionExtensions.ifException;

@Getter
public class SqlAgileConfiguration extends SqlModuleDefaultConfiguration {
    private String jdbcUrl;
    private String jdbcLogin;
    private String jdbcPassword;
    private DbProvider dbProvider;
    private boolean enableMetrics;

    public SqlAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        jdbcUrl = configString(SQL_DB_SECTION_ID, URL, super.getJdbcUrl());
        jdbcLogin = configString(SQL_DB_SECTION_ID, LOGIN, super.getJdbcLogin());
        jdbcPassword = configString(SQL_DB_SECTION_ID, PASSWORD, super.getJdbcLogin());
        dbProvider = ifException(() -> DbProvider.valueOf(configString(SQL_DB_SECTION_ID, PROVIDER).toUpperCase()), super.getDbProvider());
        enableMetrics = configBoolean(SQL_DB_SECTION_ID, ENABLE_METRICS, super.isEnableMetrics());
    }
}
