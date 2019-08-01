package ru.adk.kafka.consumer.launcher;

import org.apache.kafka.streams.KafkaStreams;
import ru.adk.kafka.consumer.registry.KafkaStreamsRegistry;
import static ru.adk.kafka.consumer.module.KafkaConsumerModule.kafkaStreamsRegistry;


public interface KafkaStreamsLauncher {
    static void startKafkaStreams() {
        kafkaStreamsRegistry().getRegistry().values().forEach(KafkaStreams::start);
    }

    static void startKafkaStreams(KafkaStreamsRegistry registry) {
        registry.getRegistry().values().forEach(KafkaStreams::start);
    }
}