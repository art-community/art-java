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


import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.kafka.consumer.configuration.KafkaConsumerConfiguration.KafkaConsumerDefaultConfiguration;
import ru.art.kafka.consumer.configuration.KafkaStreamsConfiguration.KafkaStreamsDefaultConfiguration;

public interface KafkaConsumerModuleConfiguration extends ModuleConfiguration {
    boolean isEnableTracing();

    KafkaConsumerConfiguration getKafkaConsumerConfiguration();

    KafkaStreamsConfiguration getKafkaStreamsConfiguration();

    KafkaConsumerModuleDefaultConfiguration DEFAULT_CONFIGURATION = new KafkaConsumerModuleDefaultConfiguration();

	@Getter
	class KafkaConsumerModuleDefaultConfiguration implements KafkaConsumerModuleConfiguration {
        private final boolean enableTracing = false;
        private final KafkaConsumerConfiguration kafkaConsumerConfiguration = KafkaConsumerDefaultConfiguration.builder().build();
        private final KafkaStreamsConfiguration kafkaStreamsConfiguration = KafkaStreamsDefaultConfiguration.builder().build();
    }
}
