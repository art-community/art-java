package io.art.core.context;

import io.art.core.collection.*;
import io.art.core.configuration.*;
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
    private final LazyProperty<ImmutableMap<String, Module<?, ?>>> modules;

    public void print(String message) {
        configuration.getPrinter().accept(message);
    }

    public void reload() {
        for (Map.Entry<String, Module<?, ?>> entry : modules.get().entrySet()) {
            Module<?, ?> module = entry.getValue();
            apply(configuration.getPrinter(), printer -> printer.accept(format(MODULE_RELOADING_START_MESSAGE, module.getId())));
            module.beforeReload(this);
        }
        apply(configuration.getBeforeReload(), Runnable::run);

        for (Module<?, ?> module : modules.get().values()) {
            apply(configuration.getReload(), reload -> reload.accept(module));

            module.afterReload(this);
            apply(configuration.getPrinter(), printer -> printer.accept(format(MODULE_RELOADING_END_MESSAGE, module.getId())));
        }
        apply(configuration.getAfterReload(), Runnable::run);
    }
}
