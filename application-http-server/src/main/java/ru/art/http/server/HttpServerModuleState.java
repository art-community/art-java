package ru.art.http.server;

import lombok.Getter;
import lombok.Setter;
import ru.art.core.module.ModuleState;
import ru.art.http.server.context.HttpRequestContext;

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
