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

package ru.art.soap.client.communicator;

import lombok.NoArgsConstructor;
import ru.art.entity.mapper.ValueFromModelMapper.XmlEntityFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper.XmlEntityToModelMapper;
import ru.art.http.client.communicator.HttpCommunicator;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PACKAGE;
import static ru.art.http.client.communicator.HttpCommunicator.httpCommunicator;
import static ru.art.http.constants.HttpHeaders.ACCEPT;
import static ru.art.http.constants.HttpHeaders.CONTENT_TYPE;
import static ru.art.soap.client.communicator.SoapEntityMapping.soapRequestFromModel;
import static ru.art.soap.client.communicator.SoapEntityMapping.soapResponseToModel;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor(access = PACKAGE)
class SoapCommunicationExecutor {
    static <ResponseType> Optional<ResponseType> execute(SoapCommunicationConfiguration configuration) {
        HttpCommunicator httpCommunicator = httpCommunicator(configuration.getUrl());
        configuration.getRequestInterceptors().forEach(httpCommunicator::addRequestInterceptor);
        configuration.getResponseInterceptors().forEach(httpCommunicator::addResponseInterceptor);
        httpCommunicator = httpCommunicator
                .version(configuration.getHttpVersion())
                .requestCharset(configuration.getRequestCharset())
                .addHeader(ACCEPT, configuration.getConsumesMimeType().getMimeType().toString())
                .addHeader(CONTENT_TYPE, configuration.getConsumesMimeType().getMimeType().toString());
        XmlEntityToModelMapper<?> responseMapper = soapResponseToModel(configuration);
        if (nonNull(responseMapper)) {
            httpCommunicator = httpCommunicator.responseMapper(responseMapper);
        }
        XmlEntityFromModelMapper<?> requestMapper = soapRequestFromModel(configuration);
        if (nonNull(requestMapper)) {
            httpCommunicator = httpCommunicator.requestMapper(requestMapper);
        }
        return httpCommunicator.config(configuration.getRequestConfig())
                .consumes(configuration.getConsumesMimeType().toHttpMimeToContentTypeMapper())
                .post()
                .requestEncoding(configuration.getRequestBodyEncoding())
                .produces(configuration.getProducesMimeType().toHttpMimeToContentTypeMapper())
                .execute(configuration.getRequest());
    }

    static <ResponseType> CompletableFuture<Optional<ResponseType>> executeAsynchronous(SoapCommunicationConfiguration configuration) {
        HttpCommunicator httpCommunicator = httpCommunicator(configuration.getUrl());
        configuration.getRequestInterceptors().forEach(httpCommunicator::addRequestInterceptor);
        configuration.getRequestInterceptors().forEach(httpCommunicator::addResponseInterceptor);
        httpCommunicator = httpCommunicator
                .version(configuration.getHttpVersion())
                .requestCharset(configuration.getRequestCharset())
                .addHeader(ACCEPT, configuration.getConsumesMimeType().getMimeType().toString())
                .addHeader(CONTENT_TYPE, configuration.getConsumesMimeType().getMimeType().toString());
        XmlEntityToModelMapper<?> responseMapper = soapResponseToModel(configuration);
        if (nonNull(responseMapper)) {
            httpCommunicator = httpCommunicator.responseMapper(responseMapper);
        }
        XmlEntityFromModelMapper<?> requestMapper = soapRequestFromModel(configuration);
        if (nonNull(requestMapper)) {
            httpCommunicator = httpCommunicator.requestMapper(requestMapper);
        }
        return httpCommunicator.config(configuration.getRequestConfig())
                .consumes(configuration.getConsumesMimeType().toHttpMimeToContentTypeMapper())
                .post()
                .requestEncoding(configuration.getRequestBodyEncoding())
                .produces(configuration.getProducesMimeType().toHttpMimeToContentTypeMapper())
                .asynchronous()
                .completionHandler(configuration.getResponseHandler())
                .exceptionHandler(configuration.getExceptionHandler())
                .cancellationHandler(configuration.getCancellationHandler())
                .client(configuration.getAsynchronousHttpClient())
                .executeAsynchronous(configuration.getRequest());
    }
}