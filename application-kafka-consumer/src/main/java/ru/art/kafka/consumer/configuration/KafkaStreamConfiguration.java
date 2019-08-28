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

package ru.art.kafka.consumer.configuration;

import lombok.*;
import org.apache.kafka.common.serialization.*;
import ru.art.kafka.consumer.exception.*;
import ru.art.kafka.serde.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import java.util.*;

@Getter
@Builder(builderMethodName = "streamConfiguration")
public class KafkaStreamConfiguration {
    /**
     * list ip-address and port kafka brokers
     */
    @Singular("broker")
    private final List<String> brokers;

    /**
     * @return List topics name
     */
    private final String topic;

    /**
     * deserializer for key
     */
    @Builder.Default
    private final Serde<?> keySerde = new KafkaProtobufSerde();

    /**
     * @return Deserializer for value
     */
    @Builder.Default
    private final Serde<?> valueSerde = new KafkaProtobufSerde();

    /**
     * Default value null
     * Other properties for kafka consumer
     * Read more http://kafka.apache.org/documentation/
     */
    @Builder.Default
    private final Properties additionalProperties = new Properties();

    public void validate() {
        if (isEmpty(brokers)) throw new KafkaConsumerModuleException("brokers are empty");
        if (isEmpty(topic)) throw new KafkaConsumerModuleException("topic is empty");
    }
}
