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

import lombok.NoArgsConstructor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;
import ru.art.http.client.configuration.HttpClientModuleConfiguration;
import ru.art.http.client.exception.HttpClientException;
import ru.art.http.client.model.HttpClientConfiguration;
import static java.security.KeyStore.getInstance;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.http.ssl.SSLContexts.custom;
import static ru.art.http.client.constants.HttpClientExceptionMessages.HTTP_SAL_CONFIGURATION_FAILED;
import static ru.art.http.client.module.HttpClientModule.httpClientModule;
import static ru.art.logging.LoggingModule.loggingModule;
import javax.net.ssl.HostnameVerifier;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

@NoArgsConstructor(access = PRIVATE)
public class HttpClientsFactory {
    @SuppressWarnings("Duplicates")
    public static CloseableHttpAsyncClient createAsyncHttpClient(HttpClientConfiguration configuration) {
        HttpAsyncClientBuilder clientBuilder = HttpAsyncClients.custom()
                .setDefaultRequestConfig(configuration.getRequestConfig())
                .setDefaultIOReactorConfig(configuration.getIoReactorConfig())
                .setDefaultConnectionConfig(configuration.getConnectionConfig());
        if (configuration.isSsl()) {
            try {
                if (configuration.isDisableSslHostNameVerification()) {
                    HostnameVerifier allowAll = (hostName, session) -> true;
                    clientBuilder.setSSLHostnameVerifier(allowAll);
                }
                clientBuilder.setSSLContext(custom().loadKeyMaterial(loadKeyStore(configuration), configuration.getSslKeyStorePassword().toCharArray()).build());
            } catch (Throwable e) {
                throw new HttpClientException(HTTP_SAL_CONFIGURATION_FAILED, e);
            }
        }
        if (configuration.isEnableTracing()) {
            clientBuilder.addInterceptorFirst(new LogbookHttpRequestInterceptor(httpClientModule().getLogbook()))
                    .addInterceptorLast(new LogbookHttpResponseInterceptor());
        }
        CloseableHttpAsyncClient client = clientBuilder.build();
        client.start();
        return client;
    }

    @SuppressWarnings("Duplicates")
    public static CloseableHttpClient createHttpClient(HttpClientConfiguration configuration) {
        HttpClientBuilder clientBuilder = HttpClients.custom()
                .setDefaultRequestConfig(configuration.getRequestConfig())
                .setDefaultConnectionConfig(configuration.getConnectionConfig())
                .setDefaultSocketConfig(configuration.getSocketConfig());
        if (configuration.isEnableTracing()) {
            clientBuilder.addInterceptorFirst(new LogbookHttpRequestInterceptor(httpClientModule().getLogbook()))
                    .addInterceptorLast(new LogbookHttpResponseInterceptor());
        }
        if (configuration.isSsl()) {
            try {
                if (configuration.isDisableSslHostNameVerification()) {
                    HostnameVerifier allowAll = (hostName, session) -> true;
                    clientBuilder.setSSLHostnameVerifier(allowAll);
                }
                clientBuilder.setSSLContext(custom().loadKeyMaterial(loadKeyStore(configuration), configuration.getSslKeyStorePassword().toCharArray()).build());
            } catch (Throwable e) {
                throw new HttpClientException(HTTP_SAL_CONFIGURATION_FAILED, e);
            }
        }
        return clientBuilder.build();
    }

    private static KeyStore loadKeyStore(HttpClientConfiguration configuration) {
        FileInputStream keyStoreInputStream = null;
        try {
            KeyStore keyStore = getInstance(configuration.getSslKeyStoreType());
            keyStoreInputStream = new FileInputStream(new File(configuration.getSslKeyStoreFilePath()));
            keyStore.load(keyStoreInputStream, configuration.getSslKeyStorePassword().toCharArray());
            return keyStore;
        } catch (Throwable e) {
            throw new HttpClientException(HTTP_SAL_CONFIGURATION_FAILED, e);
        } finally {
            if (nonNull(keyStoreInputStream)) {
                try {
                    keyStoreInputStream.close();
                } catch (IOException e) {
                    loggingModule()
                            .getLogger(HttpClientModuleConfiguration.class)
                            .error(HTTP_SAL_CONFIGURATION_FAILED, e);
                }
            }
        }
    }
}
