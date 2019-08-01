package ru.adk.kafka.producer.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.kafka.producer.configuration.KafkaProducerConfiguration;
import ru.adk.kafka.producer.constants.KafkaProducerModuleConstants;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.context.Context.context;

@Getter
public class KafkaProducerModule implements Module<KafkaProducerConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final KafkaProducerConfiguration kafkaProducerModule = context().getModule(KafkaProducerModuleConstants.KAFKA_PRODUCER_MODULE_ID, new KafkaProducerModule());
    private final String id = KafkaProducerModuleConstants.KAFKA_PRODUCER_MODULE_ID;
    private final KafkaProducerConfiguration defaultConfiguration = null;

    public static KafkaProducerConfiguration kafkaProducerModule() {
        return getKafkaProducerModule();
    }

}
