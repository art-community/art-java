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

package io.art.config.extensions.http;

import lombok.*;
import org.apache.http.entity.*;
import org.zalando.logbook.*;
import io.art.core.mime.*;
import io.art.http.constants.*;
import io.art.http.mapper.*;
import io.art.http.server.*;
import io.art.http.server.HttpServerModuleConfiguration.*;
import io.art.http.server.specification.*;
import io.art.metrics.http.specification.*;
import static java.util.Objects.*;
import static org.apache.http.entity.ContentType.*;
import static io.art.config.extensions.ConfigExtensions.*;
import static io.art.config.extensions.common.CommonConfigKeys.*;
import static io.art.config.extensions.http.HttpConfigKeys.*;
import static io.art.config.extensions.http.HttpContentMappersConfigurator.*;
import static io.art.core.constants.ThreadConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extension.NullCheckingExtensions.*;
import static io.art.http.server.HttpServerModuleConfiguration.*;
import static io.art.http.server.constants.HttpServerModuleConstants.*;
import static io.art.http.server.module.HttpServerModule.*;
import static io.art.information.constants.InformationModuleConstants.*;
import static io.art.information.specification.InformationServiceSpecification.*;
import static io.art.metrics.constants.MetricsModuleConstants.*;
import static io.art.metrics.http.filter.MetricsHttpLogFilter.*;
import static io.art.service.ServiceModule.*;
import java.util.*;

@Getter
public class HttpServerAgileConfiguration extends HttpServerModuleDefaultConfiguration {
    private final Map<MimeType, HttpContentMapper> contentMappers = configureHttpContentMappers(super.getContentMappers());
    private final Logbook logbook = logbookWithoutResourceLogs(logbookWithoutMetricsLogs(this::isEnableRawDataTracing), this::isEnableRawDataTracing).build();
    private int port;
    private String path;
    private int maxThreadsCount;
    private int minSpareThreadsCount;
    private boolean enableRawDataTracing;
    private boolean enableValueTracing;
    private boolean enableMetrics;
    private MimeToContentTypeMapper consumesMimeTypeMapper;
    private MimeToContentTypeMapper producesMimeTypeMapper;
    private String host;
    private boolean web;

    public HttpServerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        MimeToContentTypeMapper consumesMimeTypeMapper = super.getConsumesMimeTypeMapper();
        String consumesMimeTypeString = configString(HTTP_COMMUNICATION_SECTION_ID, CONSUMES_MIME_TYPE,
                consumesMimeTypeMapper.getMimeType().toString());
        MimeType consumesMimeType = MimeType.valueOf(consumesMimeTypeString);
        ContentType consumesContentType = getOrElse(getByMimeType(consumesMimeTypeString), consumesMimeTypeMapper.getContentType());
        this.consumesMimeTypeMapper = new MimeToContentTypeMapper(consumesMimeType, consumesContentType);
        MimeToContentTypeMapper producesMimeTypeMapper = super.getProducesMimeTypeMapper();
        String producesMimeTypeString = configString(HTTP_COMMUNICATION_SECTION_ID, PRODUCES_MIME_TYPE,
                producesMimeTypeMapper.getMimeType().toString());
        MimeType producesMimeType = MimeType.valueOf(producesMimeTypeString);
        ContentType producesContentType = getOrElse(getByMimeType(producesMimeTypeString), producesMimeTypeMapper.getContentType());
        this.producesMimeTypeMapper = new MimeToContentTypeMapper(producesMimeType, producesContentType);
        web = configBoolean(HTTP_SERVER_SECTION_ID, WEB, super.isWeb());
        enableRawDataTracing = configBoolean(HTTP_SERVER_SECTION_ID, ENABLE_RAW_DATA_TRACING, super.isEnableRawDataTracing());
        enableValueTracing = configBoolean(HTTP_SERVER_SECTION_ID, ENABLE_VALUE_TRACING, super.isEnableValueTracing());
        enableMetrics = configBoolean(HTTP_SERVER_SECTION_ID, ENABLE_METRICS, super.isEnableMetrics());
        int newPort = configInt(HTTP_SERVER_SECTION_ID, PORT, super.getPort());
        boolean restart = port != newPort;
        port = newPort;
        String newPath = configString(HTTP_SERVER_SECTION_ID, PATH, super.getPath());
        restart |= !newPath.equals(path);
        path = newPath;
        int newMaxThreadsCount = configInt(HTTP_SERVER_SECTION_ID, MAX_THREADS_COUNT, DEFAULT_THREAD_POOL_SIZE);
        restart |= newMaxThreadsCount != maxThreadsCount;
        maxThreadsCount = newMaxThreadsCount;
        String newHost = configString(HTTP_SERVER_SECTION_ID, HOST, super.getHost());
        restart |= !newHost.equalsIgnoreCase(host);
        host = newHost;
        int newMinSpareThreadsCount = configInt(HTTP_SERVER_SECTION_ID, MIN_SPARE_THREADS_COUNT, DEFAULT_THREAD_POOL_SIZE);
        restart |= newMinSpareThreadsCount != minSpareThreadsCount;
        minSpareThreadsCount = newMinSpareThreadsCount;
        registerServices();
        if (restart && context().hasModule(HTTP_SERVER_MODULE_ID)) {
            HttpServer server = httpServerModuleState().getServer();
            if (nonNull(server) && server.isWorking()) {
                server.restart();
            }
        }
    }

    private void registerServices() {
        if (web && serviceModuleState().getServiceRegistry().getServices().keySet().stream().noneMatch(id -> id.contains(path))) {
            serviceModuleState().getServiceRegistry().registerService(new HttpResourceServiceSpecification(path, getResourceConfiguration()));
        }
        if (enableMetrics && !serviceModuleState().getServiceRegistry().getServices().containsKey(METRICS_SERVICE_ID)) {
            serviceModuleState().getServiceRegistry().registerService(new MetricServiceSpecification(path));
        }
        if (serviceModuleState().getServiceRegistry().getServices().keySet().stream().noneMatch(id -> id.contains(INFORMATION_PATH))) {
            registerInformationService(path);
        }
    }
}
