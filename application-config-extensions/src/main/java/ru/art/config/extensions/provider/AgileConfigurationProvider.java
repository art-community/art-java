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

package ru.art.config.extensions.provider;

import ru.art.config.extensions.grpc.GrpcClientAgileConfiguration;
import ru.art.config.extensions.grpc.GrpcServerAgileConfiguration;
import ru.art.config.extensions.http.HttpClientAgileConfiguration;
import ru.art.config.extensions.http.HttpServerAgileConfiguration;
import ru.art.config.extensions.logging.LoggingAgileConfiguration;
import ru.art.config.extensions.metrics.MetricsAgileConfiguration;
import ru.art.config.extensions.network.NetworkManagerAgileConfiguration;
import ru.art.config.extensions.rocks.RocksDbAgileConfiguration;
import ru.art.config.extensions.rsocket.RsocketAgileConfiguration;
import ru.art.config.extensions.sql.SqlAgileConfiguration;
import ru.art.config.extensions.tarantool.TarantoolAgileConfiguration;
import ru.art.core.module.ModuleConfiguration;
import ru.art.core.provider.PreconfiguredModuleProvider;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.context.Context.defaultContext;
import static ru.art.core.context.Context.withContext;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.GRPC_CLIENT_MODULE_ID;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVER_MODULE_ID;
import static ru.art.http.client.constants.HttpClientModuleConstants.HTTP_CLIENT_MODULE_ID;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVER_MODULE_ID;
import static ru.art.logging.LoggingModuleConstants.LOGGING_MODULE_ID;
import static ru.art.metrics.constants.MetricsModuleConstants.METRICS_MODULE_ID;
import static ru.art.network.manager.constants.NetworkManagerModuleConstants.NETWORK_MANAGER_MODULE_ID;
import static ru.art.rocks.db.constants.RocksDbModuleConstants.ROCKS_DB_MODULE_ID;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_MODULE_ID;
import static ru.art.sql.constants.SqlModuleConstants.SQL_MODULE_ID;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TARANTOOL_MODULE_ID;
import java.util.Optional;

public class AgileConfigurationProvider implements PreconfiguredModuleProvider {
    @Override
    public <T extends ModuleConfiguration> Optional<T> getModuleConfiguration(String moduleId) {
        return withContext(defaultContext(), context -> {
            return getConfiguration(moduleId);
        });
    }

    private static <T extends ModuleConfiguration> Optional<T> getConfiguration(String moduleId) {
        switch (moduleId) {
            case LOGGING_MODULE_ID:
                return of(cast(new LoggingAgileConfiguration()));
            case HTTP_SERVER_MODULE_ID:
                return of(cast(new HttpServerAgileConfiguration()));
            case HTTP_CLIENT_MODULE_ID:
                return of(cast(new HttpClientAgileConfiguration()));
            case GRPC_SERVER_MODULE_ID:
                return of(cast(new GrpcServerAgileConfiguration()));
            case GRPC_CLIENT_MODULE_ID:
                return of(cast(new GrpcClientAgileConfiguration()));
            case METRICS_MODULE_ID:
                return of(cast(new MetricsAgileConfiguration()));
            case NETWORK_MANAGER_MODULE_ID:
                return of(cast(new NetworkManagerAgileConfiguration()));
            case ROCKS_DB_MODULE_ID:
                return of(cast(new RocksDbAgileConfiguration()));
            case SQL_MODULE_ID:
                return of(cast(new SqlAgileConfiguration()));
            case RSOCKET_MODULE_ID:
                return of(cast(new RsocketAgileConfiguration()));
            case TARANTOOL_MODULE_ID:
                return of(cast(new TarantoolAgileConfiguration()));
        }
        return empty();
    }
}
