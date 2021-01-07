package io.art.model.implementation.configurator;

import io.art.core.collection.*;
import lombok.*;

@Getter
@AllArgsConstructor
public class ConfiguratorModuleModel {
    private final ImmutableSet<Class<?>> customConfigurations;
}
