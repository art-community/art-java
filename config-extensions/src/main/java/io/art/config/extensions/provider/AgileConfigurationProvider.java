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

package io.art.config.extensions.provider;

import io.art.config.extensions.grpc.*;
import io.art.config.extensions.http.*;
import io.art.config.extensions.kafka.*;
import io.art.config.extensions.logging.*;
import io.art.config.extensions.metrics.*;
import io.art.config.extensions.network.*;
import io.art.config.extensions.rocks.*;
import io.art.config.extensions.rsocket.*;
import io.art.config.extensions.service.*;
import io.art.config.extensions.sql.*;
import io.art.config.extensions.tarantool.*;
import io.art.core.module.*;
import io.art.core.provider.*;
import static java.util.Optional.*;
import static io.art.core.caster.Caster.*;
import static io.art.grpc.client.constants.GrpcClientModuleConstants.*;
import static io.art.grpc.server.constants.GrpcServerModuleConstants.*;
import static io.art.http.client.constants.HttpClientModuleConstants.*;
import static io.art.http.server.constants.HttpServerModuleConstants.*;
import static io.art.kafka.broker.constants.KafkaBrokerModuleConstants.*;
import static io.art.kafka.consumer.constants.KafkaConsumerModuleConstants.*;
import static io.art.kafka.producer.constants.KafkaProducerModuleConstants.*;
import static io.art.logging.LoggingModuleConstants.*;
import static io.art.metrics.constants.MetricsModuleConstants.*;
import static io.art.network.manager.constants.NetworkManagerModuleConstants.*;
import static io.art.rocks.db.constants.RocksDbModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.service.constants.ServiceModuleConstants.*;
import static io.art.sql.constants.SqlModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import java.util.*;

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
            case KAFKA_BROKER_MODULE_ID:
                return of(cast(new KafkaBrokerAgileConfiguration()));
            case KAFKA_CONSUMER_MODULE_ID:
                return of(cast(new KafkaConsumerAgileConfiguration()));
            case KAFKA_PRODUCER_MODULE_ID:
                return of(cast(new KafkaProducerAgileConfiguration()));
            case SERVICE_MODULE_ID:
                return of(cast(new ServiceAgileConfiguration()));
        }
        return empty();
    }
}
