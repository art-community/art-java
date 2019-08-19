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

package ru.art.kafka.broker.constants;

import static ru.art.core.network.selector.PortSelector.findAvailableTcpPort;

public interface KafkaBrokerModuleConstants {
    String KAFKA_BROKER_MODULE_ID = "KAFKA_BROKER_MODULE";
    String DEFAULT_ZOOKEEPER_SNAPSHOTS_DIRECTORY = "snapshots";
    String DEFAULT_KAFKA_LOGS_DIRECTORY = "kafka-logs";
    String DEFAULT_ZOOKEEPER_LOGS_DIRECTORY = "zookeeper-logs";
    int DEFAULT_ZOOKEEPER_PORT = findAvailableTcpPort();
    int DEFAULT_BROKER_PORT = findAvailableTcpPort();
    int DEFAULT_ZOOKEEPER_MAXIMUM_CONNECTED_CLIENTS = 1024;
    int DEFAULT_ZOOKEEPER_TICK_TIME = 500;
    String KAFKA_BOOTSTRAP_THREAD = "kafka-bootstrap-thread";

    enum ZookeeperInitializationMode {
        ON_KAFKA_BROKER_INITIALIZATION,
        MANUAL
    }

    interface KafkaProperties {
        String ZOOKEEPER_CONNECT = "zookeeper.connect";
        String BROKER_ID = "broker.id";
        String HOST_NAME = "host.name";
        String PORT = "port";
        String LOG_DIR = "log.dir";
        String LOG_FLUSH_INTERVAL_MESSAGES = "log.flush.interval.messages";
        String AUTO_CREATE_TOPICS_ENABLE = "auto.create.topics.enable";
        String OFFSETS_TOPIC_REPLICATION_FACTOR = "offsets.topic.replication.factor";
    }
}
