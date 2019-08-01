package ru.adk.http.client.interceptor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import ru.adk.core.constants.InterceptionStrategy;
import static ru.adk.core.constants.InterceptionStrategy.*;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface HttpClientResponseInterception {
    static HttpClientResponseInterception interceptAndContinue(BiConsumer<HttpUriRequest, HttpResponse> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return NEXT_INTERCEPTOR;
        };
    }

    static HttpClientResponseInterception interceptAndCall(BiConsumer<HttpUriRequest, HttpResponse> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return PROCESS_HANDLING;
        };
    }

    static HttpClientResponseInterception interceptAndReturn(BiConsumer<HttpUriRequest, HttpResponse> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return STOP_HANDLING;
        };
    }

    InterceptionStrategy intercept(HttpUriRequest request, HttpResponse response) throws Exception;
}
