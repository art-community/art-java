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

import lombok.Getter;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.Logger;
import ru.art.kafka.consumer.configuration.KafkaStreamConfiguration;
import ru.art.kafka.consumer.container.KafkaStreamContainer;
import static java.lang.String.*;
import static org.apache.kafka.streams.StreamsConfig.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.kafka.consumer.module.KafkaConsumerModule.kafkaConsumerModule;
import static ru.art.logging.LoggingModule.loggingModule;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

@Getter
public class KafkaStreamsRegistry {
    private final Map<String, KafkaStreams> registry = mapOf();
    private static final Logger logger = loggingModule().getLogger(KafkaStreamsRegistry.class);

    public static <K, V> KStream<K, V> withTracing(KStream<K, V> stream) {
        if (!kafkaConsumerModule().isEnableTracing()) {
            return stream;
        }
        return stream.peek(((key, value) -> logger.info(key + COLON + SPACE + value)));
    }

    @SuppressWarnings("Duplicates")
    public KafkaStreamsRegistry registerStream(String applicationId, Function<StreamsBuilder, KafkaStreamContainer> streamCreator) {
        StreamsBuilder builder = new StreamsBuilder();
        Properties properties = new Properties();
        KafkaStreamContainer streamContainer = streamCreator.apply(builder);
        KafkaStreamConfiguration configuration = streamContainer.getConfiguration();
        configuration.validate();
        properties.put(APPLICATION_ID_CONFIG, applicationId);
        properties.put(BOOTSTRAP_SERVERS_CONFIG, join(COMMA, configuration.getBrokers()));
        properties.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, configuration.getKeySerde().getClass());
        properties.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, configuration.getValueSerde().getClass());
        properties.putAll(configuration.getAdditionalProperties());
        registry.put(applicationId, new KafkaStreams(builder.build(), properties));
        return this;
    }

    @SuppressWarnings("Duplicates")
    public <K, V> KafkaStreamsRegistry registerStream(String appId, String topic, Function<KStream<K, V>, KafkaStreamContainer> streamCreator) {
        StreamsBuilder builder = new StreamsBuilder();
        Properties properties = new Properties();
        KafkaStreamContainer streamContainer = streamCreator.apply(withTracing(builder.stream(topic)));
        KafkaStreamConfiguration configuration = streamContainer.getConfiguration();
        configuration.validate();
        properties.put(APPLICATION_ID_CONFIG, appId);
        properties.put(BOOTSTRAP_SERVERS_CONFIG, join(COMMA, configuration.getBrokers()));
        properties.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, configuration.getKeySerde().getClass());
        properties.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, configuration.getValueSerde().getClass());
        properties.putAll(configuration.getAdditionalProperties());
        registry.put(appId, new KafkaStreams(builder.build(), properties));
        return this;
    }
}

