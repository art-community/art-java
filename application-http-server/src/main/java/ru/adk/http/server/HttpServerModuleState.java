package ru.adk.http.server;

import lombok.Getter;
import lombok.Setter;
import ru.adk.core.module.ModuleState;
import ru.adk.http.server.context.HttpRequestContext;

public class HttpServerModuleState implements ModuleState {
    private final ThreadLocal<HttpRequestContext> requestContext = new ThreadLocal<>();
    @Getter
    @Setter
    private HttpServer server;

    HttpRequestContext getRequestContext() {
        return requestContext.get();
    }

    void updateRequestContext(HttpRequestContext requestContext) {
        this.requestContext.set(requestContext);
    }
}
