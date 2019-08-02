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

package ru.art.soap.client.communicator;

import lombok.NoArgsConstructor;
import ru.art.http.client.communicator.HttpCommunicator;
import static lombok.AccessLevel.PACKAGE;
import static ru.art.http.client.communicator.HttpCommunicator.httpCommunicator;
import static ru.art.http.constants.HttpHeaders.ACCEPT;
import static ru.art.http.constants.HttpHeaders.CONTENT_TYPE;
import static ru.art.soap.client.communicator.SoapEntityMapping.soapRequestFromModel;
import static ru.art.soap.client.communicator.SoapEntityMapping.soapResponseToModel;
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
