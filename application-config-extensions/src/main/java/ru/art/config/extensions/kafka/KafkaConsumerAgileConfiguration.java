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
import ru.art.kafka.consumer.configuration.KafkaStreamConfiguration;

import java.util.Map;

import static java.lang.Class.forName;
import static java.time.Duration.ofMillis;
import static org.apache.kafka.common.serialization.Serdes.serdeFrom;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.ENABLE_TRACING;
import static ru.art.config.extensions.common.CommonConfigKeys.THREAD_POOL_SIZE;
import static ru.art.config.extensions.common.DataFormats.*;
import static ru.art.config.extensions.kafka.KafkaConfigKeys.*;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.core.context.Context.context;
import static ru.art.core.extension.ExceptionExtensions.ifException;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.core.factory.CollectionsFactory.setOf;
import static ru.art.core.wrapper.ExceptionWrapper.ignoreException;
import static ru.art.kafka.consumer.configuration.KafkaStreamConfiguration.streamConfiguration;
import static ru.art.kafka.consumer.constants.KafkaConsumerModuleConstants.KAFKA_CONSUMER_MODULE_ID;
import static ru.art.kafka.consumer.controller.KafkaConsumerController.restartKafkaConsumer;
import static ru.art.kafka.consumer.module.KafkaConsumerModule.kafkaConsumerModuleState;
import static ru.art.kafka.instances.KafkaSerdes.*;

@Getter
public class KafkaConsumerAgileConfiguration extends KafkaConsumerModuleDefaultConfiguration {
    private boolean enableTracing;
    private KafkaConsumerConfiguration kafkaConsumerConfiguration = super.getKafkaConsumerConfiguration();
    private Map<String, KafkaStreamConfiguration> kafkaStreamConfigurations = super.getKafkaStreamConfigurations();

    public KafkaConsumerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        enableTracing = configBoolean(KAFKA_CONSUMER_SECTION_ID, ENABLE_TRACING, super.isEnableTracing());
        KafkaConsumerConfiguration defaultKafkaConsumerConfiguration = super.getKafkaConsumerConfiguration();
        KafkaConsumerConfiguration newKafkaConsumerConfiguration = kafkaConsumerConfiguration = KafkaConsumerConfiguration.builder()
                .brokers(configStringSet(KAFKA_CONSUMER_SECTION_ID, BROKERS, defaultKafkaConsumerConfiguration.getBrokers()))
                .executorPoolSize(configInt(KAFKA_CONSUMER_SECTION_ID, THREAD_POOL_SIZE, DEFAULT_THREAD_POOL_SIZE))
                .keyDeserializer(ifException(() -> getDeserializer(KEY_DESERIALIZER), defaultKafkaConsumerConfiguration.getKeyDeserializer()))
                .valueDeserializer(ifException(() -> getDeserializer(VALUE_DESERIALIZER), defaultKafkaConsumerConfiguration.getValueDeserializer()))
                .topics(setOf(configStringList(KAFKA_CONSUMER_SECTION_ID, TOPICS, fixedArrayOf(defaultKafkaConsumerConfiguration.getTopics()))))
                .pollTimeout(ofMillis(configLong(KAFKA_CONSUMER_SECTION_ID, POLL_TIMEOUT, defaultKafkaConsumerConfiguration.getPollTimeout().toMillis())))
                .groupId(configString(KAFKA_CONSUMER_SECTION_ID, GROUP_ID, defaultKafkaConsumerConfiguration.getGroupId()))
                .build();
        Map<String, KafkaStreamConfiguration> newKafkaStreamConfigurations = configInnerMap(KAFKA_CONSUMER_SECTION_ID, STREAMS_SECTION_ID, streamConfig ->
                streamConfiguration()
                        .brokers(streamConfig.getStringList(BROKERS))
                        .topic(streamConfig.getString(TOPIC))
                        .additionalProperties(streamConfig.getProperties(ADDITIONAL_PROPERTIES))
                        .keySerde(ifException(() -> getSerde(streamConfig.getString(KEY_SERDE)), KAFKA_MESSAGE_PACK_SERDE))
                        .valueSerde(ifException(() -> getSerde(streamConfig.getString(VALUE_SERDE)), KAFKA_MESSAGE_PACK_SERDE))
                        .build(), super.getKafkaStreamConfigurations());
        if (!kafkaConsumerConfiguration.equals(newKafkaConsumerConfiguration) && context().hasModule(KAFKA_CONSUMER_MODULE_ID)) {
            kafkaConsumerConfiguration = newKafkaConsumerConfiguration;
            setOf(kafkaConsumerModuleState()
                    .getKafkaConsumers()
                    .entrySet())
                    .stream()
                    .filter(entry -> !entry.getValue().isStopped())
                    .forEach(entry -> ignoreException(() -> restartKafkaConsumer(entry.getKey())));
        }
        kafkaConsumerConfiguration = newKafkaConsumerConfiguration;
        if (!kafkaStreamConfigurations.equals(newKafkaStreamConfigurations) && context().hasModule(KAFKA_CONSUMER_MODULE_ID)) {
            kafkaStreamConfigurations = newKafkaStreamConfigurations;
            kafkaConsumerModuleState().getKafkaStreamsRegistry().refreshStreams();
            return;
        }
        kafkaStreamConfigurations = newKafkaStreamConfigurations;
    }

    private static Serde<?> getSerde(String serdeString) throws ClassNotFoundException {
        if (isEmpty(serdeString)) {
            return KAFKA_MESSAGE_PACK_SERDE;
        }
        switch (serdeString) {
            case JSON:
                return KAFKA_JSON_SERDE;
            case PROTOBUF:
                return KAFKA_PROTOBUF_SERDE;
            case MESSAGE_PACK:
                return KAFKA_MESSAGE_PACK_SERDE;
            case XML:
                return KAFKA_XML_SERDE;
        }
        return serdeFrom(forName(serdeString));

    }

    private static Deserializer<?> getDeserializer(String deserializerString) throws ClassNotFoundException {
        if (isEmpty(deserializerString)) {
            return KAFKA_MESSAGE_PACK_DESERIALIZER;
        }
        switch (deserializerString) {
            case JSON:
                return KAFKA_JSON_DESERIALIZER;
            case PROTOBUF:
                return KAFKA_PROTOBUF_DESERIALIZER;
            case MESSAGE_PACK:
                return KAFKA_MESSAGE_PACK_DESERIALIZER;
            case XML:
                return KAFKA_XML_DESERIALIZER;
        }
        return serdeFrom(forName(deserializerString)).deserializer();

    }
}
