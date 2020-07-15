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

package io.art.kafka.consumer.starter;

import lombok.experimental.*;
import io.art.kafka.consumer.registry.*;
import static io.art.kafka.consumer.module.KafkaConsumerModule.*;


@UtilityClass
public class KafkaStreamsStarter{
    public static void startKafkaStreams() {
        kafkaStreamsRegistry().getStreams().values().forEach(stream -> stream.getKafkaStreams().start());
    }

    public static void startKafkaStreams(KafkaStreamsRegistry registry) {
        registry.getStreams().values().forEach(stream -> stream.getKafkaStreams().start());
    }
}
