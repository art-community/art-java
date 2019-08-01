package ru.adk.http.server.builder;

import lombok.RequiredArgsConstructor;
import ru.adk.http.server.constants.HttpServerExceptionMessages;
import ru.adk.http.server.exception.HttpServerException;
import ru.adk.http.server.interceptor.HttpServerInterceptor;
import ru.adk.http.server.model.HttpService;
import static java.util.Objects.isNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.factory.CollectionsFactory.linkedListOf;
import static ru.adk.http.constants.HttpExceptionsMessages.REQUEST_INTERCEPTOR_IS_NULL;
import static ru.adk.http.constants.HttpMethodType.*;
import static ru.adk.http.server.constants.HttpServerExceptionMessages.*;
import java.util.List;

@RequiredArgsConstructor
public class HttpServiceBuilderImplementation implements HttpServiceBuilder {
    private final List<HttpServerInterceptor> requestInterceptors = linkedListOf();
    private final List<HttpServerInterceptor> responseInterceptors = linkedListOf();
    private final List<HttpService.HttpMethod> methods = linkedListOf();

    @Override
    public HttpMethodWithBodyBuilder get(String methodId) {
        if (isEmpty(methodId)) throw new HttpServerException(HttpServerExceptionMessages.HTTP_METHOD_IS_EMPTY);
        return new HttpMethodBuilderImplementation(this, GET, methodId);
    }

    @Override
    public HttpMethodWithBodyBuilder post(String methodId) {
        if (isEmpty(methodId)) throw new HttpServerException(HTTP_METHOD_IS_EMPTY);
        return new HttpMethodBuilderImplementation(this, POST, methodId);
    }

    @Override
    public HttpMethodWithBodyBuilder put(String methodId) {
        if (isEmpty(methodId)) throw new HttpServerException(HTTP_METHOD_IS_EMPTY);
        return new HttpMethodBuilderImplementation(this, PUT, methodId);
    }

    @Override
    public HttpMethodWithBodyBuilder patch(String methodId) {
        if (isEmpty(methodId)) throw new HttpServerException(HTTP_METHOD_IS_EMPTY);
        return new HttpMethodBuilderImplementation(this, PATCH, methodId);
    }

    @Override
    public HttpMethodWithBodyBuilder delete(String methodId) {
        if (isEmpty(methodId)) throw new HttpServerException(HTTP_METHOD_IS_EMPTY);
        return new HttpMethodBuilderImplementation(this, DELETE, methodId);
    }

    @Override
    public HttpMethodWithParamsBuilder head(String methodId) {
        if (isEmpty(methodId)) throw new HttpServerException(HTTP_METHOD_IS_EMPTY);
        return new HttpMethodBuilderImplementation(this, HEAD, methodId);
    }

    @Override
    public HttpMethodWithParamsBuilder trace(String methodId) {
        if (isEmpty(methodId)) throw new HttpServerException(HTTP_METHOD_IS_EMPTY);
        return new HttpMethodBuilderImplementation(this, TRACE, methodId);
    }

    @Override
    public HttpMethodWithParamsBuilder connect(String methodId) {
        if (isEmpty(methodId)) throw new HttpServerException(HTTP_METHOD_IS_EMPTY);
        return new HttpMethodBuilderImplementation(this, CONNECT, methodId);
    }

    @Override
    public HttpMethodWithBodyBuilder options(String methodId) {
        if (isEmpty(methodId)) throw new HttpServerException(HTTP_METHOD_IS_EMPTY);
        return new HttpMethodBuilderImplementation(this, OPTIONS, methodId);
    }

    @Override
    public HttpServiceBuilder addRequestInterceptor(HttpServerInterceptor interceptor) {
        if (isNull(interceptor)) throw new HttpServerException(REQUEST_INTERCEPTOR_IS_NULL);
        requestInterceptors.add(interceptor);
        return this;
    }

    @Override
    public HttpServiceBuilder addResponseInterceptor(HttpServerInterceptor interceptor) {
        if (isNull(interceptor)) throw new HttpServerException(REQUEST_INTERCEPTOR_IS_NULL);
        responseInterceptors.add(interceptor);
        return this;
    }

    @Override
    public HttpService serve(String serviceContextPath) {
        if (isNull(serviceContextPath)) throw new HttpServerException(HTTP_SERVICE_LISTENING_PATH_IS_EMPTY);
        return new HttpService(serviceContextPath, methods, requestInterceptors, responseInterceptors);
    }

    HttpServiceBuilder add(HttpService.HttpMethod method) {
        if (isNull(method)) throw new HttpServerException(HTTP_METHOD_IS_NULL);
        methods.add(method);
        return this;
    }
}