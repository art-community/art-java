package ru.adk.kafka.producer.exception;

public class KafkaProducerConfigurationException extends RuntimeException {
    public KafkaProducerConfigurationException(String message) {
        super(message);
    }
}
