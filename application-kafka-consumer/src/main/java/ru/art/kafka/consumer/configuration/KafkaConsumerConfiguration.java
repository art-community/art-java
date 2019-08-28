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

package ru.art.kafka.consumer.configuration;

import lombok.*;
import org.apache.kafka.common.serialization.*;
import ru.art.kafka.deserializer.*;
import static java.util.Collections.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.constants.ThreadConstants.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.kafka.consumer.constants.KafkaConsumerModuleConstants.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public interface KafkaConsumerConfiguration {
    /**
     * @return Executor for multi-thread consumers
     */
    ExecutorService getExecutor();

    /**
     * @return pollTimeout - Timeout for poll data from kafka brokers
     */
    Duration getPollTimeout();

    /**
     * @return groupId - A unique string that identifies the producer group this producer belongs to
     */
    String getGroupId();

    /**
     * @return List topics name
     */
    Set<String> getTopics();

    /**
     * @return List ip-address and port kafka brokers
     */
    List<String> getBrokers();

    /**
     * @return Deserializer for key
     */
    <KeyDeserializer> Deserializer<KeyDeserializer> getKeyDeserializer();

    /**
     * @return Deserializer for value
     */
    <ValueDeSerializer> Deserializer<ValueDeSerializer> getValueDeserializer();

    /**
     * Default value null
     *
     * @return Other properties for kafka consumer
     * Read more http://kafka.apache.org/documentation/
     */
    default Properties getAdditionalProperties() {
        return new Properties();
    }

	@Getter
    @Builder
	class KafkaConsumerDefaultConfiguration implements KafkaConsumerConfiguration {
        @Builder.Default
        private final ExecutorService executor = new ForkJoinPool(DEFAULT_THREAD_POOL_SIZE);
        @Builder.Default
        private final Duration pollTimeout = DEFAULT_DURATION;
        @Builder.Default
        private final String groupId = DEFAULT_KAFKA_GROUP_ID;
        @Builder.Default
        private final Set<String> topics = emptySet();
        @Builder.Default
        private final List<String> brokers = fixedArrayOf();
        @Builder.Default
        private final Deserializer<?> keyDeserializer = new KafkaProtobufDeserializer();
        @Builder.Default
        private final Deserializer<?> valueDeserializer = new KafkaProtobufDeserializer();

        public <KeyDeserializer> Deserializer<KeyDeserializer> getKeyDeserializer() {
            return cast(keyDeserializer);
        }

        public <ValueDeSerializer> Deserializer<ValueDeSerializer> getValueDeserializer() {
            return cast(valueDeserializer);
        }
    }
}
