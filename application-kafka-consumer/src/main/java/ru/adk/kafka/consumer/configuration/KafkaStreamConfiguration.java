package ru.adk.kafka.consumer.configuration;

import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.common.serialization.Serde;
import ru.adk.kafka.consumer.exception.KafkaConsumerConfigurationException;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.kafka.consumer.module.KafkaConsumerModule.kafkaConsumerModule;
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
        if (isEmpty(bootstrapServers)) throw new KafkaConsumerConfigurationException("bootstrapServer is empty");
        if (isEmpty(keySerde)) throw new KafkaConsumerConfigurationException("keySerde is empty");
        if (isEmpty(valueSerde)) throw new KafkaConsumerConfigurationException("valueSerde is empty");
    }
}
