package ru.art.information.configuration;

import lombok.*;
import ru.art.core.module.*;
import java.util.function.*;

public interface InformationModuleConfiguration extends ModuleConfiguration {
    Supplier<Boolean> getStatusSupplier();

    @Getter
    class InformationModuleDefaultConfiguration implements InformationModuleConfiguration {
        private final Supplier<Boolean> statusSupplier = null;
    }
}
