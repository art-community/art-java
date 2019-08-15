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

import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.serialization.Serde;
import ru.art.kafka.consumer.exception.KafkaConsumerModuleException;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.kafka.consumer.module.KafkaConsumerModule.kafkaConsumerModule;
import java.util.Properties;

@Getter
@Builder(builderMethodName = "streamConfiguration")
public class KafkaStreamConfiguration<KeySerde, ValueSerde> {
    /**
     * list ip-address and port kafka brokers
     */
    private final String bootstrapServers;

    /**
     * deserializer for key
     */
    private final Serde<KeySerde> keySerde;

    /**
     * @return Deserializer for value
     */
    private final Serde<ValueSerde> valueSerde;

    /**
     * Default value null
     * Other properties for kafka consumer
     * Read more http://kafka.apache.org/documentation/
     */
    @Getter(lazy = true)
    private final Properties kafkaProperties = kafkaConsumerModule().getKafkaStreamsConfiguration().getCustomProperties();


    public void validate() {
        if (isEmpty(bootstrapServers)) throw new KafkaConsumerModuleException("bootstrapServer is empty");
        if (isEmpty(keySerde)) throw new KafkaConsumerModuleException("keySerde is empty");
        if (isEmpty(valueSerde)) throw new KafkaConsumerModuleException("valueSerde is empty");
    }
}
