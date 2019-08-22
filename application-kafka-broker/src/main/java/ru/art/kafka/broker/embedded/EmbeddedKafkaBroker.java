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

package ru.art.kafka.broker.embedded;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.art.kafka.broker.configuration.KafkaBrokerConfiguration;
import ru.art.kafka.broker.configuration.ZookeeperConfiguration;
import scala.collection.immutable.List;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.kafka.common.utils.Time.SYSTEM;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.extension.ThreadExtensions.thread;
import static ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.*;
import static ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.KafkaProperties.*;
import static ru.art.kafka.broker.constants.KafkaBrokerModuleConstants.ZookeeperInitializationMode.ON_KAFKA_BROKER_INITIALIZATION;
import static ru.art.kafka.broker.embedded.EmbeddedZookeeper.startupZookeeper;
import static ru.art.kafka.broker.module.KafkaBrokerModule.kafkaBrokerModule;
import static ru.art.kafka.broker.module.KafkaBrokerModule.kafkaBrokerModuleState;
import static scala.Option.empty;
import java.util.Properties;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class EmbeddedKafkaBroker {
    private final KafkaBrokerConfiguration configuration;
    private final KafkaServer server;

    public static void startupKafkaBroker(KafkaBrokerConfiguration kafkaBrokerConfiguration, ZookeeperConfiguration zookeeperConfiguration, ZookeeperInitializationMode zookeeperInitializationMode) {
        if (zookeeperInitializationMode == ON_KAFKA_BROKER_INITIALIZATION) {
            startupZookeeper();
        }
        Properties properties = new Properties();
        properties.put(ZOOKEEPER_CONNECT, kafkaBrokerConfiguration.getZookeeperConnection());
        properties.put(LISTENERS,  PLAINTEXT + COLON + kafkaBrokerConfiguration.getPort());
        properties.put(OFFSETS_TOPIC_REPLICATION_FACTOR,  kafkaBrokerConfiguration.getReplicationFactor());
        properties.putAll(kafkaBrokerConfiguration.getAdditionalProperties());
        KafkaServer kafkaServer = new KafkaServer(new KafkaConfig(properties), SYSTEM, empty(), List.empty());
        EmbeddedKafkaBroker broker = new EmbeddedKafkaBroker(kafkaBrokerConfiguration, kafkaServer);
        kafkaBrokerModuleState().setBroker(broker);
        kafkaServer.startup();
    }

    public static void startupKafkaBroker(KafkaBrokerConfiguration configuration, ZookeeperInitializationMode zookeeperInitializationMode) {
        startupKafkaBroker(configuration, kafkaBrokerModule().getZookeeperConfiguration(), zookeeperInitializationMode);
    }


    public static void startupKafkaBroker(KafkaBrokerConfiguration configuration) {
        startupKafkaBroker(configuration, kafkaBrokerModule().getZookeeperConfiguration(), kafkaBrokerModule().getZookeeperInitializationMode());
    }

    public static void startupKafkaBroker() {
        startupKafkaBroker(kafkaBrokerModule().getKafkaBrokerConfiguration());
    }

    public static void kafkaBrokerInSeparateThread(KafkaBrokerConfiguration kafkaBrokerConfiguration, ZookeeperConfiguration zookeeperConfiguration, ZookeeperInitializationMode zookeeperInitializationMode) {
        thread(KAFKA_BOOTSTRAP_THREAD, () -> startupKafkaBroker(kafkaBrokerConfiguration, zookeeperConfiguration, zookeeperInitializationMode));
    }

    public static void kafkaBrokerInSeparateThread(KafkaBrokerConfiguration configuration, ZookeeperInitializationMode zookeeperInitializationMode) {
        thread(KAFKA_BOOTSTRAP_THREAD, () -> startupKafkaBroker(configuration, zookeeperInitializationMode));
    }

    public static void kafkaBrokerInSeparateThread(KafkaBrokerConfiguration configuration) {
        thread(KAFKA_BOOTSTRAP_THREAD, () -> startupKafkaBroker(configuration));
    }

    public static void kafkaBrokerInSeparateThread() {
        thread(KAFKA_BOOTSTRAP_THREAD, EmbeddedKafkaBroker::startupKafkaBroker);
    }

    public void shutdown() {
        server.shutdown();
        server.awaitShutdown();
    }
}
