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

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import ru.art.entity.XmlEntity;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.http.client.handler.HttpCommunicationCancellationHandler;
import ru.art.http.client.handler.HttpCommunicationExceptionHandler;
import ru.art.http.client.handler.HttpCommunicationResponseHandler;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.soap.client.exception.SoapClientModuleException;
import ru.art.soap.content.mapper.SoapMimeToContentTypeMapper;
import static lombok.AccessLevel.PACKAGE;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import static ru.art.soap.client.constants.SoapClientModuleExceptionMessages.INVALID_SOAP_COMMUNICATION_CONFIGURATION;
import static ru.art.soap.client.module.SoapClientModule.soapClientModule;
import static ru.art.soap.content.mapper.SoapMimeToContentTypeMapper.textXml;
import java.nio.charset.Charset;
import java.util.List;

@Getter
@Setter(value = PACKAGE)
class SoapCommunicationConfiguration {
    private HttpClient httpClient;
    private HttpAsyncClient asynchronousHttpClient;
    private String url;
    private String operationId;
    private RequestConfig requestConfig = httpClientModule().getRequestConfig();
    private ValueFromModelMapper<?, XmlEntity> requestMapper;
    private ValueToModelMapper<?, XmlEntity> responseMapper;
    private String operationPrefix;
    private String operationNamespace;
    private String bodyPrefix = soapClientModule().getBodyPrefix();
    private String bodyNamespace = soapClientModule().getBodyNamespace();
    private String envelopePrefix = soapClientModule().getEnvelopePrefix();
    private String envelopeNamespace = soapClientModule().getEnvelopeNamespace();
    private SoapMimeToContentTypeMapper consumesMimeType = textXml();
    private SoapMimeToContentTypeMapper producesMimeType = textXml();
    private Charset requestCharset;
    private String requestBodyEncoding;
    private List<HttpClientInterceptor> requestInterceptors = linkedListOf();
    private List<HttpClientInterceptor> responseInterceptors = linkedListOf();
    private HttpVersion httpVersion;
    private HttpCommunicationResponseHandler<?, ?> responseHandler;
    private HttpCommunicationExceptionHandler<?> exceptionHandler;
    private HttpCommunicationCancellationHandler<?> cancellationHandler;
    private List<ValueInterceptor<XmlEntity, XmlEntity>> requestValueInterceptors = linkedListOf();
    private List<ValueInterceptor<XmlEntity, XmlEntity>> responseValueInterceptors = linkedListOf();
    private Object request;

    void validateRequiredFields() {
        boolean urlIsEmpty = isEmpty(url);
        boolean operationIdIsEmpty = isEmpty(operationId);
        boolean operationNamespaceIsEmpty = isEmpty(operationNamespace);
        if (!urlIsEmpty && !operationIdIsEmpty && !operationNamespaceIsEmpty) {
            return;
        }
        if (urlIsEmpty && operationIdIsEmpty && operationNamespaceIsEmpty) {
            throw new SoapClientModuleException(INVALID_SOAP_COMMUNICATION_CONFIGURATION + "url, operationId, operationNamespace");
        }
        if (urlIsEmpty && operationIdIsEmpty) {
            throw new SoapClientModuleException(INVALID_SOAP_COMMUNICATION_CONFIGURATION + "url, operationId");
        }
        if (urlIsEmpty && operationNamespaceIsEmpty) {
            throw new SoapClientModuleException(INVALID_SOAP_COMMUNICATION_CONFIGURATION + "url, operationNamespace");
        }
        if (urlIsEmpty) {
            throw new SoapClientModuleException(INVALID_SOAP_COMMUNICATION_CONFIGURATION + "url");
        }
        if (operationIdIsEmpty && operationNamespaceIsEmpty) {
            throw new SoapClientModuleException(INVALID_SOAP_COMMUNICATION_CONFIGURATION + "operationId, operationNamespace");
        }
        if (operationIdIsEmpty) {
            throw new SoapClientModuleException(INVALID_SOAP_COMMUNICATION_CONFIGURATION + "operationId");
        }
        throw new SoapClientModuleException("operationNamespace");
    }
}
