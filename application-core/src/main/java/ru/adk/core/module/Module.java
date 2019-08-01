package ru.adk.core.module;

import ru.adk.core.exception.InternalRuntimeException;
import ru.adk.core.identified.UniqueIdentified;
import static java.text.MessageFormat.format;
import static ru.adk.core.constants.ExceptionMessages.MODULE_HAS_NOT_STATE;

public interface Module<C extends ModuleConfiguration, S extends ModuleState> extends UniqueIdentified {
    default void onLoad() {
    }

    default void onUnload() {
    }

    default void reload() {
    }

    C getDefaultConfiguration();

    default S getState() {
        throw new InternalRuntimeException(format(MODULE_HAS_NOT_STATE, getId()));
    }
}
