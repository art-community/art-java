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

package ru.art.kafka.producer.configuration;

import org.apache.kafka.common.serialization.Serializer;
import ru.art.core.module.ModuleConfiguration;
import ru.art.kafka.producer.exception.KafkaProducerConfigurationException;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import java.util.Properties;

public interface KafkaProducerConfiguration extends ModuleConfiguration {

    /**
     * @return clientId - identifies producer application
     */
    String getClientId();

    /**
     * @return topic name
     */
    String getTopic();

    /**
     * @return list ip-address and port kafka brokers
     */
    String getBootstrapServers();

    /**
     * @return Serializer for key
     */
    <KeySerializer> Serializer<KeySerializer> getKeySerializer();

    /**
     * @return Serializer for value
     */
    <ValueSerializer> Serializer<ValueSerializer> getValueSerializer();

    /**
     * Default value null
     *
     * @return Other properties for kafka producer
     * Read more http://kafka.apache.org/documentation/
     */
    default Properties getOtherProperties() {
        return new Properties();
    }

    /**
     * @return configuration kafka producer for retries
     */
    default KafkaProducerRetryConfiguration getRetries() {
        return null;
    }


    default void validate() {
        if (isEmpty(getTopic())) throw new KafkaProducerConfigurationException("topic is empty");
        if (isEmpty(getClientId())) throw new KafkaProducerConfigurationException("clientId is empty");
        if (isEmpty(getBootstrapServers())) throw new KafkaProducerConfigurationException("bootstrapServer is empty");
        if (isEmpty(getKeySerializer())) throw new KafkaProducerConfigurationException("keySerializer is empty");
        if (isEmpty(getValueSerializer())) throw new KafkaProducerConfigurationException("valueSerializer is empty");
    }
}
