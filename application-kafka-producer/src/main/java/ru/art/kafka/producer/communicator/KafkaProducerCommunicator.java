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

package ru.art.kafka.producer.communicator;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.art.kafka.producer.configuration.KafkaProducerConfiguration;
import static java.lang.String.join;
import static org.apache.kafka.clients.CommonClientConfigs.*;
import static org.apache.kafka.clients.producer.ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.StringConstants.COMMA;
import static ru.art.core.extension.NullCheckingExtensions.doIfNotNull;
import static ru.art.kafka.producer.module.KafkaProducerModule.kafkaProducerModule;
import java.util.Properties;
import java.util.function.Consumer;

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
        doIfNotNull(configuration.getRetries(), (Consumer<Integer>) retries -> properties.put(RETRIES_CONFIG, retries));
        doIfNotNull(configuration.getDeliveryTimeout(), (Consumer<Long>) timeout -> properties.put(DELIVERY_TIMEOUT_MS_CONFIG, timeout));
        properties.putAll(configuration.getAdditionalProperties());
        this.topic = configuration.getTopic();
        this.configuration = configuration;
        this.kafkaProducer = new KafkaProducer<>(properties, cast(configuration.getKeySerializer()), cast(configuration.getValueSerializer()));
    }

    public static <KeyType, ValueType> KafkaProducerCommunicator<KeyType, ValueType> kafkaProducerCommunicator(KafkaProducerConfiguration configuration) {
        return new KafkaProducerCommunicator<>(configuration);
    }

    public static <KeyType, ValueType> KafkaProducerCommunicator<KeyType, ValueType> kafkaProducerCommunicator(String clientId) {
        return kafkaProducerCommunicator(kafkaProducerModule().getProducerConfigurations().get(clientId));
    }

    public KafkaProducerCommunicator<KeyType, ValueType> pushKafkaRecord(KeyType key, ValueType value) {
        ProducerRecord<KeyType, ValueType> producerRecord = new ProducerRecord<>(topic, key, value);
        kafkaProducer.send(producerRecord, callback);
        return new KafkaProducerCommunicator<>(configuration);
    }

    public KafkaProducerCommunicator<KeyType, ValueType> prepareCallbackWrapper(Callback callback) {
        this.callback = callback;
        return this;
    }
}
