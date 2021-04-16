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

package io.art.kafka.consumer.state;

import lombok.*;
import io.art.core.module.*;
import io.art.kafka.consumer.model.*;
import io.art.kafka.consumer.registry.*;
import static io.art.core.factory.CollectionsFactory.*;
import java.util.*;

@Getter
public class KafkaConsumerModuleState implements ModuleState {
    private final Map<String, ManagedKafkaConsumer> kafkaConsumers = concurrentHashMap();
    private final KafkaStreamsRegistry kafkaStreamsRegistry = new KafkaStreamsRegistry();
}
