/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.sql.configuration;

import lombok.*;
import org.jooq.*;
import io.art.core.module.*;
import io.art.sql.constants.*;
import io.art.sql.exception.*;
import static java.text.MessageFormat.*;
import static io.art.core.extensions.ExceptionExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.sql.constants.ConnectionPoolInitializationMode.BOOTSTRAP;
import static io.art.sql.constants.SqlModuleConstants.ExceptionMessages.*;
import java.util.*;

public interface SqlModuleConfiguration extends ModuleConfiguration {
    SqlModuleDefaultConfiguration DEFAULT_CONFIGURATION = new SqlModuleDefaultConfiguration();

    ConnectionPoolInitializationMode getInitializationMode();

    Map<String, SqlDbConfiguration> getDbConfigurations();

    default SqlDbConfiguration getDbConfiguration(String instanceId) {
        return exceptionIfNull(getDbConfigurations().get(instanceId), new SqlModuleException(format(SQL_DB_CONFIGURATION_NOT_FOUND, instanceId))).toBuilder().build();
    }

    default Configuration getJooqConfiguration(String instanceId) {
        return getDbConfiguration(instanceId).getJooqConfiguration();
    }

    @Getter
    class SqlModuleDefaultConfiguration implements SqlModuleConfiguration {
        private final Map<String, SqlDbConfiguration> dbConfigurations = mapOf();
        private final ConnectionPoolInitializationMode initializationMode = BOOTSTRAP;
    }
}
