package ru.adk.http.server.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.http.server.HttpServerModuleConfiguration;
import ru.adk.http.server.HttpServerModuleState;
import ru.adk.http.server.specification.HttpServiceSpecification;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.context.Context.context;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HTTP_SERVER_MODULE_ID;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.adk.service.ServiceModule.serviceModule;
import java.util.List;

@Getter
public class HttpServerModule implements Module<HttpServerModuleConfiguration, HttpServerModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static List<HttpServiceSpecification> httpServices = serviceModule().getServiceRegistry()
            .getServices()
            .values()
            .stream()
            .filter(service -> service.getServiceTypes().contains(HTTP_SERVICE_TYPE))
            .map(service -> (HttpServiceSpecification) service)
            .collect(toList());
    @Getter(lazy = true, value = PRIVATE)
    private final static HttpServerModuleConfiguration httpServerModule = context().getModule(HTTP_SERVER_MODULE_ID, new HttpServerModule());
    @Getter
    private final HttpServerModuleConfiguration defaultConfiguration = new HttpServerModuleConfiguration.HttpServerModuleDefaultConfiguration();
    @Getter
    private final HttpServerModuleState state = new HttpServerModuleState();

    private final String id = HTTP_SERVER_MODULE_ID;

    public static HttpServerModuleConfiguration httpServerModule() {
        return getHttpServerModule();
    }

    public static List<HttpServiceSpecification> httpServices() {
        return getHttpServices();
    }

    public static HttpServerModuleState httpServerModuleState() {
        return context().getModuleState(HTTP_SERVER_MODULE_ID, new HttpServerModule());
    }
}
