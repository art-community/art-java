package ru.art.kafka.consumer.configuration;

import org.apache.kafka.common.serialization.Deserializer;
import ru.art.core.module.ModuleConfiguration;
import ru.art.kafka.consumer.exception.KafkaConsumerConfigurationException;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

public interface KafkaConsumerConfiguration extends ModuleConfiguration {

    /**
     * @return Executor for multi-thread consumers
     */

    ExecutorService getExecutor();

    /**
     * @return serviceId - Unique identifier kafka producer in ADK
     */
    String getServiceId();

    /**
     * @return duration - Timeout for poll data from kafka brokers
     */
    Duration getDuration();

    /**
     * @return groupId - A unique string that identifies the producer group this producer belongs to
     */
    String getGroupId();

    /**
     * @return List topics name
     */
    List<String> getTopics();

    /**
     * @return List ip-address and port kafka brokers
     */
    String getBootstrapServers();

    /**
     * @return Deserializer for key
     */
    <KeyDeserializer> Deserializer<KeyDeserializer> getKeyDeserializer();

    /**
     * @return Deserializer for value
     */
    <ValueDeSerializer> Deserializer<ValueDeSerializer> getValueDeserializer();

    /**
     * Default value null
     *
     * @return Other properties for kafka consumer
     * Read more http://kafka.apache.org/documentation/
     */
    default Properties getOtherProperties() {
        return new Properties();
    }

    default void validate() {
        if (isEmpty(getServiceId())) throw new KafkaConsumerConfigurationException("serviceId is empty");
        if (isEmpty(getTopics())) throw new KafkaConsumerConfigurationException("topic is empty");
        if (isEmpty(getBootstrapServers())) throw new KafkaConsumerConfigurationException("bootstrapServers is empty");
        if (isEmpty(getDuration())) throw new KafkaConsumerConfigurationException("duration is empty");
        if (isEmpty(getGroupId())) throw new KafkaConsumerConfigurationException("groupId is empty");
        if (isEmpty(getKeyDeserializer())) throw new KafkaConsumerConfigurationException("keyDeserializer is empty");
        if (isEmpty(getValueDeserializer()))
            throw new KafkaConsumerConfigurationException("valueDeserializer is empty");
    }
}
