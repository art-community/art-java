/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.kafka.producer.module;

import lombok.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import io.art.kafka.producer.configuration.*;
import static lombok.AccessLevel.*;
import static io.art.core.context.Context.*;
import static io.art.kafka.producer.configuration.KafkaProducerModuleConfiguration.*;
import static io.art.kafka.producer.constants.KafkaProducerModuleConstants.*;

@Getter
public class KafkaProducerModule implements Module<KafkaProducerModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final KafkaProducerModuleConfiguration kafkaProducerModule = context().getModule(KAFKA_PRODUCER_MODULE_ID, KafkaProducerModule::new);
    private final String id = KAFKA_PRODUCER_MODULE_ID;
    private final KafkaProducerModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;

    public static KafkaProducerModuleConfiguration kafkaProducerModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getKafkaProducerModule();
    }

}
