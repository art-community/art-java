package ru.art.information.configuration;

import lombok.*;
import ru.art.core.module.*;
import ru.art.information.model.*;
import java.util.function.*;

public interface InformationModuleConfiguration extends ModuleConfiguration {
    Supplier<StatusResponse> getStatusSupplier();

    @Getter
    class InformationModuleDefaultConfiguration implements InformationModuleConfiguration {
        private final Supplier<StatusResponse> statusSupplier = null;
    }
}
