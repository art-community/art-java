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
import ru.art.core.constants.InterceptionStrategy;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.client.exception.HttpClientException;
import ru.art.http.client.handler.HttpCommunicationExceptionHandler;
import ru.art.http.client.handler.HttpCommunicationResponseHandler;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.mime.MimeType;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PACKAGE;
import static org.apache.http.client.methods.RequestBuilder.create;
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

@NoArgsConstructor(access = PACKAGE)
class HttpCommunicationExecutor {
    static <ResponseType> ResponseType executeSyncRequest(HttpCommunicationConfiguration configuration) {
        HttpUriRequest request = buildRequest(configuration);
        List<HttpClientInterceptor> requestInterceptors = configuration.getRequestInterceptors();
        if (isEmpty(requestInterceptors)) {
            requestInterceptors = httpClientModule().getRequestInterceptors();
        }
        for (HttpClientInterceptor requestInterceptor : requestInterceptors) {
            InterceptionStrategy strategy = requestInterceptor.interceptRequest(request);
            if (strategy == PROCESS_HANDLING) break;
            if (strategy == STOP_HANDLING) return null;
        }
        try {
            HttpClient client = getOrElse(configuration.getSyncClient(), httpClientModule().getClient());
            HttpResponse httpResponse = client.execute(request);
            List<HttpClientInterceptor> responseInterceptors = configuration.getResponseInterceptors();
            if (isEmpty(responseInterceptors)) {
                responseInterceptors = httpClientModule().getResponseInterceptors();
            }
            for (HttpClientInterceptor responseInterceptor : responseInterceptors) {
                InterceptionStrategy strategy = responseInterceptor.interceptResponse(request, httpResponse);
                if (strategy == PROCESS_HANDLING) break;
                if (strategy == STOP_HANDLING) return null;
            }
            return parseResponse(configuration, httpResponse);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    static void executeAsyncRequest(HttpCommunicationConfiguration configuration) {
        HttpUriRequest httpUriRequest = buildRequest(configuration);
        List<HttpClientInterceptor> requestInterceptors = configuration.getRequestInterceptors();
        if (isEmpty(requestInterceptors)) {
            requestInterceptors = httpClientModule().getRequestInterceptors();
        }
        for (HttpClientInterceptor requestInterceptor : requestInterceptors) {
            InterceptionStrategy strategy = requestInterceptor.interceptRequest(httpUriRequest);
            if (strategy == PROCESS_HANDLING) break;
            if (strategy == STOP_HANDLING) return;
        }
        HttpAsyncClient client = getOrElse(configuration.getAsyncClient(), httpClientModule().getAsyncClient());
        client.execute(httpUriRequest, new HttpAsyncClientCallback(configuration.getRequest(), httpUriRequest, configuration));
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
        MimeToContentTypeMapper producesContentType;
        MimeType producesMimeType;
        if (isNull(requestMapper) || isNull(producesContentType = configuration.getProducesContentType()) || isNull(producesMimeType = producesContentType.getMimeType())) {
            return requestBuilder.build();
        }
        Value requestValue = requestMapper.map(configuration.getRequest());
        HttpContentMapper contentMapper = httpClientModule()
                .getContentMappers()
                .get(producesMimeType);
        if (isNull(contentMapper)) {
            throw new HttpClientException(format(REQUEST_CONTENT_TYPE_NOT_SUPPORTED, producesMimeType.toString()));
        }
        byte[] payload = contentMapper.getToContent().mapToBytes(requestValue, producesMimeType, configuration.getRequestContentCharset());
        EntityBuilder entityBuilder = EntityBuilder.create().setBinary(payload)
                .setContentType(producesContentType.getContentType())
                .setContentEncoding(configuration.getRequestContentEncoding());
        if (configuration.isGzipCompressedBody()) entityBuilder.gzipCompress();
        if (configuration.isChunkedBody()) entityBuilder.chunked();
        return requestBuilder.setEntity(entityBuilder.build()).build();
    }

    private static <ResponseType> ResponseType parseResponse(HttpCommunicationConfiguration configuration, HttpResponse httpResponse) {
        byte[] bytes = readResponseBody(httpResponse.getEntity());
        if (isEmpty(bytes)) return null;
        MimeToContentTypeMapper consumesContentType = configuration.getConsumesContentType();
        MimeType consumesMimeType;
        if (isNull(consumesContentType) || isNull(consumesMimeType = consumesContentType.getMimeType())) return null;
        Header contentType = httpResponse.getEntity().getContentType();
        MimeType responseContentType = isNull(contentType) || configuration.isIgnoreResponseContentType() ? consumesMimeType : MimeType.valueOf(contentType.getValue());
        HttpContentMapper contentMapper = httpClientModule()
                .getContentMappers()
                .get(responseContentType);
        if (isNull(contentMapper)) {
            throw new HttpClientException(format(RESPONSE_CONTENT_TYPE_NOT_SUPPORTED, responseContentType.toString()));
        }
        Value responseBodyValue = contentMapper.getFromContent().mapFromBytes(bytes, responseContentType, configuration.getRequestContentCharset());
        ValueToModelMapper<Object, ? extends Value> responseMapper = cast(configuration.getResponseMapper());
        if (isNull(responseBodyValue) || isNull(responseMapper)) return null;
        return cast(responseMapper.map(cast(responseBodyValue)));
    }

    @AllArgsConstructor(access = PACKAGE)
    static class HttpAsyncClientCallback implements FutureCallback<HttpResponse> {
        private final Object request;
        private final HttpUriRequest httpUriRequest;
        private final HttpCommunicationConfiguration configuration;

        @Override
        public void completed(HttpResponse result) {
            List<HttpClientInterceptor> responseInterceptors = configuration.getResponseInterceptors();
            if (isEmpty(responseInterceptors)) {
                responseInterceptors = httpClientModule().getResponseInterceptors();
            }
            for (HttpClientInterceptor responseInterceptor : responseInterceptors) {
                InterceptionStrategy strategy = responseInterceptor.interceptResponse(httpUriRequest, result);
                if (strategy == PROCESS_HANDLING) break;
                if (strategy == STOP_HANDLING) return;
            }
            try {
                HttpCommunicationResponseHandler<?, ?> responseHandler = configuration.getResponseHandler();
                if (nonNull(responseHandler)) {
                    responseHandler.completed(ofNullable(cast(request)), ofNullable(cast(parseResponse(configuration, result))));
                }
            } catch (Exception e) {
                HttpCommunicationExceptionHandler<?> exceptionHandler = configuration.getExceptionHandler();
                if (nonNull(exceptionHandler)) {
                    exceptionHandler.failed(ofNullable(cast(request)), e);
                }
            }
        }

        @Override
        public void failed(Exception exception) {
            configuration.getExceptionHandler().failed(ofNullable(cast(request)), exception);
        }

        @Override
        public void cancelled() {
            configuration.getCancellationHandler().cancelled(ofNullable(cast(request)));
        }
    }
}
