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

package ru.art.kafka.consumer.registry;

import lombok.Getter;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import ru.art.kafka.consumer.container.KafkaStreamContainer;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

@Getter
public class KafkaStreamsRegistry {
    private final Map<String, KafkaStreams> registry = mapOf();

    public static KStream<?, ?> withTracing(KStream<?, ?> stream) {
        return stream.peek(((key, value) -> System.out.println("Key : " + key + " Value : " + value)));
    }

    @SuppressWarnings("Duplicates")
    public KafkaStreamsRegistry registerStream(String appId, Function<StreamsBuilder, KafkaStreamContainer> streamCreator) {
        StreamsBuilder builder = new StreamsBuilder();
        Properties properties = new Properties();
        KafkaStreamContainer streamContainer = streamCreator.apply(builder);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, streamContainer.getConfiguration().getBootstrapServers());
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, streamContainer.getConfiguration().getKeySerde().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, streamContainer.getConfiguration().getValueSerde().getClass());
        properties.putAll(streamContainer.getConfiguration().getKafkaProperties());
        registry.put(appId, new KafkaStreams(builder.build(), properties));
        return this;
    }

    @SuppressWarnings("Duplicates")
    public KafkaStreamsRegistry registerStream(String appId, String topic, Function<KStream<?, ?>, KafkaStreamContainer> streamCreator) {
        StreamsBuilder builder = new StreamsBuilder();
        Properties properties = new Properties();
        KafkaStreamContainer streamContainer = streamCreator.apply(withTracing(builder.stream(topic)));
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, streamContainer.getConfiguration().getBootstrapServers());
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, streamContainer.getConfiguration().getKeySerde().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, streamContainer.getConfiguration().getValueSerde().getClass());
        properties.putAll(streamContainer.getConfiguration().getKafkaProperties());
        registry.put(appId, new KafkaStreams(builder.build(), properties));
        return this;
    }
}

