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

package ru.art.http.client.module;

import lombok.Getter;
import org.apache.http.client.methods.Configurable;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.logging.log4j.Logger;
import ru.art.core.module.Module;
import ru.art.http.client.configuration.HttpClientModuleConfiguration;
import ru.art.http.client.state.HttpClientModuleState;
import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.core.context.Context.contextIsNotReady;
import static ru.art.core.wrapper.ExceptionWrapper.ignoreException;
import static ru.art.http.client.configuration.HttpClientModuleConfiguration.DEFAULT_CONFIGURATION;
import static ru.art.http.client.constants.HttpClientModuleConstants.HTTP_CLIENT_CLOSING;
import static ru.art.http.client.constants.HttpClientModuleConstants.HTTP_CLIENT_MODULE_ID;
import static ru.art.logging.LoggingModule.loggingModule;

@Getter
public class HttpClientModule implements Module<HttpClientModuleConfiguration, HttpClientModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final HttpClientModuleConfiguration httpClientModule = context().getModule(HTTP_CLIENT_MODULE_ID, HttpClientModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private static final HttpClientModuleState httpClientModuleState = context().getModuleState(HTTP_CLIENT_MODULE_ID, HttpClientModule::new);
    private final String id = HTTP_CLIENT_MODULE_ID;
    private final HttpClientModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;
    private final HttpClientModuleState state = new HttpClientModuleState();
    private static Logger logger = loggingModule().getLogger(HttpClientModule.class);

    public static HttpClientModuleConfiguration httpClientModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getHttpClientModule();
    }

    public static HttpClientModuleState httpClientModuleState() {
        return getHttpClientModuleState();
    }

    @Override
    public void onUnload() {
        httpClientModuleState().getClients().forEach(this::closeClient);
        httpClientModuleState().getAsynchronousClients().forEach(this::closeClient);
    }

    private void closeClient(CloseableHttpClient client) {
        ignoreException(client::close, logger::error);
        logger.info(format(HTTP_CLIENT_CLOSING, ((Configurable) client).getConfig().toString()));
    }

    private void closeClient(CloseableHttpAsyncClient client) {
        ignoreException(client::close, logger::error);
        logger.info(format(HTTP_CLIENT_CLOSING, ((Configurable) client).getConfig().toString()));
    }
}
