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

package ru.art.config.extensions.kafka;

import lombok.Getter;
import org.apache.kafka.common.serialization.Serializer;
import ru.art.kafka.producer.configuration.KafkaProducerConfiguration;
import ru.art.kafka.producer.configuration.KafkaProducerModuleConfiguration.KafkaProducerDefaultModuleConfiguration;
import ru.art.kafka.serializer.KafkaJsonSerializer;
import ru.art.kafka.serializer.KafkaProtobufSerializer;

import static java.lang.Class.forName;
import static org.apache.kafka.common.serialization.Serdes.serdeFrom;
import static ru.art.config.extensions.ConfigExtensions.configMap;
import static ru.art.config.extensions.kafka.KafkaConfigKeys.*;
import static ru.art.core.extension.ExceptionExtensions.ifException;
import static ru.art.kafka.constants.KafkaClientConstants.JSON_KAFKA_FORMAT;
import static ru.art.kafka.constants.KafkaClientConstants.PROTOBUF_KAFKA_FORMAT;
import static ru.art.kafka.producer.configuration.KafkaProducerConfiguration.producerConfiguration;

import java.util.Map;

@Getter
public class KafkaProducerAgileConfiguration extends KafkaProducerDefaultModuleConfiguration {
    private static final KafkaJsonSerializer KAFKA_JSON_SERIALIZER = new KafkaJsonSerializer();
    private static final KafkaProtobufSerializer KAFKA_PROTOBUF_SERIALIZER = new KafkaProtobufSerializer();
    private Map<String, KafkaProducerConfiguration> producerConfigurations;

    public KafkaProducerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        producerConfigurations = configMap(KAFKA_PRODUCERS_SECTION_ID, (key, config) -> {
            String serializerString = config.getString(KEY_SERIALIZER_KEY);
            String serializerString1 = config.getString(VALUE_SERIALIZER_KEY);
            return producerConfiguration()
                    .clientId(key)
                    .topic(config.getString(TOPIC_KEY))
                    .brokers(config.getStringList(BROKERS_KEY))
                    .additionalProperties(config.getProperties(ADDITIONAL_PROPERTIES_KEY))
                    .deliveryTimeout(config.getLong(DELIVERY_TIMEOUT))
                    .retries(config.getInt(RETRIES))
                    .keySerializer(ifException(() -> getSerializer(serializerString), KAFKA_PROTOBUF_SERIALIZER))
                    .valueSerializer(ifException(() -> getSerializer(serializerString1), KAFKA_PROTOBUF_SERIALIZER))
                    .build();
        }, super.getProducerConfigurations());
    }

    private static Serializer<?> getSerializer(String serializerString) throws ClassNotFoundException {
        return JSON_KAFKA_FORMAT
                .equalsIgnoreCase(serializerString)
                ? KAFKA_JSON_SERIALIZER
                : PROTOBUF_KAFKA_FORMAT.equalsIgnoreCase(serializerString)
                ? KAFKA_PROTOBUF_SERIALIZER
                : serdeFrom(forName(serializerString)).serializer();
    }
}
