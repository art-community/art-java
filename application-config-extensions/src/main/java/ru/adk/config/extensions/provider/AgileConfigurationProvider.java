package ru.adk.config.extensions.provider;

import ru.adk.config.extensions.grpc.GrpcClientAgileConfiguration;
import ru.adk.config.extensions.grpc.GrpcServerAgileConfiguration;
import ru.adk.config.extensions.http.HttpClientAgileConfiguration;
import ru.adk.config.extensions.http.HttpServerAgileConfiguration;
import ru.adk.config.extensions.logging.LoggingAgileConfiguration;
import ru.adk.config.extensions.metrics.MetricsAgileConfiguration;
import ru.adk.config.extensions.network.NetworkManagerAgileConfiguration;
import ru.adk.config.extensions.rocks.RocksDbAgileConfiguration;
import ru.adk.config.extensions.rsocket.RsocketAgileConfiguration;
import ru.adk.config.extensions.sql.SqlAgileConfiguration;
import ru.adk.config.extensions.tarantool.TarantoolAgileConfiguration;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.core.provider.PreconfiguredModuleProvider;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.grpc.client.constants.GrpcClientModuleConstants.GRPC_CLIENT_MODULE_ID;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVER_MODULE_ID;
import static ru.adk.http.client.constants.HttpClientModuleConstants.HTTP_CLIENT_MODULE_ID;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HTTP_SERVER_MODULE_ID;
import static ru.adk.logging.LoggingModuleConstants.LOGGING_MODULE_ID;
import static ru.adk.metrics.constants.MetricsModuleConstants.METRICS_MODULE_ID;
import static ru.adk.network.manager.constants.NetworkManagerModuleConstants.NETWORK_MANAGER_MODULE_ID;
import static ru.adk.rocks.db.constants.RocksDbModuleConstants.ROCKS_DB_MODULE_ID;
import static ru.adk.rsocket.constants.RsocketModuleConstants.RSOCKET_MODULE_ID;
import static ru.adk.sql.constants.SqlModuleConstants.SQL_MODULE_ID;
import static ru.adk.tarantool.constants.TarantoolModuleConstants.TARANTOOL_MODULE_ID;
import java.util.Optional;

public class AgileConfigurationProvider implements PreconfiguredModuleProvider {
    @Override
    public <T extends ModuleConfiguration> Optional<T> getModuleConfiguration(String moduleId) {
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
