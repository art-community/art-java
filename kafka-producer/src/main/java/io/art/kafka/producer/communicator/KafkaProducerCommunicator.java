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

package io.art.kafka.producer.communicator;

import org.apache.kafka.clients.producer.*;
import io.art.kafka.producer.configuration.*;
import io.art.kafka.producer.exception.*;
import static java.lang.String.*;
import static java.util.Objects.isNull;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extension.NullCheckingExtensions.*;
import static io.art.kafka.producer.constants.KafkaProducerModuleConstants.KAFKA_PRODUCER_CONFIGURATION_NOT_FOUND;
import static io.art.kafka.producer.module.KafkaProducerModule.*;
import java.text.*;
import java.util.*;
import java.util.function.*;

/**
 * The wrapper for KafkaProducer API
 *
 * @param <KeyType>   - type data for key
 * @param <ValueType> - type data for value
 */
public class KafkaProducerCommunicator<KeyType, ValueType> {
    private final String topic;
    private final KafkaProducer<KeyType, ValueType> kafkaProducer;
    private final KafkaProducerConfiguration configuration;
    private Callback callback;

    /**
     * Checking all required configuration for sending message to broker
     * Initialization kafka producer
     *
     * @param configuration - configuration for kafka producer proxy
     */
    private KafkaProducerCommunicator(KafkaProducerConfiguration configuration) {
        configuration.validate();
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, join(COMMA, configuration.getBrokers()));
        properties.put(CLIENT_ID_CONFIG, configuration.getClientId());
        let(configuration.getRetries(), (Consumer<Integer>) retries -> properties.put(RETRIES_CONFIG, retries));
        let(configuration.getDeliveryTimeout(), (Consumer<Long>) timeout -> properties.put(DELIVERY_TIMEOUT_MS_CONFIG, timeout));
        properties.putAll(configuration.getAdditionalProperties());
        this.topic = configuration.getTopic();
        this.configuration = configuration;
        this.kafkaProducer = new KafkaProducer<>(properties, cast(configuration.getKeySerializer()), cast(configuration.getValueSerializer()));
    }

    public static <KeyType, ValueType> KafkaProducerCommunicator<KeyType, ValueType> kafkaProducerCommunicator(KafkaProducerConfiguration configuration) {
        return new KafkaProducerCommunicator<>(configuration);
    }

    public static <KeyType, ValueType> KafkaProducerCommunicator<KeyType, ValueType> kafkaProducerCommunicator(String clientId) {
        KafkaProducerConfiguration configuration = kafkaProducerModule().getProducerConfigurations().get(clientId);
        if (isNull(configuration)) {
            throw new KafkaProducerConfigurationException(MessageFormat.format(KAFKA_PRODUCER_CONFIGURATION_NOT_FOUND, clientId));
        }
        return kafkaProducerCommunicator(configuration);
    }

    public KafkaProducerCommunicator<KeyType, ValueType> pushKafkaRecord(KeyType key, ValueType value) {
        ProducerRecord<KeyType, ValueType> producerRecord = new ProducerRecord<>(topic, key, value);
        kafkaProducer.send(producerRecord, callback);
        return this;
    }

    public KafkaProducerCommunicator<KeyType, ValueType> prepareCallbackWrapper(Callback callback) {
        this.callback = callback;
        return this;
    }
}
