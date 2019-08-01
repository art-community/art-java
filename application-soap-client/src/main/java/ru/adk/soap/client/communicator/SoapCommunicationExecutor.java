package ru.adk.soap.client.communicator;

import lombok.NoArgsConstructor;
import ru.adk.http.client.communicator.HttpCommunicator;
import static lombok.AccessLevel.PACKAGE;
import static ru.adk.http.client.communicator.HttpCommunicator.httpCommunicator;
import static ru.adk.http.constants.HttpHeaders.ACCEPT;
import static ru.adk.http.constants.HttpHeaders.CONTENT_TYPE;
import static ru.adk.soap.client.communicator.SoapEntityMapping.soapRequestFromModel;
import static ru.adk.soap.client.communicator.SoapEntityMapping.soapResponseToModel;
import java.util.Optional;

@NoArgsConstructor(access = PACKAGE)
class SoapCommunicationExecutor {
    static <ResponseType> Optional<ResponseType> execute(SoapCommunicationConfiguration configuration) {
        HttpCommunicator httpCommunicator = httpCommunicator(configuration.getUrl());
        configuration.getRequestInterceptors().forEach(httpCommunicator::addRequestInterceptor);
        configuration.getResponseInterceptors().forEach(httpCommunicator::addResponseInterceptor);
        return httpCommunicator
                .client(configuration.getSyncHttpClient())
                .version(configuration.getHttpVersion())
                .requestCharset(configuration.getRequestCharset())
                .addHeader(ACCEPT, configuration.getConsumesMimeType().getMimeType().toString())
                .addHeader(CONTENT_TYPE, configuration.getProducesMimeType().getMimeType().toString())
                .responseMapper(soapResponseToModel(configuration))
                .config(configuration.getRequestConfig())
                .consumes(configuration.getConsumesMimeType().toHttpMimeToContentTypeMapper())
                .post()
                .requestEncoding(configuration.getRequestBodyEncoding())
                .produces(configuration.getProducesMimeType().toHttpMimeToContentTypeMapper())
                .requestMapper(soapRequestFromModel(configuration))
                .execute(configuration.getRequest());
    }

    static void executeAsync(SoapCommunicationConfiguration configuration) {
        HttpCommunicator httpCommunicator = httpCommunicator(configuration.getUrl());
        configuration.getRequestInterceptors().forEach(httpCommunicator::addRequestInterceptor);
        configuration.getRequestInterceptors().forEach(httpCommunicator::addResponseInterceptor);
        httpCommunicator
                .version(configuration.getHttpVersion())
                .requestCharset(configuration.getRequestCharset())
                .addHeader(ACCEPT, configuration.getConsumesMimeType().getMimeType().toString())
                .addHeader(CONTENT_TYPE, configuration.getConsumesMimeType().getMimeType().toString())
                .responseMapper(soapResponseToModel(configuration))
                .config(configuration.getRequestConfig())
                .consumes(configuration.getConsumesMimeType().toHttpMimeToContentTypeMapper())
                .post()
                .requestEncoding(configuration.getRequestBodyEncoding())
                .produces(configuration.getProducesMimeType().toHttpMimeToContentTypeMapper())
                .requestMapper(soapRequestFromModel(configuration))
                .asynchronous()
                .completionHandler(configuration.getResponseHandler())
                .exceptionHandler(configuration.getExceptionHandler())
                .cancellationHandler(configuration.getCancellationHandler())
                .client(configuration.getAsyncHttpClient())
                .executeAsynchronous(configuration.getRequest());
    }
}
