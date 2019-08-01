package ru.adk.kafka.producer.configuration;

import org.apache.kafka.common.serialization.Serializer;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.kafka.producer.exception.KafkaProducerConfigurationException;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import java.util.Properties;

public interface KafkaProducerConfiguration extends ModuleConfiguration {

    /**
     * @return clientId - identifies producer application
     */
    String getClientId();

    /**
     * @return topic name
     */
    String getTopic();

    /**
     * @return list ip-address and port kafka brokers
     */
    String getBootstrapServers();

    /**
     * @return Serializer for key
     */
    <KeySerializer> Serializer<KeySerializer> getKeySerializer();

    /**
     * @return Serializer for value
     */
    <ValueSerializer> Serializer<ValueSerializer> getValueSerializer();

    /**
     * Default value null
     *
     * @return Other properties for kafka producer
     * Read more http://kafka.apache.org/documentation/
     */
    default Properties getOtherProperties() {
        return new Properties();
    }

    /**
     * @return configuration kafka producer for retries
     */
    default KafkaProducerRetryConfiguration getRetries() {
        return null;
    }


    default void validate() {
        if (isEmpty(getTopic())) throw new KafkaProducerConfigurationException("topic is empty");
        if (isEmpty(getClientId())) throw new KafkaProducerConfigurationException("clientId is empty");
        if (isEmpty(getBootstrapServers())) throw new KafkaProducerConfigurationException("bootstrapServer is empty");
        if (isEmpty(getKeySerializer())) throw new KafkaProducerConfigurationException("keySerializer is empty");
        if (isEmpty(getValueSerializer())) throw new KafkaProducerConfigurationException("valueSerializer is empty");
    }
}
