package ru.art.config.state;

import lombok.*;
import lombok.experimental.*;
import ru.art.core.module.*;

@Getter
@Setter
@Accessors(fluent = true)
public class ConfigModuleState implements ModuleState {
    private volatile boolean useRemoteConfiguration;
}
