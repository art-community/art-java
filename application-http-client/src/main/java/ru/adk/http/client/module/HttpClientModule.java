package ru.adk.http.client.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.http.client.configuration.HttpClientModuleConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.context.Context.context;
import static ru.adk.http.client.configuration.HttpClientModuleConfiguration.HttpClientModuleDefaultConfiguration;
import static ru.adk.http.client.constants.HttpClientModuleConstants.HTTP_CLIENT_MODULE_ID;

@Getter
public class HttpClientModule implements Module<HttpClientModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final HttpClientModuleConfiguration httpClientModule = context().getModule(HTTP_CLIENT_MODULE_ID, new HttpClientModule());
    private final HttpClientModuleConfiguration defaultConfiguration = new HttpClientModuleDefaultConfiguration();
    private final String id = HTTP_CLIENT_MODULE_ID;

    public static HttpClientModuleConfiguration httpClientModule() {
        return getHttpClientModule();
    }
}
