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

package ru.art.kafka.consumer.launcher;

import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.art.kafka.consumer.configuration.KafkaConsumerConfiguration;
import ru.art.kafka.consumer.configuration.KafkaConsumerModuleConfiguration;
import ru.art.kafka.consumer.specification.KafkaConsumerServiceSpecification;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.kafka.consumer.module.KafkaConsumerModule.kafkaConsumerModule;
import static ru.art.kafka.consumer.module.KafkaConsumerModule.kafkaConsumerServices;
import java.util.List;
import java.util.Properties;

@NoArgsConstructor(access = PRIVATE)
public class KafkaConsumerLauncher {
    public static void launchKafkaConsumer() {
        KafkaConsumerModuleConfiguration moduleConfiguration = kafkaConsumerModule();
        if (isEmpty(moduleConfiguration.getKafkaConsumerConfiguration())) {
            return;
        }
        kafkaConsumerAwait(moduleConfiguration.getKafkaConsumerConfiguration());
    }

    private static void kafkaConsumerAwait(KafkaConsumerConfiguration conf) {
        conf.validate();
        conf.getExecutor().submit(() -> {
            List<KafkaConsumerServiceSpecification> kafkaConsumerServiceSpecifications = kafkaConsumerServices();
            KafkaConsumer<?, ?> consumer = new KafkaConsumer<>(createProperties(conf), conf.getKeyDeserializer(), conf.getValueDeserializer());
            consumer.subscribe(conf.getTopics());
            try {
                //noinspection InfiniteLoopStatement
                while (true) {
                    ConsumerRecords<?, ?> poll = consumer.poll(conf.getDuration());
                    for (ConsumerRecord<?, ?> record : poll) {
                        kafkaConsumerServiceSpecifications.stream()
                                .filter(specification -> specification.getServiceId().equals(conf.getServiceId()))
                                .forEach(specification -> specification.executeMethod(record.topic(), record));
                    }
                }
            } finally {
                consumer.close();
            }
        });
    }

    private static Properties createProperties(KafkaConsumerConfiguration configuration) {
        Properties properties = ifEmpty(configuration.getOtherProperties(), new Properties());
        properties.put(BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServers());
        properties.put(GROUP_ID_CONFIG, configuration.getGroupId());
        return properties;
    }
}
