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

package ru.art.kafka.consumer.configuration;

import lombok.Getter;
import ru.art.kafka.consumer.registry.KafkaStreamsRegistry;
import java.util.Properties;

public interface KafkaStreamsConfiguration {

    KafkaStreamsRegistry getKafkaStreamsRegistry();

    /**
     * Default value null
     * Other properties for kafka consumer
     * Read more http://kafka.apache.org/documentation/
     */
    default Properties getCustomProperties() {
        return new Properties();
    }

    @Getter
    class KafkaStreamsDefaultConfiguration implements KafkaStreamsConfiguration {
        private final KafkaStreamsRegistry kafkaStreamsRegistry = new KafkaStreamsRegistry();
    }
}
