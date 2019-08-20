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

package ru.art.http.client.communicator;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.art.core.validator.BuilderValidator;
import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.client.communicator.HttpCommunicator.HttpAsynchronousCommunicator;
import ru.art.http.client.handler.HttpCommunicationCancellationHandler;
import ru.art.http.client.handler.HttpCommunicationExceptionHandler;
import ru.art.http.client.handler.HttpCommunicationResponseHandler;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.http.client.model.HttpCommunicationTargetConfiguration;
import ru.art.http.constants.MimeToContentTypeMapper;
import static java.util.Optional.ofNullable;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.constants.StringConstants.SCHEME_DELIMITER;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.http.client.communicator.HttpCommunicationExecutor.executeAsynchronousHttpRequest;
import static ru.art.http.client.communicator.HttpCommunicationExecutor.executeHttpRequest;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import static ru.art.http.constants.HttpMethodType.*;
import java.nio.charset.Charset;
import java.util.Optional;

public class HttpCommunicatorImplementation implements HttpCommunicator, HttpAsynchronousCommunicator {
    private final BuilderValidator validator = new BuilderValidator(HttpCommunicator.class.getName());
    private final HttpCommunicationConfiguration configuration = new HttpCommunicationConfiguration();

    HttpCommunicatorImplementation(String url) {
        this(HttpCommunicationTargetConfiguration.builder().build().url(url));
    }

    HttpCommunicatorImplementation(HttpCommunicationTargetConfiguration targetConfiguration) {
        config(targetConfiguration.requestConfig());
        if (isNotEmpty(targetConfiguration.url())) {
            configuration.setUrl(targetConfiguration.url());
            return;
        }
        configuration.setUrl(validator.notEmptyField(targetConfiguration.scheme(), "scheme") + SCHEME_DELIMITER
                + validator.notEmptyField(targetConfiguration.host(), "host")
                + COLON + validator.notNullField(targetConfiguration.port(), "port")
                + validator.notEmptyField(targetConfiguration.path(), "path"));
    }

    @Override
    public HttpCommunicator get() {
        configuration.setMethodType(GET);
        return this;
    }

    @Override
    public HttpCommunicator post() {
        configuration.setMethodType(POST);
        return this;
    }

    @Override
    public HttpCommunicator put() {
        configuration.setMethodType(PUT);
        return this;
    }

    @Override
    public HttpCommunicator patch() {
        configuration.setMethodType(PATCH);
        return this;
    }

    @Override
    public HttpCommunicator options() {
        configuration.setMethodType(OPTIONS);
        return this;
    }

    @Override
    public HttpCommunicator delete() {
        configuration.setMethodType(DELETE);
        return this;
    }

    @Override
    public HttpCommunicator trace() {
        configuration.setMethodType(TRACE);
        return this;
    }

    @Override
    public HttpCommunicator head() {
        configuration.setMethodType(HEAD);
        return this;
    }

    @Override
    public HttpCommunicator produces(MimeToContentTypeMapper requestContentTypeMapper) {
        configuration.setProducesMimeType(validator.notNullField(requestContentTypeMapper, "requestContentTypeMapper"));
        return this;
    }

    @Override
    public <RequestType> HttpCommunicator requestMapper(ValueFromModelMapper<RequestType, ? extends Value> requestMapper) {
        configuration.setRequestMapper(validator.notNullField(cast(requestMapper), "requestMapper"));
        return this;
    }

    @Override
    public HttpCommunicator client(HttpClient client) {
        configuration.setSyncClient(validator.notNullField(getOrElse(client, httpClientModule().getClient()), "syncClient"));
        return this;
    }

    @Override
    public HttpCommunicator consumes(MimeToContentTypeMapper responseContentTypeMapper) {
        configuration.setConsumesMimeType(validator.notNullField(responseContentTypeMapper, "responseContentTypeMapper"));
        return this;
    }

    @Override
    public HttpCommunicator ignoreResponseContentType() {
        configuration.setIgnoreResponseContentType(true);
        return this;
    }

    @Override
    public <ResponseType> HttpCommunicator responseMapper(ValueToModelMapper<ResponseType, ? extends Value> responseMapper) {
        configuration.setResponseMapper(validator.notNullField(cast(responseMapper), "responseMapper"));
        return this;

    }

    @Override
    public HttpCommunicator config(RequestConfig requestConfig) {
        configuration.setRequestConfig(validator.notNullField(getOrElse(requestConfig, httpClientModule().getRequestConfig()), "requestConfig"));
        return this;
    }

    @Override
    public HttpCommunicator version(HttpVersion httpVersion) {
        HttpVersion version = getOrElse(httpVersion, httpClientModule().getHttpVersion());
        configuration.setHttpProtocolVersion(validator.notNullField(version, "httpVersion"));
        return this;
    }

    @Override
    public HttpCommunicator requestCharset(Charset requestContentCharset) {
        Charset charset = getOrElse(requestContentCharset, contextConfiguration().getCharset());
        configuration.setRequestContentCharset(validator.notNullField(charset, "requestContentCharset"));
        return this;
    }

    @Override
    public HttpCommunicator addPathParameter(String parameter) {
        configuration.getPathParameters().add(validator.notEmptyField(parameter, "pathParameter"));
        return this;
    }

    @Override
    public HttpCommunicator addRequestInterceptor(HttpClientInterceptor interceptor) {
        configuration.getRequestInterceptors().add(validator.notNullField(interceptor, "requestInterceptor"));
        return this;
    }

    @Override
    public HttpCommunicator addResponseInterceptor(HttpClientInterceptor interceptor) {
        configuration.getResponseInterceptors().add(validator.notNullField(interceptor, "responseInterceptor"));
        return this;
    }

    @Override
    public HttpCommunicator addQueryParameter(String name, String value) {
        configuration.getQueryParameters().put(validator.notEmptyField(name, "queryParameterName"), validator.notNullField(value, "queryParameterValue"));
        return this;
    }

    @Override
    public HttpCommunicator addHeader(String name, String value) {
        configuration.getHeaders().put(validator.notEmptyField(name, "headerName"), validator.notNullField(value, "headerValue"));
        return this;
    }

    @Override
    public HttpCommunicator chunked() {
        configuration.setChunkedBody(true);
        return this;
    }

    @Override
    public HttpCommunicator gzipCompressed() {
        configuration.setGzipCompressedBody(true);
        return this;
    }

    @Override
    public HttpCommunicator requestEncoding(String encoding) {
        configuration.setRequestContentEncoding(emptyIfNull(encoding));
        return this;
    }

    @Override
    public HttpCommunicator addRequestValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        configuration.getRequestValueInterceptors().add(validator.notNullField(interceptor, "requestValueInterceptor"));
        return this;
    }

    @Override
    public HttpCommunicator addResponseValueInterceptor(ValueInterceptor<Value, Value> interceptor) {
        configuration.getResponseValueInterceptors().add(validator.notNullField(interceptor, "responseValueInterceptor"));
        return this;
    }

    @Override
    public <RequestType, ResponseType> Optional<ResponseType> execute(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        return ofNullable(executeHttpRequest(configuration));
    }

    @Override
    public <ResponseType> Optional<ResponseType> execute() {
        validator.validate();
        return ofNullable(executeHttpRequest(configuration));
    }


    @Override
    public HttpAsynchronousCommunicator client(HttpAsyncClient client) {
        configuration.setAsyncClient(validator.notNullField(client, "asyncClient"));
        return this;
    }

    @Override
    public <RequestType, ResponseType> HttpAsynchronousCommunicator completionHandler(HttpCommunicationResponseHandler<RequestType, ResponseType> handler) {
        configuration.setResponseHandler(validator.notNullField(handler, "responseHandler"));
        return this;
    }

    @Override
    public <RequestType> HttpAsynchronousCommunicator exceptionHandler(HttpCommunicationExceptionHandler<RequestType> handler) {
        configuration.setExceptionHandler(validator.notNullField(handler, "exceptionHandler"));
        return this;
    }

    @Override
    public <RequestType> HttpAsynchronousCommunicator cancellationHandler(HttpCommunicationCancellationHandler<RequestType> handler) {
        configuration.setCancellationHandler(validator.notNullField(handler, "cancellationHandler"));
        return this;
    }

    @Override
    public <RequestType> void executeAsynchronous(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        executeAsynchronousHttpRequest(configuration);
    }

    @Override
    public HttpAsynchronousCommunicator asynchronous() {
        return this;
    }

    @Override
    public void executeAsynchronous() {
        validator.validate();
        executeAsynchronousHttpRequest(configuration);
    }
}