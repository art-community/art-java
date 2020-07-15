/*
 *    Copyright 2020 ART
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

package io.art.config.extensions.kafka;

import lombok.Getter;
import org.apache.kafka.common.serialization.Serializer;
import io.art.kafka.producer.configuration.KafkaProducerConfiguration;
import io.art.kafka.producer.configuration.KafkaProducerModuleConfiguration.KafkaProducerDefaultModuleConfiguration;

import java.util.Map;

import static java.lang.Class.forName;
import static org.apache.kafka.common.serialization.Serdes.serdeFrom;
import static io.art.config.extensions.ConfigExtensions.configInnerMap;
import static io.art.config.extensions.common.DataFormats.*;
import static io.art.config.extensions.kafka.KafkaConfigKeys.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.extension.ExceptionExtensions.ifException;
import static io.art.kafka.instances.KafkaSerdes.*;
import static io.art.kafka.producer.configuration.KafkaProducerConfiguration.producerConfiguration;

@Getter
public class KafkaProducerAgileConfiguration extends KafkaProducerDefaultModuleConfiguration {
    private Map<String, KafkaProducerConfiguration> producerConfigurations;

    public KafkaProducerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        producerConfigurations = configInnerMap(KAFKA_PRODUCERS_SECTION_ID, (key, config) -> {
            String keySerializerString = config.getString(KEY_SERIALIZER);
            String valueSerializerString = config.getString(VALUE_SERIALIZER);
            return producerConfiguration()
                    .clientId(key)
                    .topic(config.getString(TOPIC))
                    .brokers(config.getStringList(BROKERS))
                    .additionalProperties(config.getProperties(ADDITIONAL_PROPERTIES))
                    .deliveryTimeout(config.getLong(DELIVERY_TIMEOUT))
                    .retries(config.getInt(RETRIES))
                    .keySerializer(ifException(() -> getSerializer(keySerializerString), KAFKA_MESSAGE_PACK_SERIALIZER))
                    .valueSerializer(ifException(() -> getSerializer(valueSerializerString), KAFKA_MESSAGE_PACK_SERIALIZER))
                    .build();
        }, super.getProducerConfigurations());
    }

    private static Serializer<?> getSerializer(String serializerString) throws ClassNotFoundException {
        if (isEmpty(serializerString)) {
            return KAFKA_MESSAGE_PACK_SERIALIZER;
        }
        switch (serializerString) {
            case JSON:
                return KAFKA_JSON_SERIALIZER;
            case PROTOBUF:
                return KAFKA_PROTOBUF_SERIALIZER;
            case MESSAGE_PACK:
                return KAFKA_MESSAGE_PACK_SERIALIZER;
            case XML:
                return KAFKA_XML_SERIALIZER;
        }
        return serdeFrom(forName(serializerString)).serializer();
    }
}
