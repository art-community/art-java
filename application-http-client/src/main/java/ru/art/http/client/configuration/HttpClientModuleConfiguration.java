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

package ru.art.http.client.configuration;

import lombok.Getter;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.client.HttpAsyncClient;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;
import ru.art.http.client.exception.HttpClientException;
import ru.art.http.client.interceptor.HttpClientInterceptor;
import ru.art.http.client.interceptor.HttpClientTracingIdentifiersRequestInterception;
import ru.art.http.client.model.HttpCommunicationTargetConfiguration;
import ru.art.http.configuration.HttpModuleConfiguration;
import static java.security.KeyStore.getInstance;
import static java.text.MessageFormat.format;
import static java.util.Collections.emptyMap;
import static java.util.Objects.nonNull;
import static org.apache.http.HttpVersion.HTTP_1_1;
import static org.apache.http.ssl.SSLContexts.custom;
import static ru.art.core.constants.NetworkConstants.LOCALHOST;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.extension.ExceptionExtensions.exceptionIfNull;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.http.client.constants.HttpClientExceptionMessages.HTTP_COMMUNICATION_TARGET_NOT_FOUND;
import static ru.art.http.client.constants.HttpClientExceptionMessages.HTTP_SSL_CONFIGURATION_FAILED;
import static ru.art.http.client.constants.HttpClientModuleConstants.RESPONSE_BUFFER_DEFAULT_SIZE;
import static ru.art.http.client.interceptor.HttpClientInterceptor.interceptRequest;
import static ru.art.http.constants.HttpCommonConstants.DEFAULT_HTTP_PORT;
import static ru.art.logging.LoggingModule.loggingModule;
import javax.net.ssl.HostnameVerifier;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;

public interface HttpClientModuleConfiguration extends HttpModuleConfiguration {
    HttpClient getClient();

    HttpAsyncClient getAsyncClient();

    RequestConfig getRequestConfig();

    SocketConfig getSocketConfig();

    IOReactorConfig getIoReactorConfig();

    ConnectionConfig getConnectionConfig();

    HttpVersion getHttpVersion();

    int getResponseBodyBufferSize();

    List<HttpClientInterceptor> getRequestInterceptors();

    List<HttpClientInterceptor> getResponseInterceptors();

    boolean isSsl();

    boolean isDisableSslHostNameVerification();

    String getSslKeyStoreType();

    String getSslKeyStoreFilePath();

    String getSslKeyStorePassword();

    String getBalancerHost();

    int getBalancerPort();


    Map<String, HttpCommunicationTargetConfiguration> getCommunicationTargets();

    default HttpCommunicationTargetConfiguration getCommunicationTargetConfiguration(String serviceId) {
        return exceptionIfNull(getCommunicationTargets().get(serviceId),
                new HttpClientException(format(HTTP_COMMUNICATION_TARGET_NOT_FOUND, serviceId))).toBuilder().build();
    }

    HttpClientModuleDefaultConfiguration DEFAULT_CONFIGURATION = new HttpClientModuleDefaultConfiguration();

    @Getter
    class HttpClientModuleDefaultConfiguration extends HttpModuleDefaultConfiguration implements HttpClientModuleConfiguration {
        private final RequestConfig requestConfig = RequestConfig.DEFAULT;
        private final SocketConfig socketConfig = SocketConfig.DEFAULT;
        private final ConnectionConfig connectionConfig = ConnectionConfig.DEFAULT;
        private final IOReactorConfig ioReactorConfig = IOReactorConfig.DEFAULT;
        private final HttpVersion httpVersion = HTTP_1_1;
        private final List<HttpClientInterceptor> requestInterceptors =
                linkedListOf(interceptRequest(new HttpClientTracingIdentifiersRequestInterception()));
        private final List<HttpClientInterceptor> responseInterceptors = linkedListOf();
        private final int responseBodyBufferSize = RESPONSE_BUFFER_DEFAULT_SIZE;
        private final boolean ssl = false;
        private final boolean disableSslHostNameVerification = true;
        private final String sslKeyStoreType = EMPTY_STRING;
        private final String sslKeyStoreFilePath = EMPTY_STRING;
        private final String sslKeyStorePassword = EMPTY_STRING;
        private final String balancerHost = LOCALHOST;
        private final int balancerPort = DEFAULT_HTTP_PORT;
        private final Map<String, HttpCommunicationTargetConfiguration> communicationTargets = emptyMap();
        @Getter(lazy = true)
        private final HttpClient client = createHttpClient();
        @Getter(lazy = true)
        private final HttpAsyncClient asyncClient = createAsyncHttpClient();

        @SuppressWarnings({"Duplicates", "WeakerAccess"})
        protected CloseableHttpAsyncClient createAsyncHttpClient() {
            HttpAsyncClientBuilder clientBuilder = HttpAsyncClients.custom()
                    .setDefaultRequestConfig(getRequestConfig())
                    .setDefaultIOReactorConfig(getIoReactorConfig())
                    .setDefaultConnectionConfig(getConnectionConfig());
            if (isSsl()) {
                try {
                    if (disableSslHostNameVerification) {
                        HostnameVerifier allowAll = (hostName, session) -> true;
                        clientBuilder.setSSLHostnameVerifier(allowAll);
                    }
                    clientBuilder.setSSLContext(custom()
                            .loadKeyMaterial(loadKeyStore(), getSslKeyStorePassword().toCharArray())
                            .build());
                } catch (Throwable e) {
                    throw new HttpClientException(HTTP_SSL_CONFIGURATION_FAILED, e);
                }
            }
            if (this.isEnableRawDataTracing()) {
                clientBuilder.addInterceptorFirst(new LogbookHttpRequestInterceptor(getLogbook()));
            }
            CloseableHttpAsyncClient client = clientBuilder.build();
            client.start();
            return client;
        }

        @SuppressWarnings({"Duplicates", "WeakerAccess"})
        protected CloseableHttpClient createHttpClient() {
            HttpClientBuilder clientBuilder = HttpClients.custom()
                    .setDefaultRequestConfig(getRequestConfig())
                    .setDefaultConnectionConfig(getConnectionConfig())
                    .setDefaultSocketConfig(getSocketConfig());
            if (this.isEnableRawDataTracing()) {
                clientBuilder.addInterceptorFirst(new LogbookHttpRequestInterceptor(getLogbook()))
                        .addInterceptorLast(new LogbookHttpResponseInterceptor());
            }
            if (isSsl()) {
                try {
                    if (disableSslHostNameVerification) {
                        HostnameVerifier allowAll = (hostName, session) -> true;
                        clientBuilder.setSSLHostnameVerifier(allowAll);
                    }
                    clientBuilder.setSSLContext(custom()
                            .loadKeyMaterial(loadKeyStore(), getSslKeyStorePassword().toCharArray())
                            .build());
                } catch (Throwable e) {
                    throw new HttpClientException(HTTP_SSL_CONFIGURATION_FAILED, e);
                }
            }
            return clientBuilder.build();
        }

        private KeyStore loadKeyStore() {
            FileInputStream keyStoreInputStream = null;
            try {
                KeyStore keyStore = getInstance(getSslKeyStoreType());
                keyStoreInputStream = new FileInputStream(new File(getSslKeyStoreFilePath()));
                keyStore.load(keyStoreInputStream, getSslKeyStorePassword().toCharArray());
                return keyStore;
            } catch (Throwable e) {
                throw new HttpClientException(HTTP_SSL_CONFIGURATION_FAILED, e);
            } finally {
                if (nonNull(keyStoreInputStream)) {
                    try {
                        keyStoreInputStream.close();
                    } catch (IOException e) {
                        loggingModule()
                                .getLogger(HttpClientModuleConfiguration.class)
                                .error(HTTP_SSL_CONFIGURATION_FAILED, e);
                    }
                }
            }
        }

    }
}

