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

package ru.art.kafka.consumer.constants;

import static java.time.Duration.ofSeconds;
import java.time.Duration;

public interface KafkaConsumerModuleConstants {
    String KAFKA_CONSUMER_MODULE_ID = "KAFKA_CONSUMER_MODULE";
    String KAFKA_CONSUMER_SERVICE_TYPE = "KAFKA_CONSUMER";
    String DEFAULT_KAFKA_SERVICE_ID = "KAFKA_SERVICE";
    String DEFAULT_KAFKA_GROUP_ID = "KAFKA_GROUP";
    String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    Duration DEFAULT_DURATION = ofSeconds(10L);
}
