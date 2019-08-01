package ru.adk.http.server;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.LogbookCreator;
import ru.adk.http.configuration.HttpModuleConfiguration;
import ru.adk.http.logger.ZalangoLogbookLogWriter;
import ru.adk.http.server.filter.HtmlLogsFilter;
import ru.adk.http.server.handler.HttpExceptionHandler;
import ru.adk.http.server.interceptor.HttpServerInterceptor;
import ru.adk.http.server.interceptor.HttpWebInterception;
import ru.adk.service.exception.ServiceExecutionException;
import static org.zalando.logbook.RawResponseFilters.replaceBody;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.constants.NetworkConstants.BROADCAST_IP_ADDRESS;
import static ru.adk.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.adk.core.factory.CollectionsFactory.*;
import static ru.adk.core.network.selector.PortSelector.findAvailableTcpPort;
import static ru.adk.http.server.constants.HttpServerModuleConstants.*;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.DEFAULT_BUFFER_SIZE;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.DEFAULT_WEB_URL;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.ResourceExtensions.*;
import static ru.adk.http.server.interceptor.HttpServerInterceptor.intercept;
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

    <T extends Exception> Map<Class<T>, HttpExceptionHandler<T>> getExceptionHandlers();

    int getRequestBodyBufferSize();

    int getMaxThreadsCount();

    int getMinSpareThreadsCount();

    int getMaxIdleTime();

    boolean isPrestartMinSpareThreads();

    boolean isAllowCasualMultipartParsing();

    boolean isEnableMetricsMonitoring();

    HttpWebConfiguration getWebConfiguration();

    @Getter
    @Builder
    class HttpWebConfiguration {
        private final String webUrl;
        @Singular("templateResourceVariables")
        private final Map<String, Function<String, String>> templateResourceVariables;
        @Singular("resourcePathMapping")
        private final Map<String, Function<String, String>> resourcePathMapping;

        @Builder.Default
        private final int resourceBufferSize = DEFAULT_BUFFER_SIZE;

        @Builder.Default
        private final Set<String> templatingResourceExtensions = setOf(HTML, WSDL);
        @Builder.Default
        private final Set<String> availableResourceExtensions = setOf(WEBP, JPEG, PNG, CSS, MAP, JS, HTML, WSDL);
    }

    @Getter
    class HttpServerModuleDefaultConfiguration extends HttpModuleDefaultConfiguration implements HttpServerModuleConfiguration {
        private final boolean enableMetricsMonitoring = true;
        private final boolean allowCasualMultipartParsing = true;
        private final int maxThreadsCount = DEFAULT_THREAD_POOL_SIZE;
        private final int minSpareThreadsCount = DEFAULT_THREAD_POOL_SIZE;
        private final int maxIdleTime = DEFAULT_MAX_IDLE_TIME;
        private final boolean prestartMinSpareThreads = DEFAULT_PRESTART_MIN_SPARE_THREADS;
        private final int requestBodyBufferSize = DEFAULT_REQUEST_BODY_BUFFER_SIZE;
        private final String host = BROADCAST_IP_ADDRESS;
        private final int port = findAvailableTcpPort();
        private final String path = DEFAULT_MODULE_PATH;
        @Getter(lazy = true)
        private final List<HttpServerInterceptor> requestInterceptors = initializeInterceptors();
        @Getter(lazy = true)
        private final List<HttpServerInterceptor> responseInterceptors = linkedListOf();
        private final boolean ignoreAcceptHeader = false;
        private final Map<? extends Class<? extends Exception>, ? extends HttpExceptionHandler<? extends Exception>> exceptionHandlers = mapOf(Exception.class, new ExceptionHttpJsonHandler())
                .add(cast(ServiceExecutionException.class), cast(new ServiceHttpJsonExceptionHandler()));
        private final HttpWebConfiguration webConfiguration = HttpWebConfiguration.builder().webUrl(DEFAULT_WEB_URL).build();

        private static List<HttpServerInterceptor> initializeInterceptors() {
            return linkedListOf(intercept(new HttpServerLoggingInterception()), intercept(new HttpWebInterception()));
        }
    }
}