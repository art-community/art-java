package ru.art.http.server.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.http.server.HttpServerModuleConfiguration;
import ru.art.http.server.HttpServerModuleState;
import ru.art.http.server.specification.HttpServiceSpecification;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVER_MODULE_ID;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.art.service.ServiceModule.serviceModule;
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
