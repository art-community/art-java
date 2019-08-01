package ru.art.network.manager.interceptor;

import ru.art.http.server.interceptor.HttpServerInterceptor;
import static ru.art.http.server.interceptor.HttpServerInterception.interceptAndContinue;
import static ru.art.http.server.interceptor.HttpServerInterceptor.intercept;
import static ru.art.network.manager.client.ApplicationStateClient.decrementSession;
import static ru.art.network.manager.client.ApplicationStateClient.incrementSession;

public interface HttpServerSessionInterceptor {
    HttpServerInterceptor httpServerSessionRequestInterceptor = intercept(interceptAndContinue((request, response) -> incrementSession()));
    HttpServerInterceptor httpServerSessionResponseInterceptor = intercept(interceptAndContinue((request, response) -> decrementSession()));
}
