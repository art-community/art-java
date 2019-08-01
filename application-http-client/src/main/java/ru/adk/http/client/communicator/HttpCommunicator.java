package ru.adk.http.client.communicator;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.http.client.handler.HttpCommunicationCancellationHandler;
import ru.adk.http.client.handler.HttpCommunicationExceptionHandler;
import ru.adk.http.client.handler.HttpCommunicationResponseHandler;
import ru.adk.http.client.interceptor.HttpClientInterceptor;
import ru.adk.http.client.model.HttpCommunicationTargetConfiguration;
import ru.adk.http.constants.MimeToContentTypeMapper;
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

    HttpCommunicator responseMapper(ValueToModelMapper responseMapper);

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

    HttpCommunicator requestMapper(ValueFromModelMapper requestMapper);

    HttpCommunicator chunked();

    HttpCommunicator gzipCompressed();

    HttpCommunicator requestEncoding(String encoding);

    <RequestType, ResponseType> Optional<ResponseType> execute(RequestType request);

    <ResponseType> Optional<ResponseType> execute();

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
