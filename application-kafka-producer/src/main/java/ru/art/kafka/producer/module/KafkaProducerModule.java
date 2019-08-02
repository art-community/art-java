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

package ru.art.kafka.producer.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.kafka.producer.configuration.KafkaProducerConfiguration;
import ru.art.kafka.producer.constants.KafkaProducerModuleConstants;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;

@Getter
public class KafkaProducerModule implements Module<KafkaProducerConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final KafkaProducerConfiguration kafkaProducerModule = context().getModule(KafkaProducerModuleConstants.KAFKA_PRODUCER_MODULE_ID, new KafkaProducerModule());
    private final String id = KafkaProducerModuleConstants.KAFKA_PRODUCER_MODULE_ID;
    private final KafkaProducerConfiguration defaultConfiguration = null;

    public static KafkaProducerConfiguration kafkaProducerModule() {
        return getKafkaProducerModule();
    }

}
