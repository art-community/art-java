package ru.art.json.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.json.configuration.JsonModuleConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.json.configuration.JsonModuleConfiguration.JsonModuleDefaultConfiguration;
import static ru.art.json.constants.JsonModuleConstants.JSON_MODULE_ID;

@Getter
public class JsonModule implements Module<JsonModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static JsonModuleConfiguration jsonModule = context().getModule(JSON_MODULE_ID, new JsonModule());
    @Getter
    private final JsonModuleConfiguration defaultConfiguration = new JsonModuleDefaultConfiguration();
    private final String id = JSON_MODULE_ID;

    public static JsonModuleConfiguration jsonModule() {
        return getJsonModule();
    }
}
