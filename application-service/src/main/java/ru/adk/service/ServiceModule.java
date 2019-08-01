package ru.adk.service;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import static org.apache.logging.log4j.ThreadContext.put;
import static ru.adk.core.context.Context.context;
import static ru.adk.logging.LoggingModuleConstants.LoggingParameters.REQUEST_ID_KEY;
import static ru.adk.service.constants.ServiceModuleConstants.DEFAULT_REQUEST_ID;
import static ru.adk.service.constants.ServiceModuleConstants.SERVICE_MODULE_ID;

@Getter
public class ServiceModule implements Module<ServiceModuleConfiguration, ModuleState> {
    @Getter
    private final ServiceModuleConfiguration defaultConfiguration = new ServiceModuleConfiguration.ServiceModuleDefaultConfiguration();

    private final String id = SERVICE_MODULE_ID;

    public static ServiceModuleConfiguration serviceModule() {
        return context().getModule(SERVICE_MODULE_ID, new ServiceModule());
    }

    @Override
    public void onLoad() {
        put(REQUEST_ID_KEY, DEFAULT_REQUEST_ID);
    }
}
