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

package ru.art.kafka.producer.configuration;

import lombok.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.*;
import ru.art.kafka.producer.exception.*;
import ru.art.kafka.serializer.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.kafka.producer.constants.KafkaProducerModuleConstants.*;
import java.util.*;

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
    @Singular("broker")
    private final List<String> brokers;

    /**
     * @return Serializer for key
     */
    @Builder.Default
    private final Serializer<?> keySerializer = new KafkaProtobufSerializer();

    /**
     * @return Serializer for value
     */
    @Builder.Default
    private final Serializer<?> valueSerializer = new KafkaProtobufSerializer();

    /**
     * @return Quantity resend any record whose send fails with a potentially temporary error
     */
    private final Integer retries;

    /**
     * @return timeout in milliseconds for an upper bound on the time to report success or
     * failure for method {@link org.apache.kafka.clients.producer.KafkaProducer#send(ProducerRecord)}
     */
    private final Long deliveryTimeout;

    /**
     * Default value null
     *
     * @return Other properties for kafka producer
     * Read more http://kafka.apache.org/documentation/
     */
    @Builder.Default
    private final Properties additionalProperties = new Properties();

    public void validate() {
        if (isEmpty(getTopic())) throw new KafkaProducerConfigurationException(TOPIC_IS_EMPTY);
        if (isEmpty(getClientId())) throw new KafkaProducerConfigurationException(CLIENT_ID_IS_EMPTY);
        if (isEmpty(getBrokers())) throw new KafkaProducerConfigurationException(BROKERS_ARE_EMPTY);
    }
}
