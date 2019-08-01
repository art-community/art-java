package ru.adk.kafka.consumer.launcher;

import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.adk.kafka.consumer.configuration.KafkaConsumerConfiguration;
import ru.adk.kafka.consumer.configuration.KafkaConsumerModuleConfiguration;
import ru.adk.kafka.consumer.module.KafkaConsumerModule;
import ru.adk.kafka.consumer.specification.KafkaConsumerServiceSpecification;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.adk.core.checker.CheckerForEmptiness.isNotEmpty;
import java.util.List;
import java.util.Properties;

@NoArgsConstructor(access = PRIVATE)
public class KafkaConsumerLauncher {
    public static void consumerKafka() {
        KafkaConsumerModuleConfiguration moduleConfiguration = KafkaConsumerModule.kafkaConsumerModule();
        if (isNotEmpty(moduleConfiguration.getKafkaConsumerConfiguration()))
            kafkaConsumerAwait(moduleConfiguration.getKafkaConsumerConfiguration());
    }

    private static void kafkaConsumerAwait(KafkaConsumerConfiguration conf) {
        conf.validate();
        conf.getExecutor().submit(() -> {
            List<KafkaConsumerServiceSpecification> kafkaConsumerServiceSpecifications = KafkaConsumerModule.kafkaConsumerServices();
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
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, configuration.getGroupId());
        return properties;
    }
}
