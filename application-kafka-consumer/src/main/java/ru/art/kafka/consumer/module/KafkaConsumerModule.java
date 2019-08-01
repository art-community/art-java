package ru.art.kafka.consumer.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.kafka.consumer.configuration.KafkaConsumerModuleConfiguration;
import ru.art.kafka.consumer.constants.KafkaConsumerModuleConstants;
import ru.art.kafka.consumer.registry.KafkaStreamsRegistry;
import ru.art.kafka.consumer.specification.KafkaConsumerServiceSpecification;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.service.ServiceModule.serviceModule;
import java.util.List;

@Getter
public class KafkaConsumerModule implements Module<KafkaConsumerModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static List<KafkaConsumerServiceSpecification> kafkaConsumerServices = serviceModule()
            .getServiceRegistry()
            .getServices()
            .values()
            .stream()
            .filter(service -> service.getServiceTypes().contains(KafkaConsumerModuleConstants.KAFKA_CONSUMER_SERVICE_TYPE))
            .map(service -> (KafkaConsumerServiceSpecification) service)
            .collect(toList());
    @Getter(lazy = true, value = PRIVATE)
    private static final KafkaConsumerModuleConfiguration kafkaConsumerModule = context().getModule(KafkaConsumerModuleConstants.KAFKA_CONSUMER_MODULE_ID, new KafkaConsumerModule());
    private String id = KafkaConsumerModuleConstants.KAFKA_CONSUMER_MODULE_ID;
    private KafkaConsumerModuleConfiguration defaultConfiguration = null;

    public static KafkaConsumerModuleConfiguration kafkaConsumerModule() {
        return getKafkaConsumerModule();
    }

    public static List<KafkaConsumerServiceSpecification> kafkaConsumerServices() {
        return getKafkaConsumerServices();
    }

    public static KafkaStreamsRegistry kafkaStreamsRegistry() {
        return getKafkaConsumerModule().getKafkaStreamsConfiguration().getKafkaStreamsRegistry();
    }
}
