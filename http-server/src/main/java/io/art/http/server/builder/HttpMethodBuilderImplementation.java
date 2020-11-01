/*
 * ART
 *
 * Copyright 2020 ART
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

import io.art.core.checker.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.art.value.immutable.Value;
import io.art.value.interceptor.ValueInterceptor;
import io.art.value.mapper.ValueFromModelMapper;
import io.art.value.mapper.ValueToModelMapper;
import io.art.http.constants.HttpMethodType;
import io.art.http.constants.HttpRequestDataSource;
import io.art.http.constants.MimeToContentTypeMapper;
import io.art.http.server.builder.HttpServiceBuilder.*;
import io.art.http.server.constants.HttpServerModuleConstants.HttpResponseHandlingMode;
import io.art.http.server.exception.HttpServerException;
import io.art.http.server.interceptor.HttpServerInterceptor;
import io.art.http.server.model.HttpService;
import io.art.http.server.path.HttpPath;
import io.art.server.constants.RequestValidationPolicy;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.constants.StringConstants.SLASH;
import static io.art.core.checker.NullityChecker.orElse;
import static io.art.core.factory.CollectionsFactory.linkedListOf;
import static io.art.core.factory.CollectionsFactory.setOf;
import static io.art.http.constants.HttpRequestDataSource.*;
import static io.art.http.server.constants.HttpServerExceptionMessages.HTTP_METHOD_LISTENING_PATH_IS_EMPTY;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResponseHandlingMode.CHECKED;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResponseHandlingMode.UNCHECKED;
import static io.art.http.server.module.HttpServerModule.httpServerModule;
import static io.art.server.constants.RequestValidationPolicy.NON_VALIDATABLE;

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
    private final List<ValueInterceptor<Value, Value>> requestValueInterceptors = linkedListOf(httpServerModule().getRequestValueInterceptors());
    @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
    private final List<ValueInterceptor<Value, Value>> responseValueInterceptors = linkedListOf(httpServerModule().getResponseValueInterceptors());
    private final List<ValueInterceptor<Value, Value>> exceptionValueInterceptors = linkedListOf();

    @Override
    public HttpServiceBuilder listen(String path) {
        if (isEmpty(path))
            throw new HttpServerException(HTTP_METHOD_LISTENING_PATH_IS_EMPTY);
        return serviceConfigBuilder.add(HttpService.HttpMethod.builder()
                .methodId(methodId)
                .path(HttpPath.builder().contextPath(path)
                        .parameters(NullityChecker.orElse(pathParams, emptySet()))
                        .build())
                .methodType(type)
                .requestDataSource(requestDataResource)
                .requestMapper(requestMapper)
                .responseMapper(responseMapper)
                .exceptionMapper(exceptionMapper)
                .requestInterceptors(requestInterceptors)
                .responseInterceptors(responseInterceptors)
                .requestValidationPolicy(orElse(requestValidationPolicy, NON_VALIDATABLE))
                .consumesMimeType(NullityChecker.orElse(consumesMimeType, httpServerModule().getConsumesMimeTypeMapper()))
                .producesMimeType(NullityChecker.orElse(producesMimeType, httpServerModule().getProducesMimeTypeMapper()))
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
        requestInterceptors.add(interceptor);
        return this;
    }

    @Override
    public HttpMethodBuilder addResponseInterceptor(HttpServerInterceptor interceptor) {
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
        this.requestMapper = requestMapper;
        return this;
    }

    @Override
    public HttpMethodRequestBuilder validationPolicy(RequestValidationPolicy policy) {
        this.requestValidationPolicy = policy;
        return this;
    }

    @Override
    public HttpMethodBuilder responseMapper(ValueFromModelMapper responseMapper) {
        this.responseMapper = responseMapper;
        return this;
    }

    public HttpMethodBuilder exceptionMapper(ValueFromModelMapper exceptionMapper) {
        this.exceptionMapper = exceptionMapper;
        return this;
    }

    @Override
    public HttpMethodBuilder addRequestValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        getRequestValueInterceptors().add(interceptor);
        return this;
    }

    @Override
    public HttpMethodBuilder addResponseValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        getResponseValueInterceptors().add(interceptor);
        return this;
    }

    @Override
    public HttpMethodBuilder addExceptionValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        exceptionValueInterceptors.add(interceptor);
        return this;
    }
}
