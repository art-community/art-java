/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.http.server;

import lombok.*;
import org.zalando.logbook.*;
import io.art.core.mime.*;
import io.art.http.configuration.*;
import io.art.http.logger.*;
import io.art.http.server.filter.*;
import io.art.http.server.handler.*;
import io.art.http.server.interceptor.*;
import static org.zalando.logbook.RawResponseFilters.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.NetworkConstants.*;
import static io.art.core.constants.ThreadConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.core.network.selector.PortSelector.*;
import static io.art.http.constants.HttpMimeTypes.*;
import static io.art.http.server.constants.HttpServerModuleConstants.*;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.*;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.HttpResourceType.*;
import static io.art.http.server.constants.HttpServerModuleConstants.HttpResourceServiceConstants.ResourceExtensions.*;
import static io.art.http.server.interceptor.HttpServerInterceptor.*;
import java.nio.charset.*;
import java.util.*;
import java.util.function.*;

public interface HttpServerModuleConfiguration extends HttpModuleConfiguration {
    static List<HttpServerInterceptor> initializeWebServerInterceptors(List<HttpServerInterceptor> parents) {
        parents.add(intercept(new HttpWebInterception()));
        return parents;
    }

    static LogbookCreator.Builder logbookWithoutResourceLogs(LogbookCreator.Builder builder, Supplier<Boolean> enabled) {
        return builder
                .rawResponseFilter(replaceBody(HttpResourceLogsFilter::replaceResponseBody))
                .writer(new ZalangoLogbookLogWriter(enabled));
    }

    static LogbookCreator.Builder logbookWithoutResourceLogs(Supplier<Boolean> enabled) {
        return Logbook.builder()
                .rawResponseFilter(replaceBody(HttpResourceLogsFilter::replaceResponseBody))
                .writer(new ZalangoLogbookLogWriter(enabled));
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
    @Builder(toBuilder = true)
    class HttpResourceConfiguration {
        @Singular("templateResourceVariable")
        private final Map<String, Object> templateResourceVariables;
        @Singular("resourcePathMapping")
        private final Map<String, HttpResource> resourcePathMappings;
        @Singular("resourceExtensionMapping")
        private final Map<String, HttpResourceExtensionMapping> resourceExtensionMappings;
        @Singular("accessControlParameter")
        private final Map<String, String> accessControlParameters;
        @Builder.Default
        private final boolean allowOriginParameterFromRequest = true;
        @Builder.Default
        private final HttpResource defaultResource = new HttpResource(INDEX_HTML, STRING, context().configuration().getCharset());
        @Builder.Default
        private final int resourceBufferSize = DEFAULT_BUFFER_SIZE;
        @Builder.Default
        private final Set<String> templatingResourceExtensions = setOf(DOT_HTML, DOT_WSDL);
    }

    @Getter
    @AllArgsConstructor
    class HttpResource {
        private final String path;
        private final HttpResourceType type;
        private final Charset charset;
    }

    @Getter
    @Builder(toBuilder = true)
    class HttpResourceExtensionMapping {
        private String extension;
        @Builder.Default
        private final MimeType mimeType = ALL;
        private final HttpResourceType resourceType;
        @Builder.Default
        private final String logbookBodyReplacement = HTTP_RESOURCE_BODY_REPLACEMENT;
        private final HttpResource customHttpResource;
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
        private final HttpResourceConfiguration resourceConfiguration = HttpResourceConfiguration.builder()
                .accessControlParameter(ACCESS_CONTROL_ALLOW_METHODS_KEY, ACCESS_CONTROL_ALLOW_METHODS_VALUE)
                .accessControlParameter(ACCESS_CONTROL_ALLOW_HEADERS_KEY, ACCESS_CONTROL_ALLOW_HEADERS_VALUE)
                .accessControlParameter(ACCESS_CONTROL_MAX_AGE_HEADERS_KEY, ACCESS_CONTROL_MAX_AGE_HEADERS_VALUE)
                .accessControlParameter(ACCESS_CONTROL_ALLOW_CREDENTIALS_KEY, ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE)

                .resourceExtensionMapping(DOT_WEBP, HttpResourceExtensionMapping.builder().extension(DOT_WEBP).mimeType(IMAGE_WEBP).resourceType(BINARY).build())
                .resourceExtensionMapping(DOT_JPG, HttpResourceExtensionMapping.builder().extension(DOT_JPG).mimeType(IMAGE_JPG).resourceType(BINARY).build())
                .resourceExtensionMapping(DOT_ICO, HttpResourceExtensionMapping.builder().extension(DOT_ICO).mimeType(IMAGE_ICO).resourceType(BINARY).build())
                .resourceExtensionMapping(DOT_JPEG, HttpResourceExtensionMapping.builder().extension(DOT_JPEG).mimeType(IMAGE_JPEG).resourceType(BINARY).build())
                .resourceExtensionMapping(DOT_PNG, HttpResourceExtensionMapping.builder().extension(DOT_PNG).mimeType(IMAGE_PNG).resourceType(BINARY).build())
                .resourceExtensionMapping(DOT_SVG, HttpResourceExtensionMapping.builder().extension(DOT_SVG).mimeType(IMAGE_SVG_XML).resourceType(BINARY).build())
                .resourceExtensionMapping(DOT_CSS, HttpResourceExtensionMapping.builder().extension(DOT_CSS).mimeType(TEXT_CSS).resourceType(STRING).build())
                .resourceExtensionMapping(DOT_JS, HttpResourceExtensionMapping.builder().extension(DOT_JS).mimeType(TEXT_JS).resourceType(STRING).build())
                .resourceExtensionMapping(DOT_HTML, HttpResourceExtensionMapping.builder().extension(DOT_HTML).mimeType(TEXT_HTML).resourceType(STRING).build())
                .resourceExtensionMapping(DOT_WSDL, HttpResourceExtensionMapping.builder().extension(DOT_WSDL).mimeType(TEXT_XML).resourceType(STRING).build())
                .resourceExtensionMapping(DOT_XML, HttpResourceExtensionMapping.builder().extension(DOT_XML).mimeType(TEXT_XML).resourceType(STRING).build())
                .resourceExtensionMapping(DOT_TXT, HttpResourceExtensionMapping.builder().extension(DOT_TXT).mimeType(TEXT_PLAIN).resourceType(STRING).build())
                .resourceExtensionMapping(DOT_MAP, HttpResourceExtensionMapping.builder().extension(DOT_MAP).mimeType(TEXT_PLAIN).resourceType(STRING).build())
                .build();

        private final Map<? extends Class<? extends Throwable>, ? extends HttpExceptionHandler<?>> exceptionHandlers =
                mapOf(
                        Throwable.class, (HttpExceptionHandler<Throwable>) new ExceptionHttpJsonHandler()
                ).add(
                        cast(DefaultServiceExecutionException.class), cast(new ServiceHttpJsonExceptionHandler())
                );

        private static List<HttpServerInterceptor> initializeInterceptors() {
            return linkedListOf(intercept(new HttpServerTracingIdentifierInterception()), intercept(new HttpWebInterception()));
        }

        public <T extends Throwable> Map<Class<T>, HttpExceptionHandler<T>> getExceptionHandlers() {
            return cast(exceptionHandlers);
        }
    }
}
