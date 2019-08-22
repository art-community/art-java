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

import ru.art.config.extensions.grpc.*;
import ru.art.config.extensions.http.*;
import ru.art.config.extensions.kafka.*;
import ru.art.config.extensions.logging.*;
import ru.art.config.extensions.metrics.*;
import ru.art.config.extensions.network.*;
import ru.art.config.extensions.rocks.*;
import ru.art.config.extensions.rsocket.*;
import ru.art.config.extensions.sql.*;
import ru.art.config.extensions.tarantool.*;
import ru.art.core.context.*;
import ru.art.core.module.*;
import ru.art.core.provider.*;
import java.util.*;
import java.util.function.*;

import static java.util.Optional.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.context.Context.*;
import static ru.art.grpc.client.constants.GrpcClientModuleConstants.*;
import static ru.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static ru.art.http.client.constants.HttpClientModuleConstants.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.*;
import static ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.*;
import static ru.art.kafka.consumer.constants.KafkaConsumerModuleConstants.*;
import static ru.art.kafka.producer.constants.KafkaProducerModuleConstants.*;
import static ru.art.logging.LoggingModuleConstants.*;
import static ru.art.metrics.constants.MetricsModuleConstants.*;
import static ru.art.network.manager.constants.NetworkManagerModuleConstants.*;
import static ru.art.rocks.db.constants.RocksDbModuleConstants.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.sql.constants.SqlModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;

public class AgileConfigurationProvider implements PreconfiguredModuleProvider {
    @Override
    public <T extends ModuleConfiguration> Optional<T> getModuleConfiguration(String moduleId) {
        return withDefaultContext((Function<Context, Optional<T>>) context -> getConfiguration(moduleId));
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
            case KAFKA_BROKER_MODULE_ID:
                return of(cast(new KafkaBrokerAgileConfiguration()));
            case KAFKA_CONSUMER_MODULE_ID:
                return of(cast(new KafkaConsumerAgileConfiguration()));
            case KAFKA_PRODUCER_MODULE_ID:
                return of(cast(new KafkaProducerAgileConfiguration()));
        }
        return empty();
    }
}
