package ru.art.kafka.consumer.configuration;

import lombok.Getter;
import ru.art.kafka.consumer.registry.KafkaStreamsRegistry;
import java.util.Properties;

public interface KafkaStreamsConfiguration {

    KafkaStreamsRegistry getKafkaStreamsRegistry();

    /**
     * Default value null
     * Other properties for kafka consumer
     * Read more http://kafka.apache.org/documentation/
     */
    default Properties getCustomProperties() {
        return new Properties();
    }

    @Getter
    class KafkaStreamModuleDefaultConfiguration implements KafkaStreamsConfiguration {
        KafkaStreamsRegistry kafkaStreamsRegistry = new KafkaStreamsRegistry();
    }
}
