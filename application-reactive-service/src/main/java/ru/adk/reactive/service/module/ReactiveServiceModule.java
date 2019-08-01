package ru.adk.reactive.service.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.reactive.service.configuration.ReactiveServiceModuleConfiguration;
import ru.adk.reactive.service.configuration.ReactiveServiceModuleConfiguration.ReactiveServiceModuleDefaultConfiguration;
import static ru.adk.core.context.Context.context;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.REACTIVE_SERVICE_MODULE_ID;

@Getter
public class ReactiveServiceModule implements Module<ReactiveServiceModuleConfiguration, ModuleState> {
    @Getter(lazy = true)
    private static final ReactiveServiceModuleConfiguration reactiveServiceModule = context().getModule(REACTIVE_SERVICE_MODULE_ID, new ReactiveServiceModule());
    private final ReactiveServiceModuleConfiguration defaultConfiguration = new ReactiveServiceModuleDefaultConfiguration();
    private final String id = REACTIVE_SERVICE_MODULE_ID;

    public static ReactiveServiceModuleConfiguration reactiveServiceModule() {
        return getReactiveServiceModule();
    }
}
