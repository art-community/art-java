package ru.art.kafka.broker.configuration;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder(builderMethodName = "topicConfiguration")
@EqualsAndHashCode
public class KafkaTopicConfiguration {
    private Integer partitions;
    private Long retention;
}
