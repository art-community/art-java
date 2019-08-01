package ru.adk.http.client.communicator;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.adk.entity.Value;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.http.client.handler.HttpCommunicationCancellationHandler;
import ru.adk.http.client.handler.HttpCommunicationExceptionHandler;
import ru.adk.http.client.handler.HttpCommunicationResponseHandler;
import ru.adk.http.client.interceptor.HttpClientInterceptor;
import ru.adk.http.constants.HttpMethodType;
import ru.adk.http.constants.MimeToContentTypeMapper;
import static lombok.AccessLevel.PACKAGE;
import static ru.adk.core.context.Context.contextConfiguration;
import static ru.adk.core.factory.CollectionsFactory.linkedListOf;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.http.client.module.HttpClientModule.httpClientModule;
import static ru.adk.http.constants.HttpMethodType.GET;
import static ru.adk.http.constants.MimeToContentTypeMapper.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Getter(value = PACKAGE)
@Setter(value = PACKAGE)
class HttpCommunicationConfiguration {
    private final List<String> pathParameters = linkedListOf();
    private final Map<String, String> queryParameters = mapOf();
    private final List<HttpClientInterceptor> requestInterceptors = linkedListOf();
    private final List<HttpClientInterceptor> responseInterceptors = linkedListOf();
    private final Map<String, String> headers = mapOf();
    private String url;
    private HttpMethodType methodType = GET;
    private ValueFromModelMapper<?, ? extends Value> requestMapper;
    private ValueToModelMapper<?, ? extends Value> responseMapper;
    private HttpCommunicationResponseHandler<?, ?> responseHandler;
    private HttpCommunicationExceptionHandler<?> exceptionHandler;
    private HttpCommunicationCancellationHandler<?> cancellationHandler;
    private Object request;
    private boolean chunkedBody;
    private boolean gzipCompressedBody;
    private RequestConfig requestConfig = httpClientModule().getRequestConfig();
    private HttpVersion httpProtocolVersion = httpClientModule().getHttpVersion();
    private MimeToContentTypeMapper producesContentType = all();
    private MimeToContentTypeMapper consumesContentType = all();
    private Charset requestContentCharset = contextConfiguration().getCharset();
    private String requestContentEncoding = contextConfiguration().getCharset().name();
    private boolean ignoreResponseContentType;
    private HttpClient syncClient;
    private HttpAsyncClient asyncClient;
}
