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
import java.util.function.Function;

@Getter
public class KafkaProducerAgileConfiguration extends KafkaProducerDefaultModuleConfiguration {
    private Map<String, KafkaProducerConfiguration> producerConfigurations;

    public KafkaProducerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        KafkaJsonSerializer kafkaJsonSerializer = new KafkaJsonSerializer();
        KafkaProtobufSerializer kafkaProtobufSerializer = new KafkaProtobufSerializer();
        Function<String, Serializer<?>> serializerProvider = serializerString -> ifException(() -> JSON_KAFKA_FORMAT
                .equalsIgnoreCase(serializerString)
                ? kafkaJsonSerializer
                : PROTOBUF_KAFKA_FORMAT.equalsIgnoreCase(serializerString)
                ? kafkaProtobufSerializer
                : serdeFrom(forName(serializerString)).serializer(), kafkaProtobufSerializer);
        producerConfigurations = configMap(KAFKA_PRODUCERS_SECTION_ID, (key, config) -> producerConfiguration()
                .clientId(key)
                .topic(config.getString(TOPIC_KEY))
                .brokers(config.getStringList(BROKERS_KEY))
                .additionalProperties(config.getProperties(ADDITIONAL_PROPERTIES_KEY))
                .deliveryTimeout(config.getLong(DELIVERY_TIMEOUT))
                .retries(config.getInt(RETRIES))
                .keySerializer(serializerProvider.apply(config.getString(KEY_SERIALIZER_KEY)))
                .valueSerializer(serializerProvider.apply(config.getString(VALUE_SERIALIZER_KEY)))
                .build(), super.getProducerConfigurations());
    }
}
