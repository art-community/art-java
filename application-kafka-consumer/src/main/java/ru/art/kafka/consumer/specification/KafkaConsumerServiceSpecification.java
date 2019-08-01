package ru.art.kafka.consumer.specification;

import ru.art.core.factory.CollectionsFactory;
import ru.art.kafka.consumer.constants.KafkaConsumerModuleConstants;
import ru.art.service.Specification;
import java.util.List;

public interface KafkaConsumerServiceSpecification extends Specification {
    @Override
    default List<String> getServiceTypes() {
        return CollectionsFactory.fixedArrayOf(KafkaConsumerModuleConstants.KAFKA_CONSUMER_SERVICE_TYPE);
    }

    @Override
    <P, R> R executeMethod(String topic, P request);
}
