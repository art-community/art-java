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

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.art.core.validator.BuilderValidator;
import ru.art.entity.XmlEntity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.client.handler.HttpCommunicationCancellationHandler;
import ru.art.http.client.handler.HttpCommunicationExceptionHandler;
import ru.art.http.client.handler.HttpCommunicationResponseHandler;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.http.client.model.HttpCommunicationTargetConfiguration;
import ru.art.soap.client.communicator.SoapCommunicator.SoapAsynchronousCommunicator;
import ru.art.soap.content.mapper.SoapMimeToContentTypeMapper;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.constants.StringConstants.SCHEME_DELIMITER;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import static ru.art.soap.client.module.SoapClientModule.soapClientModule;
import java.nio.charset.Charset;
import java.util.Optional;


public class SoapCommunicatorImplementation implements SoapCommunicator, SoapAsynchronousCommunicator {
    private final BuilderValidator validator = new BuilderValidator(SoapCommunicator.class.getName());
    private final SoapCommunicationConfiguration configuration = new SoapCommunicationConfiguration();

    SoapCommunicatorImplementation(String url) {
        this(HttpCommunicationTargetConfiguration.builder().build().url(url));
    }

    SoapCommunicatorImplementation(HttpCommunicationTargetConfiguration targetConfiguration) {
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
    public SoapCommunicator client(HttpClient syncClient) {
        configuration.setHttpClient(validator.notEmptyField(syncClient, "syncClient"));
        return this;
    }

    @Override
    public SoapCommunicator operationId(String operationId) {
        configuration.setOperationId(validator.notEmptyField(operationId, "operationId"));
        return this;
    }

    @Override
    public SoapCommunicator requestConfig(RequestConfig requestConfig) {
        RequestConfig config = getOrElse(requestConfig, httpClientModule().getRequestConfig());
        configuration.setRequestConfig(validator.notNullField(config, "requestConfig"));
        return this;
    }

    @Override
    public <RequestType> SoapCommunicator requestMapper(ValueFromModelMapper<RequestType, XmlEntity> requestMapper) {
        configuration.setRequestMapper(validator.notNullField(requestMapper, "requestMapper"));
        return this;
    }

    @Override
    public SoapCommunicator responseMapper(ValueToModelMapper<?, XmlEntity> responseMapper) {
        configuration.setResponseMapper(validator.notNullField(responseMapper, "responseMapper"));
        return this;
    }

    @Override
    public SoapCommunicator envelopeNamespace(String prefix, String namespace) {
        String envelopePrefix = getOrElse(prefix, soapClientModule().getEnvelopePrefix());
        String envelopeNamespace = getOrElse(namespace, soapClientModule().getEnvelopeNamespace());
        configuration.setEnvelopePrefix(validator.notEmptyField(envelopePrefix, "envelopePrefix"));
        configuration.setEnvelopeNamespace(validator.notEmptyField(envelopeNamespace, "envelopeNamespace"));
        return this;
    }

    @Override
    public SoapCommunicator bodyNamespace(String prefix, String namespace) {
        String bodyPrefix = getOrElse(prefix, soapClientModule().getBodyPrefix());
        String bodyNamespace = getOrElse(namespace, soapClientModule().getBodyNamespace());
        configuration.setBodyPrefix(validator.notEmptyField(bodyPrefix, "bodyPrefix"));
        configuration.setBodyNamespace(validator.notEmptyField(bodyNamespace, "bodyNamespace"));
        return this;
    }

    @Override
    public SoapCommunicator operationNamespace(String prefix, String namespace) {
        configuration.setOperationPrefix(validator.notEmptyField(prefix, "operationPrefix"));
        configuration.setOperationNamespace(validator.notEmptyField(namespace, "operationNamespace"));
        return this;
    }

    @Override
    public SoapCommunicator consumes(SoapMimeToContentTypeMapper soapMimeType) {
        configuration.setConsumesMimeType(validator.notNullField(soapMimeType, "consumesMimeType"));
        return this;
    }

    @Override
    public SoapCommunicator produces(SoapMimeToContentTypeMapper soapMimeType) {
        configuration.setProducesMimeType(validator.notNullField(soapMimeType, "producesMimeType"));
        return this;
    }

    @Override
    public SoapCommunicator requestCharset(Charset charset) {
        Charset requestCharset = getOrElse(charset, contextConfiguration().getCharset());
        configuration.setRequestCharset(validator.notNullField(requestCharset, "requestCharset"));
        return this;
    }

    @Override
    public SoapCommunicator requestBodyEncoding(String encoding) {
        configuration.setRequestBodyEncoding(emptyIfNull(encoding));
        return this;
    }

    @Override
    public SoapCommunicator withRequestInterceptor(HttpClientInterceptor interceptor) {
        configuration.getRequestInterceptors().add(validator.notNullField(interceptor, "requestInterceptor"));
        return this;
    }

    @Override
    public SoapCommunicator withResponseInterceptor(HttpClientInterceptor interceptor) {
        configuration.getResponseInterceptors().add(validator.notNullField(interceptor, "responseInterceptor"));
        return this;
    }

    @Override
    public SoapCommunicator version(HttpVersion httpVersion) {
        HttpVersion httpProtocolVersion = getOrElse(httpVersion, httpClientModule().getHttpVersion());
        configuration.setHttpVersion(validator.notEmptyField(httpProtocolVersion, "httpVersion"));
        return this;
    }

    @Override
    public <RequestType, ResponseType> Optional<ResponseType> execute(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        configuration.validateRequiredFields();
        return SoapCommunicationExecutor.execute(configuration);
    }

    @Override
    public SoapAsynchronousCommunicator client(HttpAsyncClient asyncClient) {
        configuration.setAsynchronousHttpClient(validator.notNullField(asyncClient, "asynchronousClient"));
        return this;
    }

    @Override
    public <RequestType, ResponseType> SoapAsynchronousCommunicator responseHandler(HttpCommunicationResponseHandler<RequestType, ResponseType> httpCommunicationResponseHandler) {
        configuration.setResponseHandler(validator.notNullField(httpCommunicationResponseHandler, "responseHandler"));
        return this;
    }

    @Override
    public <RequestType> SoapAsynchronousCommunicator exceptionHandler(HttpCommunicationExceptionHandler<RequestType> httpCommunicationExceptionHandler) {
        configuration.setExceptionHandler(validator.notNullField(httpCommunicationExceptionHandler, "exceptionHandler"));
        return this;
    }

    @Override
    public <RequestType> SoapAsynchronousCommunicator cancellationHandler(HttpCommunicationCancellationHandler<RequestType> httpAsyncClientCancellationHandler) {
        configuration.setCancellationHandler(validator.notNullField(httpAsyncClientCancellationHandler, "cancellationHandler"));
        return this;
    }

    @Override
    public SoapAsynchronousCommunicator asynchronous() {
        return this;
    }

    @Override
    public <RequestType> void executeAsynchronous(RequestType request) {
        configuration.setRequest(validator.notNullField(request, "request"));
        validator.validate();
        configuration.validateRequiredFields();
        SoapCommunicationExecutor.executeAsynchronous(configuration);
    }
}