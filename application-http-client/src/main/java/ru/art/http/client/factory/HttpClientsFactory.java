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

package ru.art.http.client.factory;

import lombok.experimental.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.nio.client.*;
import org.zalando.logbook.httpclient.*;
import ru.art.http.client.configuration.*;
import ru.art.http.client.exception.*;
import ru.art.http.client.model.*;
import static java.security.KeyStore.*;
import static java.util.Objects.*;
import static org.apache.http.ssl.SSLContexts.*;
import static ru.art.http.client.constants.HttpClientExceptionMessages.*;
import static ru.art.http.client.module.HttpClientModule.*;
import static ru.art.logging.LoggingModule.*;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;

@UtilityClass
public class HttpClientsFactory {
    @SuppressWarnings("Duplicates")
    public static CloseableHttpAsyncClient createAsyncHttpClient(HttpClientConfiguration configuration) {
        HttpAsyncClientBuilder clientBuilder = HttpAsyncClients.custom()
                .setMaxConnPerRoute(configuration.getMaxConnectionsPerRoute())
                .setMaxConnTotal(configuration.getMaxConnectionsTotal())
                .setDefaultRequestConfig(configuration.getRequestConfig())
                .setDefaultIOReactorConfig(configuration.getIoReactorConfig())
                .setDefaultConnectionConfig(configuration.getConnectionConfig());
        if (configuration.isSsl()) {
            try {
                if (configuration.isDisableSslHostNameVerification()) {
                    HostnameVerifier allowAll = (hostName, session) -> true;
                    clientBuilder.setSSLHostnameVerifier(allowAll);
                }
                clientBuilder.setSSLContext(custom()
                        .loadKeyMaterial(loadKeyStore(configuration), configuration.getSslKeyStorePassword().toCharArray())
                        .build());
            } catch (Throwable throwable) {
                throw new HttpClientException(HTTP_SSL_CONFIGURATION_FAILED, throwable);
            }
        }
        clientBuilder
                .addInterceptorFirst(new LogbookHttpRequestInterceptor(httpClientModule().getLogbook()))
                .addInterceptorLast(new LogbookHttpResponseInterceptor());
        CloseableHttpAsyncClient client = httpClientModuleState().registerClient(clientBuilder.build());
        client.start();
        return client;
    }

    @SuppressWarnings("Duplicates")
    public static CloseableHttpClient createHttpClient(HttpClientConfiguration configuration) {
        HttpClientBuilder clientBuilder = HttpClients.custom()
                .setMaxConnPerRoute(configuration.getMaxConnectionsPerRoute())
                .setMaxConnTotal(configuration.getMaxConnectionsTotal())
                .setDefaultRequestConfig(configuration.getRequestConfig())
                .setDefaultConnectionConfig(configuration.getConnectionConfig())
                .setDefaultSocketConfig(configuration.getSocketConfig())
                .addInterceptorFirst(new LogbookHttpRequestInterceptor(httpClientModule().getLogbook()))
                .addInterceptorLast(new LogbookHttpResponseInterceptor());
        if (configuration.isSsl()) {
            try {
                if (configuration.isDisableSslHostNameVerification()) {
                    HostnameVerifier allowAll = (hostName, session) -> true;
                    clientBuilder.setSSLHostnameVerifier(allowAll);
                }
                clientBuilder.setSSLContext(custom()
                        .loadKeyMaterial(loadKeyStore(configuration), configuration.getSslKeyStorePassword().toCharArray())
                        .build());
            } catch (Throwable throwable) {
                throw new HttpClientException(HTTP_SSL_CONFIGURATION_FAILED, throwable);
            }
        }
        return httpClientModuleState().registerClient(clientBuilder.build());
    }

    private static KeyStore loadKeyStore(HttpClientConfiguration configuration) {
        FileInputStream keyStoreInputStream = null;
        try {
            KeyStore keyStore = getInstance(configuration.getSslKeyStoreType());
            keyStoreInputStream = new FileInputStream(new File(configuration.getSslKeyStoreFilePath()));
            keyStore.load(keyStoreInputStream, configuration.getSslKeyStorePassword().toCharArray());
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
