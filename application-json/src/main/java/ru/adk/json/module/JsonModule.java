package ru.adk.json.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.json.configuration.JsonModuleConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.context.Context.context;
import static ru.adk.json.configuration.JsonModuleConfiguration.JsonModuleDefaultConfiguration;
import static ru.adk.json.constants.JsonModuleConstants.JSON_MODULE_ID;

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
