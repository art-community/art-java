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

package ru.art.kafka.consumer.module;

import lombok.*;
import ru.art.core.module.*;
import ru.art.kafka.consumer.configuration.*;
import ru.art.kafka.consumer.registry.*;
import ru.art.kafka.consumer.specification.*;
import java.util.*;

import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.context.Context.*;
import static ru.art.kafka.consumer.configuration.KafkaConsumerModuleConfiguration.*;
import static ru.art.kafka.consumer.constants.KafkaConsumerModuleConstants.*;
import static ru.art.service.ServiceModule.*;

@Getter
public class KafkaConsumerModule implements Module<KafkaConsumerModuleConfiguration, ModuleState> {
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}), value = PRIVATE)
    private final static List<KafkaConsumerServiceSpecification> kafkaConsumerServices = cast(serviceModule()
            .getServiceRegistry()
            .getServices()
            .values()
            .stream()
            .filter(service -> service.getServiceTypes().contains(KAFKA_CONSUMER_SERVICE_TYPE))
            .map(service -> (KafkaConsumerServiceSpecification) service)
            .collect(toList()));
    @Getter(lazy = true, value = PRIVATE)
    private static final KafkaConsumerModuleConfiguration kafkaConsumerModule = context()
            .getModule(KAFKA_CONSUMER_MODULE_ID, KafkaConsumerModule::new);
    private String id = KAFKA_CONSUMER_MODULE_ID;
    private KafkaConsumerModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;

    public static KafkaConsumerModuleConfiguration kafkaConsumerModule() {
        if (insideDefaultContext()) {
            return DEFAULT_CONFIGURATION;
        }
        return getKafkaConsumerModule();
    }

    public static List<KafkaConsumerServiceSpecification> kafkaConsumerServices() {
        return getKafkaConsumerServices();
    }

    public static KafkaStreamsRegistry kafkaStreamsRegistry() {
        return getKafkaConsumerModule().getKafkaStreamsConfiguration().getKafkaStreamsRegistry();
    }
}
