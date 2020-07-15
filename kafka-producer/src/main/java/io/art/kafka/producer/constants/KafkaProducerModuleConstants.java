/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.kafka.producer.constants;

public interface KafkaProducerModuleConstants {
    String KAFKA_PRODUCER_MODULE_ID = "KAFKA_PRODUCER_MODULE_ID";
    String TOPIC_IS_EMPTY = "topic is empty";
    String BROKERS_ARE_EMPTY = "brokers are empty";
    String CLIENT_ID_IS_EMPTY = "clientId is empty";
    String KAFKA_PRODUCER_CONFIGURATION_NOT_FOUND = "Kafka producer configuration was not found for clientId: ''{0}''";
}
