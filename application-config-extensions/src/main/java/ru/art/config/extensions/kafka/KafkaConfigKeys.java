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

public interface KafkaConfigKeys {
    String KAFKA_BROKER_SECTION_ID = "kafka.broker";
    String KAFKA_CONSUMER_SECTION_ID = "kafka.consumer";
    String STREAMS_SECTION_ID = "streams";
    String KAFKA_PRODUCERS_SECTION_ID = "kafka.producers";
    String KAFKA_BROKER_ZOOKEEPER_SECTION_ID = "kafka.broker.zookeeper";
    String LOGS_DIRECTORY_KEY = "logsDirectory";
    String TICK_TIME_KEY = "tickTime";
    String MAXIMUM_CONNECTED_CLIENTS_KEY = "maximumConnectedClients";
    String INITIALIZATION_MODE_KEY = "initializationMode";
    String ZOOKEEPER_CONNECTION_KEY = "zookeeperConnection";
    String ADDITIONAL_PROPERTIES_KEY = "additionalProperties";
    String REPLICATION_FACTOR_KEY = "replicationFactor";
    String BROKERS_KEY = "brokers";
    String KEY_DESERIALIZER_KEY = "keyDeserializer";
    String VALUE_DESERIALIZER_KEY = "valueDeserializer";
    String TOPICS_KEY = "topics";
    String POLL_TIMEOUT = "pollTimeout";
    String GROUP_ID = "groupId";
    String KEY_SERDE_KEY = "keySerde";
    String VALUE_SERDE_KEY = "valueSerde";
    String TOPIC_KEY = "topic";
    String CLIENT_ID_KEY = "clientId";
    String DELIVERY_TIMEOUT = "deliveryTimeout";
    String RETRIES = "retries";
    String KEY_SERIALIZER_KEY = "keySerializer";
    String VALUE_SERIALIZER_KEY = "valueSerializer";
}
