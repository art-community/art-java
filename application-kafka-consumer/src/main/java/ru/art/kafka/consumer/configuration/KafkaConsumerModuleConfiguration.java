package ru.art.kafka.consumer.configuration;


import ru.art.core.module.ModuleConfiguration;

public interface KafkaConsumerModuleConfiguration extends ModuleConfiguration {
    KafkaConsumerConfiguration getKafkaConsumerConfiguration();

    KafkaStreamsConfiguration getKafkaStreamsConfiguration();
}
