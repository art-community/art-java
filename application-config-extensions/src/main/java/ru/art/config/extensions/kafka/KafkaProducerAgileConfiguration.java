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

import lombok.*;
import org.apache.kafka.common.serialization.*;
import ru.art.kafka.producer.configuration.*;
import ru.art.kafka.producer.configuration.KafkaProducerModuleConfiguration.*;
import static java.lang.Class.*;
import static org.apache.kafka.common.serialization.Serdes.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.kafka.KafkaConfigKeys.*;
import static ru.art.core.extension.ExceptionExtensions.*;
import static ru.art.kafka.constants.KafkaClientConstants.*;
import static ru.art.kafka.instances.KafkaSerdes.*;
import static ru.art.kafka.producer.configuration.KafkaProducerConfiguration.*;
import java.util.*;
import java.lang.String;

@Getter
public class KafkaProducerAgileConfiguration extends KafkaProducerDefaultModuleConfiguration {
    private Map<String, KafkaProducerConfiguration> producerConfigurations;

    public KafkaProducerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        producerConfigurations = configMap(KAFKA_PRODUCERS_SECTION_ID, (key, config) -> {
            String keySerializerString = config.getString(KEY_SERIALIZER);
            String valueSerializerString = config.getString(VALUE_SERIALIZER);
            return producerConfiguration()
                    .clientId(key)
                    .topic(config.getString(TOPIC))
                    .brokers(config.getStringList(BROKERS))
                    .additionalProperties(config.getProperties(ADDITIONAL_PROPERTIES))
                    .deliveryTimeout(config.getLong(DELIVERY_TIMEOUT))
                    .retries(config.getInt(RETRIES))
                    .keySerializer(ifException(() -> getSerializer(keySerializerString), KAFKA_PROTOBUF_SERIALIZER))
                    .valueSerializer(ifException(() -> getSerializer(valueSerializerString), KAFKA_PROTOBUF_SERIALIZER))
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
