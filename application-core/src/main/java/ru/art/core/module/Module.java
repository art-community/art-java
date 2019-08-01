package ru.art.core.module;

import ru.art.core.exception.InternalRuntimeException;
import ru.art.core.identified.UniqueIdentified;
import static java.text.MessageFormat.format;
import static ru.art.core.constants.ExceptionMessages.MODULE_HAS_NOT_STATE;

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
