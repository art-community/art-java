/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.http.client.communicator;

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
import ru.art.http.client.model.HttpCommunicationTargetConfiguration;
import ru.art.http.constants.MimeToContentTypeMapper;
import java.nio.charset.Charset;
import java.util.Optional;

public interface HttpCommunicator {
    static HttpCommunicator httpCommunicator(String url) {
        return new HttpCommunicatorImplementation(url);
    }

    static HttpCommunicator httpCommunicator(HttpCommunicationTargetConfiguration targetConfiguration) {
        return new HttpCommunicatorImplementation(targetConfiguration);
    }

    HttpCommunicator client(HttpClient client);

    HttpCommunicator consumes(MimeToContentTypeMapper responseContentTypeMapper);

    HttpCommunicator ignoreResponseContentType();

    <ResponseType> HttpCommunicator responseMapper(ValueToModelMapper<ResponseType, ? extends Value> responseMapper);

    HttpCommunicator config(RequestConfig requestConfig);

    HttpCommunicator version(HttpVersion httpVersion);

    HttpCommunicator requestCharset(Charset charset);

    HttpCommunicator addPathParameter(String parameter);

    HttpCommunicator addRequestInterceptor(HttpClientInterceptor interceptor);

    HttpCommunicator addResponseInterceptor(HttpClientInterceptor interceptor);

    HttpCommunicator addQueryParameter(String name, String value);

    HttpCommunicator addHeader(String name, String value);

    HttpCommunicator get();

    HttpCommunicator post();

    HttpCommunicator put();

    HttpCommunicator patch();

    HttpCommunicator options();

    HttpCommunicator delete();

    HttpCommunicator trace();

    HttpCommunicator head();

    HttpCommunicator produces(MimeToContentTypeMapper requestContentTypeMapper);

    <RequestType> HttpCommunicator requestMapper(ValueFromModelMapper<RequestType, ? extends Value> requestMapper);

    HttpCommunicator chunked();

    HttpCommunicator gzipCompressed();

    HttpCommunicator requestEncoding(String encoding);

    <RequestType, ResponseType> Optional<ResponseType> execute(RequestType request);

    <ResponseType> Optional<ResponseType> execute();

    HttpCommunicator addRequestValueInterceptor(ValueInterceptor interceptor);

    HttpCommunicator addResponseValueInterceptor(ValueInterceptor interceptor);

    HttpAsynchronousCommunicator asynchronous();

    interface HttpAsynchronousCommunicator {
        HttpAsynchronousCommunicator client(HttpAsyncClient client);

        <RequestType, ResponseType> HttpAsynchronousCommunicator completionHandler(HttpCommunicationResponseHandler<RequestType, ResponseType> handler);

        <RequestType> HttpAsynchronousCommunicator exceptionHandler(HttpCommunicationExceptionHandler<RequestType> handler);

        <RequestType> HttpAsynchronousCommunicator cancellationHandler(HttpCommunicationCancellationHandler<RequestType> handler);

        <RequestType> void executeAsynchronous(RequestType request);

        void executeAsynchronous();
    }
}
