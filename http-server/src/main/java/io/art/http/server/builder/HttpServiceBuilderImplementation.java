/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.http.server.builder;

import lombok.*;
import io.art.http.server.constants.*;
import io.art.http.server.exception.*;
import io.art.http.server.interceptor.*;
import io.art.http.server.model.*;
import static java.util.Objects.*;
import static io.art.core.checker.CheckerForEmptiness.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.http.constants.HttpExceptionsMessages.*;
import static io.art.http.constants.HttpMethodType.*;
import static io.art.http.server.constants.HttpServerExceptionMessages.*;
import java.util.*;

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
