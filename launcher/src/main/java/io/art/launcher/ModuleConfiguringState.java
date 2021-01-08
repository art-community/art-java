package io.art.launcher;

import io.art.configurator.module.*;
import io.art.model.implementation.module.*;
import lombok.*;

@Getter
@AllArgsConstructor
public class ModuleConfiguringState {
    private final ConfiguratorModule configurator;
    private final ModuleModel model;
}
