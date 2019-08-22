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

package ru.art.config.extensions.kafka;

import lombok.Getter;
import ru.art.kafka.broker.configuration.KafkaBrokerConfiguration;
import ru.art.kafka.broker.configuration.KafkaBrokerModuleConfiguration.KafkaBrokerModuleDefaultConfiguration;
import ru.art.kafka.broker.configuration.ZookeeperConfiguration;
import ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.ZookeeperInitializationMode;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.PORT;
import static ru.art.config.extensions.kafka.KafkaConfigKeys.*;
import static ru.art.core.extension.ExceptionExtensions.ifException;

@Getter
public class KafkaBrokerAgileConfiguration extends KafkaBrokerModuleDefaultConfiguration {
    private ZookeeperConfiguration zookeeperConfiguration;
    private ZookeeperInitializationMode zookeeperInitializationMode;
    private KafkaBrokerConfiguration kafkaBrokerConfiguration;

    public KafkaBrokerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        ZookeeperConfiguration zookeeperConfiguration = super.getZookeeperConfiguration();
        this.zookeeperConfiguration = ZookeeperConfiguration.builder()
                .logsDirectory(configString(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, LOGS_DIRECTORY_KEY, zookeeperConfiguration.getLogsDirectory()))
                .port(configInt(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, PORT, zookeeperConfiguration.getPort()))
                .tickTime(configInt(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, TICK_TIME_KEY, zookeeperConfiguration.getTickTime()))
                .maximumConnectedClients(configInt(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, MAXIMUM_CONNECTED_CLIENTS_KEY, zookeeperConfiguration.getMaximumConnectedClients()))
                .build();
        zookeeperInitializationMode = ifException(() -> ZookeeperInitializationMode.valueOf(configString(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, INITIALIZATION_MODE_KEY).toUpperCase()), super.getZookeeperInitializationMode());
        KafkaBrokerConfiguration kafkaBrokerConfiguration = super.getKafkaBrokerConfiguration();
        this.kafkaBrokerConfiguration = KafkaBrokerConfiguration.builder()
                .additionalProperties(configProperties(KAFKA_BROKER_SECTION_ID, ADDITIONAL_PROPERTIES_KEY))
                .port(configInt(KAFKA_BROKER_SECTION_ID, PORT, kafkaBrokerConfiguration.getPort()))
                .replicationFactor(configInt(KAFKA_BROKER_SECTION_ID, REPLICATION_FACTOR_KEY, kafkaBrokerConfiguration.getReplicationFactor())
                        .shortValue())
                .zookeeperConnection(configString(KAFKA_BROKER_SECTION_ID, ZOOKEEPER_CONNECTION_KEY, kafkaBrokerConfiguration.getZookeeperConnection()))
                .build();
    }
}
