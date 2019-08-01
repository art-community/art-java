package ru.art.http.server.interceptor;

import ru.art.core.constants.InterceptionStrategy;
import static ru.art.core.constants.InterceptionStrategy.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface HttpServerInterception {
    static HttpServerInterception interceptAndContinue(BiConsumer<HttpServletRequest, HttpServletResponse> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return NEXT_INTERCEPTOR;
        };
    }

    static HttpServerInterception interceptAndCall(BiConsumer<HttpServletRequest, HttpServletResponse> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return PROCESS_HANDLING;
        };
    }

    static HttpServerInterception interceptAndReturn(BiConsumer<HttpServletRequest, HttpServletResponse> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return STOP_HANDLING;
        };
    }

    InterceptionStrategy intercept(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
