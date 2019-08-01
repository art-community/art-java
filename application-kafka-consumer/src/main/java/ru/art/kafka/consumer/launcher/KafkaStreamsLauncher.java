package ru.art.kafka.consumer.launcher;

import org.apache.kafka.streams.KafkaStreams;
import ru.art.kafka.consumer.registry.KafkaStreamsRegistry;
import static ru.art.kafka.consumer.module.KafkaConsumerModule.kafkaStreamsRegistry;


public interface KafkaStreamsLauncher {
    static void startKafkaStreams() {
        kafkaStreamsRegistry().getRegistry().values().forEach(KafkaStreams::start);
    }

    static void startKafkaStreams(KafkaStreamsRegistry registry) {
        registry.getRegistry().values().forEach(KafkaStreams::start);
    }
}