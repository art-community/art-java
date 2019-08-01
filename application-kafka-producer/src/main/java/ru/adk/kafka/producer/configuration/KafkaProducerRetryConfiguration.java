package ru.adk.kafka.producer.configuration;

import org.apache.kafka.clients.producer.ProducerRecord;

public interface KafkaProducerRetryConfiguration {
    /**
     * @return Quantity resend any record whose send fails with a potentially temporary error
     */
    int getRetries();

    /**
     * @return Quantity resend any record whose send fails with a potentially temporary error for single connection
     */
    int getMaxAttemptPerSingleConnection();

    /**
     * @return timeout in milliseconds for an upper bound on the time to report success or
     * failure for method {@link org.apache.kafka.clients.producer.KafkaProducer#send(ProducerRecord)}
     */
    int getDeliveryTimeout();


}
