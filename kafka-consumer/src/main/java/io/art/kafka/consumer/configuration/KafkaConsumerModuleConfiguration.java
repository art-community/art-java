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
import io.art.core.module.*;
import static io.art.core.factory.CollectionsFactory.*;
import java.util.*;

public interface KafkaConsumerModuleConfiguration extends ModuleConfiguration {
    boolean isEnableTracing();

    KafkaConsumerConfiguration getKafkaConsumerConfiguration();

    Map<String, KafkaStreamConfiguration> getKafkaStreamConfigurations();

    KafkaConsumerModuleDefaultConfiguration DEFAULT_CONFIGURATION = new KafkaConsumerModuleDefaultConfiguration();

	@Getter
	class KafkaConsumerModuleDefaultConfiguration implements KafkaConsumerModuleConfiguration {
        private final boolean enableTracing = false;
        private final KafkaConsumerConfiguration kafkaConsumerConfiguration = KafkaConsumerConfiguration.builder().build();
        private final Map<String, KafkaStreamConfiguration> kafkaStreamConfigurations = mapOf();
    }
}
