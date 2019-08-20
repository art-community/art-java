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

package ru.art.http.configuration;

import lombok.Getter;
import org.zalando.logbook.Logbook;
import ru.art.core.mime.MimeType;
import ru.art.core.module.ModuleConfiguration;
import ru.art.entity.Value;
import ru.art.entity.interceptor.ValueInterceptor;
import ru.art.http.constants.MimeToContentTypeMapper;
import ru.art.http.logger.ZalangoLogbookLogWriter;
import ru.art.http.mapper.HttpContentMapper;
import ru.art.http.mapper.HttpTextPlainMapper;
import ru.art.logging.LoggingValueInterceptor;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.http.constants.HttpMimeTypes.ALL;
import static ru.art.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import java.util.List;
import java.util.Map;

public interface HttpModuleConfiguration extends ModuleConfiguration {
    boolean isEnableRawDataTracing();

    boolean isEnableValueTracing();

    Map<MimeType, HttpContentMapper> getContentMappers();

    Logbook getLogbook();

    List<ValueInterceptor<Value, Value>> getRequestValueInterceptors();

    List<ValueInterceptor<Value, Value>> getResponseValueInterceptors();

    MimeToContentTypeMapper getConsumesMimeTypeMapper();

    MimeToContentTypeMapper getProducesMimeTypeMapper();

    @Getter
    class HttpModuleDefaultConfiguration implements HttpModuleConfiguration {
        private final boolean enableRawDataTracing = true;
        private final boolean enableValueTracing = true;
        private final boolean enableMetricsMonitoring = true;
        private final MimeToContentTypeMapper consumesMimeTypeMapper = applicationJsonUtf8();
        private final MimeToContentTypeMapper producesMimeTypeMapper = applicationJsonUtf8();
        private final HttpTextPlainMapper textPlainMapper = new HttpTextPlainMapper();
        private final Map<MimeType, HttpContentMapper> contentMappers =
                mapOf(ALL, new HttpContentMapper(new HttpTextPlainMapper(), new HttpTextPlainMapper()));
        @Getter(lazy = true)
        private final List<ValueInterceptor<Value, Value>> requestValueInterceptors = initializeValueInterceptors();
        @Getter(lazy = true)
        private final List<ValueInterceptor<Value, Value>> responseValueInterceptors = initializeValueInterceptors();
        @Getter(lazy = true)
        private final Logbook logbook = Logbook.builder().writer(new ZalangoLogbookLogWriter()).build();

        private List<ValueInterceptor<Value, Value>> initializeValueInterceptors() {
            return isEnableValueTracing() ? linkedListOf(new LoggingValueInterceptor<>()) : linkedListOf();
        }
    }
}