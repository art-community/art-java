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

package ru.art.http.server;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.LogbookCreator;
import ru.art.http.configuration.HttpModuleConfiguration;
import ru.art.http.logger.ZalangoLogbookLogWriter;
import ru.art.core.mime.MimeType;
import ru.art.http.server.filter.HtmlLogsFilter;
import ru.art.http.server.handler.HttpExceptionHandler;
import ru.art.http.server.interceptor.HttpServerInterceptor;
import ru.art.http.server.interceptor.HttpWebInterception;
import ru.art.service.exception.ServiceExecutionException;
import static org.zalando.logbook.RawResponseFilters.replaceBody;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.NetworkConstants.BROADCAST_IP_ADDRESS;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.core.network.selector.PortSelector.findAvailableTcpPort;
import static ru.art.http.constants.HttpMimeTypes.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.ResourceExtensions.*;
import static ru.art.http.server.interceptor.HttpServerInterceptor.intercept;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface HttpServerModuleConfiguration extends HttpModuleConfiguration {
    static List<HttpServerInterceptor> initializeWebServerInterceptors(List<HttpServerInterceptor> parents) {
        parents.add(intercept(new HttpWebInterception()));
        return parents;
    }

    static LogbookCreator.Builder logbookWithoutWebLogs(LogbookCreator.Builder builder) {
        return builder.rawResponseFilter(replaceBody(HtmlLogsFilter::replaceWebResponseBody)).writer(new ZalangoLogbookLogWriter());
    }

    static LogbookCreator.Builder logbookWithoutWebLogs() {
        return Logbook.builder().rawResponseFilter(replaceBody(HtmlLogsFilter::replaceWebResponseBody)).writer(new ZalangoLogbookLogWriter());
    }

    String getHost();

    int getPort();

    String getPath();

    List<HttpServerInterceptor> getRequestInterceptors();

    List<HttpServerInterceptor> getResponseInterceptors();

    <T extends Throwable> Map<Class<T>, HttpExceptionHandler<T>> getExceptionHandlers();

    int getRequestBodyBufferSize();

    int getMaxThreadsCount();

    int getMinSpareThreadsCount();

    int getMaxIdleTime();

    boolean isPrestartMinSpareThreads();

    boolean isAllowCasualMultipartParsing();

    boolean isEnableMetrics();

    HttpWebConfiguration getWebConfiguration();

    @Getter
    @Builder
    class HttpWebConfiguration {
        private final String webUrl;
        @Singular("templateResourceVariable")
        private final Map<String, Function<String, String>> templateResourceVariables;
        @Singular("resourcePathMapping")
        private final Map<String, Function<String, String>> resourcePathMapping;
        @Builder.Default
        private final int resourceBufferSize = DEFAULT_BUFFER_SIZE;
        @Builder.Default
        private final Set<String> templatingResourceExtensions = setOf(HTML, WSDL);
        @Builder.Default
        private final Set<String> availableResourceExtensions = setOf(WEBP, JPEG, PNG, CSS, MAP, JS, HTML, WSDL);
        @Builder.Default
        private final Map<MimeType, String> logbookResponseBodyReplacers = mapOf(TEXT_HTML, WEB_RESOURCE)
                .add(TEXT_JS, WEB_RESOURCE)
                .add(TEXT_CSS, WEB_RESOURCE)
                .add(IMAGE_WEBP, WEB_RESOURCE)
                .add(IMAGE_PNG, WEB_RESOURCE)
                .add(IMAGE_JPEG, WEB_RESOURCE)
                .add(IMAGE_GIF, WEB_RESOURCE);
    }

    HttpServerModuleDefaultConfiguration DEFAULT_CONFIGURATION = new HttpServerModuleDefaultConfiguration();

    @Getter
    class HttpServerModuleDefaultConfiguration extends HttpModuleDefaultConfiguration implements HttpServerModuleConfiguration {
        private final boolean enableMetrics = true;
        private final boolean allowCasualMultipartParsing = true;
        private final int maxThreadsCount = DEFAULT_THREAD_POOL_SIZE;
        private final int minSpareThreadsCount = DEFAULT_THREAD_POOL_SIZE;
        private final int maxIdleTime = DEFAULT_MAX_IDLE_TIME;
        private final boolean prestartMinSpareThreads = DEFAULT_PRESTART_MIN_SPARE_THREADS;
        private final int requestBodyBufferSize = DEFAULT_REQUEST_BODY_BUFFER_SIZE;
        private final String host = BROADCAST_IP_ADDRESS;
        private final int port = findAvailableTcpPort();
        private final String path = DEFAULT_MODULE_PATH;
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<HttpServerInterceptor> requestInterceptors = initializeInterceptors();
        @Getter(lazy = true, onMethod = @__({@SuppressWarnings("unchecked")}))
        private final List<HttpServerInterceptor> responseInterceptors = linkedListOf();
        private final boolean ignoreAcceptHeader = false;
        private final Map<? extends Class<? extends Throwable>, ? extends HttpExceptionHandler<? extends Throwable>> exceptionHandlers = mapOf(Throwable.class, new ExceptionHttpJsonHandler())
                .add(cast(ServiceExecutionException.class), cast(new ServiceHttpJsonExceptionHandler()));
        private final HttpWebConfiguration webConfiguration = HttpWebConfiguration.builder().webUrl(DEFAULT_WEB_URL).build();

        private static List<HttpServerInterceptor> initializeInterceptors() {
            return linkedListOf(intercept(new HttpServerTracingIdentifierInterception()), intercept(new HttpWebInterception()));
        }

        public <T extends Throwable> Map<Class<T>, HttpExceptionHandler<T>> getExceptionHandlers() {
            return cast(exceptionHandlers);
        }
    }
}