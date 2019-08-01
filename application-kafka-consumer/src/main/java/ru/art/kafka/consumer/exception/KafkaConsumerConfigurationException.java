package ru.art.kafka.consumer.exception;

public class KafkaConsumerConfigurationException extends RuntimeException {
    public KafkaConsumerConfigurationException(String message) {
        super(message);
    }
}
