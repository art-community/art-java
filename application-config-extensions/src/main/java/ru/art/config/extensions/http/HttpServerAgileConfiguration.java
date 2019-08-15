/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.config.extensions.http;

import lombok.Getter;
import org.zalando.logbook.Logbook;
import ru.art.core.mime.MimeType;
import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.server.HttpServerModuleConfiguration.HttpServerModuleDefaultConfiguration;
import ru.art.http.server.specification.HttpWebUiServiceSpecification;
import ru.art.metrics.http.specification.MetricServiceSpecification;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.http.HttpConfigKeys.*;
import static ru.art.config.extensions.http.HttpContentMappersConfigurator.configureHttpContentMappers;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static ru.art.core.context.Context.context;
import static ru.art.core.extension.ExceptionExtensions.emptyIfException;
import static ru.art.http.server.HttpServerModuleConfiguration.logbookWithoutWebLogs;
import static ru.art.http.server.constants.HttpServerModuleConstants.HTTP_SERVER_MODULE_ID;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.HttpPath.IMAGE_PATH;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.URL_TEMPLATE_VARIABLE;
import static ru.art.http.server.module.HttpServerModule.httpServerModuleState;
import static ru.art.metrics.http.filter.MetricsHttpLogFilter.logbookWithoutMetricsLogs;
import static ru.art.service.ServiceModule.serviceModule;
import java.util.Map;

@Getter
public class HttpServerAgileConfiguration extends HttpServerModuleDefaultConfiguration {
    private final Map<MimeType, HttpContentMapper> contentMappers = configureHttpContentMappers(super.getContentMappers());
    private final Logbook logbook = logbookWithoutWebLogs(logbookWithoutMetricsLogs()).build();
    private int port;
    private String path;
    private int maxThreadsCount;
    private int minSpareThreadsCount;
    private HttpWebConfiguration webConfiguration;
    private boolean enableTracing;
    private boolean enableMetrics;

    public HttpServerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        enableTracing = configBoolean(HTTP_SERVER_SECTION_ID, ENABLE_TRACING, super.isEnableTracing());
        enableMetrics = configBoolean(HTTP_SERVER_SECTION_ID, ENABLE_METRICS, super.isEnableMetrics());
        String webUrl = emptyIfException(() -> configString(HTTP_SERVER_SECTION_ID, WEB_URL));
        webConfiguration = isEmpty(webUrl) ? super.getWebConfiguration() : HttpWebConfiguration.builder()
                .webUrl(webUrl)
                .templateResourceVariables(URL_TEMPLATE_VARIABLE, variable -> webUrl)
                .build();
        int newPort = configInt(HTTP_SERVER_SECTION_ID, PORT, super.getPort());
        boolean restart = port != newPort;
        port = newPort;
        String newPath = configString(HTTP_SERVER_SECTION_ID, PATH, super.getPath());
        restart |= !newPath.equals(path);
        path = newPath;
        int newMaxThreadsCount = configInt(HTTP_SERVER_SECTION_ID, MAX_THREADS_COUNT, DEFAULT_THREAD_POOL_SIZE);
        restart |= newMaxThreadsCount != maxThreadsCount;
        maxThreadsCount = newMaxThreadsCount;
        int newMinSpareThreadsCount = configInt(HTTP_SERVER_SECTION_ID, MIN_SPARE_THREADS_COUNT, DEFAULT_THREAD_POOL_SIZE);
        restart |= newMinSpareThreadsCount != minSpareThreadsCount;
        minSpareThreadsCount = newMinSpareThreadsCount;
        if (isNotEmpty(webUrl)) {
            serviceModule().getServiceRegistry().registerService(new HttpWebUiServiceSpecification(path, path + IMAGE_PATH));
        }
        if (enableMetrics) {
            serviceModule().getServiceRegistry().registerService(new MetricServiceSpecification(path));
        }
        if (restart && context().hasModule(HTTP_SERVER_MODULE_ID)) {
            httpServerModuleState().getServer().restart();
        }
    }

}
