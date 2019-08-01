package ru.art.kafka.producer.communicator;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.art.kafka.producer.configuration.KafkaProducerConfiguration;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.CLIENT_ID_CONFIG;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import java.util.Properties;

/**
 * The wrapper for KafkaProducer API
 *
 * @param <KeyType>   - type data for key
 * @param <ValueType> - type data for value
 */
public class KafkaProducerCommunicator<KeyType, ValueType> {
    private final String topic;
    private final KafkaProducer<KeyType, ValueType> kafkaProducer;
    private Callback callback;

    /**
     * Checking all required configuration for sending message to broker
     * Initialization kafka producer
     *
     * @param configuration - configuration for kafka producer proxy
     */
    public KafkaProducerCommunicator(KafkaProducerConfiguration configuration) {
        configuration.validate();
        Properties properties = ifEmpty(configuration.getOtherProperties(), new Properties());
        properties.put(BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServers());
        properties.put(CLIENT_ID_CONFIG, configuration.getClientId());
        this.topic = configuration.getTopic();
        this.kafkaProducer = new KafkaProducer<>(properties, configuration.getKeySerializer(), configuration.getValueSerializer());
    }

    public void pushKafkaRecord(KeyType key, ValueType value) {
        ProducerRecord<KeyType, ValueType> producerRecord = new ProducerRecord<>(topic, key, value);
        kafkaProducer.send(producerRecord, callback);
        kafkaProducer.close();
    }

    public KafkaProducerCommunicator<KeyType, ValueType> prepareCallbackWrapper(Callback callback) {
        this.callback = callback;
        return this;
    }
}
