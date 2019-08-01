package ru.adk.kafka.consumer.configuration;


import ru.adk.core.module.ModuleConfiguration;

public interface KafkaConsumerModuleConfiguration extends ModuleConfiguration {
    KafkaConsumerConfiguration getKafkaConsumerConfiguration();

    KafkaStreamsConfiguration getKafkaStreamsConfiguration();
}
