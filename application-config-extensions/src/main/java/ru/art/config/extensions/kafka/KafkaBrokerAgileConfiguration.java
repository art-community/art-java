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

import lombok.*;
import ru.art.kafka.broker.configuration.*;
import ru.art.kafka.broker.configuration.KafkaBrokerModuleConfiguration.*;
import ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.kafka.KafkaConfigKeys.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.extension.ExceptionExtensions.*;
import static ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.*;
import static ru.art.kafka.broker.module.KafkaBrokerModule.*;

@Getter
public class KafkaBrokerAgileConfiguration extends KafkaBrokerModuleDefaultConfiguration {
    private ZookeeperConfiguration zookeeperConfiguration = super.getZookeeperConfiguration();
    private ZookeeperInitializationMode zookeeperInitializationMode = super.getZookeeperInitializationMode();
    private KafkaBrokerConfiguration kafkaBrokerConfiguration = super.getKafkaBrokerConfiguration();

    public KafkaBrokerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        ZookeeperConfiguration defaultZookeeperConfiguration = super.getZookeeperConfiguration();
        ZookeeperConfiguration newZookeeperConfiguration = ZookeeperConfiguration.builder()
                .logsDirectory(configString(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, LOGS_DIRECTORY_KEY, defaultZookeeperConfiguration.getLogsDirectory()))
                .port(configInt(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, PORT, defaultZookeeperConfiguration.getPort()))
                .tickTime(configInt(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, TICK_TIME_KEY, defaultZookeeperConfiguration.getTickTime()))
                .maximumConnectedClients(configInt(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, MAXIMUM_CONNECTED_CLIENTS_KEY, defaultZookeeperConfiguration.getMaximumConnectedClients()))
                .build();
        boolean restartZookeeper = false;
        if (!newZookeeperConfiguration.equals(this.zookeeperConfiguration)) {
            restartZookeeper = true;
        }
        this.zookeeperConfiguration = newZookeeperConfiguration;
        ZookeeperInitializationMode newZookeeperInitializationMode = ifException(() -> ZookeeperInitializationMode.valueOf(configString(KAFKA_BROKER_ZOOKEEPER_SECTION_ID, INITIALIZATION_MODE_KEY).toUpperCase()),
                super.getZookeeperInitializationMode());
        if (zookeeperInitializationMode != newZookeeperInitializationMode) {
            restartZookeeper = true;
        }
        this.zookeeperInitializationMode = newZookeeperInitializationMode;
        KafkaBrokerConfiguration defaultKafkaBrokerConfiguration = super.getKafkaBrokerConfiguration();
        KafkaBrokerConfiguration newKafkaBrokerConfiguration = KafkaBrokerConfiguration.builder()
                .additionalProperties(configProperties(KAFKA_BROKER_SECTION_ID, ADDITIONAL_PROPERTIES_KEY))
                .port(configInt(KAFKA_BROKER_SECTION_ID, PORT, defaultKafkaBrokerConfiguration.getPort()))
                .replicationFactor(configInt(KAFKA_BROKER_SECTION_ID, REPLICATION_FACTOR_KEY, defaultKafkaBrokerConfiguration.getReplicationFactor())
                        .shortValue())
                .zookeeperConnection(configString(KAFKA_BROKER_SECTION_ID, ZOOKEEPER_CONNECTION_KEY, defaultKafkaBrokerConfiguration.getZookeeperConnection()))
                .build();
        boolean restartBroker = false;
        if (!newKafkaBrokerConfiguration.equals(this.kafkaBrokerConfiguration)) {
            restartBroker = true;
        }
        this.kafkaBrokerConfiguration = newKafkaBrokerConfiguration;
        if (context().hasModule(KAFKA_BROKER_MODULE_ID) && restartBroker) {
            if (restartZookeeper) {
                kafkaBrokerModuleState().getBroker().restartWithZookeeper();
                return;
            }
            kafkaBrokerModuleState().getBroker().restart();
        }
    }
}
