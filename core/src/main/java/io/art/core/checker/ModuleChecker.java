package io.art.core.checker;

import lombok.experimental.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;

@UtilityClass
public class ModuleChecker {
    public static boolean withLogging() {
        return context().hasModule(LOGGING_MODULE_ID);
    }

    public static boolean withConfigurator() {
        return context().hasModule(CONFIGURATOR_MODULE_ID);
    }

    public static boolean withMeta() {
        return context().hasModule(META_MODULE_ID);
    }
}
