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

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.client.handler.HttpCommunicationCancellationHandler;
import ru.art.http.client.handler.HttpCommunicationExceptionHandler;
import ru.art.http.client.handler.HttpCommunicationResponseHandler;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.http.constants.HttpMethodType;
import ru.art.http.constants.MimeToContentTypeMapper;
import static lombok.AccessLevel.PACKAGE;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import static ru.art.http.constants.HttpMethodType.GET;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Getter(value = PACKAGE)
@Setter(value = PACKAGE)
class HttpCommunicationConfiguration {
    private List<String> pathParameters = linkedListOf();
    private Map<String, String> queryParameters = mapOf();
    private List<HttpClientInterceptor> requestInterceptors = httpClientModule().getRequestInterceptors();
    private List<HttpClientInterceptor> responseInterceptors = httpClientModule().getResponseInterceptors();
    private Map<String, String> headers = mapOf();
    private String url;
    private HttpMethodType methodType = GET;
    private ValueFromModelMapper<?, ? extends Value> requestMapper;
    private ValueToModelMapper<?, ? extends Value> responseMapper;
    private HttpCommunicationResponseHandler<?, ?> completionHandler;
    private HttpCommunicationExceptionHandler<?> exceptionHandler;
    private HttpCommunicationCancellationHandler<?> cancellationHandler;
    private Object request;
    private boolean chunkedBody;
    private boolean gzipCompressedBody;
    private RequestConfig requestConfig = httpClientModule().getRequestConfig();
    private HttpVersion httpProtocolVersion = httpClientModule().getHttpVersion();
    private MimeToContentTypeMapper producesMimeType = httpClientModule().getProducesMimeTypeMapper();
    private MimeToContentTypeMapper consumesMimeType = httpClientModule().getConsumesMimeTypeMapper();
    private Charset requestContentCharset = contextConfiguration().getCharset();
    private String requestContentEncoding = contextConfiguration().getCharset().name();
    private boolean ignoreResponseContentType;
    private HttpClient synchronousClient;
    private HttpAsyncClient asynchronousClient;
    private Executor asynchronousFuturesExecutor = httpClientModule().getAsynchronousFuturesExecutor();
    private List<ValueInterceptor<Value, Value>> requestValueInterceptors = httpClientModule().getRequestValueInterceptors();
    private List<ValueInterceptor<Value, Value>> responseValueInterceptors = httpClientModule().getResponseValueInterceptors();
}
