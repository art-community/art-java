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

import lombok.*;
import org.zalando.logbook.*;
import ru.art.core.mime.*;
import ru.art.http.configuration.*;
import ru.art.http.logger.*;
import ru.art.http.server.filter.*;
import ru.art.http.server.handler.*;
import ru.art.http.server.interceptor.*;
import ru.art.service.exception.*;
import java.util.*;

import static org.zalando.logbook.RawResponseFilters.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.constants.NetworkConstants.*;
import static ru.art.core.constants.ThreadConstants.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.core.network.selector.PortSelector.*;
import static ru.art.http.constants.HttpMimeTypes.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.HttpResourceType.*;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.ResourceExtensions.*;
import static ru.art.http.server.interceptor.HttpServerInterceptor.*;

public interface HttpServerModuleConfiguration extends HttpModuleConfiguration {
    static List<HttpServerInterceptor> initializeWebServerInterceptors(List<HttpServerInterceptor> parents) {
        parents.add(intercept(new HttpWebInterception()));
        return parents;
    }

    static LogbookCreator.Builder logbookWithoutResourceLogs(LogbookCreator.Builder builder) {
        return builder.rawResponseFilter(replaceBody(HttpResourceLogsFilter::replaceResponseBody)).writer(new ZalangoLogbookLogWriter());
    }

    static LogbookCreator.Builder logbookWithoutResourceLogs() {
        return Logbook.builder().rawResponseFilter(replaceBody(HttpResourceLogsFilter::replaceResponseBody)).writer(new ZalangoLogbookLogWriter());
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

    HttpResourceConfiguration getResourceConfiguration();

    boolean isWeb();

    @Getter
    @Builder
    class HttpResourceConfiguration {
        @Singular("templateResourceVariable")
        private final Map<String, String> templateResourceVariables;
        @Singular("resourceTypeMapping")
        private final Map<String, HttpResourceType> resourceExtensionTypeMappings = mapOf(HTML, STRING)
                .add(WSDL, STRING)
                .add(CSS, STRING)
                .add(MAP, STRING)
                .add(JS, STRING)
                .add(WEBP, BINARY)
                .add(JPEG, BINARY)
                .add(PNG, BINARY);
        @Singular("resourcePathMapping")
        private final Map<String, String> resourcePathMappings;
        @Builder.Default
        private final int resourceBufferSize = DEFAULT_BUFFER_SIZE;
        @Builder.Default
        private final Set<String> templatingResourceExtensions = setOf(HTML, WSDL);
        @Builder.Default
        private final Set<String> availableResourceExtensions = setOf(WEBP, JPEG, PNG, CSS, MAP, JS, HTML, WSDL);
        @Singular("logbookResponseBodyReplacer")
        private final Map<MimeType, String> logbookResponseBodyReplacers = mapOf(TEXT_HTML, HTTP_RESOURCE)
                .add(TEXT_JS, HTTP_RESOURCE)
                .add(TEXT_CSS, HTTP_RESOURCE)
                .add(IMAGE_WEBP, HTTP_RESOURCE)
                .add(IMAGE_PNG, HTTP_RESOURCE)
                .add(IMAGE_JPEG, HTTP_RESOURCE)
                .add(IMAGE_GIF, HTTP_RESOURCE);
        @Singular("accessControlParameter")
        private final Map<String, String> accessControlParameters = mapOf(ACCESS_CONTROL_ALLOW_METHODS_KEY, ACCESS_CONTROL_ALLOW_METHODS_VALUE)
                .add(ACCESS_CONTROL_ALLOW_HEADERS_KEY, ACCESS_CONTROL_ALLOW_HEADERS_VALUE)
                .add(ACCESS_CONTROL_MAX_AGE_HEADERS_KEY, ACCESS_CONTROL_MAX_AGE_HEADERS_VALUE)
                .add(ACCESS_CONTROL_ALLOW_CREDENTIALS_KEY, ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE);
        @Builder.Default
        private final boolean allowOriginParameterFromRequest = true;
        @Builder.Default
        private final String defaultResource = INDEX_HTML;
    }

    HttpServerModuleDefaultConfiguration DEFAULT_CONFIGURATION = new HttpServerModuleDefaultConfiguration();

    @Getter
    class HttpServerModuleDefaultConfiguration extends HttpModuleDefaultConfiguration implements HttpServerModuleConfiguration {
        private final boolean web = true;
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
        private final HttpResourceConfiguration resourceConfiguration = HttpResourceConfiguration.builder().build();
        private final Map<? extends Class<? extends Throwable>, ? extends HttpExceptionHandler<? extends Throwable>> exceptionHandlers =
                mapOf(Throwable.class, new ExceptionHttpJsonHandler())
                        .add(cast(ServiceExecutionException.class), cast(new ServiceHttpJsonExceptionHandler()));

        private static List<HttpServerInterceptor> initializeInterceptors() {
            return linkedListOf(intercept(new HttpServerTracingIdentifierInterception()), intercept(new HttpWebInterception()));
        }

        public <T extends Throwable> Map<Class<T>, HttpExceptionHandler<T>> getExceptionHandlers() {
            return cast(exceptionHandlers);
        }
    }
}