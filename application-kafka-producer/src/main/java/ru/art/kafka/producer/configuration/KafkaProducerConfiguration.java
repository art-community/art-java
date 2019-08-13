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

import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import ru.art.kafka.producer.exception.KafkaProducerConfigurationException;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import java.util.Properties;

@Getter
@Builder(builderMethodName = "producerConfiguration")
public class KafkaProducerConfiguration {
    /**
     * @return clientId - identifies producer application
     */
    private final String clientId;

    /**
     * @return topic name
     */
    private final String topic;

    /**
     * @return list ip-address and port kafka brokers
     */
    private final String bootstrapServers;

    /**
     * @return Serializer for key
     */
    private final Serializer<?> keySerializer;

    /**
     * @return Serializer for value
     */
    private final Serializer valueSerializer;

    /**
     * @return Quantity resend any record whose send fails with a potentially temporary error
     */
    private final int retries;

    /**
     * @return Quantity resend any record whose send fails with a potentially temporary error for single connection
     */
    private final int maxAttemptPerSingleConnection;

    /**
     * @return timeout in milliseconds for an upper bound on the time to report success or
     * failure for method {@link org.apache.kafka.clients.producer.KafkaProducer#send(ProducerRecord)}
     */
    private final int deliveryTimeout;

    /**
     * Default value null
     *
     * @return Other properties for kafka producer
     * Read more http://kafka.apache.org/documentation/
     */
    private final Properties otherProperties;

    public void validate() {
        if (isEmpty(getTopic())) throw new KafkaProducerConfigurationException("topic is empty");
        if (isEmpty(getClientId())) throw new KafkaProducerConfigurationException("clientId is empty");
        if (isEmpty(getBootstrapServers())) throw new KafkaProducerConfigurationException("bootstrapServer is empty");
        if (isEmpty(getKeySerializer())) throw new KafkaProducerConfigurationException("keySerializer is empty");
        if (isEmpty(getValueSerializer())) throw new KafkaProducerConfigurationException("valueSerializer is empty");
    }
}
