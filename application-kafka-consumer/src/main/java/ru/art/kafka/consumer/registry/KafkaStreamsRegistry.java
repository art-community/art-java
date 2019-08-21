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

package ru.art.kafka.consumer.registry;

import lombok.*;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.logging.log4j.*;
import ru.art.kafka.consumer.configuration.*;
import ru.art.kafka.consumer.container.*;
import java.util.*;
import java.util.function.*;

import static java.lang.String.*;
import static java.text.MessageFormat.format;
import static org.apache.kafka.streams.StreamsConfig.*;
import static org.apache.logging.log4j.ThreadContext.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.kafka.consumer.module.KafkaConsumerModule.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.logging.ThreadContextExtensions.*;

@Getter
public class KafkaStreamsRegistry {
    private final Map<String, KafkaStreams> registry = mapOf();
    private static final Logger logger = loggingModule().getLogger(KafkaStreamsRegistry.class);

    public static <K, V> KStream<K, V> withTracing(KStream<K, V> stream) {
        if (!kafkaConsumerModule().isEnableTracing()) {
            return stream;
        }
        return stream.peek(KafkaStreamsRegistry::logKafkaRecord);
    }

    private static <K, V> void logKafkaRecord(K key, V value) {
        putIfNotNull(KAFKA_KEY, key);
        putIfNotNull(KAFKA_VALUE, value);
        logger.info(format(KAFKA_TRACE_MESSAGE, key, value));
        remove(KAFKA_KEY);
        remove(KAFKA_VALUE);
    }

    @SuppressWarnings("Duplicates")
    public KafkaStreamsRegistry createStream(String streamId, Function<StreamsBuilder, KafkaStreamContainer> streamCreator) {
        StreamsBuilder builder = new StreamsBuilder();
        Properties properties = new Properties();
        KafkaStreamContainer streamContainer = streamCreator.apply(builder);
        KafkaStreamConfiguration configuration = getOrElse(streamContainer.getConfiguration(), kafkaConsumerModule()
                .getKafkaStreamsConfiguration()
                .getKafkaStreamConfigurations()
                .get(streamId));
        configuration.validate();
        properties.put(APPLICATION_ID_CONFIG, streamId);
        properties.put(BOOTSTRAP_SERVERS_CONFIG, join(COMMA, configuration.getBrokers()));
        properties.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, configuration.getKeySerde().getClass());
        properties.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, configuration.getValueSerde().getClass());
        properties.putAll(configuration.getAdditionalProperties());
        registry.put(streamId, new KafkaStreams(builder.build(), properties));
        return this;
    }

    @SuppressWarnings("Duplicates")
    public <K, V> KafkaStreamsRegistry registerStream(String streamId, Function<KStream<K, V>, KStream<?, ?>> streamCreator) {
        StreamsBuilder builder = new StreamsBuilder();
        Properties properties = new Properties();
        KafkaStreamConfiguration configuration = kafkaConsumerModule()
                .getKafkaStreamsConfiguration()
                .getKafkaStreamConfigurations()
                .get(streamId);
        streamCreator.apply(withTracing(builder.stream(configuration.getTopic())));
        configuration.validate();
        properties.put(APPLICATION_ID_CONFIG, streamId);
        properties.put(BOOTSTRAP_SERVERS_CONFIG, join(COMMA, configuration.getBrokers()));
        properties.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, configuration.getKeySerde().getClass());
        properties.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, configuration.getValueSerde().getClass());
        properties.putAll(configuration.getAdditionalProperties());
        registry.put(streamId, new KafkaStreams(builder.build(), properties));
        return this;
    }
}

