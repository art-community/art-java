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

package ru.art.kafka.producer.module;

import lombok.*;
import ru.art.core.module.*;
import ru.art.kafka.producer.configuration.*;

import static lombok.AccessLevel.*;
import static ru.art.core.context.Context.*;
import static ru.art.kafka.producer.constants.KafkaProducerModuleConstants.*;

@Getter
public class KafkaProducerModule implements Module<KafkaProducerModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final KafkaProducerModuleConfiguration kafkaProducerModule = context()
            .getModule(KAFKA_PRODUCER_MODULE_ID, new KafkaProducerModule());
    private final String id = KAFKA_PRODUCER_MODULE_ID;
    private final KafkaProducerModuleConfiguration defaultConfiguration = KafkaProducerModuleConfiguration.DEFAULT_CONFIGURATION;

    public static KafkaProducerModuleConfiguration kafkaProducerModule() {
        if (insideDefaultContext()) {
            return KafkaProducerModuleConfiguration.DEFAULT_CONFIGURATION;
        }
        return getKafkaProducerModule();
    }

}
