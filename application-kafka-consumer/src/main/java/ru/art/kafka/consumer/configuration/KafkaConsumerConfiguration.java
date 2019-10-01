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
import static ru.art.core.caster.Caster.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.constants.ThreadConstants.*;
import static ru.art.kafka.consumer.constants.KafkaConsumerModuleConstants.*;
import static ru.art.kafka.instances.KafkaSerdes.*;
import java.time.*;
import java.util.*;

@Getter
@Builder
@EqualsAndHashCode
public class KafkaConsumerConfiguration {
    @Builder.Default
    private final int executorPoolSize = DEFAULT_THREAD_POOL_SIZE;
    @Builder.Default
    private final Duration pollTimeout = DEFAULT_DURATION;
    @Builder.Default
    private final String groupId = EMPTY_STRING;
    @Builder.Default
    private final String clientId = EMPTY_STRING;
    @Singular("topic")
    private final Set<String> topics;
    @Singular("broker")
    private final Set<String> brokers;
    @Builder.Default
    private final Deserializer<?> keyDeserializer = KAFKA_MESSAGE_PACK_DESERIALIZER;
    @Builder.Default
    private final Deserializer<?> valueDeserializer = KAFKA_MESSAGE_PACK_DESERIALIZER;
    @Builder.Default
    private final Properties additionalProperties = new Properties();

    public <KeyDeserializer> Deserializer<KeyDeserializer> getKeyDeserializer() {
        return cast(keyDeserializer);
    }

    public <ValueDeSerializer> Deserializer<ValueDeSerializer> getValueDeserializer() {
        return cast(valueDeserializer);
    }
}