package ru.art.kafka.producer.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.kafka.producer.configuration.KafkaProducerConfiguration;
import ru.art.kafka.producer.constants.KafkaProducerModuleConstants;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;

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
