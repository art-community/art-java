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
import ru.art.kafka.consumer.registry.*;
import java.util.*;

public interface KafkaStreamsConfiguration {
    Map<String, KafkaStreamConfiguration> getKafkaStreamConfigurations();

    KafkaStreamsRegistry getKafkaStreamsRegistry();

    @Getter
    @Builder
    class KafkaStreamsDefaultConfiguration implements KafkaStreamsConfiguration {
        @Builder.Default
        private final KafkaStreamsRegistry kafkaStreamsRegistry = new KafkaStreamsRegistry();
        @Singular("configuration")
        private final Map<String, KafkaStreamConfiguration> kafkaStreamConfigurations;
    }
}
