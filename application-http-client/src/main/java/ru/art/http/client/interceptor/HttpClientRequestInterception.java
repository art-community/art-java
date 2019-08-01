package ru.art.http.client.interceptor;

import org.apache.http.client.methods.HttpUriRequest;
import ru.art.core.constants.InterceptionStrategy;
import static ru.art.core.constants.InterceptionStrategy.*;
import java.util.function.Consumer;

@FunctionalInterface
public interface HttpClientRequestInterception {
    static HttpClientRequestInterception interceptAndContinue(Consumer<HttpUriRequest> runnable) {
        return request -> {
            runnable.accept(request);
            return NEXT_INTERCEPTOR;
        };
    }

    static HttpClientRequestInterception interceptAndCall(Consumer<HttpUriRequest> runnable) {
        return request -> {
            runnable.accept(request);
            return PROCESS_HANDLING;
        };
    }

    static HttpClientRequestInterception interceptAndReturn(Consumer<HttpUriRequest> runnable) {
        return request -> {
            runnable.accept(request);
            return STOP_HANDLING;
        };
    }

    InterceptionStrategy intercept(HttpUriRequest request) throws Exception;
}
