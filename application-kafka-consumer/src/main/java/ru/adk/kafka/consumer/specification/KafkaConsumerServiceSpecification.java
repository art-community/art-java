package ru.adk.kafka.consumer.specification;

import ru.adk.core.factory.CollectionsFactory;
import ru.adk.kafka.consumer.constants.KafkaConsumerModuleConstants;
import ru.adk.service.Specification;
import java.util.List;

public interface KafkaConsumerServiceSpecification extends Specification {
    @Override
    default List<String> getServiceTypes() {
        return CollectionsFactory.fixedArrayOf(KafkaConsumerModuleConstants.KAFKA_CONSUMER_SERVICE_TYPE);
    }

    @Override
    <P, R> R executeMethod(String topic, P request);
}
