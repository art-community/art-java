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

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.zalando.logbook.httpclient.LogbookHttpAsyncResponseConsumer;
import ru.art.core.constants.InterceptionStrategy;
import ru.art.core.mime.MimeType;
import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptionResult;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.client.exception.HttpClientException;
import ru.art.http.client.handler.HttpCommunicationCancellationHandler;
import ru.art.http.client.handler.HttpCommunicationExceptionHandler;
import ru.art.http.client.handler.HttpCommunicationResponseHandler;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.mapper.HttpContentMapper;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static lombok.AccessLevel.PACKAGE;
import static org.apache.http.client.methods.RequestBuilder.create;
import static org.apache.http.nio.client.methods.HttpAsyncMethods.createConsumer;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.InterceptionStrategy.PROCESS_HANDLING;
import static ru.art.core.constants.InterceptionStrategy.STOP_HANDLING;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.http.client.body.descriptor.HttpBodyDescriptor.readResponseBody;
import static ru.art.http.client.builder.HttpUriBuilder.buildUri;
import static ru.art.http.client.constants.HttpClientExceptionMessages.REQUEST_CONTENT_TYPE_NOT_SUPPORTED;
import static ru.art.http.client.constants.HttpClientExceptionMessages.RESPONSE_CONTENT_TYPE_NOT_SUPPORTED;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = PACKAGE)
class HttpCommunicationExecutor {
    static <ResponseType> ResponseType executeHttpRequest(HttpCommunicationConfiguration configuration) {
        HttpUriRequest request = buildRequest(configuration);
        if (isNull(request)) {
            return null;
        }
        List<HttpClientInterceptor> requestInterceptors = configuration.getRequestInterceptors();
        for (HttpClientInterceptor requestInterceptor : requestInterceptors) {
            InterceptionStrategy strategy = requestInterceptor.interceptRequest(request);
            if (strategy == PROCESS_HANDLING) break;
            if (strategy == STOP_HANDLING) return null;
        }
        try {
            HttpClient client = getOrElse(configuration.getSynchronousClient(), httpClientModule().getClient());
            HttpResponse httpResponse = client.execute(request);
            List<HttpClientInterceptor> responseInterceptors = configuration.getResponseInterceptors();
            for (HttpClientInterceptor responseInterceptor : responseInterceptors) {
                InterceptionStrategy strategy = responseInterceptor.interceptResponse(request, httpResponse);
                if (strategy == PROCESS_HANDLING) break;
                if (strategy == STOP_HANDLING) return null;
            }
            return parseResponse(configuration, httpResponse);
        } catch (Throwable e) {
            throw new HttpClientException(e);
        }
    }

    static <ResponseType> CompletableFuture<Optional<ResponseType>> executeAsynchronousHttpRequest(HttpCommunicationConfiguration configuration) {
        HttpUriRequest httpUriRequest = buildRequest(configuration);
        if (isNull(httpUriRequest)) {
            return completedFuture(empty());
        }
        List<HttpClientInterceptor> requestInterceptors = configuration.getRequestInterceptors();
        for (HttpClientInterceptor requestInterceptor : requestInterceptors) {
            InterceptionStrategy strategy = requestInterceptor.interceptRequest(httpUriRequest);
            if (strategy == PROCESS_HANDLING) break;
            if (strategy == STOP_HANDLING) return completedFuture(empty());
        }
        HttpAsyncClient client = getOrElse(configuration.getAsynchronousClient(), httpClientModule().getAsynchronousClient());
        HttpAsynchronousClientCallback callback = new HttpAsynchronousClientCallback(configuration.getRequest(), httpUriRequest, configuration);

        return supplyAsync(() -> executeHttpUriRequest(httpUriRequest, client, callback), configuration.getAsynchronousFuturesExecutor())
                .thenApply(response -> ofNullable(parseResponse(configuration, response)));
    }

    private static HttpResponse executeHttpUriRequest(HttpUriRequest httpUriRequest, HttpAsyncClient client, HttpAsynchronousClientCallback callback) {
        try {
            if (httpClientModule().isEnableRawDataTracing()) {
                LogbookHttpAsyncResponseConsumer<HttpResponse> logbookConsumer = new LogbookHttpAsyncResponseConsumer<>(createConsumer());
                return client.execute(HttpAsyncMethods.create(httpUriRequest), logbookConsumer, callback).get();
            }
            return client.execute(httpUriRequest, callback).get();
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    private static HttpUriRequest buildRequest(HttpCommunicationConfiguration configuration) {
        RequestBuilder requestBuilder = create(configuration.getMethodType().name())
                .setUri(buildUri(configuration.getUrl(), configuration.getPathParameters(), configuration.getQueryParameters()))
                .setConfig(configuration.getRequestConfig())
                .setCharset(configuration.getRequestContentCharset())
                .setVersion(configuration.getHttpProtocolVersion());
        configuration.getHeaders().forEach(requestBuilder::addHeader);
        if (isNull(configuration.getRequest())) {
            return requestBuilder.build();
        }
        ValueFromModelMapper<Object, ? extends Value> requestMapper = cast(configuration.getRequestMapper());
        MimeToContentTypeMapper consumesMimeTypeMapper;
        MimeType producesMimeType;
        if (isNull(requestMapper)
                || isNull(consumesMimeTypeMapper = configuration.getProducesMimeType())
                || isNull(producesMimeType = consumesMimeTypeMapper.getMimeType())) {
            return requestBuilder.build();
        }
        Value requestValue = requestMapper.map(configuration.getRequest());
        List<ValueInterceptor<Value, Value>> requestValueInterceptors = configuration.getRequestValueInterceptors();
        for (ValueInterceptor<Value, Value> requestValueInterceptor : requestValueInterceptors) {
            ValueInterceptionResult<Value, Value> result = requestValueInterceptor.intercept(requestValue);
            if (isNull(result)) {
                break;
            }
            requestValue = result.getOutValue();
            if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                break;
            }
            if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                return null;
            }
        }

        HttpContentMapper contentMapper = httpClientModule()
                .getContentMappers()
                .get(producesMimeType);
        if (isNull(contentMapper)) {
            throw new HttpClientException(format(REQUEST_CONTENT_TYPE_NOT_SUPPORTED, producesMimeType.toString()));
        }
        byte[] payload = contentMapper.getToContent().mapToBytes(requestValue, producesMimeType, configuration.getRequestContentCharset());
        if (isEmpty(payload)) {
            return requestBuilder.build();
        }
        EntityBuilder entityBuilder = EntityBuilder.create().setBinary(payload)
                .setContentType(consumesMimeTypeMapper.getContentType())
                .setContentEncoding(configuration.getRequestContentEncoding());
        if (configuration.isGzipCompressedBody()) entityBuilder.gzipCompress();
        if (configuration.isChunkedBody()) entityBuilder.chunked();
        return requestBuilder.setEntity(entityBuilder.build()).build();
    }

    private static <ResponseType> ResponseType parseResponse(HttpCommunicationConfiguration configuration, HttpResponse httpResponse) {
        byte[] bytes = readResponseBody(httpResponse.getEntity());
        if (isEmpty(bytes)) return null;
        MimeToContentTypeMapper consumesMimeTypeMapper = configuration.getConsumesMimeType();
        MimeType consumesMimeType;
        if (isNull(consumesMimeTypeMapper) || isNull(consumesMimeType = consumesMimeTypeMapper.getMimeType()))
            return null;
        Header contentType = httpResponse.getEntity().getContentType();
        MimeType responseContentType = isNull(contentType) || configuration.isIgnoreResponseContentType()
                ? consumesMimeType
                : MimeType.valueOf(contentType.getValue());
        HttpContentMapper contentMapper = httpClientModule()
                .getContentMappers()
                .get(responseContentType);
        if (isNull(contentMapper)) {
            throw new HttpClientException(format(RESPONSE_CONTENT_TYPE_NOT_SUPPORTED, responseContentType.toString()));
        }
        Value responseValue = contentMapper.getFromContent().mapFromBytes(bytes, responseContentType, configuration.getRequestContentCharset());
        List<ValueInterceptor<Value, Value>> responseValueInterceptors = configuration.getResponseValueInterceptors();
        for (ValueInterceptor<Value, Value> responseValueInterceptor : responseValueInterceptors) {
            ValueInterceptionResult<Value, Value> result = responseValueInterceptor.intercept(responseValue);
            if (isNull(result)) {
                break;
            }
            responseValue = result.getOutValue();
            if (result.getNextInterceptionStrategy() == PROCESS_HANDLING) {
                break;
            }
            if (result.getNextInterceptionStrategy() == STOP_HANDLING) {
                return null;
            }
        }
        ValueToModelMapper<Object, ? extends Value> responseMapper = cast(configuration.getResponseMapper());
        if (isNull(responseValue) || isNull(responseMapper)) return null;
        return cast(responseMapper.map(cast(responseValue)));
    }

    @AllArgsConstructor(access = PACKAGE)
    static class HttpAsynchronousClientCallback implements FutureCallback<HttpResponse> {
        private final Object request;
        private final HttpUriRequest httpUriRequest;
        private final HttpCommunicationConfiguration configuration;

        @Override
        public void completed(HttpResponse result) {
            List<HttpClientInterceptor> responseInterceptors = configuration.getResponseInterceptors();
            for (HttpClientInterceptor responseInterceptor : responseInterceptors) {
                InterceptionStrategy strategy = responseInterceptor.interceptResponse(httpUriRequest, result);
                if (strategy == PROCESS_HANDLING) break;
                if (strategy == STOP_HANDLING) return;
            }
            try {
                HttpCommunicationResponseHandler<?, ?> completionHandler = configuration.getCompletionHandler();
                if (nonNull(completionHandler)) {
                    completionHandler.completed(ofNullable(cast(request)), ofNullable(cast(parseResponse(configuration, result))));
                }
            } catch (Throwable e) {
                HttpCommunicationExceptionHandler<?> exceptionHandler = configuration.getExceptionHandler();
                if (nonNull(exceptionHandler)) {
                    exceptionHandler.failed(ofNullable(cast(request)), e);
                }
            }
        }

        @Override
        public void failed(Exception exception) {
            HttpCommunicationExceptionHandler<?> exceptionHandler = configuration.getExceptionHandler();
            if (nonNull(exceptionHandler)) {
                exceptionHandler.failed(ofNullable(cast(request)), exception);
            }
        }

        @Override
        public void cancelled() {
            HttpCommunicationCancellationHandler<?> cancellationHandler = configuration.getCancellationHandler();
            if (nonNull(cancellationHandler)) {
                cancellationHandler.cancelled(ofNullable(cast(request)));
            }
        }
    }
}
