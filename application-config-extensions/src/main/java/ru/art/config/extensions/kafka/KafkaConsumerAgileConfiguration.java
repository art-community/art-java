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
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import ru.art.kafka.consumer.configuration.KafkaConsumerConfiguration;
import ru.art.kafka.consumer.configuration.KafkaConsumerModuleConfiguration.KafkaConsumerModuleDefaultConfiguration;
import ru.art.kafka.consumer.configuration.KafkaStreamsConfiguration;
import ru.art.kafka.deserializer.KafkaJsonDeserializer;
import ru.art.kafka.deserializer.KafkaProtobufDeserializer;
import ru.art.kafka.serde.KafkaJsonSerde;
import ru.art.kafka.serde.KafkaProtobufSerde;
import static java.lang.Class.forName;
import static java.time.Duration.ofMillis;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.apache.kafka.common.serialization.Serdes.serdeFrom;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.ENABLE_TRACING;
import static ru.art.config.extensions.common.CommonConfigKeys.THREAD_POOL_SIZE;
import static ru.art.config.extensions.kafka.KafkaConfigKeys.*;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.core.extension.ExceptionExtensions.ifException;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.core.factory.CollectionsFactory.setOf;
import static ru.art.kafka.constants.KafkaClientConstants.JSON_KAFKA_FORMAT;
import static ru.art.kafka.constants.KafkaClientConstants.PROTOBUF_KAFKA_FORMAT;
import static ru.art.kafka.consumer.configuration.KafkaConsumerConfiguration.KafkaConsumerDefaultConfiguration;
import static ru.art.kafka.consumer.configuration.KafkaStreamConfiguration.streamConfiguration;
import static ru.art.kafka.consumer.configuration.KafkaStreamsConfiguration.KafkaStreamsDefaultConfiguration;
import java.util.function.Function;

@Getter
public class KafkaConsumerAgileConfiguration extends KafkaConsumerModuleDefaultConfiguration {
    private boolean enableTracing;
    private KafkaConsumerConfiguration kafkaConsumerConfiguration;
    private KafkaStreamsConfiguration kafkaStreamsConfiguration;

    public KafkaConsumerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        enableTracing = configBoolean(KAFKA_CONSUMER_SECTION_ID, ENABLE_TRACING, super.isEnableTracing());
        KafkaConsumerConfiguration kafkaConsumerConfiguration = super.getKafkaConsumerConfiguration();
        KafkaJsonDeserializer kafkaJsonDeserializer = new KafkaJsonDeserializer();
        KafkaProtobufDeserializer kafkaProtobufDeserializer = new KafkaProtobufDeserializer();
        String keyDeserializerString = configString(KAFKA_CONSUMER_SECTION_ID, KEY_DESERIALIZER_KEY);
        Deserializer<?> keyDeserializer = ifException(() -> JSON_KAFKA_FORMAT.equalsIgnoreCase(keyDeserializerString)
                ? kafkaJsonDeserializer
                : PROTOBUF_KAFKA_FORMAT.equalsIgnoreCase(keyDeserializerString)
                ? kafkaProtobufDeserializer
                : serdeFrom(forName(keyDeserializerString)).deserializer(), kafkaConsumerConfiguration.getKeyDeserializer());
        String valueDeserializerString = configString(KAFKA_CONSUMER_SECTION_ID, VALUE_DESERIALIZER_KEY);
        Deserializer<?> valueDeserializer = ifException(() -> JSON_KAFKA_FORMAT.equalsIgnoreCase(valueDeserializerString)
                ? kafkaJsonDeserializer
                : PROTOBUF_KAFKA_FORMAT.equalsIgnoreCase(valueDeserializerString)
                ? kafkaProtobufDeserializer
                : serdeFrom(forName(valueDeserializerString)).deserializer(), kafkaConsumerConfiguration.getValueDeserializer());
        KafkaJsonSerde kafkaJsonSerde = new KafkaJsonSerde();
        KafkaProtobufSerde kafkaProtobufSerde = new KafkaProtobufSerde();
        Function<String, Serde<?>> serdeProvider = serdeString -> ifException(() -> JSON_KAFKA_FORMAT.equalsIgnoreCase(serdeString)
                ? kafkaJsonSerde
                : PROTOBUF_KAFKA_FORMAT.equalsIgnoreCase(serdeString)
                ? kafkaProtobufSerde
                : serdeFrom(forName(keyDeserializerString)), kafkaProtobufSerde);
        this.kafkaConsumerConfiguration = KafkaConsumerDefaultConfiguration.builder()
                .brokers(configStringList(KAFKA_CONSUMER_SECTION_ID, BROKERS_KEY, kafkaConsumerConfiguration.getBrokers()))
                .executor(newFixedThreadPool(configInt(KAFKA_CONSUMER_SECTION_ID, THREAD_POOL_SIZE, DEFAULT_THREAD_POOL_SIZE)))
                .keyDeserializer(keyDeserializer)
                .valueDeserializer(valueDeserializer)
                .topics(setOf(configStringList(KAFKA_CONSUMER_SECTION_ID, TOPICS_KEY, fixedArrayOf(kafkaConsumerConfiguration.getTopics()))))
                .pollTimeout(ofMillis(configLong(KAFKA_CONSUMER_SECTION_ID, POLL_TIMEOUT, kafkaConsumerConfiguration.getPollTimeout().toMillis())))
                .groupId(configString(KAFKA_CONSUMER_SECTION_ID, GROUP_ID, kafkaConsumerConfiguration.getGroupId()))
                .build();
        this.kafkaStreamsConfiguration = KafkaStreamsDefaultConfiguration.builder()
                .kafkaStreamsRegistry(super.getKafkaStreamsConfiguration().getKafkaStreamsRegistry())
                .kafkaStreamConfigurations(configMap(KAFKA_CONSUMER_SECTION_ID, STREAMS_SECTION_ID, streamConfig ->
                                streamConfiguration()
                                        .brokers(streamConfig.getStringList(BROKERS_KEY))
                                        .topic(streamConfig.getString(TOPIC_KEY))
                                        .additionalProperties(streamConfig.getProperties(ADDITIONAL_PROPERTIES_KEY))
                                        .keySerde(serdeProvider.apply(streamConfig.getString(KEY_SERDE_KEY)))
                                        .valueSerde(serdeProvider.apply(streamConfig.getString(VALUE_SERDE_KEY)))
                                        .build(),
                        super.getKafkaStreamsConfiguration().getKafkaStreamConfigurations()))
                .build();
    }
}
