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

package ru.art.http.server.builder;

import lombok.*;
import ru.art.entity.Value;
import ru.art.entity.interceptor.*;
import ru.art.entity.mapper.*;
import ru.art.http.constants.*;
import ru.art.http.server.builder.HttpServiceBuilder.*;
import ru.art.http.server.constants.HttpServerModuleConstants.*;
import ru.art.http.server.exception.*;
import ru.art.http.server.interceptor.*;
import ru.art.http.server.model.*;
import ru.art.http.server.path.*;
import ru.art.service.constants.*;
import java.util.*;

import static java.util.Collections.*;
import static java.util.Objects.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.http.constants.HttpExceptionsMessages.*;
import static ru.art.http.constants.HttpRequestDataSource.*;
import static ru.art.http.server.constants.HttpServerExceptionMessages.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpResponseHandlingMode.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.service.constants.RequestValidationPolicy.*;

@RequiredArgsConstructor
public class HttpMethodBuilderImplementation implements HttpMethodBuilder,
        HttpMethodWithParamsBuilder,
        HttpMethodWithBodyBuilder,
        HttpMethodResponseBuilder,
        HttpMethodRequestBuilder {
    private final HttpServiceBuilderImplementation serviceConfigBuilder;
    private final HttpMethodType type;
    private final String methodId;
    private final List<HttpServerInterceptor> requestInterceptors = linkedListOf();
    private final List<HttpServerInterceptor> responseInterceptors = linkedListOf();
    private HttpRequestDataSource requestDataResource;
    private ValueToModelMapper requestMapper;
    private ValueFromModelMapper responseMapper;
    private ValueFromModelMapper exceptionMapper;
    private Set<String> pathParams;
    private RequestValidationPolicy requestValidationPolicy;
    private MimeToContentTypeMapper consumesMimeType;
    private MimeToContentTypeMapper producesMimeType;
    private boolean ignoreRequestAcceptType;
    private boolean ignoreRequestContentType;
    private boolean overrideResponseContentType;
    private HttpResponseHandlingMode responseHandlingMode = CHECKED;
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
    private final List<ValueInterceptor<Value, Value>> requestValueInterceptors = httpServerModule().getRequestValueInterceptors();
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
    private final List<ValueInterceptor<Value, Value>> responseValueInterceptors = httpServerModule().getResponseValueInterceptors();
    private final List<ValueInterceptor<Value, Value>> exceptionValueInterceptors = linkedListOf();

    @Override
    public HttpServiceBuilder listen(String path) {
        if (isEmpty(path))
            throw new HttpServerException(HTTP_METHOD_LISTENING_PATH_IS_EMPTY);
        return serviceConfigBuilder.add(HttpService.HttpMethod.builder()
                .methodId(methodId)
                .path(HttpPath.builder().contextPath(path)
                        .parameters(getOrElse(pathParams, emptySet()))
                        .build())
                .methodType(type)
                .requestDataSource(requestDataResource)
                .requestMapper(requestMapper)
                .responseMapper(responseMapper)
                .exceptionMapper(exceptionMapper)
                .requestInterceptors(requestInterceptors)
                .responseInterceptors(responseInterceptors)
                .requestValidationPolicy(getOrElse(requestValidationPolicy, NON_VALIDATABLE))
                .consumesMimeType(getOrElse(consumesMimeType, httpServerModule().getConsumesMimeTypeMapper()))
                .producesMimeType(getOrElse(producesMimeType, httpServerModule().getProducesMimeTypeMapper()))
                .ignoreRequestAcceptType(ignoreRequestAcceptType)
                .ignoreRequestContentType(ignoreRequestContentType)
                .overrideResponseContentType(overrideResponseContentType)
                .responseHandlingMode(responseHandlingMode)
                .requestValueInterceptors(getRequestValueInterceptors())
                .responseValueInterceptors(getResponseValueInterceptors())
                .exceptionValueInterceptors(exceptionValueInterceptors)
                .build());
    }


    @Override
    public HttpMethodBuilder addRequestInterceptor(HttpServerInterceptor interceptor) {
        if (isNull(interceptor)) throw new HttpServerException(REQUEST_INTERCEPTOR_IS_NULL);
        requestInterceptors.add(interceptor);
        return this;
    }

    @Override
    public HttpMethodBuilder addResponseInterceptor(HttpServerInterceptor interceptor) {
        if (isNull(interceptor)) throw new HttpServerException(RESPONSE_INTERCEPTOR_IS_NULL);
        responseInterceptors.add(interceptor);
        return this;
    }

    @Override
    public HttpServiceBuilder listen() {
        return listen(SLASH + methodId);
    }

    @Override
    public HttpMethodRequestBuilder fromQueryParameters() {
        requestDataResource = QUERY_PARAMETERS;
        return this;
    }

    @Override
    public HttpMethodRequestBuilder fromPathParameters(String... parameters) {
        requestDataResource = PATH_PARAMETERS;
        pathParams = setOf(parameters);
        return this;
    }

    @Override
    public HttpMethodRequestBuilder fromBody() {
        requestDataResource = BODY;
        return this;
    }

    @Override
    public HttpMethodRequestBuilder fromMultipart() {
        requestDataResource = MULTIPART;
        return this;
    }

    @Override
    public HttpMethodWithBodyBuilder consumes(MimeToContentTypeMapper mimeType) {
        if (isNull(mimeType)) throw new HttpServerException(REQUEST_CONTENT_TYPE_IS_NULL);
        this.consumesMimeType = mimeType;
        return this;
    }

    @Override
    public HttpMethodResponseBuilder ignoreRequestAcceptType() {
        this.ignoreRequestAcceptType = true;
        return this;
    }

    @Override
    public HttpMethodResponseBuilder overrideResponseContentType() {
        this.overrideResponseContentType = true;
        return this;
    }

    @Override
    public HttpMethodBuilder checkedResponse() {
        this.responseHandlingMode = CHECKED;
        return this;
    }

    @Override
    public HttpMethodBuilder uncheckedResponse() {
        this.responseHandlingMode = UNCHECKED;
        return this;
    }

    @Override
    public HttpMethodResponseBuilder produces(MimeToContentTypeMapper mimeType) {
        if (isNull(mimeType)) throw new HttpServerException(RESPONSE_CONTENT_TYPE_IS_NULL);
        this.producesMimeType = mimeType;
        return this;
    }

    @Override
    public HttpMethodWithBodyBuilder ignoreRequestContentType() {
        ignoreRequestContentType = true;
        return this;
    }

    @Override
    public HttpMethodResponseBuilder requestMapper(ValueToModelMapper requestMapper) {
        if (isNull(requestMapper)) throw new HttpServerException(REQUEST_MAPPER_IS_NULL);
        this.requestMapper = requestMapper;
        return this;
    }

    @Override
    public HttpMethodRequestBuilder validationPolicy(RequestValidationPolicy policy) {
        if (isNull(policy)) throw new HttpServerException(VALIDATION_POLICY_IS_NULL);
        this.requestValidationPolicy = policy;
        return this;
    }

    @Override
    public HttpMethodBuilder responseMapper(ValueFromModelMapper responseMapper) {
        if (isNull(responseMapper)) throw new HttpServerException(RESPONSE_MAPPER_IS_NULL);
        this.responseMapper = responseMapper;
        return this;
    }

    public HttpMethodBuilder exceptionMapper(ValueFromModelMapper exceptionMapper) {
        if (isNull(exceptionMapper)) throw new HttpServerException(EXCEPTION_MAPPER_IS_NULL);
        this.exceptionMapper = exceptionMapper;
        return this;
    }

    @Override
    public HttpMethodBuilder addRequestValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        if (isNull(interceptor)) throw new HttpServerException(REQUEST_VALUE_INTERCEPTOR);
        getRequestValueInterceptors().add(interceptor);
        return this;
    }

    @Override
    public HttpMethodBuilder addResponseValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        if (isNull(interceptor)) throw new HttpServerException(RESPONSE_VALUE_INTERCEPTOR);
        getResponseValueInterceptors().add(interceptor);
        return this;
    }

    @Override
    public HttpMethodBuilder addExceptionValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        if (isNull(interceptor)) throw new HttpServerException(EXCEPTION_VALUE_INTERCEPTOR);
        exceptionValueInterceptors.add(interceptor);
        return this;
    }
}
