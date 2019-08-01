package ru.adk.kafka.consumer.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.kafka.consumer.configuration.KafkaConsumerModuleConfiguration;
import ru.adk.kafka.consumer.constants.KafkaConsumerModuleConstants;
import ru.adk.kafka.consumer.registry.KafkaStreamsRegistry;
import ru.adk.kafka.consumer.specification.KafkaConsumerServiceSpecification;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.context.Context.context;
import static ru.adk.service.ServiceModule.serviceModule;
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
