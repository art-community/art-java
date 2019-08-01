package ru.adk.network.manager.interceptor;

import ru.adk.http.server.interceptor.HttpServerInterceptor;
import static ru.adk.http.server.interceptor.HttpServerInterception.interceptAndContinue;
import static ru.adk.http.server.interceptor.HttpServerInterceptor.intercept;
import static ru.adk.network.manager.client.ApplicationStateClient.decrementSession;
import static ru.adk.network.manager.client.ApplicationStateClient.incrementSession;

public interface HttpServerSessionInterceptor {
    HttpServerInterceptor httpServerSessionRequestInterceptor = intercept(interceptAndContinue((request, response) -> incrementSession()));
    HttpServerInterceptor httpServerSessionResponseInterceptor = intercept(interceptAndContinue((request, response) -> decrementSession()));
}
