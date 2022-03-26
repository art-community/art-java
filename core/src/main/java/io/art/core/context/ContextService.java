package io.art.core.context;

import io.art.core.collection.*;
import io.art.core.configuration.*;
import io.art.core.module.*;
import io.art.core.module.Module;
import io.art.core.property.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.LoggingMessages.*;
import static java.text.MessageFormat.*;
import java.util.*;

@AllArgsConstructor
public class ContextService {
    private final ContextConfiguration configuration;
    private final LazyProperty<ImmutableMap<String, ManagedModule>> modules;

    public void print(String message) {
        configuration.getPrinter().accept(message);
    }

    public void reload() {
        for (Map.Entry<String, ManagedModule> entry : modules.get().entrySet()) {
            ManagedModule module = entry.getValue();
            Module<?, ?> delegate = module.getModule();
            apply(configuration.getPrinter(), printer -> printer.accept(format(MODULE_RELOADING_START_MESSAGE, delegate.getId())));
            delegate.beforeReload(this);
            module.onBeforeReload(this);
        }
        apply(configuration.getBeforeReload(), Runnable::run);

        for (ManagedModule module : modules.get().values()) {
            Module<?, ?> delegate = module.getModule();
            apply(configuration.getReload(), reload -> reload.accept(delegate));

            delegate.afterReload(this);
            module.onAfterReload(this);
            apply(configuration.getPrinter(), printer -> printer.accept(format(MODULE_RELOADING_END_MESSAGE, delegate.getId())));
        }
        apply(configuration.getAfterReload(), Runnable::run);
    }
}
