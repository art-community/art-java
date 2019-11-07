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

import lombok.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.config.*;
import org.apache.http.config.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.nio.client.*;
import org.apache.http.impl.nio.reactor.*;
import org.apache.http.nio.client.*;
import org.zalando.logbook.httpclient.*;
import ru.art.http.client.exception.*;
import ru.art.http.client.interceptor.*;
import ru.art.http.client.model.*;
import ru.art.http.configuration.*;
import static java.security.KeyStore.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static java.util.concurrent.ForkJoinPool.*;
import static org.apache.http.HttpVersion.*;
import static org.apache.http.ssl.SSLContexts.*;
import static ru.art.core.constants.NetworkConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.ExceptionExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.http.client.constants.HttpClientExceptionMessages.*;
import static ru.art.http.client.constants.HttpClientModuleConstants.*;
import static ru.art.http.client.interceptor.HttpClientInterceptor.*;
import static ru.art.http.constants.HttpCommonConstants.*;
import static ru.art.logging.LoggingModule.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

public interface HttpClientModuleConfiguration extends HttpModuleConfiguration {
    HttpClient getClient();

    HttpAsyncClient getAsynchronousClient();

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

    Executor getAsynchronousFuturesExecutor();

    Map<String, HttpCommunicationTargetConfiguration> getCommunicationTargets();

    default HttpCommunicationTargetConfiguration getCommunicationTargetConfiguration(String serviceId) {
        return exceptionIfNull(getCommunicationTargets().get(serviceId),
                new HttpClientException(format(HTTP_COMMUNICATION_TARGET_NOT_FOUND, serviceId))).toBuilder().build();
    }

    HttpClientModuleDefaultConfiguration DEFAULT_CONFIGURATION = new HttpClientModuleDefaultConfiguration();

    @Getter
    class HttpClientModuleDefaultConfiguration extends HttpModuleDefaultConfiguration implements HttpClientModuleConfiguration {
        private final Executor asynchronousFuturesExecutor = commonPool();
        private final RequestConfig requestConfig = RequestConfig.DEFAULT;
        private final SocketConfig socketConfig = SocketConfig.DEFAULT;
        private final ConnectionConfig connectionConfig = ConnectionConfig.DEFAULT;
        private final IOReactorConfig ioReactorConfig = IOReactorConfig.DEFAULT;
        private final HttpVersion httpVersion = HTTP_1_1;
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<HttpClientInterceptor> requestInterceptors =
                linkedListOf(interceptRequest(new HttpClientTracingIdentifiersRequestInterception()));
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
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
        private final CloseableHttpClient client = createHttpClient();
        @Getter(lazy = true)
        private final CloseableHttpAsyncClient asynchronousClient = createAsyncHttpClient();

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
                } catch (Throwable throwable) {
                    throw new HttpClientException(HTTP_SSL_CONFIGURATION_FAILED, throwable);
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
                } catch (Throwable throwable) {
                    throw new HttpClientException(HTTP_SSL_CONFIGURATION_FAILED, throwable);
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
            } catch (Throwable throwable) {
                throw new HttpClientException(HTTP_SSL_CONFIGURATION_FAILED, throwable);
            } finally {
                if (nonNull(keyStoreInputStream)) {
                    try {
                        keyStoreInputStream.close();
                    } catch (IOException ioException) {
                        loggingModule()
                                .getLogger(HttpClientModuleConfiguration.class)
                                .error(HTTP_SSL_CONFIGURATION_FAILED, ioException);
                    }
                }
            }
        }

    }
}

