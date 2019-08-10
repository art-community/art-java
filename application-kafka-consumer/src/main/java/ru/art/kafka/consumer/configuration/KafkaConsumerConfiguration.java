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
import org.apache.kafka.common.serialization.Deserializer;
import ru.art.kafka.deserializer.KafkaProtobufDeserializer;
import ru.art.kafka.consumer.exception.KafkaConsumerModuleException;
import static java.util.Collections.emptySet;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.kafka.consumer.constants.KafkaConsumerModuleConstants.*;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public interface KafkaConsumerConfiguration {
    /**
     * @return Executor for multi-thread consumers
     */
    ExecutorService getExecutor();

    /**
     * @return serviceId - Unique identifier kafka producer in ADK
     */
    String getServiceId();

    /**
     * @return duration - Timeout for poll data from kafka brokers
     */
    Duration getDuration();

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
    String getBootstrapServers();

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
    default Properties getOtherProperties() {
        return new Properties();
    }

    default void validate() {
        if (isEmpty(getServiceId())) throw new KafkaConsumerModuleException("serviceId is empty");
        if (isEmpty(getTopics())) throw new KafkaConsumerModuleException("topic is empty");
        if (isEmpty(getBootstrapServers())) throw new KafkaConsumerModuleException("bootstrapServers is empty");
        if (isEmpty(getDuration())) throw new KafkaConsumerModuleException("duration is empty");
        if (isEmpty(getGroupId())) throw new KafkaConsumerModuleException("groupId is empty");
        if (isEmpty(getKeyDeserializer())) throw new KafkaConsumerModuleException("keyDeserializer is empty");
        if (isEmpty(getValueDeserializer()))
            throw new KafkaConsumerModuleException("valueDeserializer is empty");
    }

    @Getter
    class KafkaConsumerDefaultConfiguration implements KafkaConsumerConfiguration {
        private final ExecutorService executor = newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        private final String serviceId = DEFAULT_KAFKA_SERVICE_ID;
        private final Duration duration = DEFAULT_DURATION;
        private final String groupId = DEFAULT_KAFKA_GROUP_ID;
        private final Set<String> topics = emptySet();
        private final String bootstrapServers = DEFAULT_KAFKA_BOOTSTRAP_SERVERS;
        private final Deserializer<?> keyDeserializer = new KafkaProtobufDeserializer();
        private final Deserializer<?> valueDeserializer = new KafkaProtobufDeserializer();

        public <KeyDeserializer> Deserializer<KeyDeserializer> getKeyDeserializer() {
            return cast(keyDeserializer);
        }

        public <ValueDeSerializer> Deserializer<ValueDeSerializer>getValueDeserializer() {
            return cast(valueDeserializer);
        }
    }
}
