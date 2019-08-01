package ru.art.service;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import static org.apache.logging.log4j.ThreadContext.put;
import static ru.art.core.context.Context.context;
import static ru.art.logging.LoggingModuleConstants.LoggingParameters.REQUEST_ID_KEY;
import static ru.art.service.constants.ServiceModuleConstants.DEFAULT_REQUEST_ID;
import static ru.art.service.constants.ServiceModuleConstants.SERVICE_MODULE_ID;

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
