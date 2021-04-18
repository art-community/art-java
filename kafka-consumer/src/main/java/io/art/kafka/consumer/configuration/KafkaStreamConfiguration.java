/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.kafka.consumer.configuration;

import lombok.*;
import org.apache.kafka.common.serialization.*;
import static io.art.kafka.instances.KafkaSerdes.*;
import java.util.*;

@Getter
@Builder(builderMethodName = "streamConfiguration")
@EqualsAndHashCode
public class KafkaStreamConfiguration {
    /**
     * list ip-address and port kafka brokers
     */
    @Singular("broker")
    private final Set<String> brokers;

    /**
     * @return List topics name
     */
    private final String topic;

    /**
     * deserializer for key
     */
    @Builder.Default
    private final Serde<?> keySerde = KAFKA_MESSAGE_PACK_SERDE;

    /**
     * @return Deserializer for value
     */
    @Builder.Default
    private final Serde<?> valueSerde = KAFKA_MESSAGE_PACK_SERDE;

    /**
     * Default value null
     * Other properties for kafka consumer
     * Read more http://kafka.apache.org/documentation/
     */
    @Builder.Default
    private final Properties additionalProperties = new Properties();
}
